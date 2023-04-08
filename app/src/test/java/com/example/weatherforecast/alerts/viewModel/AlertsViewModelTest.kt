package com.example.weatherforecast.alerts.viewModel

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.runner.RunWith
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myweatherapp.getOrAwaitValue
import com.example.weatherforecast.MainDispatchersRule
import com.example.weatherforecast.MyLocations
import com.example.weatherforecast.MyUserAlert
import com.example.weatherforecast.favorites.viewModel.FakeRepository
import com.example.weatherforecast.favorites.viewModel.FavoritesViewModel
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertThat

@RunWith(AndroidJUnit4::class)
class AlertsViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()


    // repo -> view model - > creat fake repository
    lateinit var viewModel : AlertsViewModel
    lateinit var repo : FakeRepository
    lateinit var firstAlert:MyUserAlert
    lateinit var secondAlert:MyUserAlert

    fun generateDummyData(){
        firstAlert= MyUserAlert(20L,20L,20L,20L,"alarm","storm",10)
        secondAlert= MyUserAlert(50L,50L,50L,50L,"notification","wind",50)

    }

    @Before
    fun setUp(){
        generateDummyData()
        repo = FakeRepository()
        viewModel = AlertsViewModel(repo)

    }

    @Test
    fun `getAllAlerts noInputs returnAllMyFavoritesAlerts` ()= runBlockingTest{
        //Given insert two Alerts into my fake database repo
        repo.insertAlert(firstAlert)
        repo.insertAlert(secondAlert)

        //When call viewModel method to get all my favorites Alerts list
        viewModel.getAllAlerts()
        var result=viewModel.finalAlerts.getOrAwaitValue {  }

        //Then compare the return locations with my test locations
        assertThat(result,not(nullValue()))
    }


    @Test
    fun `addAlert oneFavoriteAlert returnMyFavoritesAlertListOfOneAlertAdded` ()= runBlockingTest{
        //Given insert one alert into my fake database repo
        viewModel.addAlert(firstAlert)

        //When call viewModel method to get all my favorites locations list
        var result=viewModel.finalAlerts.getOrAwaitValue {  }

        //Then compare the return locations with my test locations
        assertThat(result,not(nullValue()))
    }

}