package dev.senk0n.dogbreeds.application.breed_photos

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.senk0n.dogbreeds.application.shared.StateViewModel
import dev.senk0n.dogbreeds.domain.breed_photos.shared.BreedPhotosUseCase
import dev.senk0n.dogbreeds.domain.edit_favorites.shared.EditFavoritesUseCase
import dev.senk0n.dogbreeds.domain.favorites.shared.FavoritesUseCase
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedPhotoViewModel @Inject constructor(
    private val breedPhotosUseCase: BreedPhotosUseCase,
    private val favoritesUseCase: FavoritesUseCase,
    private val editFavoritesUseCase: EditFavoritesUseCase,
) : StateViewModel<List<BreedListItem>>() {

    private var _stateBreed: Breed? = null
    private val stateBreed: Breed get() = _stateBreed!!

    fun setBreed(breed: Breed) {
        _stateBreed = breed
        refresh()
    }

    fun toggleBreedFavorite(breedPhoto: BreedPhoto) = viewModelScope.launch {
        editFavoritesUseCase.toggleFavorite(breedPhoto)
        refresh()
    }

    override suspend fun MutableStateFlow<ResultState<List<BreedListItem>>>.load() {
        if (_stateBreed == null || stateBreed.name.isBlank()) {
            value = Empty
        }

        val list = breedPhotosUseCase.loadPhotos(stateBreed)
        val favorites = favoritesUseCase.getFavoritesByBreed(stateBreed)

        if (list.isEmpty()) value = Empty
        value = Success(list.map {
            BreedListItem(it, favorites.contains(it))
        })
    }

}
