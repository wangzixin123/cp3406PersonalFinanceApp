package com.example.financetrackerapp.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TransactionRepository(private val dao: TransactionDao) {

    fun getAllTransactions(): Flow<List<Transaction>> = dao.getAllTransactions()

    fun getTransactionsByDateRange(start: LocalDate, end: LocalDate): Flow<List<Transaction>> =
        dao.getTransactionsByDateRange(start, end)

    suspend fun insert(transaction: Transaction) {
        dao.insert(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        dao.delete(transaction)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}
