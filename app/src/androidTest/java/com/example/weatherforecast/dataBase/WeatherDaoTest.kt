package com.example.weatherforecast.dataBase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.myweatherapp.getOrAwaitValue
import com.example.weatherforecast.MyLocations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {
    @get:Rule
    val instance = InstantTaskExecutorRule()
    private lateinit var database :WeatherDataBase
    @Before
    fun createDataBase(){
        database=
            Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),WeatherDataBase::class.java).build()
    }
    @After
    fun closeDataBase(){
        database.close()
    }

    @Test
    fun getAllFavorites_createedThreeFavoriteLocations_getListOfThreeLocations() = runBlocking{
        // Given
        val favLocationOne = MyLocations(20.00,30.00)
        val favLocationTwo = MyLocations(30.00,30.00)
        val favLocationThree= MyLocations(50.00,30.00)
        database.getWeatherDao().insert(favLocationOne)
        database.getWeatherDao().insert(favLocationTwo)
        database.getWeatherDao().insert(favLocationThree)

        // When
        val loaded = database.getWeatherDao().getAllFavorites().getOrAwaitValue {  }

        // Then
       // MatcherAssert.assertThat(loaded, Matchers.notNullValue())
        MatcherAssert.assertThat(loaded, `is`(listOf(favLocationOne,favLocationTwo,favLocationThree)))


    }

    @Test
    fun insert_InsertOneFavoriteLocation_getListOfOneLocations() = runBlocking{
        // Given
        val favLocation= MyLocations(20.00,30.00)
        database.getWeatherDao().insert(favLocation)

        // When
        val loaded = database.getWeatherDao().getAllFavorites().getOrAwaitValue {  }

        // Then
        // MatcherAssert.assertThat(loaded, Matchers.notNullValue())
        MatcherAssert.assertThat(loaded, `is`(listOf(favLocation)))


    }

    @Test
    fun delete_deleteOneFavoriteLocation_getListOfTheRestOfLocations() = runBlocking{
        // Given
        val favLocationOne = MyLocations(20.00,30.00)
        val favLocationTwo = MyLocations(30.00,30.00)
        val favLocationThree= MyLocations(50.00,30.00)
        database.getWeatherDao().insert(favLocationOne)
        database.getWeatherDao().insert(favLocationTwo)
        database.getWeatherDao().insert(favLocationThree)
        database.getWeatherDao().delete(favLocationThree)

        // When
        val loaded = database.getWeatherDao().getAllFavorites().getOrAwaitValue {  }

        // Then
        // MatcherAssert.assertThat(loaded, Matchers.notNullValue())
        MatcherAssert.assertThat(loaded, `is`(listOf(favLocationOne,favLocationTwo)))


    }






}