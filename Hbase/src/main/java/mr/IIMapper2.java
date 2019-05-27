package mr;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class IIMapper2 extends Mapper<LongWritable, Text, Text, Text> {

    private Text key = new Text();
    private Text value = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        this.key.set(split[0]);
        System.out.println(split);
        this.value.set(split[1] + "-->" + split[2]);
        context.write(this.key, this.value);
    }
}
