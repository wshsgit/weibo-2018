package com.atguigu.weibo.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeiboDao {

    private static Connection connection = null;

    static {
        try {
            Configuration conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104");
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建命名空间
     *
     * @param namespace
     */
    public void createNameSpace(String namespace) throws IOException {

        Admin admin = connection.getAdmin();

        try {
            admin.getNamespaceDescriptor(namespace);
        }catch (NamespaceNotFoundException e){
            NamespaceDescriptor weibo = NamespaceDescriptor.create(namespace).build();
            admin.createNamespace(weibo);
        }finally {
            admin.close();
        }

    }


    /**
     * 创建表
     *
     * @param tableName
     * @param families
     * @throws IOException
     */
    public void createTable(String tableName, String... families) throws IOException {
        createTable(tableName, 1, families);

    }


    /**
     * 创建表
     *
     * @param tableName
     * @param versions
     * @param families
     * @throws IOException
     */
    public void createTable(String tableName, Integer versions, String... families) throws IOException {
        Admin admin = connection.getAdmin();

        if(admin.tableExists(TableName.valueOf(tableName))){
            return;
        }

        HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableName));

        for (String family : families) {

            HColumnDescriptor familyDesc = new HColumnDescriptor(family);

            familyDesc.setMaxVersions(versions);

            table.addFamily(familyDesc);
        }

        admin.createTable(table);

        admin.close();
    }


    /**
     * 插入一行中的某一列
     *
     * @param tableName
     * @param rowKey
     * @param family
     * @param column
     * @param value
     * @throws IOException
     */
    public void putCell(String tableName, String rowKey, String family, String column, String value) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));

        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));

        table.put(put);

        table.close();
    }


    /**
     * 根据rowKey的前缀获取所有的rowKey
     *
     * @param tableName
     * @param prefix
     * @return
     * @throws IOException
     */
    public List<String> getRowKeysByPrefix(String tableName, String prefix) throws IOException {

        List<String> list = new ArrayList<>();

        Table table = connection.getTable(TableName.valueOf(tableName));

        Scan scan = new Scan();

        scan.setRowPrefixFilter(Bytes.toBytes(prefix));
        ResultScanner scanner = table.getScanner(scan);

        for (Result result : scanner) {
            String row = Bytes.toString(result.getRow());
            list.add(row);
        }

        scanner.close();
        table.close();
        return list;
    }

    /**
     * 删除一行
     *
     * @param tableName
     * @param rowKey
     * @throws IOException
     */
    public void deleteRow(String tableName, String rowKey) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));

        Delete delete = new Delete(Bytes.toBytes(rowKey));
        table.delete(delete);

        table.close();
    }


    /**
     * 删除一行中某列的所有版本
     *
     * @param tableName
     * @param rowKey
     * @param family
     * @param column
     * @throws IOException
     */
    public void deleteCells(String tableName, String rowKey, String family, String column) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));

        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumns(Bytes.toBytes(family), Bytes.toBytes(column));

        table.delete(delete);

        table.close();
    }


    /**
     * 根据rowKey范围获取某列的值
     *
     * @param tableName
     * @param startRow
     * @param stopRow
     * @param family
     * @param column
     * @return
     * @throws IOException
     */
    public List<String> getCellsByRange(String tableName, String startRow, String stopRow, String family, String column) throws IOException {

        List<String> list = new ArrayList<>();

        Table table = connection.getTable(TableName.valueOf(tableName));

        Scan scan = new Scan(Bytes.toBytes(startRow), Bytes.toBytes(stopRow));
        scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));

        ResultScanner scanner = table.getScanner(scan);

        for (Result result : scanner) {
            for (Cell cell : result.rawCells()) {
                String weibo = Bytes.toString(CellUtil.cloneValue(cell));
                list.add(weibo);
            }
        }

        scanner.close();
        table.close();

        return list;
    }


    /**
     * 获取某个列族的所有版本数据
     *
     * @param tableName
     * @param rowKey
     * @param family
     * @return
     * @throws IOException
     */
    public List<String> getAllVersionsFamilyByRowKey(String tableName, String rowKey, String family) throws IOException {

        List<String> list = new ArrayList<>();

        Table table = connection.getTable(TableName.valueOf(tableName));

        Get get = new Get(Bytes.toBytes(rowKey));

        get.setMaxVersions();

        get.addFamily(Bytes.toBytes(family));

        Result result = table.get(get);

        for (Cell cell : result.rawCells()) {
            list.add(Bytes.toString(CellUtil.cloneValue(cell)));
        }

        table.close();

        return list;

    }


    public String getCellByRowKey(String tableName, String rowKey, String family, String column) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));

        Get get = new Get(Bytes.toBytes(rowKey));

        get.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));

        Result result = table.get(get);

        Cell[] cells = result.rawCells();

        table.close();

        return Bytes.toString(CellUtil.cloneValue(cells[0]));
    }

    public static void main(String[] args) throws IOException {

        new WeiboDao().createNameSpace("testtest");
    }
}
