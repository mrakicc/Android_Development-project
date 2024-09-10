package com.example.projekat.cats.login

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import com.example.projekat.users.User
import com.example.projekat.users.UsersDataStore


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usersData: UsersDataStore
) :ViewModel(){

    private val _loginState = MutableStateFlow(LoginState.LoginState())
    val loginState = _loginState.asStateFlow();

    private val _loginEvents = MutableSharedFlow<LoginState.LoginUIEvent>()

    private val NICKNAME_PATTERN = Regex("[A-Za-z0-9]+")

    private fun setLoginState(updateWith: LoginState.LoginState.() -> LoginState.LoginState) =
        _loginState.getAndUpdate(updateWith)

     fun setLoginEvent(event: LoginState.LoginUIEvent) = viewModelScope.launch {
        _loginEvents.emit(event)
    }

    init {
        observeEvents()
    }

    private fun observeEvents(){
        viewModelScope.launch {
            _loginEvents.collect(){
                when(it){
                   is LoginState.LoginUIEvent.NicknameInputChanged -> nicknameChanged(it.nickname)
                    is LoginState.LoginUIEvent.NameInputChanged -> nameChanged(it.name)
                    is LoginState.LoginUIEvent.EmailInputChanged -> emailChanged(it.email)
                    is LoginState.LoginUIEvent.AddUser -> addUser()
                }
            }
        }
    }

    private fun nicknameChanged(nickname: String){
        viewModelScope.launch {
            setLoginState {
                copy(nickname = nickname)
            }
        }
    }

    private fun nameChanged(name: String){
        viewModelScope.launch {
            setLoginState {
                copy(name = name)
            }
        }
    }

    private fun emailChanged(email:String){
        viewModelScope.launch {
            setLoginState {
                copy(email = email)
            }
        }
    }

    private fun addUser(){
        val user = User(name = loginState.value.name, nickname = loginState.value.nickname, email = loginState.value.email)
        viewModelScope.launch {
            usersData.addUser(user)
            setLoginState {
                copy(loginCheckPassed = true)
            }
        }
    }


     fun isInfoValid(): Boolean{
        if(loginState.value.nickname.isEmpty())
            return false
        if(loginState.value.name.isEmpty() || !NICKNAME_PATTERN.matches(loginState.value.name))
            return false
        if (loginState.value.email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(loginState.value.email).matches())
            return false
        return true
    }


    fun hasAccount(): Boolean{
        return usersData.data.value.pick != -1
    }

}