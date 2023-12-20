package com.matrix.e_petrol

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.matrix.e_petrol.adapter.ReportAdapter
import com.matrix.e_petrol.util.Api
import com.matrix.e_petrol.util.EProgressDialog
import com.matrix.e_petrol.util.LoginManager
import com.matrix.e_petrol.util.ReportItem
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.activity_smart_cards.*
import org.json.JSONException
import org.json.JSONObject

class ReportActivity : AppCompatActivity() {

    private lateinit var mAdapter:ReportAdapter
    lateinit var reportList:ArrayList<ReportItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        LoginManager.getInstance(applicationContext).ifUserLoggedOut(this)
    }

    override fun onStart() {
        super.onStart()
        //setUpRecyclerView()


    }

    override fun onResume() {
        super.onResume()
        loadSmartCards()
    }

    private fun loadSmartCards() {
        val httpClient=AsyncHttpClient()

        val params=RequestParams()

        httpClient.get(this, Api.REPORT,params, object : AsyncHttpResponseHandler() {
            override fun onStart() {
                EProgressDialog.showDialog(this@ReportActivity,"Loading Customers Information")
                super.onStart()
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                if(responseBody==null){
                    Toast.makeText(this@ReportActivity,"couldn't get response",Toast.LENGTH_SHORT).show()
                    return
                }

                val list= Gson().fromJson<List<ReportItem>>(String(responseBody),object: TypeToken<List<ReportItem>>(){}.type)

                reportList=ArrayList()
                reportList.addAll(list)

                setUpRecyclerView()
                //mAdapter.notifyDataSetChanged()

                EProgressDialog.hideDialog()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                EProgressDialog.hideDialog()
                Log.e("ReportActivity",responseBody.toString())
            }
        })
    }

    private fun executeResult(res: JSONObject) {
        try {

        }catch (ex:JSONException){
            ex.printStackTrace()
        }
    }

    fun setUpRecyclerView() {

        mAdapter= ReportAdapter(reportList,this)

        val layoutManager: RecyclerView.LayoutManager= LinearLayoutManager(this)
        recyclerViewReport.layoutManager=layoutManager
        recyclerViewReport.itemAnimator= DefaultItemAnimator()
        recyclerViewReport.adapter=mAdapter
    }

}
