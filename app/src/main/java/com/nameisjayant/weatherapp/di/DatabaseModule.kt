package com.nameisjayant.weatherapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nameisjayant.weatherapp.data.local.CityDao
import com.nameisjayant.weatherapp.data.local.CityDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesCityDatabase(application: Application): CityDatabase =
        Room.databaseBuilder(application, CityDatabase::class.java, "city_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesCityDao(db:CityDatabase):CityDao = db.cityDao


}