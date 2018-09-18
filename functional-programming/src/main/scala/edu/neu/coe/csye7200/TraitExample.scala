package edu.neu.coe.csye7200

sealed trait TraitExample[T] extends Comparable[TraitExample[T]] {

  def name: String
  def property: T

  def compareTo(o: TraitExample[T]): Int = name.compareTo(o.name)

  def >(o: TraitExample[T]): Boolean = compareTo(o)>0
  def <(o: TraitExample[T]): Boolean = compareTo(o)<0
  def >=(o: TraitExample[T]): Boolean = compareTo(o)>=0
  def <=(o: TraitExample[T]): Boolean = compareTo(o)<=0
  def ==(o: TraitExample[T]): Boolean = compareTo(o)==0
}

case class Telephone(name: String, number: String) extends TraitExample[String] {
  override def property: String = number
  val s = new Silly {}
  val xs = Seq[Int]()
  for (x <- xs) println(x)
  xs foreach println
}

case class Age(name: String, age: Int) extends TraitExample[Int] {
  override def property: Int = age
}

trait Silly