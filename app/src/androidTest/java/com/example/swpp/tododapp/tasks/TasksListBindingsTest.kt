package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    @Test
    fun setDueDateLabel_activeDueInFiveDays_showsDMinus5() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "OLD"
                visibility = View.GONE
            }
            val task = Task(title = "Pay rent", dueDate = today.plusDays(5), isCompleted = false)

            // When
            setDueDateLabel(textView, task)

            // Then
            assertEquals("D-5", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_completedWithoutDate_showsClear() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "OLD"
                visibility = View.GONE
            }
            val task = Task(title = "Pay rent", dueDate = null, isCompleted = true)

            // When
            setDueDateLabel(textView, task)

            // Then
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_completedWithDate_showsClear() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "OLD"
                visibility = View.GONE
            }
            val task = Task(title = "Pay rent", dueDate = today.plusDays(5), isCompleted = true)

            // When
            setDueDateLabel(textView, task)

            // Then
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_activeWithoutDate_clearsStaleLabel() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "CLEAR"
                visibility = View.VISIBLE
            }
            val task = Task(title = "Pay rent", dueDate = null, isCompleted = false)

            // When
            setDueDateLabel(textView, task)

            // Then
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_nullTask_clearsStaleLabel() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "CLEAR"
                visibility = View.VISIBLE
            }
            val task = null

            // When
            setDueDateLabel(textView, task)

            // Then
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_dueYesterday_showsDPlus1() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "OLD"
                visibility = View.GONE
            }
            val task = Task(title = "Pay rent", dueDate = today.minusDays(1), isCompleted = false)

            // When
            setDueDateLabel(textView, task)

            // Then
            assertEquals("D+1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_dueToday_showsDDay() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "OLD"
                visibility = View.GONE
            }
            val task = Task(title = "Pay rent", dueDate = today, isCompleted = false)

            // When
            setDueDateLabel(textView, task)

            // Then
            assertEquals("D-Day", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_dueTomorrow_showsDMinus1() {
        instrumentation.runOnMainSync {
            // Given
            val today = LocalDate.now()
            val textView = TextView(instrumentation.targetContext).apply {
                text = "OLD"
                visibility = View.GONE
            }
            val task = Task(title = "Pay rent", dueDate = today.plusDays(1), isCompleted = false)

            // When
            setDueDateLabel(textView, task)

            // Then
            assertEquals("D-1", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }
}