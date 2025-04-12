package com.example.financetrackerapp.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "finance_settings")

object SavingPreferences {

    private val SAVING_GOAL_KEY = doublePreferencesKey("saving_goal")
    private val TOTAL_BUDGET_KEY = doublePreferencesKey("total_budget")

    // Save the saving goal to DataStore
    suspend fun saveSavingGoal(context: Context, goal: Double) {
        context.dataStore.edit { preferences ->
            preferences[SAVING_GOAL_KEY] = goal
        }
    }

    // Retrieve the saving goal from DataStore
    // Returns 20000.0 if no value is found
    fun getSavingGoal(context: Context): Flow<Double> {
        return context.dataStore.data.map { preferences ->
            preferences[SAVING_GOAL_KEY] ?: 20000.0
        }
    }

    // Save the total monthly budget to DataStore
    suspend fun saveTotalBudget(context: Context, budget: Double) {
        context.dataStore.edit { preferences ->
            preferences[TOTAL_BUDGET_KEY] = budget
        }
    }

    // Retrieve the total monthly budget from DataStore
    // Returns 5000.0 if no value is found
    fun getTotalBudget(context: Context): Flow<Double> {
        return context.dataStore.data.map { preferences ->
            preferences[TOTAL_BUDGET_KEY] ?: 5000.0
        }
    }
}

