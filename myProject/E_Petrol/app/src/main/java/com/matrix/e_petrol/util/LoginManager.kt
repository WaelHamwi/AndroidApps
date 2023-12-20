package com.matrix.e_petrol.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.content.IntentCompat
import com.matrix.e_petrol.*

/**
 * Created by Matrix on 12/11/2018.
 */

class LoginManager private constructor(val context: Context) {

    private val sharedPref: SharedPreferences

    internal var editor: SharedPreferences.Editor

    val isAdmin: Boolean
        get() = sharedPref.getBoolean(IS_ADMIN, false)

    val isEmp: Boolean
        get() = sharedPref.getBoolean(IS_EMP, false)

    val isCst: Boolean
        get() = sharedPref.getBoolean(IS_CST, false)

    val customerID: Int
        get() = sharedPref.getInt(CUSTOMER_ID, 0)


    val SHARED_PREF_NAME = "epetrol"
    val PRIVATE_MODE = 0

    init {
        sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE)
        editor = sharedPref.edit()
    }

    companion object : SingletonHolder<LoginManager, Context>(::LoginManager)

    //logout
    fun clearAndLogout() {
        editor.clear()
        editor.commit()

        //go to lgoin
        //startLoginActivity()
    }

    fun login(activity: Activity,role: Int, customerID: Int,message:String) {

        when (role) {
            ADMIN -> {
                putRole(true, false, false)
                startAdminActivity(activity)
            }
            EMP -> {
                putRole(false, true, false)
                startEmpActivity(activity)
            }
            CST -> {
                putRole(false, false, true)
                    editor.putInt(CUSTOMER_ID, customerID)
                    editor.commit()
                startCustomerActivity(activity)

            }
            NO_ROLE->{
                EProgressDialog.showDialog(context,message)
            }
        }
        //startMainActivity();
    }

    private fun startCustomerActivity(activity: Activity) {
        val i=Intent(activity,CustomerActivity::class.java)
        context.startActivity(i)
        activity.finish();
    }

    private fun startEmpActivity(activity: Activity) {
        val i=Intent(activity,SmartCardsActivity::class.java)
        context.startActivity(i)
        activity.finish();
    }

    private fun startAdminActivity(activity: Activity) {
        val i=Intent(activity,AdministratorActivity::class.java)
        context.startActivity(i)
        activity.finish();
    }

    private fun putRole(admin: Boolean, emp: Boolean, cst: Boolean) {
        editor.putBoolean(IS_LOGIN,true)

        editor.putBoolean(IS_ADMIN, admin)
        editor.putBoolean(IS_EMP, emp)
        editor.putBoolean(IS_CST, cst)
        editor.commit()
    }

    private fun removeRole() {
        editor.putBoolean(IS_LOGIN,false)

        editor.putBoolean(IS_ADMIN, false)
        editor.putBoolean(IS_EMP, false)
        editor.putBoolean(IS_CST, false)
        editor.commit()
    }

    private fun startLoginActivity(activity: Activity) {

        val i = Intent(context, MainActivity::class.java)
        //i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        i.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.putExtra("EXIT",true)
        context.startActivity(i)

        activity.finish()
    }

    /*
    private void startMainActivity(){

        Intent i=new Intent(context, SecondActivity.class);
        //i.putExtra(VehicleLog.PLUGED_NUMBER,getPlugedNumber());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
*/
    fun checkLogin(activity: Activity) {
        val isLogin = sharedPref.getBoolean(IS_LOGIN, false)

        if (isLogin) {
           if(isAdmin)
               startAdminActivity(activity)
            else if(isEmp)
               startEmpActivity(activity)
            else
               startCustomerActivity(activity)
        }
    }

    public fun ifUserLoggedOut(activity: Activity) {
        val isLogin = sharedPref.getBoolean(IS_LOGIN, false)

        if (!isLogin) {
            startLoginActivity(activity)
        }
    }


    fun logout(activity: Activity) {
        removeRole()

        startLoginActivity(activity)
    }


        val NO_ROLE=0
        val ADMIN = 1
        val EMP = 2
        val CST = 3

        val IS_ADMIN = "is admin"
        val IS_EMP = "is emp"
        val IS_CST = "is cst"

        val CUSTOMER_ID = "customerID"

        private val IS_LOGIN = "login"

}
