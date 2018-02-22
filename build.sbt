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

lazy val sparkapp = (project in file("spark-app"))

lazy val mapred = (project in file("mapreduce"))

lazy val concor = (project in file("concordance"))

lazy val hedgefund = (project in file("hedge-fund"))

lazy val sparkexp = (project in file("spark-example"))

lazy val labsort = (project in file("lab-sorted"))

lazy val labawscld = (project in file("lab-awscloudformation"))

lazy val root = (project in file(".")).aggregate(assthw, asstmd, asstrs, asstfc, asstwc, asstswc, fp, num, sparkapp, mapred, concor, hedgefund, sparkexp, labsort, labawscld)

parallelExecution in Test := false