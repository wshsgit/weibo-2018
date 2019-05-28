package com.atguigu.red.mr.group;


import com.atguigu.red.mr.bean.WCFIle;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class WcFileGroup extends WritableComparator {

    public WcFileGroup() {
        super(WCFIle.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {

        WCFIle group1 = (WCFIle) a;
        WCFIle group2 = (WCFIle) b;
        return group1.getFileName().compareTo(group2.getFileName());
    }
}
