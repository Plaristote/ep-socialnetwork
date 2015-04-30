name := """socialnetwork"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  jdbc,
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
  "org.hibernate" % "hibernate-core" % "3.5.6-Final",
  "org.hibernate" % "hibernate-annotations" % "3.5.6-Final",
  "joda-time" % "joda-time-hibernate" % "1.3"
)
