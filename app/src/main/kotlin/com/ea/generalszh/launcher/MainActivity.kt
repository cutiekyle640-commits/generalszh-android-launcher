package com.ea.generalszh.launcher

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.*
import android.view.WindowManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Full-screen immersive
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )

        webView = WebView(this).also { setContentView(it) }

        webView.settings.apply {
            javaScriptEnabled         = true
            domStorageEnabled         = true
            allowFileAccess           = true
            allowContentAccess        = true
            setSupportZoom(false)
            builtInZoomControls       = false
            displayZoomControls       = false
            loadWithOverviewMode      = true
            useWideViewPort           = true
            cacheMode                 = WebSettings.LOAD_DEFAULT
        }

        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView, request: WebResourceRequest
            ) = false
        }

        webView.loadUrl("file:///android_asset/index.html")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}
