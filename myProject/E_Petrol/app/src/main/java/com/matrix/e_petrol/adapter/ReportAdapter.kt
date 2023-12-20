package com.matrix.e_petrol.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matrix.e_petrol.R
import com.matrix.e_petrol.util.ReportItem

class ReportAdapter(var list:List<ReportItem>, val context:Context): RecyclerView.Adapter<ReportItemViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ReportItemViewHolder {
        val view:View=LayoutInflater.from(context).inflate(R.layout.item_report,p0,false)

        return ReportItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(v: ReportItemViewHolder, i: Int) {
        val item=list.get(i)

        v.customerName.text=item.customerName
        v.vehicleNumber.text=item.vehicleNumber.toString()
        v.withdrawnBalance.text=item.balanceWithdrown.toString()
        v.withdrawnFree.text=item.freeWithdrown.toString()
    }
}

