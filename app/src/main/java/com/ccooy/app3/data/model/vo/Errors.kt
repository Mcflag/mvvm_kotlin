package com.ccooy.app3.data.model.vo

sealed class Errors : Throwable() {
    object NetworkError : Errors()
    object EmptyInputError : Errors()
    object EmptyResultsError : Errors()
    object SingleError : Errors()
}