package otus.homework.coroutines.viewmodel

sealed class Result<out T>

class Success<T>(val value: T) : Result<T>()
class Load(val message: String) : Result<Nothing>()
class Error(val message: String) : Result<Nothing>()