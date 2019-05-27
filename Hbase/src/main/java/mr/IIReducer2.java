package mr;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class IIReducer2 extends Reducer<Text, Text, Text, Text> {
    private Text v = new Text();
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //累加前清零
        stringBuilder.delete(0, stringBuilder.length());
        for (Text value : values) {
            stringBuilder.append(value.toString()).append(" ");
        }
        v.set(stringBuilder.toString());
        context.write(key, v);
    }
}
