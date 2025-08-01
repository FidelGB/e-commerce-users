import scalapb.compiler.Version.scalapbVersion

val scala2Version = "2.13.14"

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.11"

lazy val core = project
  .in(file("modules/core"))
  .settings(
    name := "users",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala2Version,

    Compile / mainClass := Some(
      "com.ecommerce.users.Main"
    ),
    Compile / run / fork := true,
    libraryDependencies ++= Seq(
      "io.grpc" % "grpc-netty-shaded" % scalapb.compiler.Version.grpcJavaVersion,
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % "0.11.13",
      "org.typelevel" %% "cats-effect" % "3.5.4",
      "org.scalameta" %% "munit" % "1.0.0" % Test
    ) 
  ).dependsOn(protos)

lazy val protos = project
  .in(file("modules/protobufs"))
  .enablePlugins(Fs2Grpc, ProtocPlugin)
  .settings(
    name := "protobufs",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala2Version,

    Compile / PB.protoSources += baseDirectory.value / "src",

    libraryDependencies ++= Seq(
      "com.thesamet.scalapb" %% "scalapb-runtime" % "0.11.13" % "protobuf",
      "com.thesamet.scalapb" %% "compilerplugin" % "0.11.13"
    )
  )

enablePlugins(ScalafmtPlugin)
