package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {
    @Test
    fun dueDateLabel_completedWithDate_returnsClear() {
        // Given
        val dueDate = LocalDate.parse("2026-10-19")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = true

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_completedWithoutDate_returnsClear() {
        // Given
        val dueDate = null
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = true

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_activeWithoutDate_returnsEmpty() {
        // Given
        val dueDate = null
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("", label)
    }

    @Test
    fun dueDateLabel_dueInFiveDays_returnsDMinus5() {
        // Given
        val dueDate = LocalDate.parse("2026-10-19")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-5", label)
    }

    @Test
    fun dueDateLabel_overdueByFiveDays_returnsDPlus5() {
        // Given
        val dueDate = LocalDate.parse("2026-10-09")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+5", label)
    }

    @Test
    fun dueDateLabel_dueOneMonthLater_returnsDMinus31() {
        // Given
        val dueDate = LocalDate.parse("2026-11-14")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-31", label)
    }

    @Test
    fun dueDateLabel_dueOneYearLater_returnsDMinus365() {
        // Given
        val dueDate = LocalDate.parse("2027-10-14")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-365", label)
    }

    @Test
    fun dueDateLabel_dueYesterday_returnsDPlus1() {
        // Given
        val dueDate = LocalDate.parse("2026-10-13")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D+1", label)
    }

    @Test
    fun dueDateLabel_dueToday_returnsDDay() {
        // Given
        val dueDate = LocalDate.parse("2026-10-14")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_dueTomorrow_returnsDMinus1() {
        // Given
        val dueDate = LocalDate.parse("2026-10-15")
        val today = LocalDate.parse("2026-10-14")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_dueOnMonthEnd_returnsDDay() {
        // Given
        val dueDate = LocalDate.parse("2026-10-31")
        val today = LocalDate.parse("2026-10-31")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_dueOneDayAfterMonthEnd_returnsDMinus1() {
        // Given
        val dueDate = LocalDate.parse("2026-11-01")
        val today = LocalDate.parse("2026-10-31")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_dueTwoDaysAfterMonthEnd_returnsDMinus2() {
        // Given
        val dueDate = LocalDate.parse("2026-11-02")
        val today = LocalDate.parse("2026-10-31")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-2", label)
    }

    @Test
    fun dueDateLabel_dueOnYearEnd_returnsDDay() {
        // Given
        val dueDate = LocalDate.parse("2026-12-31")
        val today = LocalDate.parse("2026-12-31")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_dueOneDayAfterYearEnd_returnsDMinus1() {
        // Given
        val dueDate = LocalDate.parse("2027-01-01")
        val today = LocalDate.parse("2026-12-31")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_dueTwoDaysAfterYearEnd_returnsDMinus2() {
        // Given
        val dueDate = LocalDate.parse("2027-01-02")
        val today = LocalDate.parse("2026-12-31")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-2", label)
    }

    @Test
    fun dueDateLabel_dueOnLeapDayEve_returnsDDay() {
        // Given
        val dueDate = LocalDate.parse("2024-02-28")
        val today = LocalDate.parse("2024-02-28")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_dueOnLeapDay_returnsDMinus1() {
        // Given
        val dueDate = LocalDate.parse("2024-02-29")
        val today = LocalDate.parse("2024-02-28")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_dueDayAfterLeapDay_returnsDMinus2() {
        // Given
        val dueDate = LocalDate.parse("2024-03-01")
        val today = LocalDate.parse("2024-02-28")
        val isCompleted = false

        // When
        val label = dueDateLabel(dueDate, today, isCompleted)

        // Then
        assertEquals("D-2", label)
    }
}