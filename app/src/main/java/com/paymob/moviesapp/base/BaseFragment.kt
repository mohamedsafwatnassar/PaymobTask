package com.paymob.moviesapp.base

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.paymob.moviesapp.utils.ViewsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    protected fun showToast(text: String) {
        try {
            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        } catch (ex: Exception) {
            Log.d("Exception", "" + ex.localizedMessage)
        }
    }

    protected fun showLoading() {
        (requireActivity() as ViewsManager).showLoading()
    }

    protected fun hideLoading() {
        (requireActivity() as ViewsManager).hideLoading()
    }

    override fun onStop() {
        super.onStop()
        hideLoading()
    }
}
