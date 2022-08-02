package com.example.upanddowntheriver.Backend;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class Speak {
    TextToSpeech tts;
    boolean ttsInit;
    private static Speak INSTANCE;

    private Speak() {
    }

    public static Speak getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Speak();
        }
        return INSTANCE;
    }

    public void InitTTS(Context ctx) {
        tts = new TextToSpeech(ctx, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    ttsInit = true;
                    tts.setLanguage(Locale.US);
                } else {
                    ttsInit = false;
                }
            }
        });
    }

    public TextToSpeech getTTS() {
        return tts;
    }

    public static void say(String s) {
        Speak speak = Speak.getInstance();
        TextToSpeech t = speak.getTTS();
        t.speak(s, TextToSpeech.QUEUE_FLUSH, null);
    }
}
