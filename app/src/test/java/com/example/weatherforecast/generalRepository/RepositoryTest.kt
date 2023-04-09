package com.example.weatherforecast.generalRepository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myweatherapp.getOrAwaitValue
import com.example.weatherforecast.Forecast
import com.example.weatherforecast.MainDispatchersRule
import com.example.weatherforecast.dataBase.LocalSource
import com.example.weatherforecast.network.RemoteSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RepositoryTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()
    lateinit var localSource:LocalSource
    lateinit var remoteSource:RemoteSource
    lateinit var repo:RepositoryInterface
    lateinit var WeatherData:Forecast



    fun generateDummyData(){
        WeatherData=Forecast()
    }
    @Before
    fun setUp(){
        generateDummyData()
        remoteSource=FakeRemoteSource()
        localSource=FakeLocalSource()
        repo=Repository.getInstance(remoteSource,localSource)

    }

    @Test
    fun `getCurrentWeather inputLatLonUnitsLang currentWeather`()= runBlockingTest{
        //Given
        val expectedResultWeather=WeatherData
        //When
       val flowData= repo.getCurrentWeather(10.00,10.00,"en","metric").getOrAwaitValue {  }
       /*flowData.collect{
           assertEquals(expectedResultWeather,it)
       }*/

        //Then compare the recieved weather with the expected one
        assertEquals(expectedResultWeather,flowData)

    }

    @Test
    fun `addCurrentLocationToDataBase addMycurrentLocation RetrieveTheSameLocation`()= runBlockingTest {
            //Given create location to add
        val addedLocation=WeatherData
        //When call repo to add location to backup in fake database
        repo.insertMyCurrentLocation(addedLocation)
        var myStoredLocation=repo.getMyBackupLocation().getOrAwaitValue {  }
        assertThat(myStoredLocation,not(nullValue()))

        }

}