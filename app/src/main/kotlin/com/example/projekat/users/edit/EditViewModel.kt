package com.example.projekat.users.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekat.users.UsersDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val usersData: UsersDataStore
) : ViewModel() {

    private val _editState = MutableStateFlow(
        EditState.EditState(
            name = usersData.data.value.users[usersData.data.value.pick].name,
            nickname = usersData.data.value.users[usersData.data.value.pick].nickname,
            email = usersData.data.value.users[usersData.data.value.pick].email,
        )
    )
    val editState = _editState.asStateFlow()

    private val _editEvents = MutableSharedFlow<EditState.EditUIEvent>()

    private fun setEditState(updateWith: EditState.EditState.() -> EditState.EditState) =
        _editState.getAndUpdate(updateWith)

    fun setEditEvent(event: EditState.EditUIEvent) =
        viewModelScope.launch { _editEvents.emit(event) }

    init {
        observerEvents()
    }


    private fun observerEvents() {
        viewModelScope.launch {
            _editEvents.collect {
                when (it) {
                    is EditState.EditUIEvent.EmailInputChanged -> emailChange(it.email)
                    is EditState.EditUIEvent.NameInputChanged -> nameChange(it.name)
                    is EditState.EditUIEvent.NicknameInputChanged -> nicknameChange(it.nickname)
                    is EditState.EditUIEvent.SaveChanges -> updateUser()
                }
            }
        }
    }


    fun isInfoValid(): Boolean {
        if (editState.value.name.isEmpty())
            return false
        if (editState.value.nickname.isEmpty())
            return false
        if (editState.value.email.isEmpty())
            return false
        return true
    }

    private fun updateUser() {
        val users = usersData.data.value.users.toMutableList()
        val pick = usersData.data.value.pick

        users[pick] = users[pick].copy(
            name = editState.value.name,
            nickname = editState.value.nickname,
            email = editState.value.email,
        )

        viewModelScope.launch {
            usersData.updateUser(users.toImmutableList())
            setEditState { copy(saveUserPassed = true) }
        }
    }


    private fun emailChange(email: String) {
        viewModelScope.launch {
            setEditState { copy(email = email) }
        }
    }

    private fun nameChange(name: String) {
        viewModelScope.launch {
            setEditState { copy(name = name) }
        }
    }

    private fun nicknameChange(nickname: String) {
        viewModelScope.launch {
            setEditState { copy(nickname = nickname) }
        }
    }
}