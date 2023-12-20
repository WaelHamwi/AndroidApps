package com.matrix.e_petrol.util

public class Api{
    companion object{
        fun getLogin(): String {
            return URL+"Login"
        }

        fun getPoto(photo: String?): String {
            return "http://epetrols19.somee.com/"+photo
        }

        val OK="Ok"

        val AMOUNT="Amount"

        fun getCustomerHome(customerID:Int):String {
            return URL + "CustomerHome?CustomerID=" + customerID
        }

        fun getCustomerByCard(smartCardNumber: Int): String? {
            return URL+"GetCustomerByCard?SmartCardNumber="+smartCardNumber
        }



        val CREATE_CUSTOMER:String
        get() = URL+"CreateCustomer"

        val WITHDRAW_BALANCE:String
        get() = URL+"WithdrawBalance"

        val WITHDRAW_FREE:String
            get() = URL+"WithdrawFree"

        val SMART_CARDS:String
        get() = URL+"EmployeeHome"

        val REPORT:String
        get() = URL+"IssueReport"

        val URL="http://epetrols19.somee.com/Android/"
        val USERNAME="Username"
        val PASSWORD="Password"


        val MESSAGE="Message"
        val ROLE="Role"

        val SMART_CARD_NUMBER="SmartCardNumber"

        val CUSTOMER_BY_CARD:String
        get() = URL+"GetCustomerByCard"

        val CUSTOMER_ID="CustomerID"
        val FULL_NAME="FullName"
        val ADDRESS="Address"
        val RESIDENCE_PLACE="ResidencePlace"
        val MOBILE_NUMBER="MobileNumber"
        val VEHICLE_NUMBER="VehicleNumber"
        val PHOTO="Photo"

        val FREE_PETROL="FreePetrol"
        val BALANCE="Balance"
    }

    public class Role{
        companion object{
            val NO_ROLE=0
            val ADMIN = 1
            val EMP = 2
            val CST = 3
        }
    }
}