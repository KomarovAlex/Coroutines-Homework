package otus.homework.coroutines.presenter

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.api.CatsImageService
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.view.CatsModel
import otus.homework.coroutines.view.ICatsView
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
) : Presenter<ICatsView>() {
    fun onInitComplete() {
        scope.launch {
            val fact = async { catsService.getCatFact().text }
            val url = async { catsImageService.getCatImageUrl().url }
            try {
                view?.populate(
                    CatsModel(fact.await(), url.await())
                )
            } catch (t: Throwable) {
                when (t) {
                    is SocketTimeoutException -> view?.showMessage("Не удалось получить ответ от сервера.")
                    else -> {
                        CrashMonitor.trackWarning(t)
                        view?.showMessage(t.message ?: "")
                    }
                }
            }
        }
    }

}