name := "actor-experiments"
organization := "com.grab"
version := "0.1"
scalaVersion := "2.13.0"

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= {
  lazy val akkaVersion = "2.5.25"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion
  )
}