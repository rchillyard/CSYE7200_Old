package edu.neu.coe.csye7200.asstwc

import java.net.URL

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._
import scala.io.Source
import scala.util._
import scala.xml.Node

/**
  * @author scalaprof
  */
object WebCrawler extends App {

  def getURLContent(u: URL): Future[String] = {
    for {
      source <- Future(Source.fromURL(u))
    } yield source mkString
  }

  def wget(u: URL): Future[Seq[URL]] = {
    // TODO implement. 16 points. Hint: write as a for-comprehension, using the constructor new URL(URL,String) to get the appropriate URL for relative links
    def getURLs(ns: Node): Seq[URL] = for (
      nsA <- ns \\ "a";
      nsH <- nsA \ "@href";
      nH <- nsH.head;
      u2 = new URL(u, nH.toString)
    ) yield u2
    def getLinks(g: String): Try[Seq[URL]] =
      for (n <- HTMLParser.parse(g) recoverWith { case f => Failure(new RuntimeException(s"parse problem with URL $u: $f")) })
        yield getURLs(n)
    // TODO implement. 9 points. Hint: write as a for-comprehension, using getURLContent (above) and getLinks above. You might also need MonadOps.asFuture
    for {g <- getURLContent(u); us <- MonadOps.asFuture(getLinks(g))} yield us
  }

  def wget(us: Seq[URL]): Future[Seq[Either[Throwable, Seq[URL]]]] = {
    val us2 = us.distinct take 10
    // TODO implement the rest of this, based on us2 instead of us. 15 points.
    // Hint: Use wget(URL) (above). MonadOps.sequence and Future.sequence are also available to you to use.
    val usfs = for {u <- us2} yield wget(u)
    val usefs = MonadOps.sequence(usfs)
    Future.sequence(usefs)
  }

  def crawler(depth: Int, args: Seq[URL]): Future[Seq[URL]] = {
    def inner(urls: Seq[URL], depth: Int, accum: Seq[URL]): Future[Seq[URL]] =
      if (depth > 0)
        for (us <- MonadOps.flattenRecover(wget(urls), { x => System.err.println(x) }); r <- inner(us, depth - 1, accum ++: urls)) yield r
      else
        Future.successful(accum)
    inner(args, depth, Nil)
  }

  println(s"web reader: ${args.toList}")
  val urls = for (arg <- args toList) yield Try(new URL(arg))
  val s = MonadOps.sequence(urls)
  s match {
    case Success(z) =>
      println(s"invoking crawler on $z")
      val f = crawler(2, z)
      Await.ready(f, Duration("60 second"))
      for (x <- f) println(s"Links: $x")
    case Failure(z) => println(s"failure: $z")
  }
}
