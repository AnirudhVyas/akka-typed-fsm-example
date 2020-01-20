package me.free.actor

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import me.free.actor.CarFSM.IO._
import me.free.actor.CarFSMModels.{Coordinates, Mirror, Seat, SteeringWheel}

/**
  * Personalization of a car seating, mirror position and steering wheel position as defined in terms of
  * trivialized coordinates (not real/serious coordinates) but to present overall idea of FSM in akka typed.
  * Feel like example of dining philosophers is a bit obtuse for me personally.
  *
  *  Car FSM flips through states via [[Behavior]] here this would be:
  *     [[CarFSM.preEntry()]] --> [[CarFSM.running()]] --> [[CarFSM.parked()]]
  */
object CarFSM {

  /**
    * Default starting [[Behavior]]
    * @return [[Behavior]] [[preEntry()]] seeded with [[Initialized]] data.
    */
  def apply(): Behavior[EventLike] = preEntry(Initialized(List.empty, List.empty, SteeringWheel(isManual = true, Coordinates.initial)))

  /**
    * Pre-Entry state for the car, expects [[EnterCar]] event to flip it to [[running()]] state.
    * @param data seed data injected.
    * @return
    */
  private def preEntry(data: Initialized) = Behaviors.logMessages[EventLike] {
    Behaviors.setup[EventLike] { ctx =>
      val log = ctx.log
      Behaviors.receiveMessage[EventLike] { msg =>
        (msg, data) match {
          case (EnterCar(userId, replyTo), data: Initialized) =>
            // do some user profile setup here.
            replyTo ! CarEnteredSuccessfully(userId, replyTo)
            running(data)
          case (msg, data) =>
            log.info(s"invalid message $msg for  preEntry stage, please send EnterCar first")
            Behaviors.same[EventLike]
        }
      }
    }
  }

  /**
    * Running state for the car.
    * @param data data injected from previous state.
    * @return [[Behavior]] for running state.
    */
  private def running(data: DataLike): Behavior[EventLike] = Behaviors.logMessages[EventLike] {
    Behaviors.setup[EventLike] { ctx =>
      Behaviors.receiveMessage[EventLike] { msg =>
        (msg, data) match {
          case (Accelerate(replyTo), data: DataLike) =>
            // increase the speed
            // increase the music volume
            running(data)
          case (Brake(replyTo), data: DataLike) =>
            // slow the speed down
            // decrease music volume
            running(data)
          case (Halt(replyTo), data: DataLike) =>
            // parking mode.
            parked(data)
          case _ => Behaviors.same
        }
      }
    }
  }

  private def parked(data: DataLike): Behavior[EventLike] = Behaviors.logMessages[EventLike] {
    Behaviors.setup[EventLike] { ctx =>
      Behaviors.receiveMessage[EventLike] { msg =>
        (msg, data) match {
          case (Accelerate(replyTo), data: DataLike) =>
            // increase the speed (change/update speedometer state here)
            // increase the music volume (change update music system component state here)
            running(data)
          case (Brake(replyTo), data: DataLike) =>
            // slow the speed down (speedometer state here)
            // decrease music volume
            running(data)
          case (Halt(replyTo), data: DataLike) =>
            // parking mode.
            parked(data)
          case (adjustCarSeatEvent: AdjustCarSeatEvent, data: DataLike) =>
            // adjust car seat state here.
            parked(data)
          case (adjustMirrorEvent: AdjustMirrorEvent, data: DataLike) =>
            // adjust mirror state here.
            parked(data)
          case _ => Behaviors.same
        }
      }
    }
  }

  /**
    * All I/O for this FSM.
    */
  object IO {

    sealed trait EventLike {
      def replyTo: ActorRef[EventLike]
    }

    case class EnterCar(userId: String, replyTo: ActorRef[EventLike])                               extends EventLike
    case class CarEnteredSuccessfully(userId: String, replyTo: ActorRef[EventLike])                 extends EventLike
    case class Accelerate(replyTo: ActorRef[EventLike])                                             extends EventLike
    case class Brake(replyTo: ActorRef[EventLike])                                                  extends EventLike
    case class Halt(replyTo: ActorRef[EventLike])                                                   extends EventLike
    case class AdjustCarSeatEvent(seat: Seat, replyTo: ActorRef[EventLike])                         extends EventLike
    case class AdjustMirrorEvent(mirror: List[Mirror], replyTo: ActorRef[EventLike])                extends EventLike
    case class AdjustSteeringWheelEvent(steeringWheel: SteeringWheel, replyTo: ActorRef[EventLike]) extends EventLike

    sealed trait DataLike
    case class Initialized(seats: List[Seat], mirrors: List[Mirror], steeringWheel: SteeringWheel) extends DataLike

  }
}
