package com.example.weatherforecast.dataBase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.myweatherapp.getOrAwaitValue
import com.example.weatherforecast.MyLocations
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
class AlertsDaoTest {
    @get:Rule
    val instance = InstantTaskExecutorRule()
    private lateinit var database :WeatherDataBase
    lateinit var firstAlert:MyUserAlert
    lateinit var secondAlert:MyUserAlert
    lateinit var thirdAlert:MyUserAlert

    fun generateDummyData(){
         firstAlert=MyUserAlert()
         secondAlert=MyUserAlert(10L,20L,30L,50L,"notificatio","storm",10)
         thirdAlert=MyUserAlert(10L,50L,30L,100L,"alarm","storm",20)
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
    fun getAllAlerts_createedThreeAlerts_getListOfThreeAlerts() = runBlocking{
        // Given
        database.getAlertDao().insert(firstAlert)
        database.getAlertDao().insert(secondAlert)
        database.getAlertDao().insert(thirdAlert)

        // When
        val loaded = database.getAlertDao().getAllAlerts().getOrAwaitValue {  }

        // Then
        // MatcherAssert.assertThat(loaded, Matchers.notNullValue())
        MatcherAssert.assertThat(loaded, Matchers.`is`(listOf(firstAlert,secondAlert,thirdAlert)))


    }

    @Test
    fun insert_InsertAlert_getListHaveOnlyOneAlert() = runBlocking{
        // Given
        database.getAlertDao().insert(firstAlert)

        // When
        val loaded = database.getAlertDao().getAllAlerts().getOrAwaitValue {  }

        // Then
        // MatcherAssert.assertThat(loaded, Matchers.notNullValue())
        MatcherAssert.assertThat(loaded, Matchers.`is`(listOf(firstAlert)))


    }

    @Test
    fun delete_deleteOneAlert_getListOfAllAlertsWithoutDeleted() = runBlocking{
        // Given
        database.getAlertDao().insert(firstAlert)
        database.getAlertDao().insert(secondAlert)
        database.getAlertDao().insert(thirdAlert)

        database.getAlertDao().delete(firstAlert)

        // When
        val loaded = database.getAlertDao().getAllAlerts().getOrAwaitValue {  }

        // Then
        // MatcherAssert.assertThat(loaded, Matchers.notNullValue())
        MatcherAssert.assertThat(loaded, Matchers.`is`(listOf(secondAlert, thirdAlert)))


    }






}