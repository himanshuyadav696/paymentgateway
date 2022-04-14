package com.example.razorpaymentsample

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class MainActivity : AppCompatActivity() ,PaymentResultListener{
    var amountTotal:Double=0.0
    val TAG:String = MainActivity::class.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Checkout.preload(applicationContext)

        val amountgiven:EditText= findViewById(R.id.amount)
        val buttonPay:Button =findViewById(R.id.payBtn)
        buttonPay.setOnClickListener {
            var amount:String =amountgiven.text.toString()
            if(amount!=""){
                amountTotal =amount.toDouble()
                startPayment()
            }

        }

    }

    private fun startPayment() {
        /*
* You need to pass current activity in order to let Razorpay create CheckoutActivity
* */
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","App name")
            options.put("description","Demo Payment")
//You can omit the image option to fetch the image from dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency","INR")
            options.put("amount",amountTotal*100)

            val prefill = JSONObject()
            prefill.put("email","test@razorpay.com")
            prefill.put("contact","9876543210")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    override fun onPaymentError(errorCode: Int, response: String?) {
        try{
            startActivity(Intent(this,Failure::class.java))
            finish()
           // Toast.makeText(this,"Payment failed $errorCode \n $response",Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Log.e(TAG,"Exception in onPaymentSuccess", e)
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        try{
            startActivity(Intent(this,Success::class.java))
            finish()
            //Toast.makeText(this,"Payment Successful $razorpayPaymentId",Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Log.e(TAG,"Exception in onPaymentSuccess", e)
        }
    }
}