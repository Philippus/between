package nl.gn0s1s.between

final case class Interval[T](`-`: T, `+`: T)(implicit ordering: Ordering[T]) {
  t =>
  import ordering.mkOrderingOps

  require(t.`-`.<(t.`+`))

  def <(s: Interval[T]): Boolean = t.`+` < s.`-`
  def before(s: Interval[T]): Boolean = this < s
  def precedes(s: Interval[T]): Boolean = this < s // alspaugh

  def m(s: Interval[T]): Boolean = t.`+` == s.`-`
  def meets(s: Interval[T]): Boolean = this m s

  def o(s: Interval[T]): Boolean = t.`-` < s.`-` && t.`+` > s.`-` && t.`+` < s.`+`
  def overlaps(s: Interval[T]): Boolean = this o s

  def fi(s: Interval[T]): Boolean = s f this
  def finishedBy(s: Interval[T]): Boolean = s f this

  def di(s: Interval[T]): Boolean = s d this
  def contains(s: Interval[T]): Boolean = s d this

  def s(s: Interval[T]): Boolean = t.`-` == s.`-` && t.`+` < s.`+`
  def starts(s: Interval[T]): Boolean = this s s

  def si(s: Interval[T]): Boolean = s s this
  def startedBy(s: Interval[T]): Boolean = s s this

  def d(s: Interval[T]): Boolean = t.`-` > s.`-` && t.`+` < s.`+`
  def during(s: Interval[T]): Boolean = this d s

  def f(s: Interval[T]): Boolean = t.`-` > s.`-` && t.`+` == s.`+`
  def finishes(s: Interval[T]): Boolean = this f s

  def oi(s: Interval[T]): Boolean = s o this
  def overlappedBy(s: Interval[T]): Boolean = s o this

  def mi(s: Interval[T]): Boolean = s m this
  def metBy(s: Interval[T]): Boolean = s m this

  def >(s: Interval[T]): Boolean = s < this
  def after(s: Interval[T]): Boolean = s < this
  def precededBy(s: Interval[T]): Boolean = s < this // alspaugh

  def findRelation(s: Interval[T]): Relation = Relation.findRelation[T](this, s)
}
