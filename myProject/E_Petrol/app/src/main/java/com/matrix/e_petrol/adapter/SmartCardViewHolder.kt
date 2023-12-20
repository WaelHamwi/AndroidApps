package com.matrix.e_petrol.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.matrix.e_petrol.R
import kotlinx.android.synthetic.main.item_smart_card.view.*

class SmartCardViewHolder(view:View): RecyclerView.ViewHolder(view) {
    lateinit var smartCard:TextView
    lateinit var btnMoreInfo:Button

    init {
        smartCard=view.findViewById(R.id.txtSmartCadNumber)
        btnMoreInfo=view.findViewById(R.id.btnMoreInformation)
    }
}