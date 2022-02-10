package nl.gn0s1s.between

import java.time._

import org.scalacheck._
import org.scalacheck.Prop._

object IntervalSpec extends Properties("Interval") {
  def genInterval: Gen[Interval[Instant]] = for {
    i <- Gen.oneOf(1000L, 2000L, 3000L, 4000L, 5000L, 6000L, 7000L)
    j <- Gen.oneOf(2000L, 3000L, 4000L, 5000L, 6000L, 7000L, 8000L)
    if i < j
  } yield Interval[Instant](Instant.ofEpochSecond(i), Instant.ofEpochSecond(j))

  implicit val arbitraryInterval: Arbitrary[Interval[Instant]] = Arbitrary(genInterval)

  def genIntervalDouble: Gen[Interval[Double]] = for {
    i <- Gen.chooseNum[Double](1, 7)
    j <- Gen.chooseNum[Double](2, 8)
    if i < j
  } yield Interval[Double](i, j)

  implicit val arbitraryIntervalDouble: Arbitrary[Interval[Double]] = Arbitrary(genIntervalDouble)

  property("Interval[Instant] has always only one unique relation between intervals") = forAll {
    (i: Interval[Instant], j: Interval[Instant]) =>
      Relation.full.count(_.apply[Instant](i, j)) == 1
  }

  property("Interval[Instant] if a relation is true then the converse is also true") = forAll {
    (i: Interval[Instant], j: Interval[Instant]) =>
      val relation = i.findRelation(j)
      relation.inverse(j, i)
  }

  property("Interval[Double] has always only one unique relation between intervals") = forAll {
    (i: Interval[Double], j: Interval[Double]) =>
      Relation.full.count(_.apply[Double](i, j)) == 1
  }

  property("Interval[Double] if a relation is true then the converse is also true") = forAll {
    (i: Interval[Double], j: Interval[Double]) =>
      val relation = i.findRelation(j)
      relation.inverse(j, i)
  }

  property("all relations and their inverses are available") = forAll { (i: Interval[Double], j: Interval[Double]) =>
    (i before j) == (j after i) &&
    (i precedes j) == (j precededBy i) &&
    (i meets j) == (j metBy i) &&
    (i overlaps j) == (j overlappedBy i) &&
    (i finishes j) == (j finishedBy i) &&
    (i ends j) == (j endedBy i) &&
    (i during j) == (j contains i) &&
    (i starts j) == (j startedBy i) &&
    (i si j) == (i startedBy j) &&
    (i encloses j) == (j enclosedBy i)
  }

  def genDouble = Gen.chooseNum[Double](2, 7)

  property("abuts returns the expected value") = forAll { (i: Interval[Double], j: Interval[Double]) =>
    i.findRelation(j) match {
      case `m` | `mi` => i abuts j
      case _ => !(i abuts j)
    }
  }

  property("gap returns the expected Interval") = forAll { (i: Interval[Double], j: Interval[Double]) =>
    i.findRelation(j) match {
      case `<` => i.gap(j).contains(Interval(i.`+`, j.`-`))
      case `>` => i.gap(j).contains(Interval(j.`+`, i.`-`))
      case _ => i.gap(j).isEmpty
    }
  }

  property("intersection returns the expected interval") = forAll { (i: Interval[Double], j: Interval[Double]) =>
    i.findRelation(j) match {
      case `<` | `m` | `mi` | `>` => i.intersection(j).isEmpty
      case `o` => i.intersection(j).contains(Interval(j.`-`, i.`+`))
      case `f` | `d` | `s` | `is` => i.intersection(j).contains(Interval(i.`-`, i.`+`))
      case `si` | `di` | `fi` => i.intersection(j).contains(Interval(j.`-`, j.`+`))
      case `oi` => i.intersection(j).contains(Interval(i.`-`, j.`+`))
    }
  }

  property("minus returns the expected interval") = forAll { (i: Interval[Double], j: Interval[Double]) =>
    i.findRelation(j) match {
      case `<` | `m` | `mi` | `>` => i.minus(j).contains(Interval(i.`-`, i.`+`))
      case `o` | `fi` => i.minus(j).contains(Interval(i.`-`, j.`-`))
      case `f` | `d` | `s` | `is` => i.minus(j).isEmpty
      case `si` | `oi` => i.minus(j).contains(Interval(j.`+`, i.`+`))
      case `di` => i.minus(j).contains(Interval(i.`-`, j.`-`)) && i.minus(j).contains(Interval(j.`+`, i.`+`))
    }
  }

  property("span returns the expected interval") = forAll { (i: Interval[Double], j: Interval[Double]) =>
    i.findRelation(j) match {
      case `<` => i.span(j) == Interval(i.`-`, j.`+`)
      case `>` => i.span(j) == Interval(j.`-`, i.`+`)
      case _ => i.span(j) == i.union(j).get
    }
  }

  property("span encloses both intervals") = forAll { (i: Interval[Double], j: Interval[Double]) =>
    val span = i.span(j)
    (span encloses i) && (span encloses j)
  }

  property("union returns the expected interval") = forAll { (i: Interval[Double], j: Interval[Double]) =>
    i.findRelation(j) match {
      case `<` | `>` => i.union(j).isEmpty
      case `m` | `o` => i.union(j).contains(Interval(i.`-`, j.`+`))
      case `f` | `d` | `s` => i.union(j).contains(Interval(j.`-`, j.`+`))
      case `is` | `si` | `di` | `fi` => i.union(j).contains(Interval(i.`-`, i.`+`))
      case `oi` | `mi` => i.union(j).contains(Interval(j.`-`, i.`+`))
    }
  }

  property("after returns the expected value") = forAll(genIntervalDouble, genDouble) {
    (i: Interval[Double], d: Double) =>
      if (i.`-` > d)
        i.after(d)
      else
        !i.after(d)
  }

  property("before returns the expected value") = forAll(genIntervalDouble, genDouble) {
    (i: Interval[Double], d: Double) =>
      if (i.`+` < d)
        i.before(d)
      else
        !i.before(d)
  }

  property("chop chops an interval into two meeting intervals") = forAll(genIntervalDouble, genDouble) {
    (i: Interval[Double], d: Double) =>
      if (d > i.`-` && d < i.`+`) {
        val chopped = i.chop(d).get
        Relation.findRelation[Double](chopped._1, chopped._2) == m
      } else
        i.chop(d).isEmpty
  }

  property("contains returns the expected value") = forAll(genIntervalDouble, genDouble) {
    (i: Interval[Double], d: Double) =>
      if (i.`-` <= d && i.`+` >= d)
        i.contains(d)
      else
        !i.contains(d)
  }

  property("endsAt returns the expected value") = forAll(genIntervalDouble, genDouble) {
    (i: Interval[Double], d: Double) =>
      if (i.`+` == d)
        i.endsAt(d)
      else
        !i.endsAt(d)
  }

  property("startsAt returns the expected value") = forAll(genIntervalDouble, genDouble) {
    (i: Interval[Double], d: Double) =>
      if (i.`-` == d)
        i.startsAt(d)
      else
        !i.startsAt(d)
  }

  property("with- returns the expected interval") = forAll(genIntervalDouble, genDouble) {
    (i: Interval[Double], d: Double) =>
      if (d < i.`+`)
        i.`with-`(d) == Interval(d, i.`+`)
      else
        Prop.throws(classOf[IllegalArgumentException])(Interval(d, i.`+`))
  }

  property("with+ returns the expected interval") = forAll(genIntervalDouble, genDouble) {
    (i: Interval[Double], d: Double) =>
      if (d > i.`-`)
        i.`with+`(d) == Interval(i.`-`, d)
      else
        Prop.throws(classOf[IllegalArgumentException])(Interval(i.`-`, d))
  }

  property("clamp returns the expected value") = forAll(genIntervalDouble, genDouble) {
    (i: Interval[Double], d: Double) =>
      if (d < i.`-`)
        i.clamp(d) == i.`-`
      else if (d > i.`+`)
        i.clamp(d) == i.`+`
      else
        i.clamp(d) == d
  }
}
