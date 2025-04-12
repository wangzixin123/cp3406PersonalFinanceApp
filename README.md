# Personal Finance Tracker App

A simple yet powerful Android application to help users manage their personal finances.  
Built using **Kotlin**, **Jetpack Compose**, **Room Database**, and **DataStore**.

---

## Features

- Record income and expense transactions
- View monthly and yearly financial statistics
- Track total balance and set personal saving goals
- Edit monthly budget dynamically
- View transaction history and delete records if needed
- Persistent data storage using Room and DataStore
- Clean and responsive UI using Material 3

---

## Technologies & APIs Used

| Technology          | Purpose                                          |
|----------------------|---------------------------------------------------|
| Jetpack Compose      | Build modern, declarative UIs                    |
| Room Database        | Store and manage transaction data locally        |
| DataStore Preferences| Save user's saving goals and budget settings     |
| ViewModel + StateFlow| Efficient UI state management                    |

---



## Testing

- Unit tests implemented for `TransactionViewModel`
- Covered functions:
  - `updateSavingGoal()`
  - `updateTotalBudget()`
  - `insertTransaction()`
- Basic validation checks using `JUnit4`
- Test framework: `JUnit` + `MockK`

Example unit test:

```kotlin
@Test
fun testUpdateSavingGoal() {
    val newGoal = 30000.0
    viewModel.updateSavingGoal(newGoal)
    assertEquals(newGoal, viewModel.savingGoal.value, 0.001)
}
