package com.ccooy.app3.data.cache

interface ICache<T> {
    fun put(put: T)
    fun get(): T
    fun cacheName(): String
    fun clearCache()
}