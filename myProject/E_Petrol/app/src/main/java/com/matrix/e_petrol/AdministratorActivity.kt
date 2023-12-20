package com.matrix.e_petrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.matrix.e_petrol.util.LoginManager
import kotlinx.android.synthetic.main.activity_administrator.*

class AdministratorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrator)

        LoginManager.getInstance(applicationContext).ifUserLoggedOut(this)

        btnAddCustomer.setOnClickListener(View.OnClickListener {
            val i= Intent(this@AdministratorActivity,NewCustomerActivity::class.java)
            startActivity(i)
        })

        btnViewReport.setOnClickListener(View.OnClickListener {
            val i=Intent(this@AdministratorActivity,ReportActivity::class.java)
            startActivity(i)
        })

        btnLogout.setOnClickListener(View.OnClickListener {
            LoginManager.getInstance(applicationContext).logout(this@AdministratorActivity)
        })
    }
}
