import Dependencies._
import sbt._

organization in ThisBuild := "com.twosixlabs.cdr4s"
name := "cdr4s"

scalaVersion in ThisBuild := "2.12.7"

resolvers in ThisBuild ++= Seq( "Maven Central" at "https://repo1.maven.org/maven2/",
                                "JCenter" at "https://jcenter.bintray.com",
                                "Local Ivy Repository" at s"file://${System.getProperty( "user.home" )}/.ivy2/local/default" )

crossScalaVersions in ThisBuild := Seq( "2.11.12", "2.12.7" )

publishMavenStyle := false

lazy val root = ( project in file( "." ) ).aggregate( core, ladleJson, dartJson, testBase )

lazy val core = ( project in file( "cdr4s-core" ) ).settings( libraryDependencies ++= dartCommons )

lazy val ladleJson = ( project in file( "cdr4s-ladle-json" ) ).dependsOn( core ).settings( libraryDependencies ++= logging
                                                                                                                   ++ jsonValidator
                                                                                                                   ++ betterFiles
                                                                                                                   ++ dartCommons
                                                                                                                   ++ dartCommonsJson )

lazy val dartJson = ( project in file( "cdr4s-dart-json" ) ).dependsOn( core ).settings( libraryDependencies ++= logging
                                                                                                                 ++ jsonValidator
                                                                                                                 ++ betterFiles
                                                                                                                 ++ dartCommons
                                                                                                                 ++ dartCommonsJson )

lazy val testBase = ( project in file( "cdr4s-test-base" ) ).dependsOn( core )


test in publish := {}

javacOptions in ThisBuild ++= Seq( "-source", "8", "-target", "8" )
scalacOptions in ThisBuild += "-target:jvm-1.8"

sonatypeProfileName := "com.twosixlabs"
inThisBuild(List(
    organization := organization.value,
    homepage := Some(url("https://github.com/twosixlabs-dart/cdr4s")),
    licenses := List("GNU-Affero-3.0" -> url("https://www.gnu.org/licenses/agpl-3.0.en.html")),
    developers := List(
        Developer(
            "twosixlabs-dart",
            "Two Six Technologies",
            "",
            url("https://github.com/twosixlabs-dart")
            )
        )
    ))

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository := "https://s01.oss.sonatype.org/service/local"