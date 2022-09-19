package dev.senk0n.dogbreeds.application.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import dev.senk0n.dogbreeds.MainActivity
import dev.senk0n.dogbreeds.R
import dev.senk0n.dogbreeds.databinding.ActivityMainBinding
import dev.senk0n.dogbreeds.databinding.PartErrorBinding

abstract class BaseFragment : Fragment {
    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected lateinit var activityBinding: ActivityMainBinding
    private var _errorBinding: PartErrorBinding? = null
    protected val errorBinding get() = _errorBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        activityBinding = (requireActivity() as MainActivity).binding
        _errorBinding = PartErrorBinding.bind(activityBinding.root)
        activityBinding.swipeRefresh.isEnabled = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected fun hideError() {
        errorBinding.errorContainer.visibility = View.GONE
    }

    protected fun showError(
        title: String? = null,
        details: String? = null,
        button: String? = null,
        imageSetter: ((ImageView) -> Unit)? = null,
        onClickListener: ((View) -> Unit)? = null,
    ) {
        with(errorBinding) {
            titleText.text = title ?: getString(R.string.error_occurred)
            detailsText.text = details ?: getString(R.string.try_again_later)
            tryAgainButton.text = button ?: getString(R.string.refresh)
            imageSetter?.invoke(errorBinding.errorImage)

            tryAgainButton.visibility = if (onClickListener != null) View.VISIBLE else View.GONE
            onClickListener?.let { l -> tryAgainButton.setOnClickListener { l.invoke(it) } }

            errorContainer.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _errorBinding = null
    }
}