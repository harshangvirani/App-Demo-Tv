package com.livestreaming.tv.ui.fragments.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.livestreaming.tv.R
import com.livestreaming.tv.databinding.FragmentProfileMenuBinding
import com.livestreaming.tv.utils.getFocusHighlightListener
import com.livestreaming.tv.utils.log
import com.livestreaming.tv.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileMenuFragment : DialogFragment() {

    private var _binding: FragmentProfileMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileMenuBinding.inflate(layoutInflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            resources.displayMetrics.widthPixels, // full width
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // Align dialog to the right
        dialog?.window?.setGravity(Gravity.END)
        setupView()
    }

    private fun setupView() {
        with(binding) {
            //Btn Settings
            btnSettings.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    binding.btnSettings.strokeWidth = 2
                    binding.btnSettings.strokeColor =
                        ContextCompat.getColor(requireContext(), R.color.white)
                } else {
                    binding.btnSettings.strokeWidth = 0
                }
            }
            btnSettings.setOnClickListener {
                toast("Settings")
            }

            //Btn set on focus change listener
            listOf(tvEditProfile, tvAddProfile, tvRemoveProfile, tvSignOut).forEach {
                it.onFocusChangeListener = getFocusHighlightListener()
            }

            //Btn Edit ptofile
            tvEditProfile.setOnClickListener {
                log("Edit Profile")
            }

            //Btn Add Profile
            tvAddProfile.setOnClickListener {
                log("Add Profile")
            }

            //Btn Remove Profile
            tvRemoveProfile.setOnClickListener {
                log("Remove Profile")
            }

            //Btn Sign Out
            tvSignOut.setOnClickListener {
                log("Sign Out")
            }
        }
    }

    companion object{
        const val TAG = "ProfileMenuFragmentTag"
    }


}