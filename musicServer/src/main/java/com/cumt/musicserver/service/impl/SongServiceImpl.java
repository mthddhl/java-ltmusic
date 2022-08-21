package com.cumt.musicserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cumt.musicserver.domain.Consumer;
import com.cumt.musicserver.dao.SongDao;
import com.cumt.musicserver.domain.Singer;
import com.cumt.musicserver.domain.Song;
import com.cumt.musicserver.service.ISingerService;
import com.cumt.musicserver.service.ISongService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cumt.musicserver.util.FileUtil;
import com.cumt.musicserver.util.Result;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
@Transactional
public class SongServiceImpl extends ServiceImpl<SongDao, Song> implements ISongService {
    @Resource
    private SongDao songDao;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ISingerService singerService;
    @Override
    public Result upload(MultipartFile musicFile, MultipartFile picFile, Song song) {
        if(ObjectUtils.isNotEmpty(musicFile)){
            setSongTime(musicFile,song);
            String s = FileUtil.uploadFile(musicFile, "music", "store");
            song.setUrl(s);
        }
        if(ObjectUtils.isNotEmpty(picFile)){
            String s=FileUtil.uploadFile(picFile,"img","musicPic");
            song.setPic(s);
        }else {
            song.setPic(SONG_DEFAULT_PIC);
        }
        song.setCreateTime(LocalDateTime.now());
        boolean save = save(song);
        if(save){
            return Result.ok("上传成功");
        }else {
            List<String> delete=new ArrayList<>();
            delete.add(song.getPic());delete.add(song.getUrl());
            FileUtil.deleteFile(delete);
            return Result.fail("上传失败");
        }
    }

    private void setSongTime(MultipartFile musicFile, Song song) {
        try {
            String path = ResourceUtils.getURL("classpath:").getPath()+"/static/files/";
            File file=new File(path+musicFile.getOriginalFilename());
            FileUtils.copyInputStreamToFile(musicFile.getInputStream(), file);
            song.setSongTime(mp3Time(file));
            if(file.exists()){
                file.delete();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String mp3Time(File file){
        int size = 0;
        MP3File f= null;
        try {
            f = (MP3File) AudioFileIO.read(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        MP3AudioHeader audioHeader=(MP3AudioHeader)f.getAudioHeader();
        size = audioHeader.getTrackLength();
        return size/60+":"+size%60;
    }

    @Override
    public Result getAllList(Integer singerId) {
//        List<Song> list = query().eq("singer_id",singerId).list();
        List<Song> res=songDao.getAllList(singerId);
        return Result.ok(res);
    }

    @Override
    public Result getSongPage(Integer currentPage, Integer pageSize, Integer singerId) {
        currentPage=(currentPage-1)*pageSize;
        List<Song> songs=songDao.getSongBySingId(currentPage,pageSize,singerId);
//         = query().eq("singer_id", singerId).page(new Page<>(currentPage, pageSize)).getRecords();
        if(ObjectUtils.isEmpty(songs)){
            return Result.fail("无数据");
        }
        songLiked(songs);
        return Result.ok(songs);
    }

    @Override
    public Result songGetNameAndIntro(String name,Integer singerId) {
        ArrayList<String> result = new ArrayList<>();
        List<Song> list1=query().select("name")
                .eq("singer_id",singerId).likeRight("name", name).list();
        list1.forEach(each->{
            if(each.getName()!=null && !"".equals(each.getName())){
                result.add(each.getName());
            }
        });
        List<Song> list2=query().select("introduction")
                .eq("singer_id",singerId).likeRight("introduction", name).list();
        list2.forEach(each->{
            if(each.getIntroduction()!=null && !"".equals(each.getIntroduction())){
                result.add(each.getIntroduction());
            }
        });
        return Result.ok(result);
    }

    @Override
    public Result getSongLikes(String name) {
        List<Song> list = query().eq("name", name).or().eq("introduction", name).list();
        return Result.ok(list);
    }

    @Override
    @Transactional
    public Result updateSong(Song song,MultipartFile musicFile,MultipartFile picFile) {
        Song songO=lambdaQuery().select(Song::getUrl,Song::getPic)
                .eq(Song::getId,song.getId()).one();
        String musicUrl=songO.getUrl();
        String musicPic=songO.getPic();

        if(ObjectUtils.isNotEmpty(musicFile)){
            song.setUrl(FileUtil.uploadFile(musicFile,"music", "store"));
            setSongTime(musicFile,song);
        }
        if(ObjectUtils.isNotEmpty(picFile)){
            song.setPic(FileUtil.uploadFile(picFile,"img","musicPic"));
        }
        song.setUpdateTime(LocalDateTime.now());
        boolean b = updateById(song);
        if(b) {
            if(!SONG_DEFAULT_PIC.equals(musicPic) && ObjectUtils.isNotEmpty(picFile)){
                FileUtil.deleteFile(Collections.singletonList(musicPic));
            }
            if(ObjectUtils.isNotEmpty(musicFile)){
                FileUtil.deleteFile(Collections.singletonList(musicUrl));
            }
            return Result.ok("更新成功");
        }
        return Result.fail("更新失败");
    }
    @Override
    public Result deleteSongById(String id) {
        List<Song> list = lambdaQuery().select(Song::getId, Song::getPic,Song::getUrl).eq(Song::getId, id).list();
        if(removeById(id)){
            deleteSongs(list);
            return Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }
    @Override
    public Result deleteSongByIds(List<Integer> ids) {
        List<Song> list = lambdaQuery().select(Song::getId, Song::getPic,Song::getUrl).in(Song::getId, ids).list();
        if(removeByIds(ids)){
            deleteSongs(list);
            return Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }

    private void deleteSongs(List<Song> listSongs){
        List<String> list=new ArrayList<>();
        List<String> idList=new ArrayList<>();
        for(Song s:listSongs){
            if(!SONG_DEFAULT_PIC.equals(s.getPic())) {
                list.add(s.getUrl());
                list.add(s.getPic());
            }
            idList.add(s.getId().toString());
        }
        FileUtil.deleteFile(list);
        stringRedisTemplate.opsForZSet().remove(SONG_LIKES,idList.toArray());
    }
    @Override
    public Result restoreSong() {
        boolean b=update().set("delete_logic",true).update();
        return b? Result.ok("恢复成功") : Result.fail("恢复失败");
    }

    @Override
    public Result getSongCountForSinger() {
        QueryWrapper<Song> query = new QueryWrapper<>();
        query.select("singer_id as singerId","count(*) as count").groupBy("singer_id");
        List<Map<String, Object>> maps = songDao.getSongCountForSinger();
        return Result.ok(maps);
    }

    @Override
    public Result getPartSong() {

        Set<String> range = stringRedisTemplate.opsForZSet().reverseRange(SONG_LIKES, 0, 9);
        List<Song> list = songDao.getPartSong(range);
        Collections.shuffle(list);
        songLiked(list);
        return Result.ok(list);
    }

    @Override
    public Result getSongBySongListId(Integer currentPage, Integer pageSize, Integer songListId) {
        currentPage=(currentPage-1)*pageSize;
        List<Song> list = songDao.getSongBySongListIdPage(currentPage,pageSize,songListId);
        songLiked(list);
        return Result.ok(list);
    }

    @Override
    public Result consumerLikeSong(Integer songId, Boolean isLike) {
        Consumer o= (Consumer) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        stringRedisTemplate.setEnableTransactionSupport(true);
        if(BooleanUtils.isTrue(isLike)){
            stringRedisTemplate.multi();
            stringRedisTemplate.opsForZSet().remove(CONSUMER_LIKE_SONGS_SORT + o.getId().toString(), songId.toString());
            stringRedisTemplate.opsForSet().remove(CONSUMER_LIKE_SONGS + o.getId().toString(), songId.toString());
            stringRedisTemplate.opsForZSet().incrementScore(SONG_LIKES,songId.toString(),-1);
            List<Object> exec = stringRedisTemplate.exec();
            stringRedisTemplate.setEnableTransactionSupport(false);
            if(ObjectUtils.isNotEmpty(exec) && operSuc(exec)){
                return Result.ok("取消收藏成功");
            }
        }else {
            stringRedisTemplate.multi();
            stringRedisTemplate.opsForZSet().add(CONSUMER_LIKE_SONGS_SORT + o.getId().toString(), songId.toString(), System.currentTimeMillis());
            stringRedisTemplate.opsForSet().add(CONSUMER_LIKE_SONGS + o.getId().toString(), songId.toString());
            stringRedisTemplate.opsForZSet().incrementScore(SONG_LIKES,songId.toString(),1);
            List<Object> exec = stringRedisTemplate.exec();
            stringRedisTemplate.setEnableTransactionSupport(false);
            if(ObjectUtils.isNotEmpty(exec) && operSuc(exec)) return Result.ok("收藏成功");
        }
        return Result.fail("操作失败");
    }



    public final static ExecutorService threadPool = Executors.newSingleThreadExecutor();

    @Override
    public Result getConsumerLikeSong(Integer id) {
        Set<String> range = stringRedisTemplate.opsForZSet().range(CONSUMER_LIKE_SONGS_SORT + id.toString(), 0, -1);
        List<Integer> list=new ArrayList<>();
        if (ObjectUtils.isNotEmpty(range)) {
            range.forEach(each-> list.add(Integer.valueOf(each)));
        }
        List<Song> listRes=new ArrayList<>();
        if(ObjectUtils.isNotEmpty(list)) {
            listRes = songDao.getConsumerLikeSong(list);
            songLiked(listRes);
        }
        Set<String> set=new HashSet<>();
        listRes.forEach(a-> set.add(a.getId().toString()));
        threadPool.execute(()->{
            if(ObjectUtils.isNotEmpty(range)) {
                range.removeAll(set);
                if(ObjectUtils.isNotEmpty(range)) {
                    stringRedisTemplate.opsForSet().remove(CONSUMER_LIKE_SONGS + id, set.toArray());
                    stringRedisTemplate.opsForZSet().remove(CONSUMER_LIKE_SONGS_SORT + id, set.toArray());
                }
            }
        });
        return Result.ok(listRes);
    }

    public boolean operSuc(List<Object> list){
        boolean flag=true;
        for (Object o:list){
            if(o instanceof Boolean){
                if(BooleanUtils.isFalse((Boolean) o)){
                    flag=false;
                }
            } else if (o instanceof Integer) {
                if((Integer) o==0)flag=false;
            }
        }
        return flag;
    }

    @Override
    public void songLiked(List<Song> list){
        Object o= SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        if(o instanceof Consumer){
            Consumer c=(Consumer) o;

            List<String> listId=new ArrayList<>();
            list.forEach(each-> listId.add(each.getId().toString()));
            if(BooleanUtils.isTrue(stringRedisTemplate.hasKey(CONSUMER_LIKE_SONGS + c.getId().toString()))
                    && ObjectUtils.isNotEmpty(listId)){
                Map<Object, Boolean> member = stringRedisTemplate.opsForSet()
                        .isMember(CONSUMER_LIKE_SONGS + c.getId().toString(), listId.toArray());
                if(ObjectUtils.isNotEmpty(member)) {
                    for(Song each:list){
                        if (BooleanUtils.isTrue(member.get(each.getId().toString()))) {
                            each.setLiked(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public Result convertVipStatus(Integer id) {
        Song one = lambdaQuery().select(Song::getIsVip).eq(Song::getId, id).one();
        boolean update = lambdaUpdate().eq(Song::getId, id).set(Song::getIsVip,!one.getIsVip()).update();
        if(update){
            return Result.ok("转换成功");
        }
        return Result.fail("转换失败");
    }
}
