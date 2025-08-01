package com.ecommerce.users

import cats.effect.{ IO, IOApp }
import cats.effect.ExitCode
import com.ecommerce.users.grpc.UsersImplementation
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import cats.effect.kernel.Resource
import fs2.grpc.syntax.all._

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val serverResource: Resource[IO, Unit] = for {
      grpcService <- UsersImplementation.getBind[IO]
      server      <-
        NettyServerBuilder
          .forPort(9999)
          .addService(grpcService)
          .resource[IO]
          .evalMap(s => IO.println("Starting gRPC server on port 9999") *> IO.pure(s.start()))
    } yield ()

    serverResource.useForever.as(ExitCode.Success)
  }
}
