package com.cyberspace.cyberpaysdk.ui.bank

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cyberspace.cyberpaysdk.R

class BankViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal  var bank: TextView = itemView.findViewById(R.id.bank)

}