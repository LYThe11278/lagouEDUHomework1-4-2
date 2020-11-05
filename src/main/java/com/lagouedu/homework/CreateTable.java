package com.lagouedu.homework;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

/**
 * 思路：
 * Hbase基本API创建表，参照文档即可
 */
public class CreateTable {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","bd01,bd02,bd03");
        conf.set("hbase.zookeeper.property.clientPort","2181");
       Connection conn = ConnectionFactory.createConnection(conf);

       HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
        //创建表描述器
        HTableDescriptor userFriend = new HTableDescriptor(TableName.valueOf("user_friend"));
        //设置列族描述器
        userFriend.addFamily(new HColumnDescriptor("friends"));
        //执⾏创建操作
        admin.createTable(userFriend);
        System.out.println("userFriend创建成功！！");

        admin.close();
        conn.close();

    }
}
