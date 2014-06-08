name := "bouncer"

version := "1.0-SNAPSHOT"

organization := "nl.bouncer"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.apache.commons" % "commons-email" % "1.3.2"
)


