package otus.homework.coroutines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.api.CatsImageService
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.view.CatsModel
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        CrashMonitor.trackWarning(t)
        when (t) {
            is CancellationException -> {
                throw t
            }
            is SocketTimeoutException -> {
                _state.tryEmit(Error("Не удалось получить ответ от сервера."))
            }
            else -> {
                _state.tryEmit(Error("$t.message"))
            }
        }
    }

    private val _state = MutableStateFlow<Result<CatsModel>>(Load("loading"))
    val state = _state

    init {
        loadFact()
    }

    fun loadFact() {
        viewModelScope.launch(exceptionHandler) {
            _state.emit(
                Success(
                    CatsModel(
                        catsService.getCatFact().text,
                        catsImageService.getCatImageUrl().url,
                    )
                )
            )
        }
    }


    class CatsViewModelFactory(
        private val catFactsService: CatsService,
        private val catImagesService: CatsImageService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CatsViewModel(catFactsService, catImagesService) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}