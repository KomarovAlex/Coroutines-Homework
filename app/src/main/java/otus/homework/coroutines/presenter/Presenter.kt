package otus.homework.coroutines.presenter

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class Presenter<T> {
    protected var scope: CoroutineScope = PresenterScope()

    protected var view: T? = null

    fun attachView(view: T) {
        this.view = view
    }

    fun detachView() {
        view = null
        scope.cancel()
    }
}

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext =
         Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob()
}


