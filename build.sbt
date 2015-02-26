name := "finch-quickstart"

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
  "-Xlint:_",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-unused-import",
  "-Ywarn-value-discard",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

wartremoverWarnings ++= Warts.allBut(Wart.NoNeedForMonad)

mainClass in Global := Some("zdavep.Main")

assemblySettings

addCommandAlias("dist", ";clean;compile;scalastyle;assembly")
