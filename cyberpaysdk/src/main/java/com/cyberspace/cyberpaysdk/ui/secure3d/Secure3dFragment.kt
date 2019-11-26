package com.cyberspace.cyberpaysdk.ui.secure3d

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.cyberspace.cyberpaysdk.R
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.CardTransaction
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.ui.widget.ProgressDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


internal class Secure3dFragment constructor(var context: AppCompatActivity, var transaction: Transaction, var formData : Any?,  var listener: OnFinished) : BottomSheetDialogFragment(){

    private lateinit var webView: WebView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var errorPage : View
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
        errorPage = view.findViewById(R.id.error_page)
        retry = view.findViewById(R.id.retry)
        close = view.findViewById(R.id.close)

        close.setOnClickListener {
            dismiss()
        }

        retry.setOnClickListener {
            setupWebView(transaction.returnUrl)
        }

        errorPage.visibility = View.GONE
        setupWebView(transaction.returnUrl)
    }

    override fun dismiss() {
        super.dismiss()
        listener.onCancel()
    }



    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(data: String) {
        Log.e("URL", data)
        webView.keepScreenOn = true
        webView.settings.javaScriptEnabled = true // enable
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.allowContentAccess = true
        //webView.settings.domStorageEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(true)
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        //webView.settings.domStorageEnabled = true
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                if(newProgress >=100) progressDialog.dismiss()

            }



        }
        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressDialog.show("Please Wait...")
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)

                progressDialog.show("Please Wait...")
                val timerThread: Thread = object : Thread() {
                    override fun run() {
                        try {
                            sleep(3000)
                        } catch (e: InterruptedException) {
                        } finally {
                            progressDialog.dismiss()
                        }
                    }
                }
                timerThread.start()
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


                if (!isError) {
                    webView.visibility = View.VISIBLE
                    errorPage.visibility = View.GONE
                }

                if (url.startsWith("https://payment.staging.cyberpay.ng/notify?ref=")) {
                    webView.destroy()
                    //reference = url.substring(url.lastIndexOf("=") + 1)
                    listener.onFinish(transaction)
                    dialog?.dismiss()
                }

                if (url.startsWith("https://payment.staging.cyberpay.ng/pay?reference")) {
                  //  webView.destroy()
                  //  listener.onFinish(transaction)
                 //   dialog?.dismiss()
                }

                if (url.startsWith("https://payment.cyberpay.ng/notify?ref=")) {
                    webView.destroy()
                    // reference = url.substring(url.lastIndexOf("=") + 1)
                    listener.onFinish(transaction)
                    dialog?.dismiss()
                }

                if (url.startsWith("https://payment.staging.cyberpay.ng/url?ref")) {
                    webView.destroy()
                    // reference = url.substring(url.lastIndexOf("=") + 1)
                    listener.onFinish(transaction)
                    dialog?.dismiss()
                }


                if (url.startsWith("https://payment.cyberpay.ng/notify?ref=")) {
                    webView.destroy()
                    //reference = url.substring(url.lastIndexOf("=") + 1)
                    listener.onFinish(transaction)
                    dialog?.dismiss()
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
                errorPage.visibility = View.VISIBLE
            }

            @TargetApi(Build.VERSION_CODES.M)
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


        if(formData==null) webView.loadUrl(data)
        else {
            val param = formData as CardTransaction
            val form = String.format("TermUrl=%s&MD=%s&PaReq=%s",param.termUrl, param.md, param.paReq)//termUrl, md, paReq

            /*
            val html = "<!DOCTYPE html>" +
                    "<html>" +
                    "<body onload='document.frm1.submit()'>" +
                    "<form action="+param.acsUrl+" method='post' name='frm1'>" +
                    "  <input type='hidden' name='TermUrl' value="+param.termUrl+"><br>" +
                    "  <input type='hidden' name='MD' value="+param.md+"><br>" +
                    "  <input type='hidden' name='PaReq' value="+param.paReq+"><br>" +
                    "</form>" +
                    "</body>" +
                    "</html>"

                    webView.loadData(html, "text/html", "UTF-8");
             */
             webView.postUrl(param.acsUrl, form.toByteArray())
            //webView.loadUrl("https://payment.staging.cyberpay.ng/pay?reference=JAG000001261119447772")
        }
    }


}