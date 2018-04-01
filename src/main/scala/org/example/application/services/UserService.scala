package org.example.application.services

import java.util.UUID

import cats.Monad
import cats.implicits._
import org.example.domain.algebras.UserRepository
import org.example.utils.Result
import org.models.User

import scala.language.higherKinds

// TODO: Add another algebra for logging
class UserService[F[_]: Monad](ur: UserRepository[F]) {
  // NOTE: We could(and should) use EitherT instead of F[Either[X, User]](Result[User]
  def addPoints(userId: UUID, pointsToAdd: Int): F[Result[User]] = {
    ur.findUser(userId).flatMap {
      case err@(Left(_)) => implicitly[Monad[F]].pure(err)
      case Right(user:User) =>
        val updated = user.copyAndAddPoints(pointsToAdd)
        for {
          updatedResult <- ur.updateUser(updated)
        } yield updatedResult
    }
  }

  def getUser(userId: UUID): F[Result[User]] = {
    ur.findUser(userId)
  }
}
