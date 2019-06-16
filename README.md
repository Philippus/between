# Between

[![Build Status](https://travis-ci.org/Philippus/between.svg?branch=master)](https://travis-ci.org/Philippus/between)
[![codecov](https://codecov.io/gh/Philippus/between/branch/master/graph/badge.svg)](https://codecov.io/gh/Philippus/between)
![Current Version](https://img.shields.io/badge/version-0.4.1-brightgreen.svg?style=flat "0.4.1")
[![License](https://img.shields.io/badge/license-MIT-blue.svg?style=flat "MIT")](LICENSE.md)

Between is a library for working with (time) intervals and the relations between them. It takes as a basis the thirteen
relations of Allen's Interval Algebra. This is a system for reasoning about (temporal) intervals as described in
the paper [Maintaining Knowledge about Temporal Intervals](https://cse.unl.edu/~choueiry/Documents/Allen-CACM1983.pdf).

## Installation
Between is published for Scala 2.11, 2.12, and 2.13. To start using it add the following to your `build.sbt`:

```
resolvers += Resolver.bintrayRepo("gn0s1s", "releases")

libraryDependencies += "nl.gn0s1s" %% "between" % "0.4.1"
```

## Example usage
When the endpoints of an interval are known, the `Interval[T]` case class is available for testing all possible
[relations](#relations) between intervals. It needs two values `-` and `+` of type `T` which reflect the (inclusive)
endpoints of an interval. For the type `T` there needs to be an implicit `Ordering` trait available. Additionally the
endpoint `-` needs to be smaller than the endpoint `+`.

The example below shows two examples for `Double` and `java.time.Instant`:

```scala
import nl.gn0s1s.between._

val i = Interval[Int](1, 2) // i: nl.gn0s1s.between.Interval[Int] = Interval(1,2)
val j = Interval[Int](2, 3) // j: nl.gn0s1s.between.Interval[Int] = Interval(2,3)

i meets j // res0: Boolean = true
j metBy i // res1: Boolean = true

val k = Interval[java.time.Instant](java.time.Instant.ofEpochSecond(1000L), java.time.Instant.ofEpochSecond(2000L))
// k: nl.gn0s1s.between.Interval[java.time.Instant] = Interval(1970-01-01T00:16:40Z,1970-01-01T00:33:20Z)

val l = Interval[java.time.Instant](java.time.Instant.ofEpochSecond(1500L), java.time.Instant.ofEpochSecond(2500L))
// l: nl.gn0s1s.between.Interval[java.time.Instant] = Interval(1970-01-01T00:25:00Z,1970-01-01T00:41:40Z)

k overlaps l // res2: Boolean = true
l overlappedBy k // res3: Boolean = true
```

### Relations

Given two intervals there is always only one of the following thirteen defined relations true:

| relation       | symbol  | inverse | inverse relation   | diagram                     |
| -------------- | ------- | ------- | ------------------ | --------------------------- |
| x `before` y   |   `<`   |   `>`   | y `after` x        | <pre>xxx yyy</pre>          |
| x `equals` y   |   `==`  |  `==`   | y `equals` x       | <pre>  xxx<br>  yyy</pre>   |
| x `meets` y    |   `m`   |  `mi`   | y `metBy` x        | <pre>xxxyyy</pre>           |
| x `overlaps` y |   `o`   |  `oi`   | y `overlappedBy` x | <pre> xxx<br>  yyy</pre>    |
| x `during` y   |   `d`   |  `di`   | y `contains` x     | <pre>  xxx <br> yyyyy</pre> |
| x `starts` y   |   `s`   |  `si`   | y `startedBy` x    | <pre> xxx <br> yyyyy</pre>  |
| x `finishes` y |   `f`   |  `fi`   | y `finishedBy` x   | <pre>   xxx<br> yyyyy</pre> |

* `before` and `after` are also available as `precedes` and `precededBy`, respectively.
* `finishes` and `finishedBy` are also available as `ends` and `endedBy`.

There's a `findRelation` method which can be used to find out which relation exists between two intervals. The
`Relation` has an `inverse` method implemented, which gives the inverse of a relation.

```scala
import nl.gn0s1s.between._

val i = Interval[Int](1, 2) // i: nl.gn0s1s.between.Interval[Int] = Interval(1,2)
val j = Interval[Int](2, 3) // j: nl.gn0s1s.between.Interval[Int] = Interval(2,3)

val relationBetweenIAndJ = i.findRelation(j) // relationBetweenIAndJ: nl.gn0s1s.between.Relation = m

relationBetweenIAndJ.inverse // res0: nl.gn0s1s.between.Relation = mi
```

### Additional methods
A number of additional methods are availabe on the `Interval[T]` case class, some of which may be familiar for users of
the [ThreeTen-Extra](http://www.threeten.org/threeten-extra/apidocs/org/threeten/extra/Interval.html) Interval class.

* `abuts`, checks if the interval abuts the supplied interval
* `encloses`, checks if the interval encloses the supplied interval
* `enclosedBy`, checks if the interval is enclosed by the supplied interval
* `gap`, returns the interval that is between this interval and the supplied interval
* `intersection`, returns the intersection of this interval and the supplied interval
* `minus`, returns the result of subtracting the supplied interval from this interval
* `span`, returns the smallest interval that contains this interval and the supplied interval
* `union`, returns the union of this interval and the supplied interval

Some point related methods are:
* `after`, checks if the interval is after the supplied point
* `before`, checks if the interval is before the supplied point
* `chop`, chops this interval into two intervals that meet at the supplied point
* `contains`, checks if supplied point is within the interval
* `endsAt`, checks if the interval ends at the supplied point
* `startsAt`, checks if the interval starts at the supplied point
* `with-`, returns a copy of this interval with the supplied `-` endpoint
* `with+`, returns a copy of this interval with the supplied `+` endpoint

### Reasoning
I got inspired to write this library `during` [Eric Evans](https://github.com/ericevans0)' talk at the
[Domain-Driven Design Europe 2018](https://dddeurope.com/2018/) conference. I started writing it in the train on my way
back from the conference, this can be represented like this:

`write lib <-(o)- - train - -(>, mi)-> DDD Europe - -(di)-> EE talk <-(d) - - inspired`

Since the composition table of relations and the `constraints` method are implemented we can find out what the possible
relations between `write lib` and `DDD Europe` are:

```scala
import nl.gn0s1s.between._

Relation.constraints(Set(o), Set(<, m)) // res0: Set[nl.gn0s1s.between.Relation] = Set(<)
```

## Resources
Allen's Interval Algebra:
- [Maintaining Knowledge about Temporal Intervals](https://cse.unl.edu/~choueiry/Documents/Allen-CACM1983.pdf)
- [Wikipedia entry](https://en.wikipedia.org/wiki/Allen%27s_interval_algebra)
- Thomas A. Alspaugh's Foundations Material on [Allen's Interval Algebra](http://www.ics.uci.edu/~alspaugh/cls/shr/allen.html)

Related links:
- [A Modal Logic for Chopping Intervals](https://staff.fnwi.uva.nl/y.venema/papers/1991/vene-moda91.pdf)
- [SOWL QL: Querying Spatio - Temporal Ontologies in OWL](http://www.intelligence.tuc.gr/~petrakis/publications/SOWLQL-JDS.pdf)
- [AsterixDB Temporal Functions: Allen’s Relations](https://asterixdb.apache.org/docs/0.8.8-incubating/aql/allens.html)

## License
The code is available under the [MIT license](LICENSE.md).
