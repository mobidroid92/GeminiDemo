package com.bdour.gemini.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bdour.gemini.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    private lateinit var chatRecyclerAdapter: ChatRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
        listeners()
        observers()
    }

    private fun setup() {
        setSupportActionBar(binding.toolbar)
        setupRecycler()
    }

    private fun setupRecycler() {
        chatRecyclerAdapter = ChatRecyclerAdapter()
        //Need this to scroll to bottom correctly.
        (binding.chatsRecyclerView.layoutManager as? LinearLayoutManager)?.stackFromEnd = true
        binding.chatsRecyclerView.adapter = chatRecyclerAdapter

    }

    private fun listeners() {
        binding.sendMsgBtn.setOnClickListener {
            viewModel.sendMsg()
            binding.msgToSendEditText.setText("")
        }
        binding.msgToSendEditText.doOnTextChanged { _, _, _, _ ->
            viewModel.updateText(binding.msgToSendEditText.text.toString())
        }
    }

    private fun observers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.chatList.collect {
                        chatRecyclerAdapter.updateList(it) {
                            if(it.isNotEmpty()) {
                                binding.chatsRecyclerView.smoothScrollToPosition(it.lastIndex)
                            }
                        }
                    }
                }
                launch {
                    viewModel.isSendButtonEnabled.collect {
                        binding.sendMsgBtn.isEnabled = it
                    }
                }
            }
        }
    }

}