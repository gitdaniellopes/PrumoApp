package br.com.prumoapp.data.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseFirestore(
    val userId: String = "",
    val name: String = "",
    val amount: Double = 0.0,
    val dueDate: Timestamp = Timestamp.now(),
    val paid: Boolean = false,
    val paidAt: Timestamp? = null,
    val repeatType: String = "NONE",
    val installment: Boolean,
    val currentInstallment: Int? = null,
    val totalInstallments: Int? = null,
    val parentExpenseId: String? = null,
    val dueDay: Int? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
)
