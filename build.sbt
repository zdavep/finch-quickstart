name := "finch-quickstart"
version := "0.9.2"
organization := "zdavep"
scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.9.2" exclude("org.openjdk.jmh", "*"),
  "com.github.finagle" %% "finch-argonaut" % "0.9.2" exclude("org.openjdk.jmh", "*"),
  "com.typesafe" % "config" % "1.2.1",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "org.slf4j" % "slf4j-simple" % "1.7.12",
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

wartremoverWarnings in (Compile, compile) ++= Warts.unsafe

mainClass in Global := Some("app.Main")

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

addCommandAlias("dist", ";clean;compile;scalastyle;assembly")

Revolver.settings

scalariformSettings
