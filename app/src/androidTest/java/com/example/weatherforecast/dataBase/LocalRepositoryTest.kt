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
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
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
    fun insertMyCurrentLocation_insertOneLocation_getListContainsThisLocations() = runBlockingTest {
        // Given
        localDataSource.insertMyCurrentLocation(firstWeather)


        // When
        val result = localDataSource.getMyBackupLocation().getOrAwaitValue {  }

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
       Assertions.assertThat(result).contains(firstWeather)
    }

    @Test
    fun getMyBackupLocation_insertThreeBackupLocations_getListContainsAllLocations() = runBlockingTest {
        // Given
        localDataSource.insertMyCurrentLocation(firstWeather)
        localDataSource.insertMyCurrentLocation(secondWeather)
        localDataSource.insertMyCurrentLocation(thirdWeather)


        // When
        val result = localDataSource.getMyBackupLocation().getOrAwaitValue {  }

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))

    }

    @Test
    fun deleteMyCurrentLocation_deleteOneBackupLocations_getListDontContainsDeleteLocation() = runBlockingTest {
        // Given
        localDataSource.insertMyCurrentLocation(firstWeather)
        localDataSource.insertMyCurrentLocation(secondWeather)
        localDataSource.insertMyCurrentLocation(thirdWeather)
        localDataSource.deleteMyCurrentLocation(secondWeather)
        // When
        val result = localDataSource.getMyBackupLocation().getOrAwaitValue {  }

        // Then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
        Assertions.assertThat(result).doesNotContain(secondWeather)

    }







}