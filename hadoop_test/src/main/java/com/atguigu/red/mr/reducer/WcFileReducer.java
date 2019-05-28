package com.atguigu.red.mr.reducer;


import com.atguigu.red.mr.bean.WCFIle;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WcFileReducer extends Reducer<WCFIle, NullWritable, WCFIle, NullWritable> {

    int count;

    @Override
    protected void reduce(WCFIle key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {

        for (NullWritable value : values) {
            count += key.getCount();
            System.out.println(key.getWord()+"**************************"+key.getFileName());
        }
        key.setCount(count);
        context.write(key,NullWritable.get());

    }
}
