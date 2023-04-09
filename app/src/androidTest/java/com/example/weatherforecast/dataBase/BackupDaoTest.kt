package com.example.weatherforecast.dataBase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.myweatherapp.getOrAwaitValue
import com.example.weatherforecast.Current
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MyUserAlert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class BackupDaoTest {
    @get:Rule
    val instance = InstantTaskExecutorRule()
    private lateinit var database :WeatherDataBase
    lateinit var firstBackupLocation: Forecast
    lateinit var secondBackupLocation: Forecast
    lateinit var thirdBackupLocation: Forecast
    fun generateDummyData(){
        firstBackupLocation=Forecast()
        secondBackupLocation= Forecast(Current(), listOf(), listOf(),20.00,30.00, listOf(),"egypt",20,
            listOf()
        )
        thirdBackupLocation= Forecast(Current(), listOf(), listOf(),100.00,300.00, listOf(),"america",20,
            listOf()
        )
    }
    @Before
    fun createDataBase(){
        generateDummyData()
        database=
            Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),WeatherDataBase::class.java).build()
    }
    @After
    fun closeDataBase(){
        database.close()
    }

    @Test
    fun getBackupForMyLocation_createedThreeBackupLocations_getListOfThreeLocations() = runBlocking{
        // Given
        database.getBackupDao().insert(firstBackupLocation)
        database.getBackupDao().insert(secondBackupLocation)
        database.getBackupDao().insert(thirdBackupLocation)

        // When
        val loaded = database.getBackupDao().getBackupForMyLocation().getOrAwaitValue {  }

        // Then
        MatcherAssert.assertThat(loaded, Matchers.`is`(listOf(firstBackupLocation,secondBackupLocation,thirdBackupLocation)))


    }

    @Test
    fun insert_InsertBackupLocation_getListHaveOnlyOneLocation() = runBlocking{
        // Given
        database.getBackupDao().insert(firstBackupLocation)

        // When
        val loaded = database.getBackupDao().getBackupForMyLocation().getOrAwaitValue {  }

        // Then
        MatcherAssert.assertThat(loaded, Matchers.`is`(listOf(firstBackupLocation)))


    }
    @Test
    fun delete_deleteOneLocationBackup_getListOfAllLocationsWithoutDeleted() = runBlocking{
        // Given
        database.getBackupDao().insert(firstBackupLocation)
        database.getBackupDao().insert(secondBackupLocation)
        database.getBackupDao().insert(thirdBackupLocation)

        database.getBackupDao().delete(firstBackupLocation)

        // When
        val loaded = database.getBackupDao().getBackupForMyLocation().getOrAwaitValue {  }

        // Then
        MatcherAssert.assertThat(loaded, Matchers.`is`(listOf(secondBackupLocation, thirdBackupLocation)))


    }






}