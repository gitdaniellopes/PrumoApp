package br.com.prumoapp.data.mapper

import br.com.prumoapp.domain.model.auth.AuthUser
import br.com.prumoapp.domain.model.user.UserId
import dev.gitlive.firebase.auth.FirebaseUser

fun FirebaseUser.toAuthUser(): AuthUser = AuthUser(uid = UserId(uid), email = email)