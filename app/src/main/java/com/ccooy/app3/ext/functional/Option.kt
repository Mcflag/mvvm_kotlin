package com.ccooy.app3.ext.functional

class ForOption private constructor() {
    companion object
}
typealias OptionOf<A> = Kind<ForOption, A>

fun <A> identity(a: A): A = a

sealed class Option<out A> : OptionOf<A> {

    companion object {

        fun <A> just(a: A): Option<A> = Some(a)

        fun <A> empty(): Option<A> = None
    }

    abstract fun isEmpty(): Boolean

    abstract fun get(): A

    inline fun <R> fold(ifEmpty: () -> R, ifSome: (A) -> R): R = when (this) {
        is None -> ifEmpty()
        is Some<A> -> ifSome(t)
    }

    inline fun whenEmpty(action: () -> Unit) =
        fold(action, {})

    fun orNull(): A? = fold({ null }, ::identity)
}

object None : Option<Nothing>() {
    override fun get() = throw NoSuchElementException("None.get")

    override fun isEmpty() = true

    override fun toString(): String = "None"
}

data class Some<out T>(val t: T) : Option<T>() {
    override fun get() = t

    override fun isEmpty() = false

    override fun toString(): String = "Some($t)"
}

fun <T> T?.toOption(): Option<T> = this?.let { Some(it) } ?: None

fun <T> Option<T>.getOrElse(default: () -> T): T = fold({ default() }, ::identity)

fun <A> A.some(): Option<A> = Some(this)

fun <A> none(): Option<A> = None
