package dev.senk0n.dogbreeds.data.favorites.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.senk0n.dogbreeds.shared.core.Breed
import dev.senk0n.dogbreeds.shared.core.BreedPhoto

@Entity(
    tableName = "favorites",
    indices = [
        Index("breed", "sub_breed")
    ],
)
data class FavoritesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "photo_url", collate = ColumnInfo.NOCASE) val photoUrl: String,
    @ColumnInfo(name = "breed") val breed: String,
    @ColumnInfo(name = "sub_breed") val subBreed: String?,
) {
    fun toBreedPhoto(): BreedPhoto =
        BreedPhoto(
            breed = Breed(name = breed, subBreed = subBreed),
            photoUrl = photoUrl,
        )

    companion object {
        fun fromBreedPhoto(breedPhoto: BreedPhoto): FavoritesEntity =
            FavoritesEntity(
                id = 0,
                photoUrl = breedPhoto.photoUrl,
                breed = breedPhoto.breed.name,
                subBreed = breedPhoto.breed.subBreed,
            )
    }
}