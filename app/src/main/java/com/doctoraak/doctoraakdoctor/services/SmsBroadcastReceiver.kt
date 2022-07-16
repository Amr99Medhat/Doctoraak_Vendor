package com.doctoraak.doctoraakdoctor.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Telephony
import android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION
import android.provider.Telephony.Sms.Intents.getMessagesFromIntent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import java.lang.NumberFormatException

class SmsBroadcastReceiver : BroadcastReceiver()
{
    var listener: SmsListener? = null
    // TODO CHANGE THIS BASED ON Sender Name or Phone;
    private val PROVIDER_NUMBER = "Mandobee"
//    private val PROVIDER_NUMBER = "Doctoraak"


    override fun onReceive(context: Context?, intent: Intent?)
    {
        if (intent == null || intent.action != SMS_RECEIVED_ACTION)
            return

        var smsBody = ""
        var smsSender = ""
        getMessagesFromIntent(intent).forEach {
            smsSender = it.displayOriginatingAddress
            smsBody += it.messageBody
            Log.d("saif", "Sms Code=    ${it.displayMessageBody}")
        }

        listener?.let {
            if (smsSender.trim().toLowerCase() == PROVIDER_NUMBER.toLowerCase())
                listener?.onSmsCodeReceived(extractSmsCode(smsBody.split(" ")))

        }
    }

    private fun extractSmsCode(smsBody: List<String>): String
    {
        smsBody.forEach {
            if (it.trim().isDigitsOnly())
                return it.trim()
        }

        return ""
    }

}

interface SmsListener
{
    fun onSmsCodeReceived(text: String)
}