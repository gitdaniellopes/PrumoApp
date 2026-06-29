package br.com.prumoapp.data.model

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class UserFirestore(
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val monthlyBudget: Double? = null,
    val createdAt: Timestamp? = null,
)
