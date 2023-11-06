import Dependencies.{munit, *}

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "3.3.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "tapir-iron",
    libraryDependencies ++= Seq(

      "org.typelevel" %% "cats-core" % "2.10.0",
      "org.typelevel" %% "cats-effect" %  "3.4.11",

      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.8.5",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"   % "1.8.5",
      "com.softwaremill.sttp.tapir" %% "tapir-json-pickler"   % "1.8.5",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.8.5",

      "com.softwaremill.sttp.tapir" %% "tapir-iron"        % "1.8.5",
      "io.github.iltotore" %% "iron"  % "2.2.1",


      "org.http4s" %% "http4s-dsl"          % "0.23.23",
      "org.http4s" %% "http4s-ember-server" % "0.23.23",
      "org.http4s" %% "http4s-ember-client" % "0.23.23",
      "org.typelevel" %% "log4cats-slf4j" % "2.6.0",
      "ch.qos.logback" % "logback-classic" % "1.4.11",

      munit % Test
    ),
  )


