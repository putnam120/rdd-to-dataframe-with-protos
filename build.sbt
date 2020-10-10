name := "data-pipeline"
version in ThisBuild := "0.1"
scalaVersion in ThisBuild := "2.12.12"

lazy val global = project
  .in(file("."))
  .settings(settings)
  .aggregate(
    common,
    pipeline
  )
lazy val common =
  project
    .settings(settings, libraryDependencies ++= commonDependencies)
    .enablePlugins(AkkaGrpcPlugin)

lazy val pipeline = project
  .dependsOn(common)
  .settings(
    settings,
    publishArtifact in Test := false,
    assemblySettings,
    libraryDependencies ++= commonDependencies ++ Seq(
      dependencies.sparkCore,
      dependencies.sparkSql,
      dependencies.sparkProto,
      dependencies.hadoopCommon,
      dependencies.hadoopClient,
      dependencies.hadoopAws,
      dependencies.awsSdk
    )
  )

// Artifact Generation
lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case _                             => MergeStrategy.first
  },
  assemblyShadeRules in assembly := Seq(
    ShadeRule
      .rename("com.google.common.**" -> "repackaged.com.google.common.@1")
      .inAll,
    ShadeRule.rename("com.google.protobuf.**" -> "shadeproto.@1").inAll
  )
)

// Settings
lazy val settings = Seq(
  scalacOptions ++= Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8", // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:unsound-match", // Pattern match may not be typesafe.
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Xlint:infer-any" // Warn when a type argument is inferred to be `Any`.
  )
)

// Dependencies
lazy val dependencies = new {
  val akkaVersion = "2.6.8"
  val akkaGrpcVersion = "1.0.1"
  val sparkVersion = "3.0.0"
  val scalaTestVersion = "3.3.0-SNAP2"
  val sparkProtoVersion = "0.11.0-RC1"
  val hadoopVersion = "3.0.0"
  val awsSdkVersion = "1.11.879"

  val akkaTypedActor = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  val akkaDiscovery = "com.typesafe.akka" %% "akka-discovery" % akkaVersion
  val akkaPki = "com.typesafe.akka" %% "akka-pki" % akkaVersion
  val akkaProtoBuf = "com.typesafe.akka" %% "akka-protobuf" % akkaVersion

  val akkaTypedActorTestkit =
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test
  val akkaStreamTestkit =
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test

  val typesafeConfig = "com.typesafe" % "config" % "1.4.0"

  val sparkCore = "org.apache.spark" %% "spark-core" % sparkVersion
  val sparkSql = "org.apache.spark" %% "spark-sql" % sparkVersion
  val sparkProto =
    "com.thesamet.scalapb" %% "sparksql-scalapb" % sparkProtoVersion
  val hadoopAws = "org.apache.hadoop" % "hadoop-aws" % hadoopVersion
  val hadoopCommon = "org.apache.hadoop" % "hadoop-common" % hadoopVersion
  val hadoopClient = "org.apache.hadoop" % "hadoop-client" % hadoopVersion
  val awsSdk = "com.amazonaws" % "aws-java-sdk" % awsSdkVersion

  val scalatest = "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
}

lazy val commonDependencies = Seq(
  dependencies.typesafeConfig
)
