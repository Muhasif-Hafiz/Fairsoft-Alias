package com.muhasib.aliascreation.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhasib.aliascreation.model.Account
import com.muhasib.aliascreation.model.AlternateName
import com.muhasib.aliascreation.model.AlternateNameResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: AccountRepository
) : ViewModel() {

    private val _accountNames = MutableStateFlow<List<Account>>(emptyList())
    val accountNames: StateFlow<List<Account>> = _accountNames

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // For alternate names
    private val _alternateNamesByIds = MutableLiveData<List<AlternateName>>()
    val alternateNameByIds: LiveData<List<AlternateName>> = _alternateNamesByIds

    fun fetchAccountIds() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
              _accountNames.value = repository.getAccountNames()

                Log.d("testing", repository.getAccountNames().toString())
            } catch (e: Exception) {
                _error.value = "Failed to fetch IDs: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchAlternateNameById(type: String, actId: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = repository.getAlternateNamesById("NADCF2025", type, actId)

                if (response.data == null) {
                    _alternateNamesByIds.value = emptyList()
                    _error.value = response.message ?: "No data found"
                    return@launch
                }

                val alternateNames = response.data.map { data ->
                    AlternateName(
                        name = data.actName,
                        category = type,
                        id = data.actId.toString(),
                    )
                }

                _alternateNamesByIds.value = alternateNames

            } catch (e: Exception) {
                _error.value = "Failed to fetch alternate names: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

}