package com.atguigu.right_1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;

public class MDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(MDriver.class);

        job.setMapperClass(MMapper.class);
        job.setNumReduceTasks(0);

        job.addCacheFile(URI.create("file:///d:/input/pd.txt"));


        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        FileInputFormat.setInputPaths(job, new Path("d:\\input\\order.txt"));
        FileOutputFormat.setOutputPath(job, new Path("d:\\output5"));

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}
