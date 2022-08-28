package dev.senk0n.dogbreeds.data.favorites.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.senk0n.dogbreeds.data.favorites.local.entities.FavoriteUrlTuple
import dev.senk0n.dogbreeds.data.favorites.local.entities.FavoritesEntity

@Dao
interface FavoritesDataSource {
    @Query("SELECT * FROM favorites WHERE breed = :breed")
    suspend fun getByBreed(breed: String): List<FavoritesEntity>

    @Query("SELECT * FROM favorites WHERE breed = :breed AND sub_breed = :subBreed")
    suspend fun getBySubBreed(breed: String, subBreed: String): List<FavoritesEntity>

    @Insert
    suspend fun create(favoritesEntity: FavoritesEntity)

    @Delete(entity = FavoritesEntity::class)
    suspend fun removeByUrl(favoriteUrlTuple: FavoriteUrlTuple)
}