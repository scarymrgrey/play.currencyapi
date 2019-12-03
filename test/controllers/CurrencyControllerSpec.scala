package controllers

import akka.actor.ActorSystem
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import akka.actor.ActorSystem._
import akka.stream.ActorMaterializer
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class CurrencyControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit val system = ActorSystem("TodoRoCurrencyControllerSpecuteSpec")
  implicit val mat = ActorMaterializer()
  "CurrencyController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = new CurrencyController(stubControllerComponents())
      val home = controller.convert().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the application" in {
      val controller = inject[CurrencyController]
      val home = controller.convert().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }
  }
}
