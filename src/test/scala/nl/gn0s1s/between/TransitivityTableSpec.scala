package nl.gn0s1s.between

import org.scalacheck.Properties
import org.scalacheck.Prop._

object TransitivityTableSpec extends Properties("Transitivity Table") {
  property("totals 13x13 entries") = {
    Relation.transitivityTable.size == 169
  }

  property("contains 22x <") = {
    Relation.transitivityTable.values.count(_.==(Set(<))) == 22
  }

  property("contains 22x >") = {
    Relation.transitivityTable.values.count(_.==(Set(>))) == 22
  }

  property("contains 7x o, fi, di") = {
    Relation.transitivityTable.values.count(_.==(Set(o, fi, di))) == 7
  }

  property("contains 7x o, s, d") = {
    Relation.transitivityTable.values.count(_.==(Set(o, s, d))) == 7
  }

  property("contains 7x oi, si, di") = {
    Relation.transitivityTable.values.count(_.==(Set(oi, si, di))) == 7
  }

  property("contains 7x d, f, oi") = {
    Relation.transitivityTable.values.count(_.==(Set(d, f, oi))) == 7
  }

  property("contains 6x <, m, o, fi, di") = {
    Relation.transitivityTable.values.count(_.==(Set(<, m, o, fi, di))) == 6
  }

  property("contains 6x <, m, o, s, d") = {
    Relation.transitivityTable.values.count(_.==(Set(<, m, o, s, d))) == 6
  }

  property("contains 6x m") = {
    Relation.transitivityTable.values.count(_.==(Set(m))) == 6
  }

  property("contains 6x di, si, oi, mi, >") = {
    Relation.transitivityTable.values.count(_.==(Set(di, si, oi, mi, >))) == 6
  }

  property("contains 6x d, f, oi, mi, >") = {
    Relation.transitivityTable.values.count(_.==(Set(d, f, oi, mi, >))) == 6
  }

  property("contains 6x mi") = {
    Relation.transitivityTable.values.count(_.==(Set(mi))) == 6
  }

  property("contains 5x o") = {
    Relation.transitivityTable.values.count(_.==(Set(o))) == 5
  }

  property("contains 5x oi") = {
    Relation.transitivityTable.values.count(_.==(Set(oi))) == 5
  }

  property("contains 4x <, m, o") = {
    Relation.transitivityTable.values.count(_.==(Set(<, m, o))) == 4
  }

  property("contains 4x oi, mi, >") = {
    Relation.transitivityTable.values.count(_.==(Set(oi, mi, >))) == 4
  }

  property("contains 3x full") = {
    Relation.transitivityTable.values.count(_.==(Relation.full)) == 3
  }

  property("contains 3x concur") = {
    Relation.transitivityTable.values.count(_.==(Relation.concur)) == 3
  }

  property("contains 3x fi") = {
    Relation.transitivityTable.values.count(_.==(Set(fi))) == 3
  }

  property("contains 3x fi, ==, f") = {
    Relation.transitivityTable.values.count(_.==(Set(fi, is, f))) == 3
  }

  property("contains 3x s, ==, si") = {
    Relation.transitivityTable.values.count(_.==(Set(s, is, si))) == 3
  }

  property("contains 3x s") = {
    Relation.transitivityTable.values.count(_.==(Set(s))) == 3
  }

  property("contains 3x si") = {
    Relation.transitivityTable.values.count(_.==(Set(si))) == 3
  }

  property("contains 3x f") = {
    Relation.transitivityTable.values.count(_.==(Set(f))) == 3
  }

  property("contains 1x ==") = {
    Relation.transitivityTable.values.count(_.==(Set(is))) == 1
  }
}
