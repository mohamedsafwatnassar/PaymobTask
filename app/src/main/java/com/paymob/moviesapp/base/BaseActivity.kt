package com.paymob.moviesapp.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.paymob.moviesapp.R
import com.paymob.moviesapp.utils.NetworkConnectivity
import com.paymob.moviesapp.utils.SnackBarNetworkConnectivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity(), SnackBarNetworkConnectivity {

    private lateinit var networkConnectionManager: ConnectivityManager
    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConnectivityManager()
    }

    private fun initConnectivityManager() {
        networkConnectionManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkConnectionCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                invokeRetryNetworkConnection()
                hideSnackBar()
            }

            override fun onLost(network: Network) {
                showSnackBar()
            }
        }
        networkConnectionManager.registerDefaultNetworkCallback(networkConnectionCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            networkConnectionManager.unregisterNetworkCallback(networkConnectionCallback)
        } catch (ex: Exception) {
            println(ex)
        }
    }


    fun showSnackBar() {
        try {
            snackbar = Snackbar.make(
                findViewById(R.id.container),
                getString(R.string.no_internet),
                Snackbar.LENGTH_INDEFINITE,
            ).setActionTextColor(ContextCompat.getColor(this, R.color.red))
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                // .setBackgroundTint(ContextCompat.getColor(this, R.color.white))
                .setAction(getString(R.string.retry)) { invokeRetryNetworkConnection() }

            snackbar?.show()
        } catch (ex: Exception) {
            println(ex)
        }
    }

    private fun hideSnackBar() {
        if (snackbar != null) {
            if (snackbar!!.isShown) {
                snackbar!!.dismiss()
            }
        }
    }

    private fun invokeRetryNetworkConnection() {
        try {
            val fragment =
                supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.get(
                    0
                ) as NetworkConnectivity
            fragment.retryNetworkConnectionCallBack()
        } catch (ex: Exception) {
            println(ex)
        }
    }

    override fun showSnackBarNetworkConnection() {
        showSnackBar()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val isSnackBarVisible = snackbar?.isShown ?: false
        // If SnackBar is visible and the touch event is outside the Snackbar bounds,
        // consume the touch event to disable background clicks
        if (isSnackBarVisible && ev?.action == MotionEvent.ACTION_DOWN) {
            val snackBarView = snackbar?.view
            if (snackBarView != null && !isTouchInsideSnackBarView(ev, snackBarView)) {
                return true
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isTouchInsideSnackBarView(ev: MotionEvent, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = ev.rawX
        val y = ev.rawY
        return x > location[0] && x < location[0] + view.width && y > location[1] && y < location[1] + view.height
    }
}