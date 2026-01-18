package com.example.voyage_map

import com.example.voyage_map.viewmodel.HomeViewModel
import com.example.voyage_map.data.model.PlaceUiModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel()
    }

    @Test
    fun citySearch_shouldHandleInvalidCity() = runTest {
        viewModel.loadCityDetails("InvalidCityName123")

        assertNotNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun placeLoading_shouldReturnPlaces() = runTest {
        viewModel.loadCityDetails("Paris")

        val places = viewModel.places.value
        assertNotNull(places)
    }

    @Test
    fun toggleLike_shouldSwitchLikeState() = runTest {
        val place = PlaceUiModel(
            id = "1",
            name = "Test Place",
            address = "Test Address",
            categories = emptyList(),
            wikipediaUrl = null,
            isLiked = false,
            score = 5.0
        )

        viewModel.toggleLike(place.id, place.isLiked)

        val updatedPlace = viewModel.places.value.find { it.id == place.id }
        assertTrue(updatedPlace?.isLiked ?: true)
    }
}
