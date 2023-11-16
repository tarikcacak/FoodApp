package com.example.foodapp.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.foodapp.data.local.entity.Exercise

@Dao
interface ExerciseDao {

    @Upsert
    suspend fun addExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises")
    fun getExercises(): LiveData<List<Exercise>>

    @Query("DELETE FROM exercises")
    suspend fun deleteAllExercises()

}