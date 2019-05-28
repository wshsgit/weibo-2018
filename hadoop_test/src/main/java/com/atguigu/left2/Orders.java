package com.atguigu.left2;

import lombok.Data;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author fyy
 * @create 2019-04-09-16:26}
 */

@Data
public class Orders implements WritableComparable<Orders> {

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    private String orderId;

    private String productId;

    private Double price;

    @Override
    public int compareTo(Orders o) {
        int compare = o.getOrderId().compareTo(this.orderId);
        if( compare== 0){
            return Double.compare(o.price,this.price);
        }else {
            return compare;
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(orderId);
        out.writeUTF(productId);
        out.writeDouble(price);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        orderId = in.readUTF();
        productId = in.readUTF();
        price = in.readDouble();
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", price=" + price +
                '}';
    }
}
