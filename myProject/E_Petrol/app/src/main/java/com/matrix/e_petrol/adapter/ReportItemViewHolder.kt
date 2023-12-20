package com.matrix.e_petrol.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.matrix.e_petrol.R

class ReportItemViewHolder(val view:View): RecyclerView.ViewHolder(view) {
    public val  withdrawnFree:TextView
    public val  withdrawnBalance:TextView
    public val  customerName:TextView
    public val  vehicleNumber:TextView
    init {
         withdrawnFree=view.findViewById(R.id.txtFreePetrolWithdrawn)
         withdrawnBalance=view.findViewById(R.id.txtBalanceWithdrawn)
         customerName=view.findViewById(R.id.txtCustomerName)
         vehicleNumber=view.findViewById(R.id.txtVehicleNumber)
    }
}