import scalapb.compiler.Version.scalapbVersion

val scala2Version = "2.13.16"

libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.11"

lazy val appVersion = sys.env.getOrElse("APP_VERSION", "local")
lazy val grpcPort   = sys.env.getOrElse("GRPC_SERVER_PORT", "9000").toInt
lazy val dockerRegistry = sys.env.getOrElse("DOCKER_REGISTRY", "fgarcia14")
lazy val dockerPackageName = sys.env.getOrElse("DOCKER_IMAGE_NAME", "ecommerce-users")

lazy val core = project
  .in(file("modules/core"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(
    name                                 := "users",
    version                              := appVersion,
    scalaVersion                         := scala2Version,
    Compile / mainClass                  := Some(
      "com.ecommerce.users.Main",
    ),
    Compile / run / fork                 := true,
    Docker / packageName                 := s"$dockerRegistry/$dockerPackageName",
    Docker / version                     := appVersion,
    Docker / defaultLinuxInstallLocation := "/app/ecommerce-users",
    Docker / daemonUser                  := "root",
    dockerBaseImage                      := "openjdk:11-jre-slim",
    dockerExposedPorts ++= Seq(grpcPort),
    libraryDependencies ++= Seq(
      "io.grpc"               % "grpc-netty-shaded"    % scalapb.compiler.Version.grpcJavaVersion,
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % "0.11.13",
      "org.typelevel"        %% "cats-effect"          % "3.5.4",
      "org.scalameta"        %% "munit"                % "1.0.0" % Test,
      "is.cir"               %% "ciris"                % "3.5.0",
      "is.cir"               %% "ciris-circe"          % "3.5.0",
      "is.cir"               %% "ciris-circe-yaml"     % "3.5.0",
      "org.tpolecat"         %% "doobie-core"          % "1.0.0-RC10",
      "org.tpolecat"         %% "doobie-h2"            % "1.0.0-RC10",
      "org.tpolecat"         %% "doobie-hikari"        % "1.0.0-RC10",
      "org.tpolecat"         %% "doobie-postgres"      % "1.0.0-RC10",
      "org.typelevel"        %% "log4cats-slf4j"       % "2.6.0",
      "ch.qos.logback"        % "logback-classic"      % "1.4.14",
    ),
  )
  .dependsOn(protos)

lazy val protos = project
  .in(file("modules/protobufs"))
  .enablePlugins(Fs2Grpc, ProtocPlugin)
  .settings(
    name         := "protobufs",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala2Version,
    Compile / PB.protoSources += baseDirectory.value / "src",
    libraryDependencies ++= Seq(
      "com.thesamet.scalapb" %% "scalapb-runtime" % "0.11.13" % "protobuf",
      "com.thesamet.scalapb" %% "compilerplugin"  % "0.11.13",
    ),
  )

enablePlugins(ScalafmtPlugin)

addCommandAlias("publishLocal", "Docker/publishLocal")
addCommandAlias("checkFormat", "scalafmtCheckAll")
addCommandAlias("format", "scalafmtAll")
