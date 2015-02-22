name := """finch-quickstart"""

version := "0.5"

organization := "zdavep"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.5.0",
  "com.github.finagle" %% "finch-json" % "0.5.0",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

wartremoverWarnings ++= Warts.all

mainClass in Global := Some("zdavep.Main")

assemblySettings
