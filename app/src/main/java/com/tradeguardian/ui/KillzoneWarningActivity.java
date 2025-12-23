package com.tradeguardian.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.tradeguardian.R;
import com.tradeguardian.services.OverlayService;

public class KillzoneWarningActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_killzone_warning);

        // Configurar ventana overlay
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        // UI Elements
        TextView tvTime = findViewById(R.id.tv_ny_time);
        Button btnYes = findViewById(R.id.btn_yes);
        Button btnNo = findViewById(R.id.btn_no);

        // Mostrar hora de NY
        String nyTime = getIntent().getStringExtra("ny_time");
        tvTime.setText(nyTime);

        // Botón SI - Continuar al análisis
        btnYes.setOnClickListener(v -> {
            Intent intent = new Intent(this, AnalysisCheckActivity.class);
            startActivity(intent);
            finish();
        });

        // Botón NO - Cerrar MetaTrader
        btnNo.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(this, OverlayService.class);
            serviceIntent.setAction(OverlayService.ACTION_CLOSE_MT);
            startService(serviceIntent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Evitar que se cierre con back button
        // El usuario debe decidir
    }
}