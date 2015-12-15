name := "web_crawler"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "com.typesafe.akka" %% "akka-actor" % "2.4.1",
  "org.jsoup" % "jsoup" % "1.8.3"
  )