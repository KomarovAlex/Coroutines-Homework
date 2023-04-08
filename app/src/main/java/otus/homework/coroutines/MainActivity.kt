package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import otus.homework.coroutines.view.CatsView
import otus.homework.coroutines.viewmodel.CatsViewModel
import otus.homework.coroutines.viewmodel.Error
import otus.homework.coroutines.viewmodel.Load
import otus.homework.coroutines.viewmodel.Success

class MainActivity : AppCompatActivity() {

    private val catsViewModel: CatsViewModel by viewModels {
        CatsViewModel.CatsViewModelFactory(diContainer.service, diContainer.catsImageService)
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel.loadFact()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                catsViewModel.state.collect { result ->
                    when (result) {
                        is Load -> view.showMessage(result.message)
                        is Success -> view.populate(result.value)
                        is Error -> view.showMessage(result.message)
                    }
                }
            }
        }
    }
}