package com.atguigu.weibo.controller;

import com.atguigu.weibo.service.WeiboService;

import java.io.IOException;
import java.util.List;

public class WeiboController {

    private WeiboService service = new WeiboService();

    public void init() throws IOException {

        service.init();
    }


    //5) 发布微博内容
    public void publish(String star, String content) throws IOException {

        service.publish(star, content);
    }

    //6) 添加关注用户
    public void follow(String fans, String star) throws IOException {

        service.follow(fans, star);
    }

    //7) 移除（取关）用户
    public void unFollow(String fans, String star) throws IOException {

        service.unFollow(fans, star);
    }

    //8) 获取关注的人的微博内容
    //8.1 获取某个star的全部微博
    public void getAllWeibosByStarId(String star) throws IOException {

        List<String> weibos = service.getAllWeibosByStarId(star);

        for (String weibo : weibos) {
            System.out.println("weibo = " + weibo);
        }
    }

    //8.2 获取所有关注的stars的近期微博
    public void getAllRecentWeibos(String fans) throws IOException {

        List<String> weibos = service.getAllRecentWeibos(fans);

        for (String weibo : weibos) {
            System.out.println("weibo = " + weibo);
        }
    }

}
