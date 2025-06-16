package com.muhasib.aliascreation.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: AccountRepository
) : ViewModel() {

    private val _accountNames = MutableStateFlow<List<String>>(emptyList())
    val accountNames: StateFlow<List<String>> = _accountNames

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchAccountIds() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _accountNames.value = repository.getAccountNames()
            } catch (e: Exception) {
                _error.value = "Failed to fetch IDs: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
