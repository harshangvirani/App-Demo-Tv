package com.livestreaming.tv.ui.fragments.subscriptions

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.livestreaming.tv.R
import com.livestreaming.tv.adapter.PlansAdapter
import com.livestreaming.tv.databinding.FragmentSubscriptionBinding
import dagger.hilt.android.AndroidEntryPoint

data class Plan(
    val planName: String,
    val price: String,
    val description: String,
)

val monthlyPlans = listOf(
    Plan("Basic Plan", "$9.99/month", "Access to movies and shows"),
    Plan("Standard Plan", "$14.99/month", "Access to HD movies and shows"),
    Plan("Premium Plan", "$19.99/month", "Access to 4K movies and shows")
)
val yearlyPlans = listOf(
    Plan("Basic", "$199/year", "Save 15%"),
    Plan("Standard", "$1499/year", "Save 20%"),
    Plan("Premium", "$1999/year", "Save 25%")
)

@AndroidEntryPoint
class SubscriptionFragment : Fragment() {

    private var _binding: FragmentSubscriptionBinding? = null
    private val binding get() = _binding!!
    private lateinit var plansAdapter: PlansAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupToggle()
        setupRemoteNavigation()
    }

    private fun setupRecyclerView() {
        plansAdapter = PlansAdapter(monthlyPlans)

        binding.recyclerViewPlans.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = plansAdapter
        }
    }

    // ---------------- TOGGLE LOGIC ----------------
    private fun setupToggle() {
        selectMonthly()
        with(binding) {

            /*
            tvMonthly.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    tvMonthly.setBackgroundResource(R.drawable.bg_toggle_selected)
                } else {
                    tvMonthly.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white_9_percent
                        )
                    )
                }
            }

            tvYearly.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    tvYearly.setBackgroundResource(R.drawable.bg_toggle_selected)
                } else {
                    tvYearly.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white_9_percent
                        )
                    )
                }
            }
             */
            tvMonthly.setOnClickListener {
                selectMonthly()
            }

            tvYearly.setOnClickListener {
                selectYearly()
            }
        }
    }

    private fun selectMonthly() {
        plansAdapter.updatePlans(monthlyPlans)

        binding.tvMonthly.apply {
            setBackgroundResource(R.drawable.bg_toggle_selected)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        binding.tvYearly.apply {
            background = null
            setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
        }
    }

    private fun selectYearly() {
        plansAdapter.updatePlans(yearlyPlans)

        binding.tvYearly.apply {
            setBackgroundResource(R.drawable.bg_toggle_selected)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        binding.tvMonthly.apply {
            background = null
            setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
        }
    }

    // ---------------- DPAD / REMOTE ----------------
    private fun setupRemoteNavigation() {
        binding.toggleGroup.setOnKeyListener { _, keyCode, event ->
            if (event.action != KeyEvent.ACTION_DOWN) return@setOnKeyListener false

            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    selectMonthly()
                    binding.tvMonthly.requestFocus()
                    true
                }

                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    selectYearly()
                    binding.tvYearly.requestFocus()
                    true
                }

                KeyEvent.KEYCODE_DPAD_CENTER,
                KeyEvent.KEYCODE_ENTER,
                    -> {
                    when {
                        binding.tvMonthly.hasFocus() -> selectMonthly()
                        binding.tvYearly.hasFocus() -> selectYearly()
                    }
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
