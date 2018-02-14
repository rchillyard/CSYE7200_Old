package edu.neu.coe.csye7200

trait Base {
  val name: String
}
trait Organelle

trait Organism {
  def genotype: Seq[Base]
}
trait Eukaryote extends Organism {
  def organelles: Seq[Organelle]
}
trait Animal extends Eukaryote {
  def female: Boolean
  def organelles: Seq[Organelle] = Nil
}
trait Vertebrate extends Animal {
  def vertebra: Int
  def sound: Sound
}
trait Sound {
  def sound: Seq[Byte]
}
trait Voice extends Sound with (()=>String) {
  def sound: Seq[Byte] = apply().getBytes
}
trait Bear extends Mammal {
  def sound = Growl
  def growl: String
}
case object Woof extends Voice {
  def apply(): String = "Woof!"
}
case object Growl extends Sound {
  def sound: Seq[Byte] = "growl".getBytes
}
trait Mammal extends Vertebrate {
  def vertebra: Int = 33
}
trait Pet {
  def name: String
}
trait Dog extends Mammal with Pet {
  def sound = Woof
  def genotype: Seq[Base] = Nil
}
case class Chihuahua(name: String, female: Boolean, color: String) extends Dog

case class Pets[+X <: Pet with Mammal, -Y <: Sound](xs: Seq[X]) {
  def identify(s: String): X = xs find (_.name==s) get
  def sounders(y: Y): Seq[X] = xs filter(_.sound==y)
}
object Pets extends App {
  def create[X <: Pet with Mammal, Y <: Sound](xs: X*): Pets[X, Y] = Pets(xs)
  // This method takes a Chihuahua and returns it as a Dog which works because Chihuahua is a subtype of Dog.
  // All of the required properties of Dog are specified by any instance of Chihuahua
  def asDog(x: Chihuahua): Dog = x
  val bentley = Chihuahua("Bentley", false, "black")
  val gingerSnap = Chihuahua("GingerSnap", true, "ginger")
  val pixie = Chihuahua("Ralphie", true, "white")
  // List[Chihuhua] is a subtype of Seq[Dog] because A is covariant in Seq[A] and because List is a subtype of Seq
  val dogs: Seq[Dog] = List(bentley, gingerSnap, pixie)
  val pets: Pets[Dog, Voice] = Pets.create[Chihuahua, Sound](bentley, gingerSnap, pixie)
  // Dog is a subtype of Mammal: all of the required properties of Mammal are specified by any instance of Dog
  val m: Mammal = asDog(bentley)
  val ps = pets.sounders(Woof)
  println(ps.mkString(","))
}