name := "CSYE7200-Solutions"

version := "1.0"

scalaVersion := "2.12.4"

lazy val assthw = (project in file("assignment-helloworld"))

lazy val asstmd = (project in file("assignment-movie-database"))

lazy val asstrs = (project in file("assignment-random-state"))

lazy val asstfc = (project in file("assignment-functional-composition"))

lazy val asstwc = (project in file("assignment-web-crawler"))

lazy val asstswc = (project in file("assignment-spark-wordcount"))

lazy val fp = (project in file("functional-programming"))

lazy val num = (project in file("numerics"))

lazy val root = (project in file(".")).aggregate(assthw, asstmd, asstrs, asstfc, asstwc, asstswc, fp, num)

parallelExecution in Test := false