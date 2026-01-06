package com.livestreaming.tv.ui.fragments.common.languages

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.livestreaming.tv.adapter.LanguageAdapter
import com.livestreaming.tv.databinding.FragmentLanguageSelectionBinding
import com.livestreaming.tv.viewmodels.LanguageSelectionViewModel
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLanguageSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupNextButton()
        observeLanguage()
        handleRemoteControlNavigation()
    }

    private fun setupNextButton() {
        binding.btnNext.setOnClickListener {
            // Get the current selected language
            val snapHelper = LinearSnapHelper()
            val centerView = snapHelper.findSnapView(layoutManager)
            if (centerView != null) {
                val position = binding.rvLanguages.getChildAdapterPosition(centerView)
                if (position != RecyclerView.NO_POSITION && position < languages.size) {
                    val language = languages[position]
                    viewModel.selectLanguage(language.code)
                    findNavController().navigate(LanguageSelectionFragmentDirections.actionLanguageSelectionFragmentToSelectWhatYouSeeFragment())
                }
            }
        }
    }

    private fun setupRecyclerView() {
        layoutManager = CenterLayoutManager(requireContext())
        adapter = LanguageAdapter(languages) { language, position -> onLanguageSelected(language, position) }

        binding.rvLanguages.apply {
            this.layoutManager = this@LanguageSelectionFragment.layoutManager
            this.adapter = this@LanguageSelectionFragment.adapter

            // Add snap helper to center the selected item
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this)

            // Scroll to default position (3rd item, index 2)
            post {
                scrollToPosition(2)
                updateSelectedLanguage(2)
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

    private fun handleRemoteControlNavigation() {
        binding.rvLanguages.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        moveFocusUp()
                        return@setOnKeyListener true
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        moveFocusDown()
                        return@setOnKeyListener true
                    }
                    KeyEvent.KEYCODE_ENTER -> {
                        handleEnterAction()
                        Log.d("Min","Click")
                        return@setOnKeyListener true
                    }
                    else -> return@setOnKeyListener false
                }
            }
            false
        }
    }

    private fun moveFocusUp() {
        val currentPosition = (binding.rvLanguages.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (currentPosition > 0) {
            binding.rvLanguages.smoothScrollToPosition(currentPosition - 1)
            updateSelectedLanguage(currentPosition - 1)
        }
    }

    private fun moveFocusDown() {
        val currentPosition = (binding.rvLanguages.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (currentPosition < languages.size - 1) {
            binding.rvLanguages.smoothScrollToPosition(currentPosition + 1)
            updateSelectedLanguage(currentPosition + 1)
        }
    }

    private fun handleEnterAction() {
        val snapHelper = LinearSnapHelper()
        val centerView = snapHelper.findSnapView(layoutManager)
        if (centerView != null) {
            val position = binding.rvLanguages.getChildAdapterPosition(centerView)
            if (position != RecyclerView.NO_POSITION) {
                val language = languages[position]
                viewModel.selectLanguage(language.code)
                findNavController().navigate(LanguageSelectionFragmentDirections.actionLanguageSelectionFragmentToSelectWhatYouSeeFragment())
            }
        }
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
