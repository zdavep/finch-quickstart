name := "finch-quickstart"
version := "0.10.0"
organization := "zdavep"
scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.10.0" exclude("org.openjdk.jmh", "*"),
  "com.github.finagle" %% "finch-circe" % "0.10.0" exclude("org.openjdk.jmh", "*"),
  "com.typesafe" % "config" % "1.3.1",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "org.slf4j" % "slf4j-simple" % "1.7.21",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Xlint:_",
  "-Xfatal-warnings",
  "-Xfuture",
  "-Ywarn-dead-code",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-unused-import",
  "-Ywarn-value-discard"
)

mainClass in Global := Some("app.Main")

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

addCommandAlias("dist", ";clean;compile;assembly")

Revolver.settings

scalariformSettings
