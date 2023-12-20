package com.matrix.e_petrol

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import com.matrix.e_petrol.adapter.ReportAdapter
import com.matrix.e_petrol.adapter.SmartCardsAdapter
import com.matrix.e_petrol.util.*
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_smart_cards.*
import org.json.JSONException
import org.json.JSONObject

class SmartCardsActivity : AppCompatActivity() {

    private lateinit var mAdapter:SmartCardsAdapter
    lateinit var smartCardList:ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_cards)

        LoginManager.getInstance(applicationContext).ifUserLoggedOut(this@SmartCardsActivity)

        setUpRecyclerView()

        loadSmartCards()
    }

    private fun loadSmartCards() {
        val httpClient= AsyncHttpClient()

        val params= RequestParams()

        httpClient.get(this, Api.SMART_CARDS,params, object : AsyncHttpResponseHandler() {
            override fun onStart() {
                EProgressDialog.showDialog(this@SmartCardsActivity,"Loading smart cards")
                super.onStart()
            }

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                if(responseBody==null){
                    Toast.makeText(this@SmartCardsActivity,"couldn't get response", Toast.LENGTH_SHORT).show()
                    return
                }

                val list= Gson().fromJson<List<Int>>(String(responseBody),object: TypeToken<List<Int>>(){}.type)
                smartCardList.addAll(list)
                mAdapter.notifyDataSetChanged()

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

    private fun executeResult(res: JSONObject) {
        try {

        }catch (ex: JSONException){
            ex.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menuLogout -> {
                LoginManager.getInstance(applicationContext).logout(this@SmartCardsActivity)
            }
        }
        return true
    }

    fun setUpRecyclerView() {
        smartCardList=ArrayList()
        mAdapter= SmartCardsAdapter(smartCardList,this)

        val layoutManager= LinearLayoutManager(this)
        recyclerSmartCards.layoutManager=layoutManager
        recyclerSmartCards.itemAnimator= DefaultItemAnimator()
        recyclerSmartCards.adapter=mAdapter
    }
}
