name         := "between"
organization := "nl.gn0s1s"
startYear    := Some(2018)
homepage     := Some(url("https://github.com/philippus/between"))
licenses += ("MPL-2.0", url("https://www.mozilla.org/MPL/2.0/"))

developers := List(
  Developer(
    id = "philippus",
    name = "Philippus Baalman",
    email = "",
    url = url("https://github.com/philippus")
  )
)

ThisBuild / versionScheme          := Some("semver-spec")
ThisBuild / versionPolicyIntention := Compatibility.None

Compile / packageBin / packageOptions += Package.ManifestAttributes("Automatic-Module-Name" -> "nl.gn0s1s.between")

scalaVersion := "2.13.17"
crossScalaVersions += "3.3.7"

scalacOptions += "-Xsource:3"

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.19.0" % Test
)
