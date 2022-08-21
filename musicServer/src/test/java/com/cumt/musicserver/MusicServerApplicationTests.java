package com.cumt.musicserver;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.cumt.musicserver.dao.ListSongDao;
import com.cumt.musicserver.dao.SongListDao;
import com.cumt.musicserver.domain.ListSong;
import com.cumt.musicserver.domain.Singer;
import com.cumt.musicserver.domain.Song;
import com.cumt.musicserver.domain.SongList;
import com.cumt.musicserver.service.IListSongService;
import com.cumt.musicserver.service.ISingerService;
import com.cumt.musicserver.service.ISongListService;
import com.cumt.musicserver.service.ISongService;
import com.cumt.musicserver.util.FileUtil;
import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.cumt.musicserver.util.StaticString.*;

@SpringBootTest
class MusicServerApplicationTests {

//    @Resource
//    private MinioClient minioClient;
//
//    @Resource
//    private ISingerService singerService;
//    @Resource
//    private StringRedisTemplate stringRedisTemplate;
//    @Resource
//    private ISongListService songListService;
//    @Resource
//    private IListSongService iListSongService;
//    @Resource
//    private ListSongDao listSongDao;
//
//    @Resource
//    private SongListDao songListDao;
//
//    @Resource
//    private ISongService songService;
//
//    @Test
//    public void test1(){
//        List<Singer> list = singerService.list();
//
//        Set<ZSetOperations.TypedTuple<String>> set=new HashSet<>();
//
//        for (int i = 0; i <list.size() ; i++) {
//            ZSetOperations.TypedTuple<String> t1=new DefaultTypedTuple<>(list.get(i).getId().toString(), (double) 0);
//            set.add(t1);
//        }
//        stringRedisTemplate.opsForZSet().add(SINGER_LIKES_COUNT,set);
//    }
//    @Test
//    public void test2(){
//        Random r=new Random();
//        List<Song> list = songService.list();
//        Set<ZSetOperations.TypedTuple<String>> set=new HashSet<>();
//
//        for (int i = 0; i <list.size() ; i++) {
//            ZSetOperations.TypedTuple<String> t1=new DefaultTypedTuple<>(list.get(i).getId().toString(), (double)r.nextInt(5000));
//            set.add(t1);
//        }
//        stringRedisTemplate.opsForZSet().add(SONG_LIKES,set);
//    }
//    @Test
//    public void test3() throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
//        int size = 0;
//        File file = new File("E:\\music\\阿杜\\差一点\\差一点.mp3");
//        MP3File f=(MP3File) AudioFileIO.read(file);
//        MP3AudioHeader audioHeader=(MP3AudioHeader)f.getAudioHeader();
//        size = audioHeader.getTrackLength();
//        System.out.println(size/60+":"+size%60);
//
//    }
//
//
//    public static final ThreadPoolExecutor t1=new ThreadPoolExecutor(
//            2,
//            2,
//            1,
//            TimeUnit.MINUTES,
//            new LinkedBlockingQueue<>(),
//            new ThreadPoolExecutor.AbortPolicy()
//    );
//
//
//    @Test
//    public void test4(){
//         File f1=new File("E:\\music1");
//         File[] files = f1.listFiles();
//         for (File f2:files){
//             oper(f2);
//         }
////        FileUtil.uploadFileByStream(f1,"music","store",".mp3","audio/mpeg");
////         t1.shutdown();
//    }
//
//
//    public void oper(File f2){
//        File[] f3=f2.listFiles();
//        String singerName=f2.getName();
//        Singer one = singerService.lambdaQuery().eq(Singer::getName, singerName).one();
//        System.out.println(1);
//        Integer singerId=null;
//        if(ObjectUtils.isNotEmpty(one))singerId=one.getId();
//        if(ObjectUtils.isEmpty(one)){
//            Singer s1=new Singer();
//            s1.setName(singerName);
//            s1.setPic(SINGER_DEFAULT_PIC);
//            singerService.save(s1);
//            singerId=s1.getId();
//            System.out.println(s1.getName()+"创建成功");
//        }else return;
//        for (File f4:f3){
//            if(isMp3(f4)){
//                File[] f5=f4.listFiles();
//                Song s1=new Song();
//                s1.setSingerId(singerId);
//                s1.setCreateTime(LocalDateTime.now());
//                s1.setName(f4.getName());
//                for (File f6:f5){
//                    String[] split = f6.getName().split("\\.");
//                    if(Objects.equals(split[split.length-1], "mp3")){
//                        s1.setSongTime(mp3Time(f6));
//                        String s = FileUtil.uploadFileByStream(f6, "music", "store", ".mp3", "audio/mpeg");
//                        s1.setUrl(s);
//                    }
//                    if(Objects.equals(split[split.length-1], "jpg")){
//                        String s = FileUtil.uploadFileByStream(f6, "img", "musicPic", ".jpg", "image/jpeg");
//                        s1.setPic(s);
//                    }
//                }
//                songService.save(s1);
//                System.out.println(s1.getName()+"上传成功");
//            }
//        }
//    }
//
//
//    public static String mp3Time(File file){
//        int size = 0;
//        MP3File f= null;
//        try {
//            f = (MP3File) AudioFileIO.read(file);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        MP3AudioHeader audioHeader=(MP3AudioHeader)f.getAudioHeader();
//        size = audioHeader.getTrackLength();
//        return size/60+":"+size%60;
//    }
//    public boolean isMp3(File f){
//        File[] f1=f.listFiles();
//        for(File f2:f1){
//            String[] split = f2.getName().split("\\.");
//            if(f2.isFile() && Objects.equals(split[split.length-1], "mp3")) return true;
//        }
//        return false;
//    }
//
//    @Test
//    public void existSinger(){
//        Singer one = singerService.lambdaQuery().eq(Singer::getName, "SHE").one();
//    }
//
//    @Test
//    public void test6(){
//        int index=1150;
//        for (int i = index; i <=1316 ; i++) {
//            Song one = songService.lambdaQuery().eq(Song::getId, i).one();
//            List<String> list =new ArrayList<>();
//            list.add(one.getPic());list.add(one.getUrl());
//            FileUtil.deleteFile(list);
//            songService.removeById(one.getId());
//            System.out.println(i+"删除");
//        }
//    }

}
