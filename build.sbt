name := "CSYE7200-Solutions"

version := "1.0"

scalaVersion := "2.12.4"

lazy val assthw = (project in file("assignment-helloworld"))

lazy val root = (project in file(".")).aggregate(assthw)