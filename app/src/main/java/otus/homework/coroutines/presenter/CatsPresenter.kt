package otus.homework.coroutines.presenter

import kotlinx.coroutines.Job
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
) : Presenter {

    private var _catsView: ICatsView? = null
    private lateinit var job: Job

    fun onInitComplete() {
        job = scope.launch {
            try {
                _catsView?.populate(
                    CatsModel(
                        catsService.getCatFact().text,
                        catsImageService.getCatImageUrl().url,
                    )
                )
            } catch (t: Throwable) {
                when (t) {
                    is SocketTimeoutException -> _catsView?.showMessage("Не удалось получить ответ от сервера.")
                    else -> {
                        CrashMonitor.trackWarning(t)
                        _catsView?.showMessage(t.message ?: "")
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancel()
    }
}