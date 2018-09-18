package edu.neu.coe.csye7200.assthw

/**
  * This is the second part of assignment,
  * You should be able to run this program
  * with provided csv file.
  * You task is to replace line 38 with
  * the following 2 lines code:
  * val kiwiMovies = for (m <- ingester(source); if (m.properties(20)=="New Zealand")) yield m
  * println(kiwiMovies.size)
  * Run this program with provided csv file,
  * and submit the screenshot of the result.
  * It should be the number of Kiwi (New Zealand) Movies.
  */

import scala.io.Source

trait Ingestible[X] {
  def fromStrings(ws: Seq[String]): X
}

class Ingest[T: Ingestible] extends (Source => Iterator[T]) {
  def apply(source: Source): Iterator[T] = source.getLines.toSeq.map(e => implicitly[Ingestible[T]].fromStrings(e.split(",").toList)).iterator
}

case class Movie(properties: Seq[String])

object Ingest extends App {

  trait IngestibleMovie extends Ingestible[Movie] {
    def fromStrings(ws: Seq[String]): Movie = Movie.apply(ws)
  }

  implicit object IngestibleMovie extends IngestibleMovie

  val ingester = new Ingest[Movie]()
  val source = Source.fromFile(args.toList match {
    case Nil => "movie_metadata_5000.csv"
    case h :: _ => h
  })
  for (m <- ingester(source)) println(m.properties.mkString(", "))
  source.close()
}