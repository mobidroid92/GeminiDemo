package com.bdour.gemini.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bdour.gemini.presentation.model.MsgModel
import com.bdour.gemini.presentation.model.MsgType
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    generativeModel: GenerativeModel
) : ViewModel() {

    private val _chatList = MutableStateFlow(listOf<MsgModel>())
    val chatList = _chatList.asStateFlow()

    private val _isSendButtonEnabled = MutableStateFlow(false)
    val isSendButtonEnabled = _isSendButtonEnabled.asStateFlow()

    private var msgToSend: String = ""

    private val chat: Chat = generativeModel.startChat()

    fun updateText(msg: String) {
        msgToSend = msg
        _isSendButtonEnabled.update {
            msgToSend.isNotBlank() &&
                    (_chatList.value.isEmpty() || _chatList.value.last().msgType != MsgType.Loading)
        }
    }

    fun sendMsg() {
        _chatList.update {
            it +
            MsgModel(text = msgToSend, msgType = MsgType.Me) +
            MsgModel(msgType = MsgType.Loading)
        }

        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            removeLoading()
            if (_chatList.value.last().msgType == MsgType.Me) {
                _chatList.update {
                    it.toMutableList().apply {
                        set(it.lastIndex, it.last().copy(isSuccess = false))
                    }
                }
            }
        }
        viewModelScope.launch(exceptionHandler) {
            val sendMsgResult = chat.sendMessage(msgToSend)
            removeLoading()
            if (sendMsgResult.text != null) {
                _chatList.update {
                    it + MsgModel(text = sendMsgResult.text!!, msgType = MsgType.Other)
                }
            }
        }

        updateText("")
    }

    private fun removeLoading() {
        if (_chatList.value.last().msgType == MsgType.Loading) {
            _chatList.update { it.dropLast(1) }
        }
    }
}