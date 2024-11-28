package com.example.smsvoicealert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class SmsReceiver extends BroadcastReceiver {
    private TextToSpeech textToSpeech;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String msgFrom;
        String msgBody = "";

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    msgFrom = msgs[i].getOriginatingAddress();
                    msgBody += msgs[i].getMessageBody();
                }
                textToSpeech = new TextToSpeech(context, status -> {
                    if (status == TextToSpeech.SUCCESS) {
                        textToSpeech.setLanguage(Locale.ENGLISH);
                        String alert = "Alert: " + msgBody;
                        for (int i = 0; i < 3; i++) {
                            textToSpeech.speak(alert, TextToSpeech.QUEUE_ADD, null, null);
                        }
                    } else {
                        Log.e("TTS", "Initialization failed.");
                    }
                });
                
            }
        }
    }
}
