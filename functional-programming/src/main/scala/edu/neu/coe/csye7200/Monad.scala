package edu.neu.coe.csye7200

import scala.language.higherKinds
import scala.util.{Success, Try}


trait Monad[M[_]] extends Functor[M] {
  def unit[A](a: A): M[A]

  def bind[A, B](am: M[A])(f: A => M[B]): M[B]

  def map[A, B](am: M[A])(f: A => B): M[B] = bind(am)(a => unit[B](f(a)))

  def map[A, B, C](am: M[A], bm: M[B])(f: (A, B) => C): M[C] = bind(am) { a => map(bm)(b => f(a, b)) }
}

object Monad {

  implicit object OptionMonad extends Monad[Option] {
    def unit[A](a: A) = Some(a)

    def bind[A, B](ao: Option[A])(f: A => Option[B]): Option[B] = ao flatMap f
  }

  implicit object TryMonad extends Monad[Try] {
    def unit[A](a: A) = Success(a)

    def bind[A, B](ay: Try[A])(f: A => Try[B]): Try[B] = ay flatMap f
  }

  implicit object ListMonad extends Monad[List] {
    def unit[A](a: A) = List(a)

    def bind[A, B](as: List[A])(f: A => List[B]): List[B] = as flatMap f
  }

  def sequence[M[_], A](ams: List[M[A]])(implicit mm: Monad[M]): M[List[A]] =
    ams.foldRight(mm.unit(List[A]())) { (am, asm) => mm.bind(am) { a => mm.bind(asm) { as => mm.unit(a :: as) } } }
}

trait Functor[F[_]] {
  def map[A, B](m: F[A])(f: A => B): F[B]

  def lift[A, B](f: A => B): List[A] => List[B] = _ map f
}

object Functor {

  implicit object ListFunctor extends Functor[List] {
    def map[A, B](as: List[A])(f: A => B): List[B] = as map f
  }

  implicit object OptionFunctor extends Functor[Option] {
    def map[A, B](as: Option[A])(f: A => B): Option[B] = as map f
  }

  implicit object TryFunctor extends Functor[Try] {
    def map[A, B](as: Try[A])(f: A => B): Try[B] = as map f
  }

}

