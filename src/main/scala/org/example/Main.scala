package org.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import monix.cats._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.example.api.services.UserHttpService
import org.example.interpreters.TaskInterpreter
import org.example.application.modules.UserModuleImpl
import org.example.core.utils.FutureTransformableInstances._

import scala.concurrent.Future
import scala.io.StdIn

object Main extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()

  // Composition Root
  val userInterpreter = new Object() with TaskInterpreter
  val userModule = new UserModuleImpl(userInterpreter)

  // TODO: Compose several http Service
  val routes = UserHttpService[Task](userModule).routes

  // TODO: akka-http-circe vv
  //    val fut: Future[Json] = Http().singleRequest(HttpRequest(uri = uri)).flatMap(Unmarshal(_).to[Json])

  val serverComputation = for {
    binding <- Http().bindAndHandle(routes, "localhost", 6666)
    // TODO: REMOVE THIS NASTY PRINT WITHOUT IO
    io = println(s"Server online at http://localhost:6666/\nPress RETURN to stop...")
  } yield binding

  StdIn.readLine()
  serverComputation.onComplete(_ => system.terminate())
}
