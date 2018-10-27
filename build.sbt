name := "between"
organization := "nl.gn0s1s"
version := "0.3.0"
startYear := Some(2018)
homepage := Some(url("https://github.com/philippus/between"))
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

crossScalaVersions := List("2.11.12", "2.12.7", "2.13.0-M5")
scalaVersion := "2.12.7"

bintrayOrganization := Some("gn0s1s")
bintrayRepository := "releases"

libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test
)

pomExtra :=
  <scm>
    <url>git@github.com:Philippus/between.git</url>
    <connection>scm:git@github.com:Philippus/between.git</connection>
  </scm>
    <developers>
      <developer>
        <id>philippus</id>
        <name>Philippus Baalman</name>
        <url>https://github.com/philippus</url>
      </developer>
    </developers>
