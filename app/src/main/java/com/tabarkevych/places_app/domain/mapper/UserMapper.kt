package com.tabarkevych.places_app.domain.mapper

import com.google.firebase.auth.FirebaseUser
import com.tabarkevych.places_app.domain.model.UserInfo

fun FirebaseUser.toUserInfo() =
    UserInfo(
        userId = uid,
        username = displayName,
        profilePictureUrl = photoUrl?.toString()
    )
