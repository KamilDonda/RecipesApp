package com.example.recipesapp

import com.example.recipesapp.model.utils.RatingSystem
import org.junit.Assert
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class RatingSystemTest {
    private val ratingSystem = RatingSystem()

    @TestFactory
    fun `check calculation is right`() =
        listOf(
            //Filled, Half, Empty
            arrayOf(5, 0, 0) to 5f,
            arrayOf(4, 0, 1) to 4f,
            arrayOf(3, 0, 2) to 3f,
            arrayOf(2, 0, 3) to 2f,
            arrayOf(1, 0, 4) to 1f,
            arrayOf(0, 0, 5) to 0f,
            arrayOf(5, 0, 0) to 4.9f,
            arrayOf(5, 0, 0) to 4.76f,
            arrayOf(5, 0, 0) to 4.75f,
            arrayOf(4, 1, 0) to 4.74f,
            arrayOf(4, 1, 0) to 4.5f,
            arrayOf(4, 1, 0) to 4.26f,
            arrayOf(4, 1, 0) to 4.25f,
            arrayOf(4, 0, 1) to 4.24f,
            arrayOf(3, 0, 2) to 3.2f,
            arrayOf(2, 1, 2) to 2.5f,
            arrayOf(2, 0, 3) to 1.75f,
            arrayOf(0, 1, 4) to 0.74f,
            arrayOf(0, 1, 4) to 0.25f,
            arrayOf(0, 0, 5) to 0.24f
        ).map {
            dynamicTest("Arg: ${it.second}") {
                Assert.assertArrayEquals(it.first, ratingSystem.calculateStars(it.second))
            }
        }
}
