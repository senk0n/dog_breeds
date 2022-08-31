package dev.senk0n.dogbreeds.application.breed_photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.senk0n.dogbreeds.application.MutableLiveResult
import dev.senk0n.dogbreeds.application.MutableLiveSnack
import dev.senk0n.dogbreeds.application.Snack
import dev.senk0n.dogbreeds.application.public
import dev.senk0n.dogbreeds.domain.breed_photos.shared.BreedPhotosUseCase
import dev.senk0n.dogbreeds.domain.edit_favorites.shared.EditFavoritesUseCase
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedPhotoViewModel @Inject constructor(
    private val breedPhotosUseCase: BreedPhotosUseCase,
    private val editFavoritesUseCase: EditFavoritesUseCase,
) : ViewModel() {

    private val _breedPhotos = MutableLiveResult<List<BreedListItem>>()
    val breedPhotos = _breedPhotos.public()
    private val _snack = MutableLiveSnack()
    val snack = _snack.public()

    private var _stateBreed: Breed? = null
    private val stateBreed: Breed get() = _stateBreed!!

    fun setBreed(breed: Breed) {
        _stateBreed = breed
        loadPhotos(stateBreed)
    }

    fun refresh() {
        if (_stateBreed != null) {
            loadPhotos(stateBreed)
        } else _breedPhotos.value = Empty
    }

    fun toggleBreedFavorite(photoUrl: String) {

    }

    private fun loadPhotos(breed: Breed) {

        viewModelScope.launch {
            if (breed.name.isBlank()) _breedPhotos.value = Empty
            _breedPhotos.value = Pending

            val b1 = BreedListItem(
                BreedPhoto(
                    Breed("hound", "afghan"),
                    "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg"
                ), true
            )
            val b12 = BreedListItem(
                BreedPhoto(
                    Breed("hound", "afghan"),
                    "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg"
                ), false
            )
            val b2 = BreedListItem(
                BreedPhoto(
                    Breed("bulldog", "boston"),
                    "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11427.jpg"
                ), false
            )
            val b22 = BreedListItem(
                BreedPhoto(
                    Breed("bulldog", "boston"),
                    "https://images.dog.ceo/breeds/bulldog-boston/n02096585_11427.jpg"
                ), true
            )

            delay(500)
            _breedPhotos.value = Success(listOf(b1, b2))

            delay(250)
            _snack.value = Event(Snack("No Internet connection!", "GO", {}))

            delay(1500)
            _breedPhotos.value = Success(listOf(b1, b22, b2, b12))

//            delay(2500)
//            _breedPhotos.value = Error(Throwable("No Internet connection!"))
//
//            delay(1500)
//            _breedPhotos.value = Empty
        }
    }
}
