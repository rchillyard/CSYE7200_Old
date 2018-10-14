/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.csye7200.parse

import scala.annotation.tailrec

object Substring {

  /**
    * Method to determine if s is a substring of t
    *
    * @param sub    the candidate substring
    * @param string the full string
    * @return true if sub is a substring of string
    */
  def substring(sub: String, string: String): Boolean = {

    /**
      * Tail-recursive method to determine if p is a substring of s
      *
      * @param s the sequence to be tested (part of the original "string")
      * @return true if p is a substring of s
      */
    @tailrec def substring(p: Seq[Char], s: Seq[Char]): Boolean = {
      /**
        * Tail-recursive method to determine if q is a prefix of t
        *
        * @param r the current value of the result
        * @param q the candidate prefix
        * @param t the candidate string
        * @return as follows:
        *         (1) if q is empty then true;
        *         (2) if t is empty then r;
        *         (3) if q matches h1::z1 and t matches h2::z2 then prefix(r && h1==h2, z1, z2)
        */
      @tailrec def prefix(r: Boolean, q: Seq[Char], t: Seq[Char]): Boolean = (q, t) match {
        case (Nil, _) => true
        case (_, Nil) => r
        case (h1 :: z1, h2 :: z2) => prefix(r && h1 == h2, z1, z2)
        case _ => throw new Exception(s"prefix: logic error: $r, $q")
      }

      if (p.isEmpty) true
      else if (s.isEmpty) false
      else if (prefix(true, p, s)) true
      else s match {
        case Nil => false
        case _ :: z => substring(p, z)
      }
    }

    substring(sub.toList, string.toList)
  }

}
