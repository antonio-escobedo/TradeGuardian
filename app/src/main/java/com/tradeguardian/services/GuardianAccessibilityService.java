package com.tradeguardian.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;

import com.tradeguardian.R;
import com.tradeguardian.utils.KillzoneUtils;
import com.tradeguardian.utils.KillzoneUtils.Killzone;

public class GuardianAccessibilityService extends AccessibilityService {

    private static final String TAG = "GuardianAccessibility";

    private WindowManager windowManager;
    private View overlayView;

    /** Control de estado */
    private boolean overlayActive = false;
    private boolean flowCompleted = false;
    private String lastPackage = "";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 300;
        info.flags =
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS |
                        AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;

        setServiceInfo(info);

        Log.i(TAG, "Accessibility CONNECTED");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event == null || event.getPackageName() == null) return;

        String pkg = event.getPackageName().toString();
        Log.d(TAG, "App detectada: " + pkg);

        // Evita procesar el mismo paquete repetidamente
        if (pkg.equals(lastPackage)) return;
        lastPackage = pkg;

        // Si NO es MetaTrader, reseteamos el flujo
        if (!pkg.equals("net.metaquotes.metatrader4")
                && !pkg.equals("net.metaquotes.metatrader5")) {

            flowCompleted = false;
            return;
        }

        // Ya se mostró el flujo para esta apertura
        if (flowCompleted || overlayActive) return;

        overlayActive = true;
        startFlow();
    }

    /** Decide qué overlay mostrar según la Killzone */
    private void startFlow() {

        Killzone zone = KillzoneUtils.getCurrentKillzone();

        if (zone == Killzone.NONE) {
            showOutsideKillzoneOverlay();
        } else {
            showKillzoneInfo(zone.name());
        }
    }

    /** Overlay cuando ESTÁS dentro de Killzone */
    private void showKillzoneInfo(String zone) {

        if (overlayView != null) return;

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        overlayView = LayoutInflater.from(this)
                .inflate(R.layout.overlay_killzone_active, null);

        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        PixelFormat.TRANSLUCENT
                );

        Button btnOk = overlayView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> removeOverlay());

        windowManager.addView(overlayView, params);

        Log.i(TAG, "Killzone activa: " + zone);
    }

    /** Overlay cuando estás FUERA de Killzone */
    private void showOutsideKillzoneOverlay() {

        if (overlayView != null) return;

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        overlayView = LayoutInflater.from(this)
                .inflate(R.layout.overlay_outside_killzone, null);

        WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                        PixelFormat.TRANSLUCENT
                );

        Button btnOk = overlayView.findViewById(R.id.btnOk);

        if (btnOk != null) {
            btnOk.setOnClickListener(v -> removeOverlay());
        }

        windowManager.addView(overlayView, params);

        Log.i(TAG, "Overlay OUTSIDE killzone mostrado");
    }


    /** Quita overlay y marca flujo como completado */
    private void removeOverlay() {
        try {
            if (overlayView != null && windowManager != null) {
                windowManager.removeView(overlayView);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error removiendo overlay", e);
        } finally {
            overlayView = null;
            overlayActive = false;
            flowCompleted = true;
        }
    }

    @Override
    public void onInterrupt() {
        Log.w(TAG, "Accessibility INTERRUPTED");
    }

    @Override
    public void onDestroy() {
        removeOverlay();
        super.onDestroy();
        Log.w(TAG, "Accessibility DESTROYED");
    }
}
