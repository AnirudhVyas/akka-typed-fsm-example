import Dependencies._

name := "akka-typed-fsm-example"
version := "1.0"
scalaVersion := "2.12.9"
libraryDependencies ++= akkaCore
libraryDependencies ++= akkaTest.map(_ % Test)
libraryDependencies += logback % Test