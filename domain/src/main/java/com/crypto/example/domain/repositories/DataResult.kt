package com.crypto.example.domain.repositories

import java.lang.Exception

sealed interface DataResult<out T>
data class Success<out T>(val values: T): DataResult<T>
data class Error(val exception: Exception): DataResult<Nothing>
