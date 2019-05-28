package com.atguigu.red.mr.mapper;


import com.atguigu.red.mr.bean.WCFIle;
import com.atguigu.red.mr.constant.SplitEnum;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class WcFileMapper extends Mapper<LongWritable, Text, WCFIle, NullWritable> {


    private String fileName;
    private WCFIle val = new WCFIle();
    int one = 1;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        FileSplit inputSplit = (FileSplit) context.getInputSplit();
        fileName = inputSplit.getPath().getName();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] words = value.toString().split(SplitEnum.SPACE.getValue());
        for (int i = 0; i < words.length; i++) {
            val.setWord(words[i]);
            val.setFileName(fileName);
            val.setCount(one);
            context.write(val, NullWritable.get());
        }

    }
}
