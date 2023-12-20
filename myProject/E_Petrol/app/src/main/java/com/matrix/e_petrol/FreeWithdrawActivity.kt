package com.matrix.e_petrol

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_balance_withdraw.*
import kotlinx.android.synthetic.main.customer_information.*
import org.json.JSONObject

class FreeWithdrawActivity : AppCompatActivity() {

    var smartCardNumber:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_withdraw)

        LoginManager.getInstance(applicationContext).ifUserLoggedOut(this)

        loadCustomerInformation()

        btnWithdraw.setOnClickListener(View.OnClickListener {

        })
    }


    private fun loadCustomerInformation() {
        smartCardNumber=intent.getIntExtra(Api.SMART_CARD_NUMBER,0)

        getCustomerInformation(smartCardNumber)
    }

    private fun withdraw(amount:Int){
        val httpClient= AsyncHttpClient()

        val params= RequestParams()
        params.put(Api.AMOUNT,amount)
        params.put(Api.SMART_CARD_NUMBER,smartCardNumber)

        httpClient.get(this, Api.WITHDRAW_FREE,params, object : AsyncHttpResponseHandler() {
            override fun onStart() {
                EProgressDialog.showDialog(this@FreeWithdrawActivity,"Loading Customer Info")
                super.onStart()
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                if(responseBody==null){
                    Toast.makeText(this@FreeWithdrawActivity,"couldn't get response", Toast.LENGTH_SHORT).show()
                    return
                }

                val json= JSONObject(String(responseBody))
                executeResultWithdraw(json)

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

    private fun executeResultWithdraw(json: JSONObject) {
        val ok=json.getBoolean(Api.OK)
        val message=json.getString(Api.MESSAGE)

        Toast.makeText(this,message, Toast.LENGTH_LONG).show()

        if(ok){

        }else{

        }
    }

    private fun getCustomerInformation(smartCardNumber: Int) {
        val httpClient= AsyncHttpClient()

        val params= RequestParams()

        httpClient.get(this, Api.getCustomerByCard(smartCardNumber),params, object : AsyncHttpResponseHandler() {
            override fun onStart() {
                EProgressDialog.showDialog(this@FreeWithdrawActivity,"Loading smart cards")
                super.onStart()
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                if(responseBody==null){
                    Toast.makeText(this@FreeWithdrawActivity,"couldn't get response", Toast.LENGTH_SHORT).show()
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
                Log.e("FreeWithdrawActivity",responseBody.toString())
            }
        })
    }

    private fun executeResult(json: JSONObject) {
        val name=json.getString(Api.FULL_NAME)
        val address=json.getString(Api.ADDRESS)
        val place=json.getString(Api.RESIDENCE_PLACE)
        val mobile=json.getDouble(Api.MOBILE_NUMBER)
        val customerID=json.getInt(Api.CUSTOMER_ID)
        val vehicleNumber=json.getInt(Api.VEHICLE_NUMBER)
        val photo=json.getString(Api.PHOTO)
        val cardNumber=json.getInt(Api.SMART_CARD_NUMBER)

        txtAddress.text=address
        txtFullName.text=name
        txtResidencePlace.text=place
        txtVehicleNumber.text=vehicleNumber.toString()
        txtSmartCadNumber.text=cardNumber.toString()
        txtMobileNumber.text=mobile.toString()

        //Glide.with(this).load(Api.getPoto(photo)).into(imgCustomer)
    }

}
