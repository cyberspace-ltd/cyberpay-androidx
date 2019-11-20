package com.cyberspace.cyberpaysdk.ui.bank

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cyberspace.cyberpaysdk.R
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse

internal class BankAdapter(internal var mContext: Context, list: MutableList<BankResponse>) : RecyclerView.Adapter<BankViewHolder>() {

    private var banks: MutableList<BankResponse> = list

    interface OnClickListener{
        fun onClick(bank : BankResponse)
    }

    var listener : OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bank_item, parent, false)
        return BankViewHolder(view)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {

        holder.bank.text = banks[position].bankName
        holder.bank.setOnClickListener {
            if(listener!=null) listener?.onClick(banks[position])
        }
    }

    override fun getItemCount(): Int {
        return banks.size
    }
}
