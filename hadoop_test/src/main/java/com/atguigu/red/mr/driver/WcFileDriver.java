package com.atguigu.red.mr.driver;


import com.atguigu.red.mr.bean.WCFIle;
import com.atguigu.red.mr.group.WcFileGroup;
import com.atguigu.red.mr.mapper.WcFileMapper;
import com.atguigu.red.mr.reducer.WcFileReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class WcFileDriver {
    public static void main(String[] args) throws Exception {

        Job job = Job.getInstance(new Configuration());
        job.setJarByClass(WcFileDriver.class);

        job.setMapperClass(WcFileMapper.class);
        job.setReducerClass(WcFileReducer.class);
//        job.setGroupingComparatorClass(WcFileGroup.class);

        job.setMapOutputKeyClass(WCFIle.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(WCFIle.class);
        job.setOutputValueClass(NullWritable.class);
//        job.setGroupingComparatorClass(WcFileGroup.class);

        FileInputFormat.setInputPaths(job, new Path("D:\\input\\input"));
        FileOutputFormat.setOutputPath(job, new Path("d:/output"));


        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);

    }
}
