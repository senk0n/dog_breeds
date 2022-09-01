package dev.senk0n.dogbreeds.application.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.senk0n.dogbreeds.application.MutableLiveResult
import dev.senk0n.dogbreeds.application.MutableLiveSnack
import dev.senk0n.dogbreeds.application.public
import dev.senk0n.dogbreeds.domain.breeds.shared.BreedsUseCase
import dev.senk0n.dogbreeds.domain.edit_favorites.shared.EditFavoritesUseCase
import dev.senk0n.dogbreeds.domain.favorites.shared.FavoritesUseCase
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesUseCase: FavoritesUseCase,
    private val editFavoritesUseCase: EditFavoritesUseCase,
    private val breedsUseCase: BreedsUseCase,
) : ViewModel() {

    private val _favorites = MutableLiveResult<List<BreedListItem>>()
    val favorites = _favorites.public()
    private val _snack = MutableLiveSnack()
    val snack = _snack.public()

    private var stateBreed: Breed? = null

    init {
        loadFavorites(stateBreed)
    }

    fun setBreed(breed: Breed) {
        stateBreed = breed
        loadFavorites(stateBreed)
    }

    fun refresh() {
        loadFavorites(stateBreed)
    }

    fun deleteFavorite(breedPhoto: BreedPhoto) = viewModelScope.launch {
        editFavoritesUseCase.removeFavorite(breedPhoto)
        refresh()
    }

    private fun loadFavorites(breed: Breed?) =
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _favorites.value = Error(throwable)
        }) {
            _favorites.value = Pending

            var list: List<BreedPhoto>
            withContext(Dispatchers.Default) {
                list = if (breed == null) {
                    favoritesUseCase.getFavorites()
                } else {
                    favoritesUseCase.getFavoritesByBreed(breed)
                }
            }

            if (list.isEmpty()) _favorites.value = Empty
            _favorites.value = Success(list.map { BreedListItem(it, true) })
        }

}