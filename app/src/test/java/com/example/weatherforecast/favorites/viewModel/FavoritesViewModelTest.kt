package com.example.weatherforecast.favorites.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myweatherapp.getOrAwaitValue
import com.example.weatherforecast.MainDispatchersRule
import com.example.weatherforecast.MyLocations
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertThat



@RunWith(AndroidJUnit4::class)
class FavoritesViewModelTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    // repo -> view model - > creat fake repository
    lateinit var viewModel :FavoritesViewModel
    lateinit var repo : FakeRepository
    lateinit var firstLocation:MyLocations
    lateinit var secondLocation:MyLocations

    fun generateDummyData(){
        firstLocation= MyLocations(100.00,100.00)
        secondLocation= MyLocations(200.00,200.00)

    }
    @Before
    fun setUp(){
        generateDummyData()
        repo = FakeRepository()
        viewModel = FavoritesViewModel(repo)

    }

    @Test
    fun `getAllFavoritesLocations noInputs returnAllMyFavoritesLocations` ()= runBlockingTest{
        //Given insert two locations into my fake database repo
        repo.insert(firstLocation)
        repo.insert(secondLocation)

        //When call viewModel method to get all my favorites locations list
        viewModel.getAllFavLocations()
        var result=viewModel.finalWeather.getOrAwaitValue {  }

        //Then compare the return locations with my test locations
        assertThat(result,not(nullValue()))
    }

    @Test
    fun `addToFavorites oneFavoriteLocation returnMyFavoritesLocationListOfOneLocationAdded` ()= runBlockingTest{
        //Given insert one locations into my fake database repo
        viewModel.addToFavorites(firstLocation)

        //When call viewModel method to get all my favorites locations list
        var result=viewModel.finalWeather.getOrAwaitValue {  }

        //Then compare the return locations with my test locations
        assertThat(result,not(nullValue()))
    }


    @Test
    fun `deleteFromFavorites oneFavoriteLocation returnMyFavoritesLocationListAfterRemoveLocation` ()= runBlockingTest{
        //Given insert one locations into my fake database repo
        repo.insert(firstLocation)


        //When call viewModel method to delete my favorite locations
        viewModel.deleteFromFavorites(firstLocation)
        var result=viewModel.finalWeather.getOrAwaitValue {  }

        //Then compare the return locations with my test locations
        assertThat(result,`is`(emptyList()))
    }


}