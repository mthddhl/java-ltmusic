package com.cumt.musicserver.upload;


import com.alibaba.fastjson.JSONArray;
import com.cumt.musicserver.domain.Singer;
import com.cumt.musicserver.domain.Song;
import com.cumt.musicserver.service.ISingerService;
import com.cumt.musicserver.service.ISongService;
import com.cumt.musicserver.util.FileUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JsonTest {

    @Resource
    private ISongService songService;

    @Resource
    private ISingerService singerService;

    @Resource
    private FileUtil fileUtil;


    public final static String path="E:\\music";

    public static final ThreadPoolExecutor t1=new ThreadPoolExecutor(
            3,
            3,
            1,
            TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    public static void main(String[] args) throws IOException, InterruptedException {
        File origin=new File(path+"/dick");
        File[] files = origin.listFiles();
        for (File f:files) {
                t1.execute(() -> {
                    try {
                        test(f);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            }
        t1.shutdown();
        }

//        String s1="http://freetyst.nf.migu.cn/public/product9th/product46/2022/08/1209/2022%E5%B9%B408%E6%9C%8811%E6%97%A522%E7%82%B940%E5%88%86%E7%B4%A7%E6%80%A5%E5%86%85%E5%AE%B9%E5%87%86%E5%85%A5%E9%82%93%E7%B4%AB%E6%A3%8B%E5%B7%A5%E4%BD%9C%E5%AE%A4421%E9%A6%96015202/%E6%A0%87%E6%B8%85%E9%AB%98%E6%B8%85/MP3_128_16_Stero/69031400001092840.mp3?channelid=02&msisdn=a44779fa-f96c-4f8a-8831-0cac76c7fb70&Tim=1660560210780&Key=642648803923b302";
//        String decode = URLDecoder.decode(s1, "UTF-8");
//        downLoadFromUrl(decode,"111.mp3",path);



    public void test2(){
        File f1=new File(path);
        File[] files = f1.listFiles();
        for (File f2:files){
            File[] f3=f2.listFiles();
            String singerName=f2.getName();
            Singer one = singerService.lambdaQuery().eq(Singer::getName, singerName).one();
            Integer singerId=null;
            singerId=one.getId();
            if(ObjectUtils.isEmpty(one)){
                Singer s1=new Singer();
                s1.setName(singerName);
                singerService.save(s1);
                singerId=s1.getId();
            }
            for (File f4:f3){
                if(isMp3(f4)){
                    File[] f5=f4.listFiles();
                    Song s1=new Song();
                    s1.setSingerId(singerId);
                    s1.setCreateTime(LocalDateTime.now());
                    s1.setName(f4.getName());
                    for (File f6:f5){
                        if(Objects.equals(f6.getName().split("\\.")[0], "mp3")){
                            s1.setSongTime(mp3Time(f6));
                            String s = FileUtil.uploadFileByStream(f6, "music", "store", ".mp3", "audio/mp3");
                            s1.setUrl(s);
                        }
                        if(Objects.equals(f6.getName().split("\\.")[0], "jpg")){
                            FileUtil.uploadFileByStream(f6,"img","musicPic",".jpg","image/jpeg");
                        }
                    }
                    songService.save(s1);
                }
            }
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
    public boolean isMp3(File f){
        File[] f1=f.listFiles();
        for(File f2:f1){
            if(f2.isFile() && Objects.equals(f2.getName().split("\\.")[0], ".mp3")) return true;
        }
        return false;
    }


    public static void test(File f) throws IOException {
        String conver = JsonTest.conver(f);
        String s = f.getName().split(".tx")[0];
        List<Model> list = JSONArray.parseArray(conver, Model.class);
        File saveDir1 = new File(path+"/"+s);
        if(!saveDir1.exists()){
            saveDir1.mkdirs();
        }
        for(Model m:list) {

            String s1 = m.getName().split(" ")[0];
            s1=s1.split("\\(")[0];
            File saveDir = new File(saveDir1 + "/" + s1);
            if(!saveDir.exists()) saveDir.mkdirs();
            else continue;
            try {
                JsonTest.downLoadFromUrl(m.getCover(), s1 + ".jpg", saveDir.toString());
                downLoadFromUrl(m.getLrc(), s1 + ".lrc", saveDir.toString());
//                Thread.sleep(1000);
                try {
                    JsonTest.downLoadFromUrl(m.getUrl_320(), s1 + ".mp3", saveDir.toString());
                } catch (Exception e) {
                    String[] split = e.toString().split(":");
//                    System.out.println(split[1] + ":" + split[2]);
                    String encode = URLEncoder.encode(split[1] + ":" + split[2], "ISO-8859-1");
                    String replace = encode.substring(1).replace("%2F", "/").replace("%3A", ":");
                    JsonTest.downLoadFromUrl(replace, s1 + ".mp3", saveDir.toString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public static String conver(File file) throws IOException {
        File f =file;
        FileInputStream in = new FileInputStream(f);

        byte[] b = new byte[(int)f.length()];
        in.read(b);
        String s= new String(b);
        return s.substring(s.indexOf("["),s.indexOf("]")+1);
    }

    public static void downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdirs();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }

        System.out.println("info:"+fileName+" download success");

    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
