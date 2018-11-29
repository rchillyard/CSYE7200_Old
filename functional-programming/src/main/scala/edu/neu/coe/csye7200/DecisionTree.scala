package edu.neu.coe.csye7200

case class DecisionTree(ks: Seq[String]) {

  def render(suffix: String): String = {
    def leaf(ws: Seq[String]): String = s""""${ws.mkString}$suffix""""

    def comparisons(k: String, js: Seq[String], last: Boolean): String =
      if (last) ""
      else "if "+((js map (k+" > "+_)) mkString("(", " && ", ")"))

    def doRenderString(k: String) = {
      val js = ks filterNot (_ == k)
      s""" ${comparisons(k, js, k == ks.last)} """ + DecisionTree(js).render(k + suffix) + "\n "
    }

    if (ks.size==2) s"""if (${ks.head}>${ks.last}) ${leaf(ks.reverse)} else ${leaf(ks)}"""
    else (for (k <- ks) yield doRenderString(k)) mkString(" ", " else ", "")
  }


}

object DecisionTree extends App {
  val tree = DecisionTree(Seq("a", "b", "c", "d"))
  val w = tree.render("")
  println(w)
}