/**
  * Created by wss on 2019/4/10
  */
object MergeSort {
  def main(args: Array[String]): Unit = {
    val data = Array(9, -16, 21, 23, -30, -49, 21, 30, 30)
    System.out.println("排序之前：\n" + java.util.Arrays.toString(data))
    merge_sort(data, data.length)
    System.out.println("排序之后：\n" + java.util.Arrays.toString(data))
  }

  private def merge_sort(data: Array[Int], length: Int): Unit = {
    merge_sort_c(data, 0, length - 1)
  }

  private def merge_sort_c(data: Array[Int], left: Int, right: Int): Unit = {
    if (left < right) {
      val mid = (left + right) / 2
      merge_sort_c(data, left, mid)
      merge_sort_c(data, mid + 1, right)
      merge(data, left, mid, right)
    }
  }

  private def merge(data: Array[Int], left: Int, mid: Int, right: Int): Unit = {
    var i = left
    var j = mid + 1
    var k = left
    var temp = left
    val tempData = new Array[Int](data.length)
    while ( {
      i < mid && j < right
    })
      if (data(i) <= data(j)) tempData({
      k += 1; k - 1
    }) = data({
      i += 1; i - 1
    })
    else tempData({
      k += 1; k - 1
    }) = data({
      j += 1; j - 1
    })
    var start = i
    var end = mid
    if (j < right) {
      start = j
      end = right
    }
    while ( {
      start < end
    }) tempData({
      k += 1; k - 1
    }) = data({
      start += 1; start - 1
    })
    while ( {
      temp <= right
    }) data(temp) = tempData({
      temp += 1; temp - 1
    })
  }
}
