package asset.trak.views.baseclasses

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.markss.rfidtemplate.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    open fun toastMsg(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    protected open fun exitFromApp() {
        val localIntent = Intent("android.intent.action.MAIN")
        localIntent.addCategory("android.intent.category.HOME")
        localIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(localIntent)
        finish()
    }

    fun addFragment(fragmentManager: FragmentManager?, fragment: Fragment, id: Int) {
        fragmentManager?.beginTransaction()
            ?.add(id, fragment, MainActivity.TAG_CONTENT_FRAGMENT)?.commit()
    }

    fun replaceFragment(fragmentManager: FragmentManager?, fragment: Fragment, id: Int) {
        fragmentManager?.beginTransaction()
            ?.replace(id, fragment, MainActivity.TAG_CONTENT_FRAGMENT)?.addToBackStack(null)?.commit()
    }
}