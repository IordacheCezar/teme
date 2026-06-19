package com.sd.laborator.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import com.sd.laborator.interfaces.IExpenseService
import com.sd.laborator.pojo.Expense
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import kotlin.math.exp

@RestController
class ExpenseController {
    @Autowired
    private lateinit var expenseService: IExpenseService

    @RequestMapping(value = *arrayOf("/users/{userId}/expense"), method = arrayOf(RequestMethod.GET))
    fun listExpenses(@PathVariable userId: Int) : ResponseEntity<List<Expense>> {
        val list = expenseService.listExpenses(userId)
        var status = HttpStatus.OK
        return ResponseEntity(list, status)
    }

    @RequestMapping(value = *arrayOf("/users/{userId}/expense"), method = arrayOf(RequestMethod.POST))
    fun addExpense(@PathVariable userId: Int, @RequestBody expense: Expense) : ResponseEntity<Unit> {
        val status = HttpStatus.CREATED
        expenseService.addExpense(userId, expense)
        return ResponseEntity(Unit, status)
    }

    @RequestMapping(value = *arrayOf("/users/{userId}/expense/{id}"), method = arrayOf(RequestMethod.GET))
    fun getExpense(@PathVariable userId: Int, @PathVariable id: Int) : ResponseEntity<Expense> {
        val expense = expenseService.getExpense(userId, id)
        if (expense != null) {
            return ResponseEntity(expense, HttpStatus.OK)
        }
        else
            return ResponseEntity(expense,HttpStatus.NOT_FOUND)
    }

    @RequestMapping(value = *arrayOf("/users/{userId}/expense/{id}"), method = arrayOf(RequestMethod.DELETE))
    fun deleteExpense(@PathVariable userId: Int, @PathVariable id: Int) : ResponseEntity<Unit> {
        if (expenseService.getExpense(userId, id) == null) {
            return ResponseEntity(Unit, HttpStatus.NOT_FOUND)
        }
        expenseService.deleteExpense(userId, id)
        return ResponseEntity(Unit, HttpStatus.OK)
    }

    @RequestMapping(value = *arrayOf("/users/{userId}/expense/search/{category}"), method = arrayOf(RequestMethod.GET))
    fun searchExpenses(@PathVariable userId: Int, @PathVariable category: String): ResponseEntity<List<Expense>> {
        var list = expenseService.searchExpenses(userId, category)
        if (list.isEmpty()) {
            return ResponseEntity(list, HttpStatus.NOT_FOUND)
        }
        else
            return ResponseEntity(list, HttpStatus.OK)
    }

    @RequestMapping (value = *arrayOf("/users/{userId}/expense/{id}"), method = arrayOf(RequestMethod.PATCH))
    fun updateExpense(@PathVariable userId: Int, @PathVariable id: Int, @RequestBody patchOperations: JsonPatch): ResponseEntity<Unit> {
        expenseService.getExpense(userId, id)?.let {
            val objectMapper = ObjectMapper()
            val patchedPersonJsonNode =
                patchOperations.apply(objectMapper.valueToTree(it))
            val patchedPerson =
                objectMapper.treeToValue(patchedPersonJsonNode, Expense::class.java)
            expenseService.updateExpense(userId, id, patchedPerson)
            return ResponseEntity(Unit, HttpStatus.OK)
        }?: return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}