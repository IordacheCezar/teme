package com.sd.laborator.interfaces

import com.sd.laborator.pojo.Expense

//-addExpense
//-deleteExpense
//-updateExpense
//-searchExpense

interface IExpenseService {
    fun listExpenses(userId: Int): List<Expense>
    fun addExpense(userId: Int, expense: Expense)
    fun getExpense(userId: Int, expenseId: Int): Expense?
    fun deleteExpense(userId: Int, expenseId: Int)
    fun updateExpense(userId: Int, expenseId: Int, expense: Expense)
    fun searchExpenses(userId: Int, category: String): List<Expense>
}