package id.hikmah.stiki.tandur_1.repository

import id.hikmah.stiki.tandur_1.remote.model.LoginInfo
import id.hikmah.stiki.tandur_1.remote.service.APIService

class UserRepo(private val api: APIService) {
    suspend fun loginAccount(email: String, password: String) = api.loginAccount(
        LoginInfo(email, password)
    )
}