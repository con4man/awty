package textspammer.connorha.washington.edu.textspammer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast
import android.app.PendingIntent

class AlarmReceiver : BroadcastReceiver() {
    private val maxSmsLength = 160
    private val smsSent = "SMS_SENT"
    private val smsDelivered = "SMS_DELIVERED"
    override fun onReceive(context: Context, intent: Intent) {
        val numberToText = intent.getStringExtra("numberToText")
        val messageString = intent.getStringExtra("messageString")
        val smsManager = SmsManager.getDefault()
        val piSent = PendingIntent.getBroadcast(context, 0, Intent(smsSent), 0)
        val piDelivered = PendingIntent.getBroadcast(context, 0, Intent(smsDelivered), 0)

        try {
            if (messageString.length < maxSmsLength) {
                smsManager.sendTextMessage(numberToText, null, messageString, piSent, piDelivered)
                Toast.makeText(context, "SMS sent.",
                        Toast.LENGTH_LONG).show()
            } else {
                val messagePartsList = smsManager.divideMessage(messageString)
                smsManager.sendMultipartTextMessage(numberToText, null, messagePartsList, null, null)
                Toast.makeText(context, "SMS sent.",
                        Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Something went wrong when sending SMS", Toast.LENGTH_LONG).show()
        }
    }
}