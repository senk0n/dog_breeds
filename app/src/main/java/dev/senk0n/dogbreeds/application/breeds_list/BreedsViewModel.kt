package dev.senk0n.dogbreeds.application.breeds_list

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.senk0n.dogbreeds.application.shared.StateViewModel
import dev.senk0n.dogbreeds.domain.breed_photos.shared.BreedPhotosUseCase
import dev.senk0n.dogbreeds.domain.breeds.shared.BreedsUseCase
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class BreedsViewModel @Inject constructor(
    private val breedsUseCase: BreedsUseCase,
    private val breedPhotosUseCase: BreedPhotosUseCase,
) : StateViewModel<List<BreedPhoto>>() {

    init {
        refresh()
    }

    override fun refresh(): Job {
        mutableState.value = Pending
        return super.refresh()
    }

    override suspend fun MutableStateFlow<ResultState<List<BreedPhoto>>>.load() {
        val breeds = breedsUseCase.getBreeds()
        if (breeds.isEmpty()) {
            value = Empty
            return
        }

        value = Success(breeds.map { BreedPhoto(it, "") })
        breedPhotosUseCase.loadPhotos(breeds).collect { photo ->
            val oldList = value.valueOrNull ?: emptyList()
            val newList = oldList.map { if (it.breed == photo.breed) photo else it }
            value = Success(newList)
        }

    }

}
