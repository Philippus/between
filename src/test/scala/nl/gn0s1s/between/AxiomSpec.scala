package nl.gn0s1s.between

import org.scalacheck._
import org.scalacheck.Prop._

import java.time.Instant
import scala.math.Ordering.Implicits._

/** Spec for axioms in "Moments and Points in an Interval-Based Temporal Logic"
  */
object AxiomSpec extends Properties("Axiom") {
  def genM1 = for {
    p <- Gen.choose(Instant.MIN, Instant.MAX)
    q <- Gen.choose(p, Instant.MAX) if p < q
    r <- Gen.choose(q, Instant.MAX) if q < r
    s <- Gen.choose(q, Instant.MAX) if q < s
    t <- Gen.choose(Instant.MIN, q) if t < q
  } yield (
    Interval[Instant](p, q), // i
    Interval[Instant](q, r), // j
    Interval[Instant](q, s), // k
    Interval[Instant](t, q) // l
  )

  property("M1: if two periods both meet a third, then any period met by one must also be met by the other.") =
    forAll(genM1) { case (i, j, k, l) =>
      (i.meets(j) && i.meets(k) && l.meets(j)) ==> l.meets(k)
    }

  def genM2 = for {
    p <- Gen.choose(Instant.MIN, Instant.MAX)
    q <- Gen.choose(p, Instant.MAX) if p < q
    r <- Gen.choose(q, Instant.MAX) if q < r
    s <- Gen.choose(Instant.MIN, q) if s < q
    t <- Gen.choose(s, Instant.MAX) if s < t
    u <- Gen.choose(t, Instant.MAX) if t < u
  } yield (
    Interval[Instant](p, q), // i
    Interval[Instant](q, r), // j
    Interval[Instant](s, t), // k
    Interval[Instant](t, u) // l
  )

  property(
    "M2: if period i meets j and period k meets l, then exactly one of the following holds: 1) i meets l; 2) there is an m such that i meets m and m meets l; 3) there is an n such that k meets n and n meets j"
  ) = forAll(genM2) { case (i, j, k, l) =>
    val m = if (i < l) i.gap(l) else None // i:m:l
    val n = if (k < j) k.gap(j) else None // k:n:j
    (i.meets(j) && k.meets(l)) ==>
      (i.meets(l) || m.isDefined || n.isDefined) &&
      !(i.meets(l) && m.isDefined) &&
      !(i.meets(l) && n.isDefined) &&
      !(m.isDefined && n.isDefined)
  }

  def genML1 = for {
    p <- Gen.choose(Instant.MIN, Instant.MAX)
    q <- Gen.choose(p, Instant.MAX) if p < q
  } yield Interval[Instant](p, q) // i

  property("ML1: an interval cannot meet itself") = forAll(genML1) { i =>
    !i.meets(i)
  }

  def genML2 = for {
    p <- Gen.choose(Instant.MIN, Instant.MAX)
    q <- Gen.choose(p, Instant.MAX) if p < q
    r <- Gen.choose(q, Instant.MAX) if q < r
  } yield (
    Interval[Instant](p, q), // i
    Interval[Instant](q, r) // j
  )

  property("ML2: if i meets j then j does not meet i") = forAll(genML2) { case (i, j) =>
    i.meets(j) ==> !j.meets(i)
  }

  def genML3 = for {
    p <- Gen.choose(Instant.MIN, Instant.MAX)
    q <- Gen.choose(p, Instant.MAX) if p < q
    r <- Gen.choose(q, Instant.MAX) if q < r
  } yield (
    Interval[Instant](p, q), // i
    Interval[Instant](q, r) // m
  )

  property("ML3: there is no period m such that i:m:i,") = forAll(genML3) { case (i, m) =>
    !(i.meets(m) && m.meets(i))
  }

  def genM3 = for {
    p <- Gen.choose(Instant.MIN, Instant.MAX)
    q <- Gen.choose(p, Instant.MAX) if p < q
    r <- Gen.choose(Instant.MIN, p) if r < p
    s <- Gen.choose(q, Instant.MAX) if q < s
  } yield (
    Interval[Instant](p, q), // i
    r, // startOfJ
    s // endOfK
  )

  property("M3: time does not start or stop") = forAll(genM3) { case (i, startOfJ, endOfK) =>
    val j = Interval(startOfJ, i.`-`)
    val k = Interval(i.`+`, endOfK)
    j.meets(i) && i.meets(k)
  }

  def genM4 = for {
    p <- Gen.choose(Instant.MIN, Instant.MAX)
    q <- Gen.choose(p, Instant.MAX) if p < q
    r <- Gen.choose(q, Instant.MAX) if q < r
    s <- Gen.choose(Instant.MIN, p) if s < p
    t <- Gen.choose(r, Instant.MAX) if r < t
  } yield (
    Interval[Instant](p, q), // i
    Interval[Instant](q, r), // j
    s, // startOfM
    t // endOfN
  )

  property("M4: if two meets are separated by intervals, then this sequence is a longer interval") = forAll(genM4) {
    case (i, j, startOfM, endOfN) =>
      val m = Interval(startOfM, i.`-`)
      val n = Interval(j.`+`, endOfN)
      val k = Interval(m.`+`, n.`-`)
      (m.meets(i) && i.meets(j) && j.meets(n)) &&
      (m.meets(k) && k.meets(n))
  }

  def genM5 = for {
    p <- Gen.choose(Instant.MIN, Instant.MAX)
    q <- Gen.choose(p, Instant.MAX) if p < q
    r <- Gen.choose(q, Instant.MAX) if q < r
    s <- Gen.choose(r, Instant.MAX) if r < s
  } yield (
    Interval[Instant](p, q), // i
    Interval[Instant](q, r), // j
    Interval[Instant](r, s), // l
    Interval[Instant](q, s) // k
  )

  property("M5: There is only one time period between any two meeting-places.") = forAll(genM5) { case (i, j, l, k) =>
    ((i.meets(j) && j.meets(l)) &&
      (i.meets(k) && k.meets(l))) == (j == k)
  }

  property("M4.1: i:j implies m:i+j:n") = forAll(genM4) { case (i, j, startOfM, endOfN) =>
    val m = Interval(startOfM, i.`-`)
    val n = Interval(j.`+`, endOfN)
    val ij = i.union(j).get
    (m.meets(i) && i.meets(j) && j.meets(n)) &&
    (m.meets(ij) && ij.meets(n))
  }
}
