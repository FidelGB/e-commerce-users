package com.ecommerce.users.config

import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import cats.syntax.apply._

import ciris._

case class GlobalConfig(
  appVersion: String,
  grpc: GrpcServerConfig,
  postgres: PostgresConfig,
)

case class GrpcServerConfig(
  port: Int,
)

case class PostgresConfig(
  host: String,
  port: Int,
  user: String,
  password: Secret[String],
  database: String,
)

object GlobalConfig {
  def load[F[_] : Async]: Resource[F, GlobalConfig] = (
    env("APP_VERSION").as[String].default("local"),
    loadGrpcConfig,
    loadPostgresConfig,
  ).mapN(GlobalConfig.apply).resource[F]

  private def loadGrpcConfig: ConfigValue[Effect, GrpcServerConfig] =
    env("GRPC_SERVER_PORT")
      .as[Int]
      .default(9000)
      .map(GrpcServerConfig.apply)

  private def loadPostgresConfig: ConfigValue[Effect, PostgresConfig] =
    (
      env("DB_HOST").as[String],
      env("DB_PORT").as[Int].default(5432),
      env("DB_USER").as[String],
      env("DB_PASSWORD").as[String].secret,
      env("DB_DATABASE").as[String].default("ecommerce_users"),
    ).mapN(PostgresConfig.apply)
}
