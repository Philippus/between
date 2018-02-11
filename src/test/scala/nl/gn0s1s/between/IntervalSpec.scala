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

  property("all relations and their inverses are available") = forAll {
    (i: Interval[Double], j: Interval[Double]) =>
      (i before j) == (j after i) &&
        (i precedes j) == (j precededBy i) &&
        (i meets j) == (j metBy i) &&
        (i overlaps j) == (j overlappedBy i) &&
        (i finishes j) == (j finishedBy i) &&
        (i during j) == (j contains i) &&
        (i starts j) == (j startedBy i) &&
        (i si j) == (i startedBy j)
  }
}
