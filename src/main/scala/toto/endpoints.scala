package toto

import sttp.tapir.endpoint
import sttp.tapir.*
import sttp.tapir.json.pickler.jsonBody
import toto.model.*
import toto.picklers.given

object endpoints:

  val ironToto = endpoint.post
    .in("toto" / "iron")
    .in(jsonBody[IronToto])
    .out(emptyOutput)

  val vanillaToto = endpoint.post
    .in("toto" / "vanilla")
    .in(jsonBody[VanillaToto])
    .out(emptyOutput)
