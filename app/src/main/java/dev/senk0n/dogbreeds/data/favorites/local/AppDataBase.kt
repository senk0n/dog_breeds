package dev.senk0n.dogbreeds.data.favorites.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.senk0n.dogbreeds.data.favorites.local.entities.FavoritesEntity

@Database(
    version = 1,
    entities = [
        FavoritesEntity::class,
    ]
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getFavoritesDao(): FavoritesSource

}