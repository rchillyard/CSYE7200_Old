import org.apache.spark.SparkContext

/**
  * Created by scalaprof on 10/19/16.
  */
class ApplicationClass {

  def doSomethingUseful(): Unit = {
    val sc: SparkContext = ContextServer.sc
    println("I have a SparkContext")
  }
}
