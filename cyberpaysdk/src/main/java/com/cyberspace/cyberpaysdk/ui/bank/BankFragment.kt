package com.cyberspace.cyberpaysdk.ui.bank

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cyberspace.cyberpaysdk.R
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

internal class BankFragment (var context: AppCompatActivity, var banks: MutableList<BankResponse>, var listener  : OnSelected) : BottomSheetDialogFragment(){

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var close : View

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        //Set the custom view
        val view = LayoutInflater.from(context).inflate(R.layout.banks, null)
        dialog.setContentView(view)

        dialog.setOnShowListener {
            // For AndroidX use: com.google.android.material.R.id.design_bottom_sheet
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

       // this.isCancelable = false
        close = view.findViewById(R.id.close)
        close.setOnClickListener {
            dismiss()
        }

        mRecyclerView = view.findViewById(R.id.list)

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = BankAdapter(context,banks)

        adapter.listener = object : BankAdapter.OnClickListener {
            override fun onClick(bank: BankResponse) {
                listener.onSelect(bank)
                dismiss()
            }
        }

        mRecyclerView.adapter = adapter

    }
}