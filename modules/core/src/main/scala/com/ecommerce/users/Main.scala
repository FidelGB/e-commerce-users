package com.ecommerce.users

import cats.effect.{ IO, IOApp }
import cats.effect.ExitCode
import com.ecommerce.users.grpc.UsersImplementation
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import cats.effect.kernel.Resource
import fs2.grpc.syntax.all._
import com.ecommerce.users.config.GlobalConfig

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val serverResource: Resource[IO, Unit] = for {
      config      <- GlobalConfig.load[IO]
      grpcService <- UsersImplementation.getBind[IO]
      server      <-
        NettyServerBuilder
          .forPort(config.grpc.port)
          .addService(grpcService)
          .resource[IO]
          .evalMap(s =>
            IO.println(s"Starting gRPC server on port ${config.grpc.port}") *> IO.pure(s.start()),
          )
    } yield ()

    serverResource.useForever.as(ExitCode.Success)
  }
}
