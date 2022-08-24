package dev.senk0n.dogbreeds.application.breeds_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.senk0n.dogbreeds.application.MutableLiveResult
import dev.senk0n.dogbreeds.application.MutableLiveSnack
import dev.senk0n.dogbreeds.application.Snack
import dev.senk0n.dogbreeds.application.public
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BreedsViewModel : ViewModel() {
    private val _breeds = MutableLiveResult<List<Breed>>()
    val breeds = _breeds.public()
    private val _snack = MutableLiveSnack()
    val snack = _snack.public()

    init {
        loadBreeds()
    }

    fun loadBreeds() {
        viewModelScope.launch {
            _breeds.value = Pending

            delay(1500)
            _breeds.value = Success(
                listOf(
                    Breed("awdawd", "gffff"),
                    Breed("sagdhf", "mfh"),
                    Breed("xvvfvdfgd", "mnbvcbcv"),
                )
            )
//            delay(1500)
//            _snack.value = Event(Snack("No Internet connection!", "GO", {}))

            delay(1500)
            _breeds.value = Success(
                listOf(
                    Breed("awdawd", "gffff"),
                    Breed("xvvfvdfgd", "mnbvcbcv"),
                )
            )

            delay(1000)
            _breeds.value = Empty

            delay(1500)
            _breeds.value = Error(Throwable("No Internet connection!"))
        }
    }
}
