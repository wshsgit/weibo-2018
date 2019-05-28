package com.atguigu.left2;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author fyy
 * @create 2019-04-09-16:53
 */
public class GroupDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());
        args = new String[] { "d:/input/pd.txt", "d:/output9" };
        job.setJarByClass(GroupDriver.class);

        job.setMapperClass(GroupMapper.class);
        job.setCombinerClass(GroupReduce.class);
        job.setNumReduceTasks(1);
        job.setReducerClass(GroupReduce.class);

        job.setMapOutputKeyClass(Orders.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setOutputKeyClass(Orders.class);
        job.setOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job,new Path(args[0]));
       FileOutputFormat.setOutputPath(job,new Path(args[1]));

       System.exit(job.waitForCompletion(true)?0:1);
    }
}
