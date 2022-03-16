package asset.trak.views.baseclasses

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import asset.trak.views.fragments.HomeFragment
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.home.MainActivity.TAG_CONTENT_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment(@LayoutRes private val layout: Int) : Fragment(layout) {

    private var loaderDialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    open fun stopLoader() {
        if (loaderDialog != null) {
            loaderDialog?.dismiss()
        }
    }

    fun toastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


    private fun setWidthPercentOfDialog(dialog: Dialog) {
        val percentage = 90
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun addFragment(fragmentManager: FragmentManager?, fragment: Fragment, id: Int) {
        fragmentManager?.beginTransaction()
            ?.add(id, fragment, TAG_CONTENT_FRAGMENT)?.commit()
    }

    fun replaceFragment(fragmentManager: FragmentManager?, fragment: Fragment, id: Int) {
        fragmentManager?.beginTransaction()
            ?.replace(id, fragment, TAG_CONTENT_FRAGMENT)?.addToBackStack(null)?.commit()
    }

    fun getBackToPreviousFragment() {
     //   requireActivity().supportFragmentManager.popBackStackImmediate()
        requireActivity().supportFragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE)
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.content_frame, HomeFragment())?.commit()

    }
}