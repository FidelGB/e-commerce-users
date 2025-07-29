import scalapb.compiler.Version.scalapbVersion


val scala3Version = "3.7.1"
val scala2Version = "2.13.14"

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.11"

lazy val root = project
  .in(file("modules/core"))
  .settings(
    name := "users",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test
  )

lazy val protos = project
  .in(file("modules/protobufs"))
  .enablePlugins(Fs2Grpc)
  .settings(
    name := "protobufs",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala2Version,
    PB.targets in Compile := Seq(
      scalapb.gen(grpc = true) -> (sourceManaged in Compile).value
    ),
    Compile / PB.protoSources += baseDirectory.value / "src",
    Compile / PB.targets := Seq(
      scalapb.gen() -> (Compile / sourceManaged).value
    ),

    libraryDependencies ++= Seq(
      "com.thesamet.scalapb" %% "scalapb-runtime" % "0.11.13" % "protobuf",
      "io.grpc" % "grpc-netty" % "1.53.0",
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % "0.11.13",
      "com.thesamet.scalapb" %% "compilerplugin" % "0.11.13"
    )
  ).enablePlugins(ProtocPlugin)
