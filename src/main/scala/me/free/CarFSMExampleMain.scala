package me.free

import akka.actor.typed.ActorSystem
import me.free.actor.CarFSM

object CarFSMExampleMain {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem(CarFSM(), "car_fsm")
    val log         = actorSystem.log
    log.info(s"Car FSM Started.")
  }
}
