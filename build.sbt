import com.typesafe.sbt.SbtStartScript

seq(SbtStartScript.startScriptForClassesSettings: _*)

scalaVersion  := "2.9.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-explaintypes", "-optimise", "-Ydependent-method-types")

mainClass in Compile := Some("DocsUpWatcher")

libraryDependencies += "joda-time" % "joda-time" % "2.0"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"
