package dev.senk0n.dogbreeds.data.favorites.local.entities

import androidx.room.ColumnInfo

data class FavoriteUrlTuple(@ColumnInfo(name = "photo_url") val photoUrl: String)

data class BreedTuple(
    @ColumnInfo(name = "breed") val breed: String,
    @ColumnInfo(name = "sub_breed") val subBreed: String?,
)