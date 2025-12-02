package com.example.campuslink_android.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.AuthApi
import com.example.campuslink_android.data.repository.AuthRepositoryImpl
import com.example.campuslink_android.domain.model.User
import com.example.campuslink_android.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    val loginState: LiveData<LoginState> get() = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val user: User = authRepository.login(email, password)

                // ⭐ 로그인 성공 → 이메일 저장 ⭐
                TokenStore.saveEmail(email)

                _loginState.value = LoginState.Success(user)
            } catch (e: Exception) {
                e.printStackTrace()
                _loginState.value = LoginState.Error(e.message ?: "로그인 실패")
            }
        }
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val user: User) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    companion object {
        fun create(): LoginViewModel {
            val authApi = ApiClient.create(AuthApi::class.java)
            val repo: AuthRepository =
                AuthRepositoryImpl(authApi, TokenStore)
            return LoginViewModel(repo)
        }
    }
}
