name := "ShapelessSamples"

version := "1.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq( //"org.scalaz" %% "scalaz-core" % "7.2.14",
  "com.github.tototoshi" %% "scala-csv" % "1.3.5",
  "org.typelevel" %% "cats-core" % "1.0.0-MF",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % Test,
  "org.scalacheck" %% "scalacheck" % "1.13.5")

initialCommands in console := "import org.scalacheck.Prop, org.scalacheck.Gen"
        