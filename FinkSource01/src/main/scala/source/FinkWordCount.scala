package source

import org.apache.flink.streaming.api.scala._

/**
  * Created by wss on 2019/3/4
  */
object FinkWordCount {

  def main(args: Array[String]): Unit = {
    val environment = StreamExecutionEnvironment.getExecutionEnvironment
    val  stream = environment.readTextFile("test00.txt")

    stream.flatMap(item=>item.split(" ")).filter(item => "hadoop".equals(item)).print()

//    stream.print()
    environment.execute()

  }

}
