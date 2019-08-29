package neotypes
package implicits.mappers

import java.time.{Duration, LocalDate, LocalDateTime, LocalTime, Period, OffsetDateTime, OffsetTime, ZonedDateTime}
import java.util.UUID

import mappers.ParameterMapper

import org.neo4j.driver.v1.Value
import org.neo4j.driver.v1.types.{IsoDuration, Point}

import scala.collection.compat._
import scala.collection.compat.IterableOnce
import scala.jdk.CollectionConverters._
import scala.language.higherKinds

trait ParameterMappers {
  implicit final val BooleanParameterMapper: ParameterMapper[Boolean] =
    ParameterMapper.fromCast(b => new java.lang.Boolean(b))

  implicit final val ByteArrayParameterMapper: ParameterMapper[Array[Byte]] =
    ParameterMapper.identity

  implicit final val DoubleParameterMapper: ParameterMapper[Double] =
    ParameterMapper.fromCast(d => new java.lang.Double(d))

  implicit final val DurationParameterMapper: ParameterMapper[Duration] =
    ParameterMapper.identity

  implicit final val FloatParameterMapper: ParameterMapper[Float] =
    ParameterMapper.fromCast(f => new java.lang.Float(f))

  implicit final val IntParameterMapper: ParameterMapper[Int] =
    ParameterMapper.fromCast(i => new java.lang.Integer(i))

  implicit final val IsoDurationParameterMapper: ParameterMapper[IsoDuration] =
    ParameterMapper.identity

  implicit final val LocalDateParameterMapper: ParameterMapper[LocalDate] =
    ParameterMapper.identity

  implicit final val LocalDateTimeParameterMapper: ParameterMapper[LocalDateTime] =
    ParameterMapper.identity

  implicit final val LocalTimeParameterMapper: ParameterMapper[LocalTime] =
    ParameterMapper.identity

  implicit final val LongParameterMapper: ParameterMapper[Long] =
    ParameterMapper.fromCast(l => new java.lang.Long(l))

  implicit final val OffsetDateTimeParameterMapper: ParameterMapper[OffsetDateTime] =
    ParameterMapper.identity

  implicit final val OffsetTimeParameterMapper: ParameterMapper[OffsetTime] =
    ParameterMapper.identity

  implicit final val PeriodParameterMapper: ParameterMapper[Period] =
    ParameterMapper.identity

  implicit final val PointParameterMapper: ParameterMapper[Point] =
    ParameterMapper.identity

  implicit final val StringParameterMapper: ParameterMapper[String] =
    ParameterMapper.identity

  implicit final val UUIDParameterMapper: ParameterMapper[UUID] =
    ParameterMapper[String].contramap(_.toString)

  implicit final val ValueParameterMapper: ParameterMapper[Value] =
    ParameterMapper.identity

  implicit final val ZonedDateTimeParameterMapper: ParameterMapper[ZonedDateTime] =
    ParameterMapper.identity

  private final def iterableOnceParameterMapper[T](mapper: ParameterMapper[T]): ParameterMapper[IterableOnce[T]] =
    ParameterMapper.fromCast { col =>
      col.iterator.map(v => mapper.toQueryParam(v).underlying).asJava
    }

  implicit final def collectionParameterMapper[T, C[_]](implicit mapper: ParameterMapper[T], ev: C[T] <:< IterableOnce[T]): ParameterMapper[C[T]] =
    iterableOnceParameterMapper(mapper).contramap(ev)

  implicit final def optionParameterMapper[T >: Null](implicit mapper: ParameterMapper[T]): ParameterMapper[Option[T]] =
    ParameterMapper.fromCast { optional =>
      optional.map(v => mapper.toQueryParam(v).underlying).orNull
    }
}
