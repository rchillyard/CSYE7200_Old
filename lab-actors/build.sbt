name := "LabActors"

version := "1.0"

scalaVersion := "2.11.9"

val akkaGroup = "com.typesafe.akka"
val akkaVersion = "2.5.17"
val sprayGroup = "io.spray"
val sprayVersion = "1.3.4"
val sprayJsonVersion = "1.3.4"
val scalaTestVersion = "3.0.5"

//ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

//resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
//resolvers += Resolver.sonatypeRepo("public")
//resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

//resolvers += "Typesafe Repository" at "https://mvnrepository.com/artifact/com.typesafe/config"

libraryDependencies ++= List("spray-client") map {c => sprayGroup %% c % sprayVersion}
libraryDependencies ++= List("spray-json") map {c => sprayGroup %% c % sprayJsonVersion}

libraryDependencies ++= Seq(
  akkaGroup %% "akka-actor" % akkaVersion,
  akkaGroup %% "akka-testkit" % akkaVersion % "test",
  akkaGroup %% "akka-slf4j" % akkaVersion,
  "com.typesafe" % "config" % "1.3.2",
  "com.github.nscala-time" %% "nscala-time" % "2.18.0",
  "ch.qos.logback" % "logback-core" % "1.2.3",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)