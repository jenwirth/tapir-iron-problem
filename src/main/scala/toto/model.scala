package toto

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import sttp.tapir.*
import sttp.tapir.Schema.annotations.validate
import sttp.tapir.codec.iron.TapirCodecIron
import toto.model.PhoneNumber

object model {
  type PhoneNumber = String :| Match["\\+[\\d\\(\\)\\.\\s\\-]+"]

  case class IronToto(
    phoneNumber: PhoneNumber
  )

  case class VanillaToto(
    @validate(Validator.pattern("\\+[\\d\\(\\)\\.\\s\\-]+"))
    phoneNumber: String
  )

}

object schemas extends TapirCodecIron {

  given (using s: Schema[PhoneNumber]): Schema[PhoneNumber] = s
    .description("E.164 international phone number")
    .encodedExample("+12125551234")

}
