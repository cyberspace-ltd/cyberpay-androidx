package com.cyberspace.cyberpaysdk.ui.secure3d

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.cyberspace.cyberpaysdk.R
import com.cyberspace.cyberpaysdk.model.Transaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.graphics.Bitmap
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.cyberspace.cyberpaysdk.ui.widget.ProgressDialog

internal class Secure3dFragment constructor(var context: AppCompatActivity, var transaction: Transaction, var listener: OnFinished) : BottomSheetDialogFragment(){

    private lateinit var webView: WebView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var error_page : View
    private lateinit var retry : View
    private lateinit var close : View

    private var isError = false

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val view = LayoutInflater.from(context).inflate(R.layout.secure3d, null)
        dialog.setContentView(view)

        dialog.setOnShowListener {
            // For AndroidX use: com.google.android.material.R.id.design_bottom_sheet
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        this.isCancelable = false
        progressDialog = ProgressDialog(context)

        webView = view.findViewById(R.id.webView)
        error_page = view.findViewById(R.id.error_page)
        retry = view.findViewById(R.id.retry)
        close = view.findViewById(R.id.close)

        close.setOnClickListener { dismiss() }

        retry.setOnClickListener {
            setupWebView(transaction.returnUrl)
        }

        error_page.visibility = View.GONE
        setupWebView(transaction.returnUrl)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(data: String) {
        Log.e("URL", data)
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webView.keepScreenOn = true
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webView.settings.javaScriptEnabled = true // enable
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.allowContentAccess = true
        webView.settings.domStorageEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(true)
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true
        webView.settings.builtInZoomControls = true
        //webView.setWebChromeClient(CyberPayChromeClient(getApplicationContext<Context>()))
        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressDialog.show("Please Wait...")
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                progressDialog.show("Please Wait...")
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                progressDialog.dismiss()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressDialog.dismiss()

               if(!isError){
                   webView.visibility = View.VISIBLE
                   error_page.visibility = View.GONE
               }

                if (url.startsWith("https://payment.staging.cyberpay.ng/notify?ref=")) {
                    webView.destroy()
                    //reference = url.substring(url.lastIndexOf("=") + 1)
                    listener.onFinish(transaction)
                }

                if (url.startsWith("https://payment.staging.cyberpay.ng/pay?reference")) {
                    webView.destroy()
                    listener.onFinish(transaction)
                }

                if (url.startsWith("https://payment.cyberpay.ng/notify?ref=")) {
                    webView.destroy()
                   // reference = url.substring(url.lastIndexOf("=") + 1)
                    listener.onFinish(transaction)
                }

                if (url.startsWith("https://payment.cyberpay.ng/notify?ref=")) {
                    webView.destroy()
                    //reference = url.substring(url.lastIndexOf("=") + 1)
                    listener.onFinish(transaction)
                }
            }

            @SuppressWarnings("deprecation")
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                Log.e("ERROR", description.toString())
                isError = true
                progressDialog.dismiss()
                webView.visibility = View.GONE
                error_page.visibility = View.VISIBLE
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                //swipeRefreshLayout.setRefreshing(false)

                onReceivedError(view, error.errorCode, error.description.toString(), request.url.toString())
            }
        }

        webView.loadUrl(data)
    }


}