name := """finch-quickstart"""

version := "0.1"

organization := "zdavep"

scalaVersion := "2.10.4"

resolvers ++= Seq(
  "central.maven" at "http://central.maven.org/maven2",
  "Finch.io" at "http://repo.konfettin.ru",
  "twitter" at "http://maven.twttr.com/"
)

libraryDependencies ++= Seq(
  "io" %% "finch" % "0.1.6",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)

mainClass in Global := Some("zdavep.Hello")
