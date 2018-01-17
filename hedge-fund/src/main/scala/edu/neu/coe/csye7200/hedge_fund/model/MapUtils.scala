package edu.neu.coe.csye7200.hedge_fund.model

/**
 * @author robinhillyard
 */
object MapUtils {
  import scala.language.postfixOps
  def flatten[K, V](x: Map[K, Option[V]]): Map[K, V] =
    // TODO fix this warning!
    x filter { case (k, v) => v match { case Some(x) => true; case _ => false } } map { case (k, Some(v)) => k -> v } toMap
}