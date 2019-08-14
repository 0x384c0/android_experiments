package com.example.corenetwork.model.authsettings

import java.util.*

class UserInfo(
    var refreshToken: String,
    var accessToken: String,
    var accessTokenExpires: Date,
    var isAuthorizationNotCompleted:Boolean
)
