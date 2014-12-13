import AssemblyKeys._

name := """finch-quickstart"""

version := "0.1"

organization := "zdavep"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.2.0",
  "com.github.finagle" %% "finch-json" % "0.2.0",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)

mainClass in Global := Some("zdavep.HelloApp")

assemblySettings
