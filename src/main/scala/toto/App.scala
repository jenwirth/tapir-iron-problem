package toto


import cats.effect.*
import cats.syntax.all.*
import org.typelevel.log4cats.slf4j.Slf4jLogger
import com.comcast.ip4s.*
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.Logger
import sttp.tapir.server.http4s.Http4sServerInterpreter
import org.http4s.implicits.*
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object App extends IOApp.Simple:

  given Logger[IO] = Slf4jLogger.getLogger[IO]

  val ironRoute = Http4sServerInterpreter[IO]().toRoutes(endpoints.ironToto.serverLogic(toto=>{
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


