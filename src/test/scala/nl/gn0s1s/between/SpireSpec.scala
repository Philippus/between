package nl.gn0s1s.between

import org.scalacheck.Prop._
import org.scalacheck._

import spire.math.Real

object SpireSpec extends Properties("Using Spire") {
  property("works with spire types that have a total ordering") = {
    nl.gn0s1s.between.Interval(Real(2), Real(3)).after(nl.gn0s1s.between.Interval(Real(0), Real(1)))
  }

  property("works with non-spire types that have a spire ordering") = {
    import spire.implicits.BigDecimalAlgebra
    nl.gn0s1s.between.Interval(BigDecimal(2), BigDecimal(3)).after(nl.gn0s1s.between.Interval(BigDecimal(0), BigDecimal(1)))
  }
}
