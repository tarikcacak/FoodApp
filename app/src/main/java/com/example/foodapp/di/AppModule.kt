package com.example.foodapp.di

import android.content.Context
import androidx.room.Room
import com.example.foodapp.data.local.AppDatabase
import com.example.foodapp.data.local.daos.ExerciseDao
import com.example.foodapp.data.local.daos.FavoriteDao
import com.example.foodapp.data.local.daos.HistoryDao
import com.example.foodapp.data.local.daos.TodayMealDao
import com.example.foodapp.data.remote.ApiService
import com.example.foodapp.ui.add.AddTodayMealRepository
import com.example.foodapp.ui.add.AddTodayMealRepositoryImpl
import com.example.foodapp.ui.details.FavoriteSaveRepository
import com.example.foodapp.ui.details.FavoriteSaveRepositoryImpl
import com.example.foodapp.ui.exercise.AddExerciseRepository
import com.example.foodapp.ui.exercise.AddExerciseRepositoryImpl
import com.example.foodapp.ui.favorites.FavoritesRepository
import com.example.foodapp.ui.favorites.FavoritesRepositoryImpl
import com.example.foodapp.ui.home.ExerciseRepository
import com.example.foodapp.ui.home.ExerciseRepositoryImpl
import com.example.foodapp.ui.home.HistoryRepository
import com.example.foodapp.ui.home.HistoryRepositoryImpl
import com.example.foodapp.ui.home.MealRepository
import com.example.foodapp.ui.home.MealRepositoryImpl
import com.example.foodapp.ui.history.HistoryDataRepository
import com.example.foodapp.ui.history.HistoryDataRepositoryImpl
import com.example.foodapp.ui.today.TodayExerciseRepository
import com.example.foodapp.ui.today.TodayExerciseRepositoryImpl
import com.example.foodapp.ui.today.TodayRepository
import com.example.foodapp.ui.today.TodayRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseFirestore() = Firebase.firestore

    @Singleton
    @Provides
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun getRetrofitServiceInstance(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun getRetrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "meal.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideAddTodayMealRepository(todayMealDao: TodayMealDao): AddTodayMealRepository {
        return AddTodayMealRepositoryImpl(todayMealDao)
    }

    @Provides
    @Singleton
    fun provideTodayRepository(todayMealDao: TodayMealDao): TodayRepository {
        return TodayRepositoryImpl(todayMealDao)
    }

    @Provides
    @Singleton
    fun provideAddExerciseRepository(exerciseDao: ExerciseDao): AddExerciseRepository {
        return AddExerciseRepositoryImpl(exerciseDao)
    }

    @Provides
    @Singleton
    fun provideTodayExerciseRepository(exerciseDao: ExerciseDao): TodayExerciseRepository {
        return TodayExerciseRepositoryImpl(exerciseDao)
    }

    @Provides
    @Singleton
    fun provideMealRepository(todayMealDao: TodayMealDao): MealRepository {
        return MealRepositoryImpl(todayMealDao)
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDao: ExerciseDao): ExerciseRepository {
        return ExerciseRepositoryImpl(exerciseDao)
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(historyDao: HistoryDao): HistoryRepository {
        return HistoryRepositoryImpl(historyDao)
    }

    @Provides
    @Singleton
    fun provideHistoryDataRepository(historyDao: HistoryDao): HistoryDataRepository {
        return HistoryDataRepositoryImpl(historyDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteSaveRepository(favoriteDao: FavoriteDao): FavoriteSaveRepository {
        return FavoriteSaveRepositoryImpl(favoriteDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDao: FavoriteDao): FavoritesRepository {
        return FavoritesRepositoryImpl(favoriteDao)
    }

    @Provides
    @Singleton
    fun provideTodayMealDao(appDatabase: AppDatabase): TodayMealDao = appDatabase.todayMealDao()

    @Provides
    @Singleton
    fun provideExerciseDao(appDatabase: AppDatabase): ExerciseDao = appDatabase.exerciseDao()

    @Provides
    @Singleton
    fun provideHistoryDao(appDatabase: AppDatabase): HistoryDao = appDatabase.historyDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(appDatabase: AppDatabase): FavoriteDao = appDatabase.favoriteDao()

}