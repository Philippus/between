name := "between"
organization := "nl.gn0s1s"
startYear := Some(2018)
homepage := Some(url("https://github.com/philippus/between"))
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

developers := List(
  Developer(
    id = "philippus",
    name = "Philippus Baalman",
    email = "",
    url = url("https://github.com/philippus")
  )
)

crossScalaVersions := List("2.13.6")
scalaVersion := crossScalaVersions.value.last

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.15.4" % Test
)
