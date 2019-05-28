package com.atguigu.left2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author fyy
 * @create 2019-04-09-16:39}
 */
public class GroupMapper extends Mapper<LongWritable,Text,Orders,NullWritable> {

    private Orders orders = new Orders();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        orders.setOrderId(split[0]);
        orders.setProductId(split[1]);
        orders.setPrice(Double.parseDouble(split[2]));
        context.write(orders,NullWritable.get());
    }
}
