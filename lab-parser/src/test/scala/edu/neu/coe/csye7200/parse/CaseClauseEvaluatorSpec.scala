/*
 * Copyright (c) 2017. HSBC
 */

package edu.neu.coe.csye7200.parse

import org.scalatest.{FlatSpec, Matchers}

import scala.util.Try

/**
  * Created by scalaprof on 10/19/16.
  */
class CaseClauseEvaluatorSpec extends FlatSpec with Matchers {

  private val account = "x10177789"
  private val c0 = "CASE WHEN (TRUE) THEN (42) END"
  private val c1 = "CASE WHEN FALSE THEN 0 WHEN TRUE THEN SLR.ACCOUNT END"
  private val c2 = "CASE WHEN FALSE THEN 0 WHEN FALSE THEN SLR.ACCOUNT END"
  private val c3 = "CASE WHEN (FALSE) THEN 0 WHEN (FALSE) THEN SLR.ACCOUNT ELSE SLR.PRODUCT END"
  private val c4 = "CASE WHEN (FALSE) THEN (0) WHEN (TRUE) THEN (42) END"
  private val c5 = "CASE WHEN (FALSE) THEN (0) WHEN (TRUE) THEN (SLR.ACCOUNT) END"
  private val c6 = "CASE WHEN (FALSE) THEN (0) WHEN (FALSE) THEN (SLR.ACCOUNT) ELSE (SLR.PRODUCT) END"
  private val c7 = "CASE WHEN (SLR.COUNTRY_OF_INCORPORATION IN ('UK','US','FR')) THEN (SLR.ACCOUNT) ELSE (SLR.PRODUCT) END"
  private val c8 = "CASE WHEN SLR.COUNTRY_OF_INCORPORATION IN ('UK','US','FR') OR SLR.ACCOUNT IN ('" + account + "') THEN (SLR.ACCOUNT) ELSE (SLR.PRODUCT) END"
  private val c9 = "CASE WHEN SLR.COUNTRY_OF_INCORPORATION IN ('UK','US','FR') OR SLR.ACCOUNT = '" + account + "' THEN (SLR.ACCOUNT) ELSE (SLR.PRODUCT) END"
  private val c10 = "CASE WHEN IN ('UK','US','FR') THEN 42 ELSE 0 END"

  behavior of "evaluate"

//  private val xxxx = "xxxx"
//  private val fortyTwo = 42
//  it should "evaluate " + c0 in {
//    val caseClauseEvaluator = CaseClauseEvaluator[Int](MockUDF)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[Int] = caseClauseEvaluator.evaluate(c0)
//    ry should matchPattern { case Success(`fortyTwo`) => }
//  }
//
//  it should "evaluate " + c4 in {
//    val caseClauseEvaluator = CaseClauseEvaluator[Int](MockUDF)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[Int] = caseClauseEvaluator.evaluate(c4)
//    ry should matchPattern { case Success(`fortyTwo`) => }
//  }
//
//  it should "evaluate " + c5 in {
//    val variables = Map("SLR.ACCOUNT" -> account, "SLR.PRODUCT" -> xxxx)
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[String] = caseClauseEvaluator.evaluate(c5)
//    ry should matchPattern { case Success(`account`) => }
//  }
//
//  it should "evaluate " + c6 in {
//    val variables = Map("SLR.ACCOUNT" -> account, "SLR.PRODUCT" -> xxxx)
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[String] = caseClauseEvaluator.evaluate(c6)
//    ry should matchPattern { case Success(`xxxx`) => }
//  }
//
//  it should "evaluate " + c1 in {
//    val variables = Map("SLR.ACCOUNT" -> account)
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[String] = caseClauseEvaluator.evaluate(c1)
//    ry should matchPattern { case Success(`account`) => }
//  }
//
//  it should "evaluate " + c2 in {
//    val variables = Map("SLR.ACCOUNT" -> account)
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[String] = caseClauseEvaluator.evaluate(c2)
//    //noinspection ScalaStyle
//    ry should matchPattern { case Success(null) => }
//  }
//
//  it should "evaluate " + c3 in {
//    val variables = Map("SLR.ACCOUNT" -> account, "SLR.PRODUCT" -> xxxx)
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[Any] = caseClauseEvaluator.evaluate(c3)
//    ry should matchPattern { case Success(`xxxx`) => }
//  }
//
//  it should "evaluate " + c7 in {
//    val variables = Map("SLR.ACCOUNT" -> account, "SLR.PRODUCT" -> xxxx, "SLR.COUNTRY_OF_INCORPORATION" -> "US")
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[String] = caseClauseEvaluator.evaluate(c7)
//    ry should matchPattern { case Success(`account`) => }
//  }
//
//  it should """evaluate """ + c8 in {
//    val variables = Map("SLR.ACCOUNT" -> account, "SLR.PRODUCT" -> xxxx, "SLR.COUNTRY_OF_INCORPORATION" -> "US")
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[String] = caseClauseEvaluator.evaluate(c8)
//    ry should matchPattern { case Success(`account`) => }
//  }
//
//  it should """evaluate """ + c9 in {
//    val variables = Map("SLR.ACCOUNT" -> account, "SLR.PRODUCT" -> xxxx, "SLR.COUNTRY_OF_INCORPORATION" -> "US")
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[String] = caseClauseEvaluator.evaluate(c9)
//    ry should matchPattern { case Success(`account`) => }
//  }
//
//  it should """evaluate """ + c10 in {
//    val variables = Map("SLR.ACCOUNT" -> account, "SLR.PRODUCT" -> xxxx, "SLR.COUNTRY_OF_INCORPORATION" -> "US")
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    implicit val fl = caseClauseEvaluator.functionLibrary
//    val ry: Try[String] = caseClauseEvaluator.evaluate(c10)
//    ry should matchPattern { case Failure(_) => }
//    an[RenderableFunctionException] should be thrownBy ry.get
//    //noinspection ScalaStyle
//    ry match {
//      case Failure(t) => println(t.getLocalizedMessage)
//      case _ =>
//    }
//  }
//
//  it should "work for BETWEEN" in {
//    val criteriaEvaluator = CaseClauseEvaluator[Int](MockUDF)
//    criteriaEvaluator.setCriteria(Map("x" -> 2))
//    criteriaEvaluator.evaluate(s"""CASE WHEN x between 1 AND 3 THEN $fortyTwo ELSE 0 END""") match {
//      case Success(s) => s shouldBe fortyTwo
//      case Failure(x) => fail(x)
//    }
//  }
//
//  val filenameStdJoinSample = "stdJoinSample.txt"
//  it should "evaluate " + filenameStdJoinSample in {
//    val variables = Map("REPORTING_PERIOD_RUN_CONTROL" -> "2017Q4", "COST_CENTER_CODE" -> "XYZ")
//    val caseClauseEvaluator = CaseClauseEvaluator(MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    val uo = Option(getClass.getResource(filenameStdJoinSample))
//    uo should matchPattern { case Some(_) => }
//    val wto: Option[Try[String]] = for (u <- uo; s = u.openStream) yield
//      caseClauseEvaluator.evaluate(Source.fromInputStream(s).mkString)
//    // CONSIDER replacing toOption with FP.toOption
//    val wo = for (wt <- wto; w <- wt.toOption) yield w
//    wo should matchPattern { case Some("key: GXXX-XYZ-DEXXXX-REXXXXX, eff: 2017Q4") => }
//  }
//
//  /**
//    * NOTE: the RMF1... file has been modified slightly: each THEN (and ELSE) clause returns a code to enable testing;
//    * Additionally, the "D" case has had its logic altered.
//    */
//  val ruleFile = "RMF1-GLBL_GRCA_PRIM_SUPP_BATCH-GLOBL-GLBL_O_GRCA_IFRS9-GLBL_RS_120-GLBL_R_PRM_016-GRCA_CUSTOM1.txt"
//  it should "evaluate A " + ruleFile in {
//    val variables = Map("SLR.ACCOUNT" -> account, "SLR.PRODUCT" -> xxxx)
//    val caseClauseEvaluator = CaseClauseEvaluator(MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    val uo = Option(getClass.getResource(ruleFile))
//    uo should matchPattern { case Some(_) => }
//    val wto: Option[Try[String]] = for (u <- uo; s = u.openStream) yield
//      caseClauseEvaluator.evaluate(Source.fromInputStream(s).mkString)
//    // CONSIDER replacing toOption with FP.toOption
//    val wo = for (wt <- wto; w <- wt.toOption) yield w
//    wo should matchPattern { case Some("A") => }
//  }
//
//  it should "evaluate R " + ruleFile in {
//    val variables = Map("SLR.ACCOUNT" -> account, "SLR.PRODUCT" -> xxxx)
//
//    def mockBoolean(a: Boolean, b: Boolean): Boolean = a && b
//
//    val caseClauseEvaluator = CaseClauseEvaluator(UDF(MockStandardJoin(), MockRecursiveJoin(mockBoolean, "")))
//    caseClauseEvaluator.setCriteria(variables)
//    val uo = Option(getClass.getResource(ruleFile))
//    uo should matchPattern { case Some(_) => }
//    val wto: Option[Try[String]] = for (u <- uo; s = u.openStream) yield
//      caseClauseEvaluator.evaluate(Source.fromInputStream(s).mkString)
//    // CONSIDER replacing toOption with FP.toOption
//    val wo = for (wt <- wto; w <- wt.toOption) yield w
//    wo should matchPattern { case Some("R") => }
//  }
//
//  it should "evaluate D " + ruleFile in {
//    val variables = Map("SLR.ACCOUNT" -> account, "SLR.PRODUCT" -> xxxx)
//
//    def mockBoolean(a: Boolean, b: Boolean): Boolean = !(a || b)
//
//    val caseClauseEvaluator = CaseClauseEvaluator(UDF(MockStandardJoin(), MockRecursiveJoin(mockBoolean, "GLOBL")))
//    caseClauseEvaluator.setCriteria(variables)
//    val uo = Option(getClass.getResource(ruleFile))
//    uo should matchPattern { case Some(_) => }
//    val wto: Option[Try[String]] = for (u <- uo; s = u.openStream) yield
//      caseClauseEvaluator.evaluate(Source.fromInputStream(s).mkString)
//    // CONSIDER replacing toOption with FP.toOption
//    val wo = for (wt <- wto; w <- wt.toOption) yield w
//    wo should matchPattern { case Some("D") => }
//  }
//
//  it should "evaluate a very complex case clause (1)" in {
//    val sb = new StringBuffer("case")
//    for (_ <- 1 to 100) sb.append(" when false then 1")
//    sb.append(" else 0 end")
//    val caseClauseEvaluator = CaseClauseEvaluator[Int](MockUDF)
//    val result: Try[Int] = caseClauseEvaluator.evaluate(sb.toString)
//    result should matchPattern { case Success(0) => }
//  }
//
//  it should "evaluate a very complex case clause (2)" in {
//    val sb = new StringBuffer("case")
//    for (_ <- 1 to 100) sb.append(" when x in ('X','Y','Z') then 1")
//    sb.append(" else 0 end")
//    val caseClauseEvaluator = CaseClauseEvaluator[Int](MockUDF)
//    caseClauseEvaluator.setCriteria(Map("x" -> "Z"))
//    caseClauseEvaluator.evaluate(sb.toString) should matchPattern { case Success(1) => }
//    caseClauseEvaluator.setCriteria(Map("x" -> "A"))
//    caseClauseEvaluator.evaluate(sb.toString) should matchPattern { case Success(0) => }
//  }
//
//  behavior of "evaluateInvocation"
//  val filenameSimple = "simple-rule.txt"
//  it should "evaluate " + filenameSimple in {
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    val uo = Option(getClass.getResource(filenameSimple))
//    uo should matchPattern { case Some(_) => }
//    val ito: Option[Try[Invocation]] = for (u <- uo; s = u.openStream) yield
//      caseClauseEvaluator.parseString(Source.fromInputStream(s).mkString)
//    val wto: Option[Try[String]] = for (it: Try[Invocation] <- ito) yield
//      for (i: Invocation <- it; c <- caseClauseEvaluator.evaluateInvocation[String](i); w <- c()) yield w
//    wto should matchPattern { case Some(Success("x")) => }
//  }
//
//  val filenameCaseSample = "caseSample.txt"
//  it should "evaluate " + filenameCaseSample in {
//    val variables = Map("ACCOUNT" -> account, "GLBL_DETAIL_CUSTOMER_TYPE_CODE" -> xxxx)
//    val caseClauseEvaluator = CaseClauseEvaluator[String](MockUDF)
//    caseClauseEvaluator.setCriteria(variables)
//    val uo = Option(getClass.getResource(filenameCaseSample))
//    uo should matchPattern { case Some(_) => }
//    val ito: Option[Try[Invocation]] = for (u <- uo; s = u.openStream) yield
//      caseClauseEvaluator.parseString(Source.fromInputStream(s).mkString)
//    val wto: Option[Try[String]] = for (it: Try[Invocation] <- ito) yield
//      for (i: Invocation <- it; c <- caseClauseEvaluator.evaluateInvocation[String](i); w <- c()) yield w
//    wto should matchPattern { case Some(Success("CI")) => }
//  }
//
  behavior of "Option.toSeq"
  it should "work for Some" in {
    val x = Some("x")
    x.toSeq.size shouldBe 1
    x.toSeq shouldBe Seq("x")
  }
  it should "work for None" in {
    val x = None
    x.toSeq.size shouldBe 0
    x.toSeq shouldBe Seq.empty
  }
}
