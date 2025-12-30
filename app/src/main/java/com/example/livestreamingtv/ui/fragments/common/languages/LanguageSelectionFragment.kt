package com.example.livestreamingtv.ui.fragments.common.languages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.livestreamingtv.R
import com.example.livestreamingtv.adapter.LanguageAdapter
import com.example.livestreamingtv.databinding.FragmentLanguageSelectionBinding
import com.example.livestreamingtv.viewmodels.LanguageSelectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguageSelectionFragment : Fragment() {
    
    private var _binding: FragmentLanguageSelectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LanguageSelectionViewModel by viewModels()
    
    private val languages = listOf(
        Language("en", "English"),
        Language("es", "Español"),
        Language("de", "Deutsch"),
        Language("fr", "Français"),
        Language("hi", "हिन्दी"),
        Language("ar", "العربية"),
        Language("zh", "中文"),
        Language("ja", "日本語")
    )
    
    private lateinit var adapter: LanguageAdapter
    private lateinit var layoutManager: CenterLayoutManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLanguageSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupNextButton()
        observeLanguage()
    }
    
    private fun setupNextButton() {
        binding.btnNext.setOnClickListener {
            navigateToNextScreen()
        }
        
        // TV Remote D-pad support for Next button
        binding.rvLanguages.setOnKeyListener { _, keyCode, event ->
            if (event.action == android.view.KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    android.view.KeyEvent.KEYCODE_DPAD_CENTER,
                    android.view.KeyEvent.KEYCODE_ENTER -> {
                        navigateToNextScreen()
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }
    }
    
    private fun navigateToNextScreen() {
        // Get current center position and navigate
        val snapHelper = LinearSnapHelper()
        val centerView = snapHelper.findSnapView(layoutManager)
        if (centerView != null) {
            val position = binding.rvLanguages.getChildAdapterPosition(centerView)
            if (position != RecyclerView.NO_POSITION && position < languages.size) {
                val language = languages[position]
                viewModel.selectLanguage(language.code)
                findNavController().navigate(R.id.action_languageSelectionFragment_to_selectWhatYouSeeFragment)
            }
        }
    }
    
    private fun setupRecyclerView() {
        layoutManager = CenterLayoutManager(requireContext())
        adapter = LanguageAdapter(languages) { language, position ->
            onLanguageSelected(language, position)
        }
        
        val snapHelper = LinearSnapHelper()
        
        binding.rvLanguages.apply {
            this.layoutManager = this@LanguageSelectionFragment.layoutManager
            this.adapter = this@LanguageSelectionFragment.adapter
            
            // Add snap helper to snap to center
            snapHelper.attachToRecyclerView(this)
            
            // Scroll to default position (3rd item, index 2)
            post {
                scrollToPosition(2)
                updateSelectedLanguage(2)
            }
            
            // TV Remote D-pad UP/DOWN navigation
            setOnKeyListener { _, keyCode, event ->
                if (event.action == android.view.KeyEvent.ACTION_DOWN) {
                    val currentPosition = this@LanguageSelectionFragment.adapter.getSelectedPosition()
                    when (keyCode) {
                        android.view.KeyEvent.KEYCODE_DPAD_UP -> {
                            if (currentPosition > 0) {
                                val newPosition = currentPosition - 1
                                smoothScrollToPosition(newPosition)
                                updateSelectedLanguage(newPosition)
                            }
                            true
                        }
                        android.view.KeyEvent.KEYCODE_DPAD_DOWN -> {
                            if (currentPosition < languages.size - 1) {
                                val newPosition = currentPosition + 1
                                smoothScrollToPosition(newPosition)
                                updateSelectedLanguage(newPosition)
                            }
                            true
                        }
                        android.view.KeyEvent.KEYCODE_DPAD_CENTER,
                        android.view.KeyEvent.KEYCODE_ENTER -> {
                            // Select current language and navigate
                            if (currentPosition >= 0 && currentPosition < languages.size) {
                                viewModel.selectLanguage(languages[currentPosition].code)
                                findNavController().navigate(R.id.action_languageSelectionFragment_to_selectWhatYouSeeFragment)
                            }
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
            
            // Listen to scroll to update selected item
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        // Get center position
                        val centerView =
                            snapHelper.findSnapView(this@LanguageSelectionFragment.layoutManager)
                        if (centerView != null) {
                            val position = recyclerView.getChildAdapterPosition(centerView)
                            if (position != RecyclerView.NO_POSITION) {
                                updateSelectedLanguage(position)
                            }
                        }
                    }
                }
            })
            
            // Request focus for TV remote navigation
            requestFocus()
        }
    }
    
    private fun updateSelectedLanguage(position: Int) {
        if (position >= 0 && position < languages.size) {
            adapter.setSelectedPosition(position)
        }
    }
    
    private fun observeLanguage() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedLanguage.collect { languageCode ->
                // Update display if needed
            }
        }
    }
    
    private fun onLanguageSelected(language: Language, position: Int) {
        viewModel.selectLanguage(language.code)
        // Scroll to selected position
        binding.rvLanguages.smoothScrollToPosition(position)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    data class Language(
        val code: String,
        val name: String,
    )
}
