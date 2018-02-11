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
}
