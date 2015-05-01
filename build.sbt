name := "finch-quickstart"
version := "0.6"
organization := "zdavep"
scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.6.0",
  "com.github.finagle" %% "finch-argonaut" % "0.6.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Xlint:_",
  "-Xfuture",
  "-Ywarn-dead-code",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-unused-import",
  "-Ywarn-value-discard"
)

wartremoverWarnings in (Compile, compile) ++= Warts.allBut(Wart.Throw)

mainClass in Global := Some("zdavep.Main")

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

addCommandAlias("dist", ";clean;compile;scalastyle;assembly")
