package dev.senk0n.dogbreeds.application.breed_photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.senk0n.dogbreeds.application.MutableLiveResult
import dev.senk0n.dogbreeds.application.MutableLiveSnack
import dev.senk0n.dogbreeds.application.public
import dev.senk0n.dogbreeds.domain.breed_photos.shared.BreedPhotosUseCase
import dev.senk0n.dogbreeds.domain.edit_favorites.shared.EditFavoritesUseCase
import dev.senk0n.dogbreeds.domain.favorites.shared.FavoritesUseCase
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedPhotoViewModel @Inject constructor(
    private val breedPhotosUseCase: BreedPhotosUseCase,
    private val favoritesUseCase: FavoritesUseCase,
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

    fun toggleBreedFavorite(breedPhoto: BreedPhoto) = viewModelScope.launch {
        editFavoritesUseCase.toggleFavorite(breedPhoto)
        refresh()
    }

    private fun loadPhotos(breed: Breed) =
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _breedPhotos.value = Error(throwable)
        }) {
            _breedPhotos.value = Pending
            if (breed.name.isBlank()) _breedPhotos.value = Empty

            val list = breedPhotosUseCase.loadPhotos(breed)
            val favorites = favoritesUseCase.getFavoritesByBreed(breed)

            if (list.isEmpty()) _breedPhotos.value = Empty
            _breedPhotos.value = Success(list.map {
                BreedListItem(it, favorites.contains(it))
            })
        }

}
