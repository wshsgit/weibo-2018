package com.atguigu.red.mr.bean;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class WCFIle implements WritableComparable<WCFIle> {

    /**
     * 单词
     */
    private String word;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 总数
     */
    private Integer count;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(fileName);
        out.writeInt(count);
        out.writeUTF(word);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        fileName = in.readUTF();
        count = in.readInt();
        word = in.readUTF();

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(word).append('\t');
        sb.append(fileName).append('\t');
        sb.append(count);
        return sb.toString();
    }

    @Override
    public int compareTo(WCFIle o) {
        int compare = o.getWord().compareTo(this.word);
        if (compare == 0) {
            return o.getFileName().compareTo(this.fileName);
        }
        return compare;
    }
}
