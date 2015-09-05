package com.myniotech.motor;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.service.PdService;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;

import AndGraph.AGGameManager;
import AndGraph.AGInputManager;


public class GameActivity extends Activity {

    private AGGameManager vrManager = null;

    public static final int INTRO = 0;
    public static final int MENU = 1;
    public static final int GAME = 2;


    private static final String TAG = "motor";

    private PdService pdService = null;

    private final ServiceConnection pdConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pdService = ((PdService.PdBinder) service).getService();
            try {
                initPd();
                loadPatch1();

            } catch (IOException e) {
                Log.e(TAG, e.toString());
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // this method will never be called
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vrManager = new AGGameManager(this,true);

        vrManager.addScene(new Intro(vrManager));
        vrManager.addScene(new Menu(vrManager));
        vrManager.addScene(new Game(vrManager));

        initSystemServices();
        bindService(new Intent(this, PdService.class), pdConnection,
                BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(pdConnection);
        vrManager.release();
        vrManager = null;
        System.gc();

    }



    private void initPd() throws IOException {
        // Configure the audio glue
        AudioParameters.init(this);
        int sampleRate = AudioParameters.suggestSampleRate();
        pdService.initAudio(sampleRate, 0, 2, 10.0f);
        start();

        // Create and install the dispatcher

    }

    private void start() {
        if (!pdService.isRunning()) {
            Intent intent = new Intent(GameActivity.this, GameActivity.class);
            pdService.startAudio(intent, R.drawable.ic_launcher, "Motor",
                    "Return to Motor");
        }
    }

    private void loadPatch1() throws IOException {
        File dir = getFilesDir();
        IoUtils.extractZipResource(getResources().openRawResource(R.raw.motorbike),
                dir, true);
        File patchFile = new File(dir, "motorbike.pd");
        PdBase.openPatch(patchFile.getAbsolutePath());
    }


    // for call interruption
    private void initSystemServices() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (pdService == null)
                    return;
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    start();
                } else {
                    pdService.stopAudio();
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }


    //Metodo chamado no momento em que a Activity vai para segundo plano
    public void onPause() {
        super.onPause();
        vrManager.onPause();
    }

    //Testa o botao de voltar
    public void onBackPressed() {
        AGInputManager.vrTouchEvents.bBackButtonClicked = true;
    }

    //Metodo chamado no momento em que a Activity volta do segundo plano
    public void onResume() {
        super.onResume();
        vrManager.onResume();
    }

}

