package org.example.application.modules

import cats.Monad
import org.example.domain.algebras.UserRepository
import org.example.core.modules.BaseModule
import org.example.application.services.UserService

trait UserModule[F[_]] extends BaseModule [F]{
  val userService: UserService[F]
}

class UserModuleImpl[F[_]: Monad](interpreter: UserRepository[F]) extends UserModule[F] {
  override lazy val userService  = new UserService[F](interpreter)
}
