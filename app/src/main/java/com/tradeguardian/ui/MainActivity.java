package com.tradeguardian.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.tradeguardian.R;
import com.tradeguardian.services.OverlayService;

public class MainActivity extends Activity {

    TextView tvAccessibility, tvOverlay;

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        tvAccessibility = findViewById(R.id.tvAccessibilityStatus);
        tvOverlay = findViewById(R.id.tvOverlayStatus);

        findViewById(R.id.btnAccessibility)
                .setOnClickListener(v ->
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));

        findViewById(R.id.btnOverlay)
                .setOnClickListener(v ->
                        startActivity(new Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName())
                        )));

        findViewById(R.id.btnBattery)
                .setOnClickListener(v ->
                        startActivity(new Intent(
                                Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)));

        findViewById(R.id.btnTest)
                .setOnClickListener(v -> testOverlay());
    }

    private void updateStatus() {
        tvAccessibility.setText(
                "Accesibilidad: " +
                        (isAccessibilityEnabled() ? "✅" : "❌"));

        tvOverlay.setText(
                "Overlay: " +
                        (Settings.canDrawOverlays(this) ? "✅" : "❌"));
    }

    private boolean isAccessibilityEnabled() {
        try {
            int enabled = Settings.Secure.getInt(
                    getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            return enabled == 1;
        } catch (Exception e) {
            return false;
        }
    }

    private void testOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Overlay no permitido", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this, OverlayService.class);
        i.setAction(OverlayService.ACTION_START_FLOW);
        startForegroundService(i);
    }
}
