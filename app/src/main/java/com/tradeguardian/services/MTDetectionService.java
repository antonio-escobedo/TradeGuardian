package com.tradeguardian.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MTDetectionService extends AccessibilityService {
    private static final String TAG = "MTDetectionService";

    private static final String[] MT_PACKAGES = {
            "net.metaquotes.metatrader",
            "com.metaquotes.mt",
            "metatrader",
            "mt4",
            "mt5"
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = event.getPackageName() != null ?
                    event.getPackageName().toString().toLowerCase() : "";

            // Verificar si es MetaTrader
            for (String mtKeyword : MT_PACKAGES) {
                if (packageName.contains(mtKeyword)) {
                    Log.i(TAG, "MetaTrader detectado: " + packageName);

                    // Iniciar el flujo de verificación
                    Intent intent = new Intent(this, OverlayService.class);
                    intent.setAction(OverlayService.ACTION_START_FLOW);
                    intent.putExtra("package_name", packageName);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        startForegroundService(intent);
                    } else {
                        startService(intent);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.w(TAG, "Servicio interrumpido");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG, "Servicio de detección conectado");
    }
}