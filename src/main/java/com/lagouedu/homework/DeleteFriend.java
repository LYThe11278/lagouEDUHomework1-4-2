package com.lagouedu.homework;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * 需求：Hbase的表需要进行同步删除，则需要通过Hbase的协处理器完成。
 * 思路：1、通过java生成一个对象继承协处理器BaseRegionObserver，重写postDelete。
 * 2、监听表的删除操作，
 * 3、通过postdelete获取到删除对象delete，
 * 4、通过delete对象获取执行删除的列族队列集合，循环队列获取cells集合，
 * 5、循环cells判断每一个cell是否包含相同rowkey的列，如果是则生成一个delQualifier对象进行删除
 */
public class DeleteFriend extends BaseRegionObserver {

    @Override
    public void postDelete(ObserverContext<RegionCoprocessorEnvironment> e, final Delete delete, WALEdit edit, Durability durability) throws IOException {
        HTableWrapper userRel = (HTableWrapper) e.getEnvironment().getTable(TableName.valueOf("user_friend"));
        //获取删除的行rowkey
        byte[] row = delete.getRow();
        //获取到firends下所有的cell
        List<Cell> friends = delete.getFamilyCellMap().get(Bytes.toBytes("friends"));
        for (Cell friend : friends) {
            byte[] bytes = CellUtil.cloneQualifier(friend);
            Delete delete1 = new Delete(bytes);
            delete1.addColumn(Bytes.toBytes("friends"), row);
            userRel.delete(delete1);
        }
        userRel.flushCommits();
        userRel.close();
    }
}
