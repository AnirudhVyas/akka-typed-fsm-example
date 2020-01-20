package me.free.actor

/**
  * All models for [[CarFSM]] for cleaner FSM namespacing. However, All I/O stuff for FSM lies in [[CarFSM.IO]].
  */
object CarFSMModels {

  /**
    * Event trait that all Events come from used in FSM for Event modelling.
    */
  sealed trait EventLike

  /**
    * All data that is injected to [[akka.actor.typed.Behavior]] which works as `State` in [[akka.actor.FSM]] (in classic sense but in typed)
    */
  sealed trait DataLike

  sealed trait PositionLike {
    def currentY: Double
    def currentX: Double
    def currentZ: Double
    def angle: Double
  }

  /**
    * X represents how far object is gone horizontally - ideally this needs more representation but meh.
    * Y represents how high the object is set.
    * Angle represents how folded it is with respect to ground.
    *
    * @param currentY height set.
    * @param currentX x set.
    * @param currentZ z set.
    * @param angle    angle set.
    */
  case class Coordinates(currentY: Double, currentX: Double, currentZ: Double, angle: Double)

  /**
    * Fine grained components a car is made up of to track states better.
    */
  sealed trait CarComponentLike {
    def isManual: Boolean
    def adjustableCoordinates: Coordinates
  }

  sealed trait SeatComponentLike                                                  extends CarComponentLike
  case class SeatComponent(isManual: Boolean, adjustableCoordinates: Coordinates) extends SeatComponentLike

  sealed trait UnitLike

  sealed trait MetricLike {
    def name: String
    def measurement: Double
    def unit: UnitLike
  }

  /**
    * Re-usable temperature unit.
    */
  sealed trait TemperatureUnitLike extends UnitLike
  case object Fahrenheit           extends TemperatureUnitLike
  case object Celsius              extends TemperatureUnitLike

  /**
    * Generalized temperature metric.
    *
    * @param measurement measurement provided.
    * @param unit        unit of measurement of temperature (C or F)
    * @see [[TemperatureUnitLike]]
    */
  case class Temperature(measurement: Double, unit: TemperatureUnitLike)

  object Temperature {
    def warm: Temperature = Temperature(30, Celsius)
    def hot: Temperature  = Temperature(45, Celsius)
    def cold: Temperature = Temperature(10, Celsius)
  }

  /**
    * Seating position blueprint.
    */
  sealed trait SeatPositionKindLike

  object SeatPositions {
    case object DriverFront    extends SeatPositionKindLike
    case object PassangerFront extends SeatPositionKindLike
    case object RearRight      extends SeatPositionKindLike
    case object RearLeft       extends SeatPositionKindLike
    case object RearMiddle     extends SeatPositionKindLike
  }

  /**
    * Car seat blueprint.
    */
  sealed trait SeatLike {
    def back: SeatComponent
    def base: SeatComponent
    def headRest: SeatComponent
    def temperature: Temperature
  }

  final case class Mirror(isManual: Boolean, adjustableCoordinates: Coordinates)        extends CarComponentLike
  final case class SteeringWheel(isManual: Boolean, adjustableCoordinates: Coordinates) extends CarComponentLike
  case class Cabin(isManual: Boolean, adjustableCoordinates: Coordinates)               extends CarComponentLike

  /**
    * NOTE: This is a special car - james bond stuff, it can send different probes out to examine
    * if atmosphere is healthy for occupant to go out ;-)
    * This is done so we can see example of spinning off another FSM from within an FSM which sends events
    * back to the parent/mothership car.
    */
  sealed trait ProbeLike extends CarComponentLike {
    def name: String
    def measurement: MetricLike
  }

  /**
    * Car seat component.
    *
    * @param back        back reset.
    * @param base        base of the seat.
    * @param headRest    head rest.
    * @param temperature temperature.
    */
  case class Seat(back: SeatComponent, base: SeatComponent, headRest: SeatComponent, temperature: Temperature) extends SeatLike

  /**
    * Companion coordinates.
    */
  object Coordinates {
    def min: Coordinates     = Coordinates(0.0, 0.0, 0.0, 0.0)
    def max: Coordinates     = Coordinates(10.0, 10.0, 10.0, 180.0)
    def initial: Coordinates = Coordinates(5.0, 5.0, 5.0, 60.0)
  }
}
