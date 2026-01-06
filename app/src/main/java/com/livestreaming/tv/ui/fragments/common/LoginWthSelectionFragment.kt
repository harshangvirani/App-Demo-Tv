package com.livestreaming.tv.ui.fragments.common

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.livestreaming.tv.databinding.FragmentLoginWthSelectionBinding
import dagger.hilt.android.AndroidEntryPoint
import com.livestreaming.tv.R

@AndroidEntryPoint
class LoginWthSelectionFragment : Fragment() {

    private var _binding: FragmentLoginWthSelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedCard: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginWthSelectionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set initial selection
        selectedCard = binding.cardScanQr
        updateCardSelection(binding.cardScanQr, true)
        updateCardSelection(binding.cardMobile, false)

        // Make cards focusable for TV remote
        binding.cardScanQr.isFocusable = true
        binding.cardScanQr.isFocusableInTouchMode = true
        binding.cardMobile.isFocusable = true
        binding.cardMobile.isFocusableInTouchMode = true

        setupCardFocus(binding.cardScanQr)
        setupCardFocus(binding.cardMobile)

        // Request focus on first card
        binding.cardScanQr.post {
            binding.cardScanQr.requestFocus()
        }
    }

    private fun setupCardFocus(card: CardView) {
        // Touch/Mouse click
        card.setOnClickListener {
            selectCard(card)
            navigateAccordingToSelection(card)
        }

        // TV Remote D-pad CENTER/ENTER
        card.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_DPAD_CENTER,
                    KeyEvent.KEYCODE_ENTER -> {
                        selectCard(card)
                        navigateAccordingToSelection(card)
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }

        // Focus change listener for scale animation and selection
        card.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(150).start()
                selectCard(card)
            } else {
                v.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
            }
        }
    }

    private fun selectCard(card: CardView) {
        if (selectedCard == card) return

        // Deselect previous card
        updateCardSelection(selectedCard, false)

        // Select new card
        selectedCard = card
        updateCardSelection(card, true)
    }

    private fun updateCardSelection(card: CardView, isSelected: Boolean) {
        val materialCard = card as? com.google.android.material.card.MaterialCardView
        card.isSelected = isSelected
        if (isSelected) {
            // Selected: Transparent background, White Border
            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
            materialCard?.strokeColor = ContextCompat.getColor(requireContext(), R.color.white)
            materialCard?.strokeWidth = 4
            card.cardElevation = 16f
        } else {
            // Unselected: Transparent background, No Border
            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
            materialCard?.strokeColor = ContextCompat.getColor(requireContext(), android.R.color.transparent)
            materialCard?.strokeWidth = 0
            card.cardElevation = 4f
        }
    }

    private fun navigateAccordingToSelection(card: CardView) {
        when (card.id) {
            R.id.card_scan_qr -> {
                findNavController().navigate(R.id.action_loginWthSelectionFragment_to_loginWithQrFragment)
            }
            R.id.card_mobile -> {
                findNavController().navigate(R.id.action_loginWthSelectionFragment_to_loginWihtPhoneScreenFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}