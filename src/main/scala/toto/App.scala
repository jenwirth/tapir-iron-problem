package toto


import cats.effect.*
import cats.syntax.all.*
import org.typelevel.log4cats.slf4j.Slf4jLogger
import com.comcast.ip4s.*
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.Logger
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import org.http4s.implicits.*
import sttp.tapir.DecodeResult
import sttp.tapir.DecodeResult.Error
import sttp.tapir.DecodeResult.Error.JsonDecodeException
import sttp.tapir.server.interceptor.DecodeFailureContext
import sttp.tapir.server.interceptor.decodefailure.DefaultDecodeFailureHandler.{FailureMessages, default}
import sttp.tapir.server.interceptor.decodefailure.DefaultDecodeFailureHandler
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import toto.util.IronException

object App extends IOApp.Simple:

  given Logger[IO] = Slf4jLogger.getLogger[IO]

  def failureDetailMessage(failure: DecodeResult.Failure): Option[String] = failure match {
    case Error(_, JsonDecodeException(errors, IronException(originalValue, errorMessage))) =>
      Some(s"Failed to parse value $originalValue. Error: $errorMessage. Json errors: ${errors}")
    case other => FailureMessages.failureDetailMessage(other)
  }

  def failureMessage(ctx: DecodeFailureContext): String = {
    val base = FailureMessages.failureSourceMessage(ctx.failingInput)
    val detail = failureDetailMessage(ctx.failure)
    FailureMessages.combineSourceAndDetail(base, detail)
  }

  val handler = new DefaultDecodeFailureHandler(default.respond, failureMessage, default.response)
  val serverOptions: Http4sServerOptions[IO] = Http4sServerOptions.customiseInterceptors[IO].decodeFailureHandler(handler).options

  val ironRoute = Http4sServerInterpreter[IO](serverOptions).toRoutes(endpoints.ironToto.serverLogic(toto=>{
    Logger[IO].info("received message with iron toto")
    IO.pure(().asRight)
  }))

  val vanillaRoute = Http4sServerInterpreter[IO]().toRoutes(endpoints.vanillaToto.serverLogic(toto => {
    Logger[IO].info("received message with vanilla toto")
    IO.pure(().asRight)
  }))


  val swaggerRoute = Http4sServerInterpreter[IO]().toRoutes(
    SwaggerInterpreter().fromEndpoints(
      List(endpoints.ironToto, endpoints.vanillaToto ),
      "toto",
      "0.1"
    )
  )

  val app = (ironRoute <+> vanillaRoute <+> swaggerRoute).orNotFound

  override val run = (for {
    _ <-  EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"7777")
      .withHttpApp(app)
      .build
  } yield ()).useForever


