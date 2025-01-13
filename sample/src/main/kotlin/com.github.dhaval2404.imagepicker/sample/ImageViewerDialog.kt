package com.github.dhaval2404.imagepicker.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.github.dhaval2404.imagepicker.sample.databinding.DialogImageviewerBinding

/**
 * Dialog to View Image
 *
 * @author Dhaval Patel
 * @version 1.6
 * @since 05 January 2019
 */
class ImageViewerDialog : DialogFragment() {

    companion object {

        private const val EXTRA_IMAGE_RESOURCE = "extra.image_resource"

        @JvmStatic
        fun newInstance(resource: Int) = ImageViewerDialog().apply {
            arguments = Bundle().apply {
                putInt(EXTRA_IMAGE_RESOURCE, resource)
            }
        }
    }
    private lateinit var  binding: DialogImageviewerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogImageviewerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.codeImg.setImageResource(arguments?.getInt(EXTRA_IMAGE_RESOURCE, 0) ?: 0)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.setLayout(width, height)
            it.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}
