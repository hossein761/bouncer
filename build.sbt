name := "bouncer"

version := "1.0-SNAPSHOT"

organization := "com.github.hossein761"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.apache.commons" % "commons-email" % "1.3.2"
)

publishArtifact in Test := false

publishMavenStyle := true

pomIncludeRepository := { _ => false }

publishTo := {
  if(version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
  else
    Some("releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
}

credentials += Credentials("Sonatype Nexus Repository Manager","oss.sonatype.org","","")

licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/hossein761/bouncer"))

pomExtra :=
  <scm>
  <url>git@github.com:hossein761/bouncer.git</url>
    <connection>scm:git:git@github.com:hossein761/bouncer.git</connection>
  </scm>
  <developers>
    <developer>
      <id>hossein</id>
      <name>Hossein Kazemi</name>
      <url>http://www.hosseinkazemi.me</url>
    </developer>
  </developers>



