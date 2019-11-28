package controllers

import play.api.libs.json.{Json, Reads}
import javax.inject.{Inject, _}
import play.api.mvc._

object CurrencyRequest {
  implicit val itemReads: Reads[CurrencyRequest] = Json.reads[CurrencyRequest]
}

case class CurrencyRequest(id: String, value: Double, from_currency: String, to_currency: String)

object CurrencyResponse {
  implicit val writes = Json.writes[CurrencyResponse]
}

case class CurrencyResponse(id: String, initial: Double, converted: Double, from_currency: String, to_currency: String)


@Singleton
class CurrencyController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  private val rates: Map[String, Double] =
    Map(
      "USD_PLN" -> 4.1,
      "PLN_USD" -> 1 / 4.1,
      "USD_EUR" -> 1.1,
      "EUR_USD" -> 1 / 1.1
    )

  private def doConvert(from: String, to: String, amount: Double): Double = {
    val rate = rates(s"${from}_${to}")
    amount * rate
  }

  def convert: Action[AnyContent] = Action { request: Request[AnyContent] =>
    val jsonBody = request.body.asJson
    jsonBody
      .map { json =>
        val result = json.as[Array[CurrencyRequest]].map(z => {
          val conversion = doConvert(z.from_currency, z.to_currency, z.value)
          CurrencyResponse(z.id, z.value, conversion, z.from_currency, z.to_currency)
        })
        Ok(Json.toJson(result))
      }
      .getOrElse {
        BadRequest("Expecting application/json request body")
      }
  }
}
