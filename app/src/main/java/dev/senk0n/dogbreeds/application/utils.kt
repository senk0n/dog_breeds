package dev.senk0n.dogbreeds.application

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import dev.senk0n.dogbreeds.R
import dev.senk0n.dogbreeds.shared.core.Event
import dev.senk0n.dogbreeds.shared.core.ResultState

// Result
typealias LiveResult<T> = LiveData<ResultState<T>>
typealias MutableLiveResult<T> = MutableLiveData<ResultState<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<ResultState<T>>

// Event
typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

fun <T> MutableLiveData<T>.public(): LiveData<T> = this

fun <T> LiveEvent<T>.observe(owner: LifecycleOwner, observer: T.() -> Unit) {
    this.observe(owner) { event ->
        event?.valueOrNull?.let { value ->
            value.observer()
        }
    }
}

// SnackBar
class Snack(val message: String, val actionMsg: String? = null, val action: (() -> Unit)? = null)
typealias LiveSnack = LiveEvent<Snack>

typealias MutableLiveSnack = MutableLiveEvent<Snack>

fun Fragment.showSnack(snack: Event<Snack>) {
    snack.valueOrNull?.let { snackBar ->
        Snackbar.make(requireView(), snackBar.message, Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.bottom_navigation)
            .setAction(snackBar.actionMsg) { snackBar.action }
            .show()
    }
}
