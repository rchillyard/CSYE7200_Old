name := "FunctionalProgramming"

version := "1.0"

scalaVersion := "2.12.4"

lazy val scalaModules = "org.scala-lang.modules"

lazy val scalaModulesVersion = "1.0.6"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  scalaModules %% "scala-xml" % scalaModulesVersion,
  "io.spray" %%  "spray-json" % "1.3.3"
)