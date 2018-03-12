package nl.gn0s1s.between

import org.scalacheck._
import org.scalacheck.Gen.oneOf
import org.scalacheck.Prop._

object RelationSpec extends Properties("Relation") {
  def genRelation: Gen[Relation] = oneOf(Relation.full.toSeq)

  property("there are thirteen relations defined") = Relation.full.size == 13

  property("the inverse of the inverse of a relation is the relation") = forAll(genRelation) {
    relation =>
      relation.inverse.inverse.equals(relation)
  }

  property("from the paper - constraints between L and R given path: R <-(< m mi >)- - S - -(o m)-> L ") =
    Relation.constraints(Set(oi, mi), Set(<, m, mi, >)) == Set(<, >, o, m, di, s, si, fi, is)

  property("from the paper - constraints between S and R given path: S - -(o m)-> L - - (o s)-> R") =
    Relation.constraints(Set(o, m), Set(o, s)) == Set(<, o, m)

  property("from the paper - constraints between D and R given path: D - -(d)-> S - -(< m)-> R") =
    Relation.constraints(Set(d), Set(<, m)) == Set(<)

  property("from the paper - constraints between D and L given path: D - -(d)-> S - -(o m)-> L") =
    Relation.constraints(Set(d), Set(o, m)) == Set(<, o, m, d, s)

  property("from the readme - constraints between W and D given path: W <-(o)- - T - -(>, mi)-> D") =
    Relation.constraints(Set(o), Set(<, m)) == Set(<)
}
