package com.example.corenetwork.utils

import com.example.corenetwork.model.authsettings.UserInfo

/**
 * Интерфейс хранилища данных о авторизации
 */
interface AuthDataProvider {
    var currentUser:UserInfo?
    var users:List<UserInfo>?
}