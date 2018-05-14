package textspammer.connorha.washington.edu.textspammer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val numberToText = intent.getStringExtra("numberToText")
        val messageString = intent.getStringExtra("messageString")
        val formattedPhoneNumber = "(${numberToText.substring(0, 3)}) ${numberToText.substring(3, 6)}-${numberToText.substring(6)}"
        Toast.makeText(context, "$formattedPhoneNumber: $messageString", Toast.LENGTH_SHORT).show()
    }
}