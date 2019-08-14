package com.example.corenetwork

import com.example.corenetwork.model.ErrorResponse
import com.example.corenetwork.model.authsettings.UserInfo
import com.example.corenetwork.utils.AuthDataProvider
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableJust
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
class SessionManager(
    private val authDataProvider: AuthDataProvider,
    private val api: Api,
    private val cryptoHelper: CryptoHelper = CryptoHelper.getInstance()
) {


    val users: List<UserInfo>
        get() {
            return authDataProvider.users ?: listOf()
        }

    private var cachedUser: UserInfo? = null
    var currentUser: UserInfo?
        get() {
            if (cachedUser == null)
                cachedUser = authDataProvider.currentUser
            return cachedUser
        }
        private set(value) {
            cachedUser = null
            authDataProvider.currentUser = value
        }


    val isLoggedIn: Boolean
        get() {
            cleanupUsers()
            return accessToken != null && refreshToken != null
        }

    val isNeedUpdateToken: Boolean
        get() {
            val expiresTime = (currentUser?.accessTokenExpires?.time ?: 0)
            val diffMin = (expiresTime - Date().time) / (1000 * 60)
            return diffMin <= Constants.TIME_BEFORE_TOKEN_UPDATE_MIN
        }

    val accessToken: String?
        get() {
            return currentUser?.accessToken
        }

    val refreshToken: String?
        get() {
            return currentUser?.refreshToken
        }


    fun logIn(name: String, password: String): Observable<Unit> {
        cleanupUsers()
        val encryptedPassword = cryptoHelper.encryptWithAES(password)
        return api
            .login(
                name = name,
                encryptedPassword = encryptedPassword
            )
            .map { user ->
                addOrUpdateUser(user)
            }
    }


    fun changePassword(oldPassword: String, newPassword: String, removeCurrentUser: Boolean): Observable<Unit> {

        val encryptedOldPassword = cryptoHelper.encryptWithAES(oldPassword)
        val encryptedNewPassword = cryptoHelper.encryptWithAES(newPassword)
        return api.changePass(
            oldPasswordEncrypted = encryptedOldPassword,
            newPasswordEncrypted = encryptedNewPassword
        )
            .map { user ->
                addOrUpdateUser(user)
            }
    }


    fun updateTokenIfNeeded(): Observable<Unit> {
        return if (isNeedUpdateToken) {
            api
                .refreshToken(
                    refreshToken = refreshToken!!
                )
                .doOnError { handleServerError(it) }
                .map { user ->
                    addOrUpdateUser(user)
                }
        } else {
            ObservableJust(Unit)
        }
    }


    private fun handleServerError(error: Throwable) {
        val apiError = ErrorResponse.create(error)
        val authError = apiError?.error
        if (authError == "auth_error") {
            logOut()
        }
    }


    fun logOut() {
        removeCurrentUser()
        EventsManager.getInstance().loginStateChanged.post()
    }


    fun switchToUser(userInfo: UserInfo): Observable<Unit> {
        return if (userInfo != currentUser) {
            val backupUser = currentUser
            currentUser = userInfo
            Observable.just(Unit)

        } else
            Observable.just(Unit)
    }


    fun cleanupUsers() {
        if (currentUser?.isAuthorizationNotCompleted == true) {
            removeCurrentUser()
        }
    }


    private fun addOrUpdateUser(userInfo: UserInfo) {
        val users = users.toMutableList()
        val i = users.indexOfFirst { it == userInfo }
        if (i != -1) {
            users[i] = userInfo
        } else {
            users.add(userInfo)
        }
        currentUser = userInfo
        authDataProvider.users = users.toList()
    }


    private fun removeCurrentUser() {
        val users = users.toMutableList()
        val i = users.indexOfFirst { it == currentUser }
        users.removeAt(i)
        if (users.count() != 0) {
            val anotherUser = users.firstOrNull()
            currentUser = anotherUser //make remaining user active, if exists
        } else {
            currentUser = null
        }
        authDataProvider.users = users.toList()
    }

    private fun generateRandomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val stringLen = 10
        return (1..stringLen)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    companion object {
        //singleton
        private lateinit var sessionManager: SessionManager


        fun getInstance(): SessionManager {
            return sessionManager
        }


        fun init(
            authDataProvider: AuthDataProvider,
            api: Api
        ) {
            sessionManager = SessionManager(
                authDataProvider,
                api
            )
        }
    }
}