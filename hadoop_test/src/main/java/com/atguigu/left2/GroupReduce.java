package com.atguigu.left2;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author fyy
 * @create 2019-04-09-16:48}
 */
public class GroupReduce extends Reducer<Orders,NullWritable,Orders,NullWritable> {
    private int i= 0;

    @Override
    protected void reduce(Orders key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        i = 0;
        for (NullWritable value : values) {
            if(i<3){
                context.write(key,NullWritable.get());
                i++;
            }

        }
    }
}
