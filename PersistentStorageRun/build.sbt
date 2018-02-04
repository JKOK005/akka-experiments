name := "akka-quickstart-scala"

version := "1.0"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.3"

libraryDependencies ++= Seq(
	"com.typesafe.akka" %% "akka-actor" % akkaVersion,
	"com.typesafe.akka" %% "akka-testkit" % akkaVersion,
	"org.scalatest" %% "scalatest" % "3.0.1" % "test",
	"com.typesafe.akka" %% "akka-persistence" % "2.5.4",
	"org.iq80.leveldb" % "leveldb" % "0.7",
	"org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8"
)
