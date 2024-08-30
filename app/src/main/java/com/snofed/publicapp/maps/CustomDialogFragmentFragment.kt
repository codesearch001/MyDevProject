package com.snofed.publicapp.maps

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentCustomDialogFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomDialogFragmentFragment : DialogFragment() {
    private var _binding: FragmentCustomDialogFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_custom_dialog_fragment, container, false)
        _binding = FragmentCustomDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up button click listener
        binding.btnCancel.setOnClickListener {
            // Handle button action
            dismiss() // Close the dialog
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            dialog.window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setBackgroundDrawableResource(android.R.color.transparent) // Optional: makes background transparent
                attributes = attributes.apply {
                    gravity = android.view.Gravity.BOTTOM
                    // Apply the custom animation
                    val slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom)
                    val slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
                    setWindowAnimations(slideOut,slideIn)
                }
            }
        }
        return dialog
    }
    private fun setWindowAnimations(slideIn: Animation, slideOut: Animation) {
        dialog?.window?.apply {
            // Note: setAnimations is a hypothetical function. Android does not support this directly.
            // Instead, you might need to use the `onShowListener` to manually start animations.
            // This part is conceptual and may need adjustments based on actual implementation.
        }
    }
}