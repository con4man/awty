package textspammer.connorha.washington.edu.textspammer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var numberToText = ""
    private var spammerFrequency = 0
    private var messageString = ""
    private var appIsRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeSwitchButton()
        setNumberTextOnChangeListener()
        setFrequencyTextOnChangeListener()
        setMessageTextOnChangeListener()
        setSwitchButtonOnClickListener()
    }

    private fun initializeSwitchButton() {
        changeSwitchButtonText()
        enableSwitchButton()
    }

    private fun setNumberTextOnChangeListener() {
        editTextMessage.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                numberToText = editTextNumber.text.toString()
                enableStartButtonIfRequiredFieldsAreValid()
            }
        })
    }

    private fun setFrequencyTextOnChangeListener() {
        editTextMessage.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                spammerFrequency = editTextFrequency.text.toString().toInt()
                enableStartButtonIfRequiredFieldsAreValid()
            }
        })
    }

    private fun setMessageTextOnChangeListener() {
        editTextMessage.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                messageString = editTextMessage.text.toString()
                enableStartButtonIfRequiredFieldsAreValid()
            }
        })
    }

    private fun setSwitchButtonOnClickListener() {
        buttonSwitch.setOnClickListener{
            if (smsPermissionIsGranted()) {
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)
                if (!appIsRunning) {
                    intent.putExtra("numberToText", numberToText)
                    intent.putExtra("messageString", messageString)
                    //converts frequency from minutes to milliseconds
                    val alarmFrequency = spammerFrequency.toString().toLong() * 1000 * 60
                    val alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmFrequency, alarmIntent)
                    appIsRunning = true
                } else {
                    val alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                    alarmManager.cancel(alarmIntent)
                    appIsRunning = false
                }
                changeSwitchButtonText()
            }
        }
    }

    private fun enableSwitchButton() {
        buttonSwitch.isEnabled = true
    }

    private fun disableSwitchButton() {
        buttonSwitch.isEnabled = false
    }

    private fun changeSwitchButtonText() {
        when (appIsRunning) {
            true -> buttonSwitch.text = getString(R.string.stop)
            false -> buttonSwitch.text = getString(R.string.start)
        }
    }

    private fun enableStartButtonIfRequiredFieldsAreValid() {
        if (numberIsValid() && spammerFrequencyIsValid()&& messageString.isNotEmpty()) {
            enableSwitchButton()
        } else {
            disableSwitchButton()
        }
    }

    private fun numberIsValid() : Boolean {
        return numberToText.length == 10
    }

    private fun spammerFrequencyIsValid() :  Boolean {
        return spammerFrequency in 1..100
    }

    private fun smsPermissionIsGranted () : Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //requests permission to send sms
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 1)
            return false
        } else {
            return true
        }
    }

}
