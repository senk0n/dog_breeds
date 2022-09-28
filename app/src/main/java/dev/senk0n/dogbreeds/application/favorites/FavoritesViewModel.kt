package dev.senk0n.dogbreeds.application.favorites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.senk0n.dogbreeds.application.shared.StateViewModel
import dev.senk0n.dogbreeds.domain.edit_favorites.shared.EditFavoritesUseCase
import dev.senk0n.dogbreeds.domain.favorites.shared.FavoritesUseCase
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesUseCase: FavoritesUseCase,
    private val editFavoritesUseCase: EditFavoritesUseCase,
    private val savedStateHandle: SavedStateHandle,
) : StateViewModel<List<BreedListItem>>() {

    private val _breedsOfFavorites = MutableStateFlow<List<Breed>>(emptyList())
    val breedsOfFavorites = _breedsOfFavorites.asStateFlow()

    private var stateBreed: Breed? = null

    init {
        val breed: String? = savedStateHandle["breed"]
        val subBreed: String? = savedStateHandle["subBreed"]
        if (breed != null) {
            setBreed(Breed(breed, subBreed))
        } else refresh()
    }

    fun setBreed(breed: Breed?) {
        stateBreed = breed
        refresh()
        savedStateHandle["breed"] = stateBreed?.name
        savedStateHandle["subBreed"] = stateBreed?.subBreed
    }

    override fun refresh(): Job {
        loadListOfBreeds()
        return super.refresh()
    }

    fun deleteFavorite(breedPhoto: BreedPhoto) = viewModelScope.launch {
        editFavoritesUseCase.removeFavorite(breedPhoto)
        refresh()
    }

    private fun loadListOfBreeds() = viewModelScope.launch {
        _breedsOfFavorites.value = favoritesUseCase.getBreedsOfFavorites()
    }

    override suspend fun MutableStateFlow<ResultState<List<BreedListItem>>>.load() {
        val breed = stateBreed

        val list = if (breed == null) {
            favoritesUseCase.getFavorites()
        } else {
            favoritesUseCase.getFavoritesByBreed(breed)
        }

        if (list.isEmpty()) value = Empty
        value = Success(list.map { BreedListItem(it, true) })
    }

}
