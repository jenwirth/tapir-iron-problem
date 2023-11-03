package toto

import sttp.tapir.json.pickler.Pickler
import toto.model.*
import toto.schemas.given
import toto.util.upickle.given
import toto.util.pickle.given

object picklers {
  given Pickler[IronToto] = Pickler.derived
  given Pickler[VanillaToto] = Pickler.derived
}
