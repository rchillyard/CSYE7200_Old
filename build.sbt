name := "CSYE7200-Solutions"

version := "1.0"

scalaVersion := "2.12.4"

lazy val assthw = (project in file("assignment-helloworld"))

lazy val asstmd = (project in file("assignment-movie-database"))

lazy val asstrs = (project in file("assignment-random-state"))

lazy val root = (project in file(".")).aggregate(assthw, asstmd, asstrs)