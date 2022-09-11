package dev.senk0n.dogbreeds.application.breed_photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.senk0n.dogbreeds.application.shared.LiveResult
import dev.senk0n.dogbreeds.application.shared.LiveSnack
import dev.senk0n.dogbreeds.application.shared.MutableLiveResult
import dev.senk0n.dogbreeds.application.shared.MutableLiveSnack
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
    val breedPhotos: LiveResult<List<BreedListItem>> = _breedPhotos
    private val _snack = MutableLiveSnack()
    val snack: LiveSnack = _snack

    private var _stateBreed: Breed? = null
    private val stateBreed: Breed get() = _stateBreed!!

    fun setBreed(breed: Breed) {
        _stateBreed = breed
        load()
    }

    fun refresh() {
        _breedPhotos.value = Pending
        load()
    }

    fun toggleBreedFavorite(breedPhoto: BreedPhoto) = viewModelScope.launch {
        editFavoritesUseCase.toggleFavorite(breedPhoto)
        load()
    }

    private fun load() =
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _breedPhotos.value = Error(throwable)
        }) {
            if (_stateBreed == null || stateBreed.name.isBlank()) {
                _breedPhotos.value = Empty
                return@launch
            }

            val list = breedPhotosUseCase.loadPhotos(stateBreed)
            val favorites = favoritesUseCase.getFavoritesByBreed(stateBreed)

            if (list.isEmpty()) _breedPhotos.value = Empty
            _breedPhotos.value = Success(list.map {
                BreedListItem(it, favorites.contains(it))
            })
        }

}
