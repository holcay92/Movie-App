package com.example.movieapp.room
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FavoriteMovie::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}
