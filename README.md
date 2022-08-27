# music
项目名：聆听音乐<br>
●相关技术：springboot+mysql(mybatisPlus)+redis+nginx+springsecurity+RabbitMq+minio+Vue+elementui<br>
●项目前台链接： http://120.25.161.31/ ，代码链接： https://github.com/mthddhl/music.git<br>
●项目介绍：基于个人兴趣建立的一个音乐网站，集合歌单、歌手、歌曲、登录、点赞、评论等。此站分为前台与后台，后台主要负责资源的的管理，前台用于向用户展示资源。<br>
●目前进度：<br>
1、基于 mybatisplus 完成资源的增删改查；<br>
2、基于 redis 完成用户点赞、收藏记录，歌单、歌手收藏、播放计数；<br>
3、基于 springsecurity 完成用户登录于权限控制，redis+token 维护登陆状态；<br>
4、基于 minio 完成对音乐、图片文件的管理；<br>
5、另建模块接入支付宝支付完成会员支付功能，利用 RabbitMQ 进行两模块间异步通信，nginx 做请求调度处理；<br>
6、基于 Vue+elementui 完成对前端的构建；<br>
待完成：<br>
1、播放详情页<br>
2、MV功能<br>
下载至本地时一定将以下文件得url与IP做修改：<br>
1、cli/src/ajax/request.js<br>
2、cli/src/store/store.js<br>
3、music-manage/src/api/http.js<br>
4、music-manage/src/store/index.js<br>
5、musicPay/src/main/java/com/cumt/musicpay/config/MyPayConfig.java<br>
6、musicPay/src/main/resources/alipay.properties（这个与上一个文件组成支付宝沙箱支付数据，请注意自己申请沙箱，并做修改）<br>
7、musicPay/src/main/resources/application.yaml<br>
8、musicServer/src/main/java/com/cumt/musicserver/config/WebMvConfig.java<br>
9、musicServer/src/main/resources/application.yaml<br>
10、nginx得conf配置<br>
