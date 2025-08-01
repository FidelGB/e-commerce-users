package com.ecommerce.users.grpc

import cats.effect._
import cats.syntax.all._
import io.grpc.Metadata
import fs2.Stream
import doobie.hikari.HikariTransactor
import org.slf4j.Logger
import com.ecommerce.proto.api.UsersServiceFs2Grpc
import com.ecommerce.proto.users._
import com.ecommerce.proto.commons.Ok
import com.ecommerce.users.config.GlobalConfig
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.SelfAwareLogger

class UsersImplementation[F[_] : Async](
  config: GlobalConfig,
  xa: HikariTransactor[F],
  logger: SelfAwareLogger[F],
) extends UsersServiceFs2Grpc[F, Metadata] {

  override def createUser(request: CreateUserCmd, ctx: Metadata): F[Ok] =
    logger.info(s"[CreateUserCmd] Received request: $request") *>
      Async[F].pure(Ok.defaultInstance)

  override def getUser(request: GetUserQry, ctx: Metadata): F[GetUser] =
    logger.info(s"[GetUserQry] Received request: $request") *>
      Async[F].pure(GetUser.defaultInstance)

  override def login(request: LoginCmd, ctx: Metadata): F[Login] =
    logger.info(s"[LoginCmd] Received request: $request") *>
      Async[F].pure(Login.defaultInstance)

}

object UsersImplementation {
  def getBind[F[_] : Async](
    config: GlobalConfig,
    xa: HikariTransactor[F],
    logger: SelfAwareLogger[F],
  ) = UsersServiceFs2Grpc.bindServiceResource[F](new UsersImplementation[F](config, xa, logger))
}
