package com.matrix.e_petrol

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.matrix.e_petrol.util.Api
import com.matrix.e_petrol.util.EProgressDialog
import com.matrix.e_petrol.util.LoginManager
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoginManager.getInstance(applicationContext).checkLogin(this);

        btnLogin.setOnClickListener(View.OnClickListener {
            val httpClient=AsyncHttpClient()

            var params=RequestParams()
            params.add(Api.USERNAME,edtUsername.text.toString().trim())
            params.add(Api.PASSWORD,edtPassword.text.toString())

            httpClient.post(applicationContext,Api.getLogin(),params, object : AsyncHttpResponseHandler() {
                override fun onStart() {
                    EProgressDialog.showDialog(this@MainActivity,"Logging in")
                    super.onStart()
                }

                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {



                    if(responseBody!=null) {
                        Log.e("MainActivity",String(responseBody))
                        val jsonObject = JSONObject(String(responseBody))
                        executeResult(jsonObject)
                    }

                    EProgressDialog.hideDialog()
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    EProgressDialog.hideDialog()
                    error?.printStackTrace()
                }
            })

        })
    }

    private fun executeResult(res: JSONObject) {
        try {
            var customerID=0;
            var message="";
            val role=res.getInt(Api.ROLE)

            if(role==Api.Role.CST)
                customerID=res.getInt(Api.CUSTOMER_ID)

            if(role==Api.Role.NO_ROLE)
                message=res.getString(Api.MESSAGE)

            LoginManager.getInstance(applicationContext).login(this,role,customerID,message)
        }catch (ex:JSONException){
            Log.e("Login",ex.message)
        }
    }
}
