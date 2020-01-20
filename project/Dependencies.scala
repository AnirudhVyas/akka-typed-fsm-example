import sbt._

object Dependencies {
  val akkaVersion = "2.6.1"
  val scalaTestVersion = "3.0.8"
  lazy val akkaCore: Seq[ModuleID] = Seq("com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion)
  lazy val akkaTest: Seq[ModuleID] = Seq("com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion)
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
}
