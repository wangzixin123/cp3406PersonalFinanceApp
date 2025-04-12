package com.example.financetrackerapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financetrackerapp.data.Transaction
import com.example.financetrackerapp.data.TransactionDatabase
import com.example.financetrackerapp.data.TransactionRepository
import com.example.financetrackerapp.data.SavingPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.runtime.*

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TransactionRepository
    val transactions: StateFlow<List<Transaction>>

    private val context = application.applicationContext

    // Saving goal state
    private val _savingGoal = mutableStateOf(20000.0)
    val savingGoal: State<Double> = _savingGoal

    // Total budget state
    private val _totalBudget = mutableStateOf(5000.0)
    val totalBudget: State<Double> = _totalBudget

    init {
        val dao = TransactionDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(dao)

        transactions = repository.getAllTransactions()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Load saved saving goal from local storage
        viewModelScope.launch {
            SavingPreferences.getSavingGoal(context).collect { savedGoal ->
                _savingGoal.value = savedGoal
            }
        }

        // Load saved total budget from local storage
        viewModelScope.launch {
            SavingPreferences.getTotalBudget(context).collect { savedBudget ->
                _totalBudget.value = savedBudget
            }
        }
    }

    fun insertTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.insert(transaction)
    }

    fun getTransactionsByDateRange(start: LocalDate, end: LocalDate): Flow<List<Transaction>> {
        return repository.getTransactionsByDateRange(start, end)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        repository.delete(transaction)
    }

    fun updateSavingGoal(newGoal: Double) {
        _savingGoal.value = newGoal
        viewModelScope.launch {
            SavingPreferences.saveSavingGoal(context, newGoal)
        }
    }

    fun updateTotalBudget(newBudget: Double) {
        _totalBudget.value = newBudget
        viewModelScope.launch {
            SavingPreferences.saveTotalBudget(context, newBudget)
        }
    }
}



