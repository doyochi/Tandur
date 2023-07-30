package id.hikmah.stiki.tandur_1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.hikmah.stiki.tandur_1.helper.Resource
import id.hikmah.stiki.tandur_1.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException


class LoginViewModel constructor(
    private val repository: UserRepo
): ViewModel() {

    fun loginAccount(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))

        try {
            emit(Resource.success(data = repository.loginAccount(email, password)))
        } catch (e: IOException) {
            emit(Resource.error(null, e.message ?: "Error Occurred!"))
        } catch (e: HttpException) {
            emit(Resource.error(null, e.message() ?: "Error Occurred!"))
        }

    }

}