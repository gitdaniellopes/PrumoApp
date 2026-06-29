package br.com.prumoapp.data.mapper

import br.com.prumoapp.data.extensions.toInstant
import br.com.prumoapp.data.extensions.toUtcTimestamp
import br.com.prumoapp.data.model.UserFirestore
import br.com.prumoapp.domain.model.common.Money
import br.com.prumoapp.domain.model.user.User
import br.com.prumoapp.domain.model.user.UserId
import dev.gitlive.firebase.firestore.Timestamp

fun UserFirestore.toDomain(id: String): User {
    return User(
        id = UserId(id),
        name = name,
        email = email,
        photoUrl = photoUrl,
        monthlyBudget = monthlyBudget?.let { Money.of(it) },
        createdAt = createdAt?.toInstant()
    )
}

fun User.toFirestore(): UserFirestore {
    return UserFirestore(
        name = name,
        email = email,
        photoUrl = photoUrl.toString(),
        monthlyBudget = monthlyBudget?.let { it.cents / 100.0 },
        createdAt = createdAt?.toUtcTimestamp() ?: Timestamp.now()
    )
}