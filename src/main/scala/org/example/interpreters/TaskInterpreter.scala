package org.example.interpreters

import java.util.UUID

import monix.eval.Task
import org.example.domain.algebras.UserRepository
import org.models.{User, testUser}
import monix.cats._
import org.example.utils.Result

trait TaskInterpreter extends UserRepository[Task] {
  override def findUser(id: UUID): Task[Result[User]] =
    Task.now { Right(testUser) }

  override def updateUser(u: User): Task[Result[User]] =
    Task.now { Right(testUser)} /* as above */
}
