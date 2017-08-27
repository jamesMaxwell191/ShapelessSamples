name := "ShapelessSamples"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq("org.scalaz" %% "scalaz-core" % "7.2.14",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test,
  "org.scalacheck" %% "scalacheck" % "1.13.5")

initialCommands in console := "import org.scalacheck.Prop, org.scalacheck.Gen"
        