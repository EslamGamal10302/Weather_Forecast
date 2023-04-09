package com.example.weatherforecast.dataBase

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.myweatherapp.getOrAwaitValue
import com.example.weatherforecast.Forecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalRepositoryTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: LocalRepository
    private lateinit var database: WeatherDataBase
    lateinit var firstWeather : Forecast
    lateinit var secondWeather : Forecast
    lateinit var thirdWeather : Forecast



    fun createDummyData(){
        firstWeather= Forecast()
        secondWeather=Forecast()
        thirdWeather=Forecast()

    }

    @Before
    fun setup() {
        createDummyData()
        var app: Application = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),WeatherDataBase::class.java)
            .allowMainThreadQueries()
            .build()

        localDataSource = LocalRepository.getInstance(app)
    }
    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun insertMyCurrentLocation_insertThreeLocation_getListOfthreeLocations() = runBlocking {
        // Given
        localDataSource.insertMyCurrentLocation(firstWeather)


        // When
        val result = localDataSource.getMyBackupLocation().getOrAwaitValue {  }

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
    }

}