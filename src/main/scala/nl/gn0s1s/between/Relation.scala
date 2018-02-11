package nl.gn0s1s.between

sealed trait Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean
  def inverse: Relation
}

case object `<` extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t < s
  val inverse: Relation = `>`
}

case object m extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t m s
  val inverse: Relation = mi
}

case object o extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t o s
  val inverse: Relation = oi
}

case object fi extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t fi s
  val inverse: Relation = f
}

case object di extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t di s
  val inverse: Relation = d
}

case object s extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t s s
  val inverse: Relation = si
}

case object is extends Relation {
  override val toString = "=="

  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t == s
  val inverse: Relation = this
}

case object si extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t si s
  val inverse: Relation = s
}

case object d extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t d s
  val inverse: Relation = di
}

case object f extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t f s
  val inverse: Relation = fi
}

case object oi extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t oi s
  val inverse: Relation = o
}

case object mi extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t mi s
  val inverse: Relation = m
}

case object `>` extends Relation {
  def apply[T](t: Interval[T], s: Interval[T]): Boolean = t > s
  val inverse: Relation = `<`
}

object Relation {
  val dur: Set[Relation] = Set(s, d, f)
  val con: Set[Relation] = Set(fi, di, si)
  val concur: Set[Relation] = Set(o, fi, di, s, is, si, d, f, oi)
  val full: Set[Relation] = Set(`<`, m, o, fi, di, s, is, si, d, f, oi, mi, `>`)

  val transitivityTable: Map[(Relation, Relation), Set[Relation]] = Map(
    (<, <) -> Set(<),
    (<, >) -> full,
    (<, d) -> Set(<, o, m, d, s),
    (<, di) -> Set(<),
    (<, o) -> Set(<),
    (<, oi) -> Set(<, o, m, d, s),
    (<, m) -> Set(<),
    (<, mi) -> Set(<, o, m, d, s),
    (<, s) -> Set(<),
    (<, si) -> Set(<),
    (<, f) -> Set(<, o, m, d, s),
    (<, fi) -> Set(<),
    (<, is) -> Set(<),

    (>, <) -> full,
    (>, >) -> Set(>),
    (>, d) -> Set(>, oi, mi, d, f),
    (>, di) -> Set(>),
    (>, o) -> Set(>, oi, mi, d, f),
    (>, oi) -> Set(>),
    (>, m) -> Set(>, oi, mi, d, f),
    (>, mi) -> Set(>),
    (>, s) -> Set(>, oi, mi, d, f),
    (>, si) -> Set(>),
    (>, f) -> Set(>),
    (>, fi) -> Set(>),
    (>, is) -> Set(>),

    (d, <) -> Set(<),
    (d, >) -> Set(>),
    (d, d) -> Set(d),
    (d, di) -> full,
    (d, o) -> Set(<, o, m, d, s),
    (d, oi) -> Set(>, oi, mi, d, f),
    (d, m) -> Set(<),
    (d, mi) -> Set(>),
    (d, s) -> Set(d),
    (d, si) -> Set(>, oi, mi, d, f),
    (d, f) -> Set(d),
    (d, fi) -> Set(<, o, m, d, s),
    (d, is) -> Set(d),

    (di, <) -> Set(<, o, m, di, fi),
    (di, >) -> Set(>, oi, di, mi, si),
    (di, d) -> concur,
    (di, di) -> Set(di),
    (di, o) -> Set(o, di, fi),
    (di, oi) -> Set(oi, di, si),
    (di, m) -> Set(o, di, fi),
    (di, mi) -> Set(oi, di, si),
    (di, s) -> Set(o, di, fi),
    (di, si) -> Set(di),
    (di, f) -> Set(di, si, oi),
    (di, fi) -> Set(di),
    (di, is) -> Set(di),

    (o, <) -> Set(<),
    (o, >) -> Set(>, oi, di, mi, si),
    (o, d) -> Set(o, d, s),
    (o, di) -> Set(<, o, m, di, fi),
    (o, o) -> Set(<, o, m),
    (o, oi) -> concur,
    (o, m) -> Set(<),
    (o, mi) -> Set(oi, di, si),
    (o, s) -> Set(o),
    (o, si) -> Set(o, di, fi),
    (o, f) -> Set(d, s, o),
    (o, fi) -> Set(<, o, m),
    (o, is) -> Set(o),

    (oi, <) -> Set(<, o, m, di, fi),
    (oi, >) -> Set(>),
    (oi, d) -> Set(oi, d, f),
    (oi, di) -> Set(>, oi, di, mi, si),
    (oi, o) -> concur,
    (oi, oi) -> Set(>, oi, mi),
    (oi, m) -> Set(o, di, fi),
    (oi, mi) -> Set(>),
    (oi, s) -> Set(oi, d, f),
    (oi, si) -> Set(oi, >, mi),
    (oi, f) -> Set(oi),
    (oi, fi) -> Set(oi, di, si),
    (oi, is) -> Set(oi),

    (m, <) -> Set(<),
    (m, >) -> Set(>, oi, di, mi, si),
    (m, d) -> Set(o, d, s),
    (m, di) -> Set(<),
    (m, o) -> Set(<),
    (m, oi) -> Set(o, d, s),
    (m, m) -> Set(<),
    (m, mi) -> Set(f, fi, is),
    (m, s) -> Set(m),
    (m, si) -> Set(m),
    (m, f) -> Set(d, s, o),
    (m, fi) -> Set(<),
    (m, is) -> Set(m),

    (mi, <) -> Set(<, o, m, di, fi),
    (mi, >) -> Set(>),
    (mi, d) -> Set(oi, d, f),
    (mi, di) -> Set(>),
    (mi, o) -> Set(oi, d, f),
    (mi, oi) -> Set(>),
    (mi, m) -> Set(s, si, is),
    (mi, mi) -> Set(>),
    (mi, s) -> Set(d, f, oi),
    (mi, si) -> Set(>),
    (mi, f) -> Set(mi),
    (mi, fi) -> Set(mi),
    (mi, is) -> Set(mi),

    (s, <) -> Set(<),
    (s, >) -> Set(>),
    (s, d) -> Set(d),
    (s, di) -> Set(<, o, m, di, fi),
    (s, o) -> Set(<, o, m),
    (s, oi) -> Set(oi, d, f),
    (s, m) -> Set(<),
    (s, mi) -> Set(mi),
    (s, s) -> Set(s),
    (s, si) -> Set(s, si, is),
    (s, f) -> Set(d),
    (s, fi) -> Set(<, m, o),
    (s, is) -> Set(s),

    (si, <) -> Set(<, o, m, di, fi),
    (si, >) -> Set(>),
    (si, d) -> Set(oi, d, f),
    (si, di) -> Set(di),
    (si, o) -> Set(o, di, fi),
    (si, oi) -> Set(oi),
    (si, m) -> Set(o, di, fi),
    (si, mi) -> Set(mi),
    (si, s) -> Set(s, si, is),
    (si, si) -> Set(si),
    (si, f) -> Set(oi),
    (si, fi) -> Set(di),
    (si, is) -> Set(si),

    (f, <) -> Set(<),
    (f, >) -> Set(>),
    (f, d) -> Set(d),
    (f, di) -> Set(>, oi, mi, di, si),
    (f, o) -> Set(o, d, s),
    (f, oi) -> Set(>, oi, mi),
    (f, m) -> Set(m),
    (f, mi) -> Set(>),
    (f, s) -> Set(d),
    (f, si) -> Set(>, oi, mi),
    (f, f) -> Set(f),
    (f, fi) -> Set(f, fi, is),
    (f, is) -> Set(f),

    (fi, <) -> Set(<),
    (fi, >) -> Set(>, oi, mi, di, si),
    (fi, d) -> Set(o, d, s),
    (fi, di) -> Set(di),
    (fi, o) -> Set(o),
    (fi, oi) -> Set(oi, di, si),
    (fi, m) -> Set(m),
    (fi, mi) -> Set(si, oi, di),
    (fi, s) -> Set(o),
    (fi, si) -> Set(di),
    (fi, f) -> Set(f, fi, is),
    (fi, fi) -> Set(fi),
    (fi, is) -> Set(fi),

    (is, <) -> Set(<),
    (is, >) -> Set(>),
    (is, d) -> Set(d),
    (is, di) -> Set(di),
    (is, o) -> Set(o),
    (is, oi) -> Set(oi),
    (is, m) -> Set(m),
    (is, mi) -> Set(mi),
    (is, s) -> Set(s),
    (is, si) -> Set(si),
    (is, f) -> Set(f),
    (is, fi) -> Set(fi),
    (is, is) -> Set(is))

  def constraints(r1: Set[Relation], r2: Set[Relation]): Set[Relation] = {
    (for {
      elem <- r1
      elem2 <- r2
    } yield transitivityTable(elem, elem2)).flatten
  }

  def findRelation[T](t: Interval[T], s: Interval[T]): Relation = full.find(_.apply[T](t, s)).get
}
