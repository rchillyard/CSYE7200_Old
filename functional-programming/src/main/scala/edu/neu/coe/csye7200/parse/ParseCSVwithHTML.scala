package edu.neu.coe.csye7200.parse

import java.io.{BufferedWriter, File, FileWriter}

import scala.collection.mutable.Stack
import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}

case class ParseCSVwithHTML(csvParser: CsvParser) {

  val html = new HTML

  def parseElementIntoHTMLElement(w: String, header: Boolean = false): String = {
    val result = new HTML
    result.tag( if (header) "th" else "td" )
    result.append(w)
    result.close()
    result.toString()
  }

  def parseRowIntoHTMLRow(w: String, header: Boolean = false): String = {
    val result = new HTML
    result.tag("tr")
    val wsy: Try[List[String]] = csvParser.parseRow(w)
    wsy match {
      case Success(ws) => for (w <- ws) result.append(parseElementIntoHTMLElement(w, header))
      case Failure(x) => System.err.println(s"Error parsing `$w`: ${x.getLocalizedMessage}")
    }
    result.close
    result.toString
  }

  def preamble(w: String) = {
    val result = new HTML
    result.tag("head")
    result.tag("title")
    result.append(w)
    result.close()
    result.toString
  }

  def parseStreamIntoHTMLTable(ws: Stream[String], title: String): String = {
    val result = new HTML
    result.tag("html")
    result.append(preamble(title))
    result.tag("body")
    result.tag("table")
    ws match {
      case header #:: body =>
        result.append(parseRowIntoHTMLRow(header, true))
        for (w <- body) result.append(parseRowIntoHTMLRow(w))
    }
    result.close()
    result.toString
  }
}

/**
  * Mutable class to form an HTML string
  */
class HTML() {
  val content = new StringBuilder("")
  val tagStack = Stack[String]()
  def tag(w: String) = { tagStack.push(w); content.append(s"<$w>") }
  def unTag = content.append(s"</${tagStack.pop()}>")
  def append(w: String) = content.append(w)
  def close() =  while (!tagStack.isEmpty) { unTag }
  override def toString: String = content.toString + "\n"
}

object ParseCSVwithHTML extends App {
  val parser = ParseCSVwithHTML(CsvParser(delimiter = '\t'+""))
  val resource = "report.csv"
  val title = "Report"
  if (args.size>0) {
    val filename = args.head
    val source: BufferedSource = Source.fromFile(filename,"UTF-16")
    val w = parser.parseStreamIntoHTMLTable(source.getLines.toStream, title)
    val file = new File("output.html")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(w)
    bw.close()
  }
  else
    System.err.println("syntax: ParseCSVwithHTML filename")

}