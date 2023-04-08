package otus.homework.coroutines.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.presenter.CatsPresenter

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catsModel: CatsModel) {
        findViewById<TextView>(R.id.fact_textView).text = catsModel.fact
        Picasso.get().load(catsModel.imageUrl).into(findViewById<ImageView>(R.id.fact_image))
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catsModel: CatsModel)
    fun showMessage(message: String)
}