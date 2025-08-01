package com.ecommerce.users.grpc

import cats.effect._
import io.grpc.Metadata
import fs2.Stream
import com.ecommerce.proto.api.UsersServiceFs2Grpc
import com.ecommerce.proto.users._
import com.ecommerce.proto.commons.Ok

class UsersImplementation[F[_]: Async] extends UsersServiceFs2Grpc[F, Metadata]{

    override def createUser(request: CreateUserCmd, ctx: Metadata): F[Ok] = Async[F].pure(Ok.defaultInstance)

    override def getUser(request: GetUserQry, ctx: Metadata): F[GetUser] = Async[F].pure(GetUser.defaultInstance)
    
    override def login(request: LoginCmd, ctx: Metadata): F[Login] = Async[F].pure(Login.defaultInstance)
  
}

object UsersImplementation {
    def getBind[F[_]: Async] = UsersServiceFs2Grpc.bindServiceResource[F](new UsersImplementation[F])
}
