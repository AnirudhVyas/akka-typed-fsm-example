package me.free.actor

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.typed.ActorRef
import me.free.actor.CarFSM.IO.{AdjustCarSeatEvent, CarEnteredSuccessfully, EnterCar, EventLike}
import me.free.actor.CarFSMModels.{Coordinates, Seat, SeatComponent, Temperature}
import org.scalatest.{BeforeAndAfter, FeatureSpecLike, Matchers}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Tests all features with scenarios.
  */
class CarFSMTest extends ScalaTestWithActorTestKit with Matchers with BeforeAndAfter with FeatureSpecLike {
  val fsm: ActorRef[EventLike] = testKit.spawn(CarFSM())

  /**
    * Tests all scenarios related to car profiling.
    */
  feature("car_profile") {
    scenario("Car entry initiated") {
      val probe = testKit.createTestProbe[EventLike]("test_probe")
      fsm ! EnterCar("ricky.nj@gmail.com", probe.ref)
      probe.expectMessage(2 minutes, CarEnteredSuccessfully("ricky.nj@gmail.com", probe.ref))
    }

    /**
      * Invalid/Negative test scenario.
      */
    scenario("adjust seat scenario - *BEFORE* car entry, this is *NOT* possible in this car") {
      val probe = testKit.createTestProbe[EventLike]("test_probe")
      fsm ! AdjustCarSeatEvent(
        Seat(
          back = SeatComponent(isManual = true, Coordinates.max),
          headRest = SeatComponent(isManual = true, Coordinates.max),
          base = SeatComponent(isManual = true, Coordinates.max),
          temperature = Temperature.warm
        ),
        probe.ref
      )
      // this message will be ignored as you have
      probe.expectNoMessage()
    }
  }
}
