package com.ecommerce.users.config

import cats.effect.kernel.Async
import cats.effect.kernel.Resource
import doobie.hikari.HikariTransactor
import com.zaxxer.hikari.HikariConfig
import com.ecommerce.users.config

object PostgresConnection {
  def resource[F[_] : Async](cfg: PostgresConfig): Resource[F, HikariTransactor[F]] = for {
    hikariConfig <- Resource.pure {
                      val config = new HikariConfig()
                      config.setDriverClassName("org.postgresql.Driver")
                      config.setJdbcUrl(
                        s"jdbc:postgresql://${cfg.host}:${cfg.port}/${cfg.database}",
                      )
                      config.setUsername(cfg.user)
                      config.setPassword(cfg.password.value)
                      config
                    }
    xa           <- HikariTransactor.fromHikariConfig[F](hikariConfig)
  } yield xa
}
