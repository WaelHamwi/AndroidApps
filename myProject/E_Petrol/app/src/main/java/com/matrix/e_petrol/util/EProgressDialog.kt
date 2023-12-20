package com.matrix.e_petrol.util

import android.app.ProgressDialog
import android.content.Context

public class EProgressDialog{


    companion object{
        lateinit var pDialog:ProgressDialog

        fun showDialog(context: Context,message:String){
            pDialog=ProgressDialog(context);
            pDialog.setMessage(message);
            pDialog.show();
        }

        fun hideDialog(){
            pDialog.hide();
        }


    }
}