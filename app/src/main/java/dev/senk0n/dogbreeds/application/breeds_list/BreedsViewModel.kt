package dev.senk0n.dogbreeds.application.breeds_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.senk0n.dogbreeds.application.MutableLiveResult
import dev.senk0n.dogbreeds.application.MutableLiveSnack
import dev.senk0n.dogbreeds.application.public
import dev.senk0n.dogbreeds.domain.breeds.shared.BreedsUseCase
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedsViewModel @Inject constructor(
    private val breedsUseCase: BreedsUseCase,
) : ViewModel() {
    private val _breeds = MutableLiveResult<List<BreedPhoto>>(Pending)
    val breeds = _breeds.public()
    private val _snack = MutableLiveSnack()
    val snack = _snack.public()

    init {
        loadBreeds()
    }

    fun loadBreeds() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _breeds.value = Error(throwable)
        }) {
            _breeds.value = Pending

            val list = breedsUseCase.getBreeds()

            if (list.isEmpty()) _breeds.value = Empty
            _breeds.value = Success(list)
        }
    }

}
