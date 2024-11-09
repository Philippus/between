package nl.gn0s1s.between

import scala.annotation.nowarn

@nowarn
final case class Interval[T] private (`-`: T, `+`: T)(implicit ordering: Ordering[T]) {
  t =>
  import ordering.mkOrderingOps

  def <(s: Interval[T]): Boolean        = t.`+` < s.`-`
  def before(s: Interval[T]): Boolean   = this < s
  def precedes(s: Interval[T]): Boolean = this < s

  def m(s: Interval[T]): Boolean     = t.`+` == s.`-`
  def meets(s: Interval[T]): Boolean = this m s

  def o(s: Interval[T]): Boolean        = t.`-` < s.`-` && t.`+` > s.`-` && t.`+` < s.`+`
  def overlaps(s: Interval[T]): Boolean = this o s

  def fi(s: Interval[T]): Boolean         = s f this
  def finishedBy(s: Interval[T]): Boolean = s f this
  def endedBy(s: Interval[T]): Boolean    = s f this

  def di(s: Interval[T]): Boolean       = s d this
  def contains(s: Interval[T]): Boolean = s d this

  def s(s: Interval[T]): Boolean      = t.`-` == s.`-` && t.`+` < s.`+`
  def starts(s: Interval[T]): Boolean = this s s

  def si(s: Interval[T]): Boolean        = s s this
  def startedBy(s: Interval[T]): Boolean = s s this

  def d(s: Interval[T]): Boolean      = t.`-` > s.`-` && t.`+` < s.`+`
  def during(s: Interval[T]): Boolean = this d s

  def f(s: Interval[T]): Boolean        = t.`-` > s.`-` && t.`+` == s.`+`
  def finishes(s: Interval[T]): Boolean = this f s
  def ends(s: Interval[T]): Boolean     = this f s

  def oi(s: Interval[T]): Boolean           = s o this
  def overlappedBy(s: Interval[T]): Boolean = s o this

  def mi(s: Interval[T]): Boolean    = s m this
  def metBy(s: Interval[T]): Boolean = s m this

  def >(s: Interval[T]): Boolean          = s < this
  def after(s: Interval[T]): Boolean      = s < this
  def precededBy(s: Interval[T]): Boolean = s < this

  def findRelation(s: Interval[T]): Relation = Relation.findRelation[T](this, s)

  def abuts(s: Interval[T]): Boolean = (this m s) || (this mi s)

  def encloses(s: Interval[T]): Boolean   = (this di s) || (this == s) || (this si s) || (this fi s)
  def enclosedBy(s: Interval[T]): Boolean = (this d s) || (this == s) || (this s s) || (this f s)

  def gap(s: Interval[T]): Option[Interval[T]] =
    if (this < s || this > s) {
      Interval[T](t.`+`.min(s.`+`), t.`-`.max(s.`-`))
    } else
      None

  def intersection(s: Interval[T]): Option[Interval[T]] =
    if (!((this < s) || (this abuts s) || (this > s)))
      Interval[T](t.`-`.max(s.`-`), t.`+`.min(s.`+`))
    else
      None

  def span(s: Interval[T]): Interval[T] =
    new Interval[T](t.`-`.min(s.`-`), t.`+`.max(s.`+`))

  def minus(s: Interval[T]): Set[Interval[T]] =
    if (this enclosedBy s)
      Set()
    else if ((this o s) || (this fi s))
      Set(new Interval[T](t.`-`, s.`-`))
    else if ((this oi s) || (this si s))
      Set(new Interval[T](s.`+`, t.`+`))
    else if (this di s)
      Set(new Interval[T](t.`-`, s.`-`), new Interval[T](s.`+`, t.`+`))
    else
      Set(this)

  def union(s: Interval[T]): Option[Interval[T]] =
    if (!(this < s || this > s))
      Interval[T](t.`-`.min(s.`-`), t.`+`.max(s.`+`))
    else
      None

  def after(p: T): Boolean = p < t.`-`

  def before(p: T): Boolean = t.`+` < p

  def chop(p: T): Option[(Interval[T], Interval[T])] =
    if (t.`-` < p && t.`+` > p)
      Some((new Interval[T](t.`-`, p), new Interval[T](p, t.`+`)))
    else
      None

  def contains(p: T): Boolean = t.`-` <= p && t.`+` >= p

  def endsAt(p: T): Boolean = t.`+` == p

  def startsAt(p: T): Boolean = t.`-` == p

  def `with-`(p: T): Option[Interval[T]] = Interval[T](p, t.`+`)

  def `with+`(p: T): Option[Interval[T]] = Interval[T](t.`-`, p)

  def clamp(p: T): T =
    if (p < t.`-`)
      t.`-`
    else if (p > t.`+`)
      t.`+`
    else
      p
}

object Interval {
  def apply[T](`-`: T, `+`: T)(implicit ordering: Ordering[T]): Option[Interval[T]] = {
    import ordering.mkOrderingOps
    if (`-`.<(`+`))
      Some(new Interval(`-`, `+`))
    else
      None
  }
}
