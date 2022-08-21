package com.cumt.musicserver.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cumt.musicserver.domain.*;
import com.cumt.musicserver.dao.SongListDao;
import com.cumt.musicserver.service.IConsumerService;
import com.cumt.musicserver.service.ISongListCommentService;
import com.cumt.musicserver.service.ISongListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.util.FileUtil;
import com.cumt.musicserver.util.Result;
import com.cumt.musicserver.util.StaticString;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

import static com.cumt.musicserver.util.StaticString.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mthddhl
 * @since 2022-05-16
 */
@Service
public class SongListServiceImpl extends ServiceImpl<SongListDao, SongList> implements ISongListService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IConsumerService consumerService;
    @Resource
    private ISongListCommentService songListCommentService;

    @Resource
    private SongListDao songListDao;

    @Override
    public Result songListGetPage(Integer currentPage, Integer pageSize,String likeNameOne) {
//        List<SongList> records;
        Map<String,Object> map=new HashMap<>();
        if(likeNameOne==null || "".equals(likeNameOne)) {
//            List<SongList>  records = query().page(new Page<>(currentPage, pageSize)).getRecords();
            Integer count = query().count();
            List<SongList> records1 = query().page(new Page<>(currentPage, pageSize)).getRecords();
            map.put("count",count);
            map.put("data",records1);
            setCount(records1);
            return Result.ok(map);
        }
        QueryChainWrapper<SongList> title1 = query().eq("title", likeNameOne);
        Integer count=title1.count();
        List<SongList> title=title1.page(new Page<>(currentPage, pageSize)).getRecords();
        map.put("count",count);
        map.put("data",title);
        setCount(title);
        return Result.ok(map);

    }

    @Override
    @Transactional
    public Result changeImg(MultipartFile multipartFile, Integer id) {
        if(ObjectUtils.isEmpty(id)){
            String s = FileUtil.uploadFile(multipartFile, "img", "songList");
            return FileUtil.afterUpload(s);
        }
        SongList one = lambdaQuery().select(SongList::getPic).eq(SongList::getId, id).one();
        String temp=one.getPic();
        String s = FileUtil.uploadFile(multipartFile, "img", "songList");
        if(ObjectUtils.isNotEmpty(s)) {
            boolean update = update().eq("id", id).set("pic", s).update();
            if (update) {
                if (!SONG_LIST_DEFAULT_PIC.equals(temp) &&  FileUtil.deleteFile(Collections.singletonList(temp))) {
                    return Result.ok("更新成功");
                }
            }
        }
        return Result.fail("更新失败");
    }

    @Override
    public Result getPartSongList() {
        Random r=new Random();
        int start=r.nextInt(2)+1;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().
                reverseRangeWithScores(SONGLIST_PLAY_COUNT, start, start+9);
        Map<Integer,Double> map=new HashMap<>();
        List<SongList> list;
        if(ObjectUtils.isNotEmpty(typedTuples) && typedTuples.size()>10) {
            typedTuples.forEach(a-> map.put(Integer.valueOf(a.getValue()),a.getScore()));
             list= lambdaQuery().in(SongList::getId, map.keySet()).list();
            list.forEach(a-> a.setCount(map.get(a.getId()).intValue()));
            Collections.shuffle(list);
        }else {
            list=page(new Page<>(start,10)).getRecords();
            List<String> reList=new ArrayList<>();
            list.forEach(a->reList.add(a.getId().toString()));
            List<Double> score = stringRedisTemplate.opsForZSet().score(SONGLIST_PLAY_COUNT, reList.toArray());
            for (int i = 0; i < list.size(); i++) {
                if(ObjectUtils.isEmpty(score.get(i))) continue;
                list.get(i).setCount(score.get(i).intValue());
            }
        }
        return Result.ok(list);
    }

    @Override
    public Result consumerLikeSongList(Integer id) {
        Object o=SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        Consumer c=(Consumer) o;
        Integer consumerId = c.getId();
//        boolean update = lambdaUpdate().eq(SongList::getId, id).setSql("likes=likes+1").update();
        Double aDouble = stringRedisTemplate.opsForZSet().incrementScore(SONGLIST_LIKES_COUNT, id.toString(), 1);
        if(ObjectUtils.isNotEmpty(aDouble) && aDouble.intValue()>=0){
            Long add = stringRedisTemplate.opsForSet().add(SONGLIST_LIKES + id, String.valueOf(consumerId));
            Long add1 = stringRedisTemplate.opsForSet().add(StaticString.CONSUMER_LIKE_SONGlIST + c.getId(), id.toString());
            if(add!=null && add!=0 && add1!=null && add1!=0){
                return Result.ok();
            }
        }
        return Result.fail("收藏失败");
    }

    @Override
    public Result getSongListGetById(Integer id) {
        SongList one=songListDao.getSongListGetById(id);
        Object o=SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        if(o instanceof Consumer) {
            Consumer c=(Consumer)o;
            Boolean member = stringRedisTemplate.opsForSet().isMember(SONGLIST_LIKES + id.toString(), c.getId().toString());
            if(BooleanUtils.isTrue(member)){
                one.setIsLike("success");
            }
        }
        Integer count;
        setCount(Collections.singletonList(one));
        if(BooleanUtils.isFalse(stringRedisTemplate.hasKey(SONGLIST_COMMENT_COUNT + id.toString()))){
            count = (songListCommentService.lambdaQuery().eq(SongListComment::getSongListId, id).count());
            stringRedisTemplate.opsForValue().set(SONGLIST_COMMENT_COUNT + id,count.toString());
        }else {
            count= Integer.valueOf(Objects.requireNonNull(stringRedisTemplate.opsForValue().get(SONGLIST_COMMENT_COUNT + id)));
        }
        one.setCommentCount(count);
        return Result.ok(one);
    }

    @Override
    public Result consumerGetLikeSingList(Integer id) {
        if(BooleanUtils.isTrue(stringRedisTemplate.hasKey(CONSUMER_LIKE_SONGlIST + id))) {
            Set<String> members = stringRedisTemplate.opsForSet().members(CONSUMER_LIKE_SONGlIST + id);
            if (Objects.isNull(members)) return Result.ok();
            List<Integer> list = new ArrayList<>();
            for (String s : members) {
                list.add(Integer.valueOf(s));
            }
            List<SongList> list1 = lambdaQuery().in(SongList::getId, list).list();
            Set<String> set=new HashSet<>(list1.size());
            list1.forEach(a->set.add(a.getId().toString()));
            members.removeAll(set);
            if (!members.isEmpty())stringRedisTemplate.opsForSet().remove(CONSUMER_LIKE_SONGlIST + id, members.toArray());
            setCount(list1);
            return Result.ok(list1);
        }
        return Result.ok(new ArrayList<>());
    }

    @Override
    public Result consumerDeleteLikeSongList(Integer id) {
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        Long remove = stringRedisTemplate.opsForSet().remove(CONSUMER_LIKE_SONGlIST + o.getId(), id.toString());
        Long remove1 = stringRedisTemplate.opsForSet().remove(SONGLIST_LIKES + id, o.getId().toString());
        if(!Objects.isNull(remove) && !Objects.isNull(remove1)){
//            boolean update = lambdaUpdate().eq(SongList::getId, id).setSql("likes=likes-1").update();
            Double aDouble = stringRedisTemplate.opsForZSet().incrementScore(SONGLIST_LIKES_COUNT, id.toString(), -1);
            if(ObjectUtils.isNotEmpty(aDouble) && aDouble>=0) return Result.ok();
        }
        return Result.fail("取消收藏失败");
    }

    @Override
    public Result songListGetCountByLetter(String letter) {
        if(ObjectUtils.isEmpty(letter)){
            return Result.fail("查询失败");
        }
        Integer count;
        if("全部".equals(letter)){
            count = lambdaQuery().count();
            return Result.ok(count);
        }
        count=songListDao.songListGetCountByLetter(letter);
        return Result.ok(count);
    }

    @Override
    public Result songListGetByLetter(String letter, Integer currentPage, Integer pageSize) {
        if(ObjectUtils.isEmpty(letter)){
            return Result.fail("查询失败");
        }
        if("全部".equals(letter)){
            List<SongList> records = query().page(new Page<>(currentPage, pageSize)).getRecords();
            setCount(records);
            return Result.ok(records);
        }
        currentPage=(currentPage-1)*pageSize;
       List<SongList> list= songListDao.songListGetByLetter(letter,currentPage,pageSize);
       setCount(list);
       return Result.ok(list);
    }

    @Override
    public Result getLikeSongList(Integer id) {
        List<String> list=null;
        if(BooleanUtils.isTrue(stringRedisTemplate.hasKey(SONGLIST_LIKES + id))) {
            list = stringRedisTemplate.opsForSet().randomMembers(SONGLIST_LIKES + id, 8);
        }
        List<Consumer> res=new ArrayList<>();
        if(ObjectUtils.isNotEmpty(list)) {
            res=consumerService.lambdaQuery()
                    .select(Consumer::getId,Consumer::getAvator)
                    .in(Consumer::getId, list).list();
        }
        return Result.ok(res);
    }

    @Override
    public Result getTogetLikedSongList(Integer id) {
        Object o= SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        Consumer c= (Consumer) o;
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(CONSUMER_LIKE_SONGlIST + c.getId(), CONSUMER_LIKE_SONGlIST + id);
        List<SongList> list = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(intersect)) {
            list=lambdaQuery().in(SongList::getId, intersect).list();
        }
        setCount(list);
        return Result.ok(list);
    }

    @Override
    public void setCount(List<SongList> list1) {
        List<String> list=new ArrayList<>();
        list1.forEach(a-> {
            if(ObjectUtils.isNotEmpty(a))
            list.add(a.getId().toString());
        });
        List<Double> score=null;
        if(ObjectUtils.isNotEmpty(list))  score= stringRedisTemplate.opsForZSet().score(SONGLIST_PLAY_COUNT, list.toArray());
        if(ObjectUtils.isNotEmpty(score)){
            for (int i=0;i<score.size();i++){
                if(ObjectUtils.isEmpty(score.get(i))) list1.get(i).setCount(0);
                else list1.get(i).setCount(score.get(i).intValue());
            }
        }
        List<Double> score1=null;
        if(ObjectUtils.isNotEmpty(list)) score1= stringRedisTemplate.opsForZSet().score(SONGLIST_LIKES_COUNT, list.toArray());
        if(ObjectUtils.isNotEmpty(score1)){
            for (int i=0;i<score1.size();i++){
                if(ObjectUtils.isEmpty(score1.get(i))) list1.get(i).setLikes(0);
                else list1.get(i).setLikes(score1.get(i).intValue());
            }
        }
    }

    @Override
    public void deleteCount(List<Integer> list1) {
        List<String> list=new ArrayList<>();
        list1.forEach(a->list.add(a.toString()));
        stringRedisTemplate.opsForZSet().remove(SONGLIST_LIKES_COUNT,list.toArray());
        stringRedisTemplate.opsForZSet().remove(SONGLIST_PLAY_COUNT,list.toArray());
        List<String> list2=new ArrayList<>();
        list1.forEach(a->list2.add(SONGLIST_LIKES+a.toString()));
        stringRedisTemplate.delete(list2);
    }

    @Override
    public Result consumerCreatedSongList(SongList songList, MultipartFile file) {
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        songList.setCreatedConId(o.getId());
        if(ObjectUtils.isNotEmpty(file)){
            String s = FileUtil.uploadFile(file, "img", "songList");
            songList.setPic(s);
        }else {
            songList.setPic(SONG_LIST_DEFAULT_PIC);
        }
        songList.setCreatedTime(LocalDate.now());
        if(save(songList)){
            return Result.ok("创建成功");
        }
        return Result.fail("创建失败");
    }

    @Override
    public Result consumerGetCreatedSongList(Integer id) {
        List<SongList> list = lambdaQuery().eq(SongList::getCreatedConId, id).list();
        setCount(list);
        return Result.ok(list);
    }

    @Override
    public Result consumerAddSongGetSongList(Integer consumerId,Integer songId) {
        List<SongList> list=songListDao.consumerAddSongGetSongList(consumerId,songId);
        return Result.ok(list);
    }
}
