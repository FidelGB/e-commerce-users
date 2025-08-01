addSbtPlugin("com.thesamet"   % "sbt-protoc"          % "1.0.4")
addSbtPlugin("org.typelevel"  % "sbt-fs2-grpc"        % "2.7.14")
addSbtPlugin("org.scalameta"  % "sbt-scalafmt"        % "2.5.2")
addSbtPlugin("nl.gn0s1s"      % "sbt-dotenv"          % "3.1.1")
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.10.4")

libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "compilerplugin" % "0.11.13",
)
