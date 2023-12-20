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
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_new_customer.*
import org.json.JSONObject

class NewCustomerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_customer)

        btnSaveCustomer.setOnClickListener(View.OnClickListener {
            createCustomer()
        })
    }

    private fun createCustomer(){
        val httpClient= AsyncHttpClient()

        val params= RequestParams()
        params.put(Api.FULL_NAME,edtFullName.text.toString().trim())
        params.put(Api.ADDRESS,edtAddress.text.toString().trim())
        params.put(Api.RESIDENCE_PLACE,edtResidencePlace.text.toString().trim())
        params.put(Api.MOBILE_NUMBER,edtMobileNumber.text.toString().trim())
        params.put(Api.VEHICLE_NUMBER,edtVehicleNumber.text.toString().trim())
        params.put(Api.SMART_CARD_NUMBER,edtSmartCardNumber.text.toString().trim())


        httpClient.post(this, Api.CREATE_CUSTOMER,params, object : AsyncHttpResponseHandler() {
            override fun onStart() {
                EProgressDialog.showDialog(this@NewCustomerActivity,"Loading Customer Info")
                super.onStart()
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                if(responseBody==null){
                    Toast.makeText(this@NewCustomerActivity,"couldn't get response", Toast.LENGTH_SHORT).show()
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
                Log.e("SmartCardActivity",responseBody.toString())
            }
        })
    }

    private fun executeResult(json: JSONObject) {
        val ok=json.getBoolean(Api.OK)
        val message=json.getString(Api.MESSAGE)

        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        finish()
    }
}
