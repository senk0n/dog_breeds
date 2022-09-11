package dev.senk0n.dogbreeds.application.breeds_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.senk0n.dogbreeds.application.*
import dev.senk0n.dogbreeds.application.shared.LiveResult
import dev.senk0n.dogbreeds.application.shared.LiveSnack
import dev.senk0n.dogbreeds.application.shared.MutableLiveResult
import dev.senk0n.dogbreeds.application.shared.MutableLiveSnack
import dev.senk0n.dogbreeds.domain.breed_photos.shared.BreedPhotosUseCase
import dev.senk0n.dogbreeds.domain.breeds.shared.BreedsUseCase
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedsViewModel @Inject constructor(
    private val breedsUseCase: BreedsUseCase,
    private val breedPhotosUseCase: BreedPhotosUseCase,
) : ViewModel() {
    private val _breeds = MutableLiveResult<List<BreedPhoto>>(Pending)
    val breeds: LiveResult<List<BreedPhoto>> = _breeds
    private val _snack = MutableLiveSnack()
    val snack: LiveSnack = _snack

    init {
        loadBreeds()
    }

    fun loadBreeds() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _breeds.value = Error(throwable)
        }) {
            _breeds.value = Pending

            val breeds = breedsUseCase.getBreeds()
            if (breeds.isEmpty()) {
                _breeds.value = Empty
            } else {
                _breeds.value = Success(breeds.map { BreedPhoto(it, "") })
                _breeds.value = Success(breedPhotosUseCase.loadPhotos(breeds))
            }
        }
    }

}
