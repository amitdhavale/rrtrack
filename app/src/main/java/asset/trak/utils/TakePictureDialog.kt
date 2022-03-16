package asset.trak.utils

import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import asset.trak.repository.BookRepository
import asset.trak.views.baseclasses.BaseDialogFragment
import com.markss.rfidtemplate.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TakePictureDialog : BaseDialogFragment() {

    private lateinit var camera: AppCompatButton
    private lateinit var gallery: AppCompatButton
    private lateinit var ivCrossTakePic: AppCompatImageView
    @Inject
    lateinit var mProfileRepository: BookRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.transparent
                    )
                )
            )
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val rootView: View =
            inflater.inflate(R.layout.image_picker_dialog, container, true)
        val window: Window? = dialog!!.window
        window?.setGravity(Gravity.CENTER)
        val params: WindowManager.LayoutParams = window?.attributes!!
        params.y = 60
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.dimAmount = 0.50f
        params.flags = params.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        window.attributes = params

        setWidthPercentOfDialog()

        camera = rootView.findViewById(R.id.camera)
        gallery = rootView.findViewById(R.id.galley)
        ivCrossTakePic = rootView.findViewById(R.id.ivCrossTakePic)
        camera.setOnClickListener {
            mProfileRepository.mTakePickAction.postValue("Camera")
            dialog?.dismiss()
        }
        gallery.setOnClickListener {
            mProfileRepository.mTakePickAction.postValue("Gallery")
            dialog?.dismiss()
        }
        ivCrossTakePic.setOnClickListener {
            dialog?.dismiss()
        }
        return rootView
    }

    /**
     * dialog width according to percentage wise of full width of screen
     */
    private fun setWidthPercentOfDialog() {
        val percentage = 90
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}