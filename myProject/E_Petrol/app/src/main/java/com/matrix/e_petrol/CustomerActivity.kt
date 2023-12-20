package com.matrix.e_petrol

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.matrix.e_petrol.util.Api
import com.matrix.e_petrol.util.EProgressDialog
import com.matrix.e_petrol.util.LoginManager
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_customer.*
import kotlinx.android.synthetic.main.activity_new_customer.*
import org.json.JSONObject

class CustomerActivity : AppCompatActivity() {

    var customerID:Int=0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        LoginManager.getInstance(applicationContext).ifUserLoggedOut(this)

        customerID=LoginManager.getInstance(applicationContext).customerID

        btnLogout.setOnClickListener(View.OnClickListener {
            LoginManager.getInstance(applicationContext).logout(this@CustomerActivity)
        })

        getCustomerInformation()
    }

    private fun getCustomerInformation(){
        val httpClient= AsyncHttpClient()

        val params= RequestParams()

        httpClient.post(this, Api.getCustomerHome(customerID),params, object : AsyncHttpResponseHandler() {
            override fun onStart() {
                EProgressDialog.showDialog(this@CustomerActivity,"Loading Customer Info")
                super.onStart()
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                if(responseBody==null){
                    Toast.makeText(this@CustomerActivity,"couldn't get response", Toast.LENGTH_SHORT).show()
                    return
                }

                val json= JSONObject(String(responseBody))
                executeResult(json)

                EProgressDialog.hideDialog()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                EProgressDialog.hideDialog()
                Log.e("CustomerActivity",responseBody.toString())
            }
        })
    }

    private fun executeResult(json: JSONObject) {
        val freePetrol=json.getInt(Api.FREE_PETROL)
        val balance=json.getInt(Api.BALANCE)
        val name=json.getString(Api.FULL_NAME)

        txtFullName.text=name
        txtBalance.text=balance.toString()
        txtFree.text=freePetrol.toString()
    }

}
