package com.ccooy.app3.ext.functional

interface Kind<out F, out A>
typealias Kind2<F, A, B> = Kind<Kind<F, A>, B>

class ForEither private constructor() {
    companion object
}
typealias EitherOf<A, B> = Kind2<ForEither, A, B>

sealed class Either<out A, out B> : EitherOf<A, B> {

    internal abstract val isRight: Boolean

    internal abstract val isLeft: Boolean

    fun isLeft(): Boolean = isLeft

    fun isRight(): Boolean = isRight

    inline fun <C> fold(ifLeft: (A) -> C, ifRight: (B) -> C): C = when (this) {
        is Either.Right -> ifRight(b)
        is Either.Left -> ifLeft(a)
    }

    fun swap(): Either<B, A> = fold({ Right(it) }, { Left(it) })

    fun toOption(): Option<B> =
        fold({ None }, { Some(it) })

    data class Left<out A> @PublishedApi internal constructor(val a: A) : Either<A, Nothing>() {
        override val isLeft
            get() = true
        override val isRight
            get() = false

        companion object {
            operator fun <A> invoke(a: A): Either<A, Nothing> = Left(a)
        }
    }

    data class Right<out B> @PublishedApi internal constructor(val b: B) : Either<Nothing, B>() {
        override val isLeft
            get() = false
        override val isRight
            get() = true

        companion object {
            operator fun <B> invoke(b: B): Either<Nothing, B> = Right(b)
        }
    }

    companion object {

        fun <L> left(left: L): Either<L, Nothing> = Left(left)

        fun <R> right(right: R): Either<Nothing, R> = Right(right)
    }
}

fun <A> A.left(): Either<A, Nothing> = Either.Left(this)

fun <A> A.right(): Either<Nothing, A> = Either.Right(this)

fun <L> Left(left: L): Either<L, Nothing> = Either.left(left)

fun <R> Right(right: R): Either<Nothing, R> = Either.right(right)