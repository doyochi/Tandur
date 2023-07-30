package id.hikmah.stiki.tandur_1.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.hikmah.stiki.tandur_1.helper.DatastoreManager
import kotlinx.coroutines.launch

class DatastoreViewModel constructor(private val pref: DatastoreManager) : ViewModel() {

    fun saveLoginState(value: Boolean) {
        viewModelScope.launch {
            pref.saveLoginStateToDataStore(value)
        }
    }

    fun getLoginState(): LiveData<Boolean> {
        return pref.readLoginStateFromDataStore().asLiveData()
    }

    fun saveAccessToken(value: String) {
        viewModelScope.launch {
            pref.saveAccessTokenToDataStore(value)
        }
    }

    fun getAccessToken(): LiveData<String> {
        return pref.readAccessTokenFromDataStore().asLiveData()
    }

    fun saveIdUser(value: Int) {
        viewModelScope.launch {
            pref.saveIdUserToDataStore(value)
        }
    }

    fun getIdUser(): LiveData<Int> {
        return pref.readIdUserFromDataStore().asLiveData()
    }

    fun saveMsgSnackbar(value: String) {
        viewModelScope.launch {
            pref.saveMsgSnackbarToDataStore(value)
        }
    }

    fun getMsgSnackbar(): LiveData<String> {
        return pref.readMsgSnackbarFromDataStore().asLiveData()
    }

    fun saveTriggerUpdateProduct(value: Boolean) {
        viewModelScope.launch {
            pref.saveTriggerUpdateProductToDataStore(value)
        }
    }

    fun getTriggerUpdateProduct(): LiveData<Boolean> {
        return pref.readTriggerUpdateProducteFromDataStore().asLiveData()
    }

    fun saveIsUserProfileComplete(value: Boolean) {
        viewModelScope.launch {
            pref.saveProfileComplete(value)
        }
    }

    fun getIsUserProfileComplete(): LiveData<Boolean> {
        return pref.getProfileComplete().asLiveData()
    }

    fun deleteAllData() {
        viewModelScope.launch {
            pref.removeFromDataStore()
        }
    }
}