package com.matrix.e_petrol.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matrix.e_petrol.BalanceWithdrawActivity
import com.matrix.e_petrol.R
import com.matrix.e_petrol.util.Api
import com.matrix.e_petrol.util.ReportItem
import com.matrix.e_petrol.util.SmartCardItem

class SmartCardsAdapter(var list:List<Int>, val context: Context): RecyclerView.Adapter<SmartCardViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SmartCardViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.item_smart_card,p0,false)

        return SmartCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(v: SmartCardViewHolder, i: Int) {
        val item=list.get(i)

        v.smartCard.text=item.toString()
        v.btnMoreInfo.setOnClickListener(View.OnClickListener {
            val intent= Intent(context,BalanceWithdrawActivity::class.java)
            intent.putExtra(Api.SMART_CARD_NUMBER,item)
            context.startActivity(intent)
        })
    }
}