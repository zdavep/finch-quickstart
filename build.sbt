name := "finch-quickstart"
version := "0.11.1"
organization := "zdavep"
scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % "0.11.1" exclude("org.openjdk.jmh", "*"),
  "com.github.finagle" %% "finch-circe" % "0.11.1" exclude("org.openjdk.jmh", "*"),
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
mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case "META-INF/io.netty.versions.properties" => MergeStrategy.first
    case "com/twitter/finagle/http/codec/ChannelBufferUsageTracker$$anonfun$1.class" => MergeStrategy.first
    case "com/twitter/finagle/http/codec/ChannelBufferUsageTracker$$anonfun$2.class" => MergeStrategy.first
    case "com/twitter/finagle/http/codec/ChannelBufferUsageTracker$.class" => MergeStrategy.first
    case "com/twitter/finagle/http/codec/ChannelBufferUsageTracker$state$.class" => MergeStrategy.first
    case "com/twitter/finagle/http/codec/ChannelBufferUsageTracker.class" => MergeStrategy.first
    case "com/twitter/finagle/http/codec/ChannelBufferManager.class" => MergeStrategy.first
    case x => old(x)
  }
}

assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  cp filter { x =>
    x.data.getName == "finagle-netty4_2.11-6.40.0.jar"
  }
}
