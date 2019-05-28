/**
  * Created by wss on 2019/4/10
  */
object QuickSort {
  def main(args: Array[String]) {

    val data = Array(9, -16, 21, 23, -30, -49, 21, 30, 30)
    System.out.println("排序之前：\n" + java.util.Arrays.toString(data))

    quicksort(0, data.length - 1, data); //快速排序调用

    System.out.println("排序之后：\n" + java.util.Arrays.toString(data))

  }

  def quicksort(left: Int, right: Int, ary_input: Array[Int]): Array[Int] = {
    if (left > right) {
      return ary_input;
    }
    val temp = ary_input(left); //temp中存的就是基准数
    var i = left;
    var j = right;
    while (i != j) {
      //顺序很重要，要先从右往左找
      while (ary_input(j) >= temp && i < j) {
        j -= 1;
      }
      //再从左往右找
      while (ary_input(i) <= temp && i < j) {
        i += 1;
      }

      //交换两个数在数组中的位置
      if (i < j) //当哨兵i和哨兵j没有相遇时
      {
        val t = ary_input(i);
        ary_input(i) = ary_input(j);
        ary_input(j) = t;
      }
    }
    //最终将基准数归位
    ary_input(left) = ary_input(i);
    ary_input(i) = temp;
    val ary_input_l = quicksort(left, i - 1, ary_input); //继续处理左边的，这里是一个递归的过程
    val ary_input_r = quicksort(i + 1, right, ary_input_l); //继续处理右边的，这里是一个递归的过程
    return ary_input_r;
  }
}