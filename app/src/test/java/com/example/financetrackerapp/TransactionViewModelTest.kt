package com.example.financetrackerapp

import com.example.financetrackerapp.viewmodel.TransactionViewModel
import com.example.financetrackerapp.data.Transaction
import org.junit.Assert.assertEquals
import org.junit.Test
import android.app.Application
import io.mockk.mockk
import java.time.LocalDate

class TransactionViewModelTest {

    private val application = mockk<Application>(relaxed = true)
    private val viewModel = TransactionViewModel(application)

    @Test
    fun testUpdateSavingGoal() {
        val newGoal = 30000.0
        viewModel.updateSavingGoal(newGoal)
        assertEquals(newGoal, viewModel.savingGoal.value, 0.001)
    }

    @Test
    fun testUpdateTotalBudget() {
        val newBudget = 10000.0
        viewModel.updateTotalBudget(newBudget)
        assertEquals(newBudget, viewModel.totalBudget.value, 0.001)
    }

    @Test
    fun testInsertTransaction() {
        val transaction = Transaction(
            id = 0,
            amount = 500.0,
            category = "Test Category",
            paymentMethod = "Cash",
            date = LocalDate.now()
        )
        viewModel.insertTransaction(transaction)
    }
}
