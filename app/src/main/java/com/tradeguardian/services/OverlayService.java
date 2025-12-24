package com.tradeguardian.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.View;

import com.tradeguardian.R;

public class OverlayService extends Service {

    public static final String ACTION_START_FLOW = "START_FLOW";
    private static final String TAG = "OverlayService";

    private WindowManager wm;
    private View overlay;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) return START_NOT_STICKY;

        if (ACTION_START_FLOW.equals(intent.getAction())) {
            showOverlay();
        }

        return START_NOT_STICKY;
    }

    private void showOverlay() {

        if (overlay != null) return;

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        overlay = LayoutInflater.from(this)
                .inflate(R.layout.overlay_outside_killzone, null);

        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                                : WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                        PixelFormat.TRANSLUCENT
                );

        wm.addView(overlay, params);

        overlay.findViewById(R.id.btnNo).setOnClickListener(v -> remove());
        overlay.findViewById(R.id.btnYes).setOnClickListener(v -> remove());

        Log.i(TAG, "Overlay mostrado");
    }

    private void remove() {
        if (overlay != null) {
            wm.removeView(overlay);
            overlay = null;

            sendBroadcast(new Intent("RESET_OVERLAY_FLAG"));
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
