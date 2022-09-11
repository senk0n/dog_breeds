package dev.senk0n.dogbreeds.application.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.senk0n.dogbreeds.application.shared.LiveResult
import dev.senk0n.dogbreeds.application.shared.LiveSnack
import dev.senk0n.dogbreeds.application.shared.MutableLiveResult
import dev.senk0n.dogbreeds.application.shared.MutableLiveSnack
import dev.senk0n.dogbreeds.domain.edit_favorites.shared.EditFavoritesUseCase
import dev.senk0n.dogbreeds.domain.favorites.shared.FavoritesUseCase
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesUseCase: FavoritesUseCase,
    private val editFavoritesUseCase: EditFavoritesUseCase,
) : ViewModel() {

    private val _favorites = MutableLiveResult<List<BreedListItem>>()
    val favorites: LiveResult<List<BreedListItem>> = _favorites
    private val _breedsOfFavorites = MutableLiveData<List<Breed>>()
    val breedsOfFavorites: LiveData<List<Breed>> = _breedsOfFavorites
    private val _snack = MutableLiveSnack()
    val snack: LiveSnack = _snack

    private var stateBreed: Breed? = null

    init {
        refresh()
    }

    fun setBreed(breed: Breed?) {
        stateBreed = breed
        refresh()
    }

    fun refresh() {
        loadFavorites(stateBreed)
        loadListOfBreeds()
    }

    fun deleteFavorite(breedPhoto: BreedPhoto) = viewModelScope.launch {
        editFavoritesUseCase.removeFavorite(breedPhoto)
        refresh()
    }

    private fun loadListOfBreeds() = viewModelScope.launch {
        _breedsOfFavorites.value = favoritesUseCase.getBreedsOfFavorites()
    }

    private fun loadFavorites(breed: Breed?) =
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _favorites.value = Error(throwable)
        }) {
            _favorites.value = Pending

            val list = if (breed == null) {
                favoritesUseCase.getFavorites()
            } else {
                favoritesUseCase.getFavoritesByBreed(breed)
            }

            if (list.isEmpty()) _favorites.value = Empty
            _favorites.value = Success(list.map { BreedListItem(it, true) })
        }

}
