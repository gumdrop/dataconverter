
val circeVersion = "0.8.0"
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "quizleague",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "dataconverter",
    
    libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.4",
    libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.8.4",
    libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.4",
    libraryDependencies ++= Seq(
	  "io.circe" %% "circe-core",
	  "io.circe" %% "circe-generic",
	  "io.circe" %% "circe-parser"
	).map(_ % circeVersion),
	libraryDependencies += "io.github.cquiroz" %% "scala-java-time" % "2.0.0-M8"

  )

