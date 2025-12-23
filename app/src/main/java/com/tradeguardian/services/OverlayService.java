package com.tradeguardian.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.tradeguardian.R;
import com.tradeguardian.ui.MainActivity;

public class OverlayService extends Service {
    private static final String TAG = "OverlayService";
    public static final String ACTION_START_FLOW = "START_FLOW";
    public static final String ACTION_CLOSE_MT = "CLOSE_MT";

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
        Log.i(TAG, "Servicio creado");
    }

    private void startForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this,
                "TradeGuardianChannel")
                .setContentTitle("TradeGuardian Activo")
                .setContentText("Monitoreando MetaTrader")
                .setSmallIcon(R.drawable.ic_shield)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setSound(null)
                .build();

        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            Log.i(TAG, "Acción recibida: " + action);

            if (ACTION_START_FLOW.equals(action)) {
                // Aquí iniciarías el flujo de killzone
                Log.i(TAG, "Iniciando flujo de verificación...");
            }
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}