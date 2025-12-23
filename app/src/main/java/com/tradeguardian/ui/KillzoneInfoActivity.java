package com.tradeguardian.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.tradeguardian.R;

public class KillzoneInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_killzone_info);

        // Configurar overlay
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // Obtener datos
        String killzoneName = getIntent().getStringExtra("killzone_name");
        String motivationalPhrase = getIntent().getStringExtra("motivational_phrase");
        String nyTime = getIntent().getStringExtra("ny_time");

        // UI Elements
        TextView tvKillzone = findViewById(R.id.tv_killzone);
        TextView tvMotivation = findViewById(R.id.tv_motivation);
        TextView tvTime = findViewById(R.id.tv_ny_time);
        Button btnAccept = findViewById(R.id.btn_accept);

        // Mostrar información
        tvKillzone.setText(killzoneName);
        tvMotivation.setText(motivationalPhrase);
        tvTime.setText(nyTime);

        // Botón Aceptar - Ir a análisis
        btnAccept.setOnClickListener(v -> {
            Intent intent = new Intent(this, AnalysisCheckActivity.class);
            startActivity(intent);
            finish();
        });
    }
}