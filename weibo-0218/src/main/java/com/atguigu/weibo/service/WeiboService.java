package com.atguigu.weibo.service;

import com.atguigu.weibo.constant.Names;
import com.atguigu.weibo.dao.WeiboDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeiboService {

    private WeiboDao dao = new WeiboDao();

    public void init() throws IOException {

        //1) 创建命名空间以及表名的定义
        dao.createNameSpace(Names.NAMESPACE_WEIBO);

        //2) 创建微博内容表
        dao.createTable(Names.TABLE_WEIBO, Names.WEIBO_FAMILY_DATA);

        //3) 创建用户关系表
        dao.createTable(Names.TABLE_RELATION, Names.RELATION_FAMILY_DATA);

        //4) 创建用户微博内容接收邮件表
        dao.createTable(Names.TABLE_INBOX, Names.INBOX_FAMILY_VERSIONS, Names.INBOX_FAMILY_DATA);

    }


    public void publish(String star, String content) throws IOException {

        //1.往weibo中插入一条数据
        String rowKey = star + "_" + System.currentTimeMillis();
        dao.putCell(Names.TABLE_WEIBO, rowKey, Names.WEIBO_FAMILY_DATA, Names.WEIBO_COLUMN_CONTENT, content);

        //2.从relation中获取所有fans的id
        String prefix = star + ":followedby:";
        List<String> rowKeys = dao.getRowKeysByPrefix(Names.TABLE_RELATION, prefix);

        //如果该star无fans，直接返回
        if (rowKeys.size() <= 0) return;

        List<String> fansIds = new ArrayList<>();

        for (String key : rowKeys) {
            fansIds.add(key.split(":")[2]);
        }

        //3.往所有fans的inbox中插入本条微博的id
        for (String fansId : fansIds) {
            dao.putCell(Names.TABLE_INBOX, fansId, Names.INBOX_FAMILY_DATA, star, rowKey);
        }
    }

    public void follow(String fans, String star) throws IOException {

        //1.往relation中插入两条数据
        String rowKey1 = fans + ":follow:" + star;
        String rowKey2 = star + ":followedby:" + fans;
        String time = System.currentTimeMillis() + "";
        dao.putCell(Names.TABLE_RELATION, rowKey1, Names.RELATION_FAMILY_DATA, Names.RELATION_COLUMN_TIME, time);
        dao.putCell(Names.TABLE_RELATION, rowKey2, Names.RELATION_FAMILY_DATA, Names.RELATION_COLUMN_TIME, time);


        //2.从weibo中获取star近期的5条weiboId
        List<String> rowKeys = dao.getRowKeysByPrefix(Names.TABLE_WEIBO, star);

        int fromIndex = rowKeys.size() > Names.INBOX_FAMILY_VERSIONS ? rowKeys.size() - Names.INBOX_FAMILY_VERSIONS : 0;
        List<String> recentWeiboIds = rowKeys.subList(fromIndex, rowKeys.size());

        //3.将star的近期weiboId插入fans的inbox中
        for (String recentWeiboId : recentWeiboIds) {
            dao.putCell(Names.TABLE_INBOX, fans, Names.INBOX_FAMILY_DATA, star, recentWeiboId);
        }
    }

    public void unFollow(String fans, String star) throws IOException {

        //1.从relation中删除两条数据
        String rowKey1 = fans + ":follow:" + star;
        String rowKey2 = star + ":followedby:" + fans;
        dao.deleteRow(Names.TABLE_RELATION, rowKey1);
        dao.deleteRow(Names.TABLE_RELATION, rowKey2);

        //2.从fans的inbox中删除star的近期微博
        dao.deleteCells(Names.TABLE_INBOX, fans, Names.INBOX_FAMILY_DATA, star);

    }

    public List<String> getAllWeibosByStarId(String star) throws IOException {

        String startRow = star;
        String stopRow = star + "_|";
        return dao.getCellsByRange(Names.TABLE_WEIBO, startRow, stopRow, Names.WEIBO_FAMILY_DATA, Names.WEIBO_COLUMN_CONTENT);
    }

    public List<String> getAllRecentWeibos(String fans) throws IOException {

        List<String> weibos = new ArrayList<>();

        //1.从inbox中获取所有的weiboIds
        List<String> weiboIds = dao.getAllVersionsFamilyByRowKey(Names.TABLE_INBOX, fans, Names.INBOX_FAMILY_DATA);

        //2.根据weiboIds去weibo表中获取微博内容
        for (String weiboId : weiboIds) {
            String weibo = dao.getCellByRowKey(Names.TABLE_WEIBO,weiboId,Names.WEIBO_FAMILY_DATA,Names.WEIBO_COLUMN_CONTENT);
            weibos.add(weibo);
        }

        return weibos;
    }
}
