package com.sd.laborator.services

import com.sd.laborator.interfaces.IExpenseService
import com.sd.laborator.pojo.Expense
import org.springframework.stereotype.Service

@Service
class ExpenseService : IExpenseService {

    private val expenses = mutableListOf<Expense>()

    override fun getExpense(userId: Int, expenseId: Int): Expense? {
        return expenses.find { it.id == expenseId && it.userId == userId }
    }

    override fun listExpenses(userId: Int): List<Expense> {
        return expenses.filter { it.userId == userId }
    }

    override fun addExpense(userId: Int, expense: Expense) {
        expense.userId = userId
        expenses.add(expense)
    }

    override fun deleteExpense(userId: Int, expenseId: Int) {
        val expense = expenses.find { it.id == expenseId && it.userId == userId }
            ?: throw NoSuchElementException("Expense not found")

        expenses.remove(expense)
    }

    override fun updateExpense(userId: Int, expenseId: Int, expense: Expense) {

        val index = expenses.indexOfFirst {
            it.id == expenseId && it.userId == userId
        }

        if (index == -1) {
            throw NoSuchElementException("Expense not found")
        }

        expense.userId = userId
        expense.id = expenseId

        expenses[index] = expense
    }

    override fun searchExpenses(userId: Int, category: String): List<Expense> {
        return expenses.filter {
            it.userId == userId &&
                    it.category.equals(category, ignoreCase = true)
        }
    }
}