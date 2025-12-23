package com.tradeguardian.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.tradeguardian.R;

public class DisciplineReminderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discipline_reminder);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        TextView tvMessage = findViewById(R.id.tv_message);
        Button btnAccept = findViewById(R.id.btn_accept);

        // Mensajes aleatorios
        String[] messages = {
                "Que tengas suerte con tus apuestas, recuerda que para tener éxito hay que ser disciplinado y no apostar a lo pendejo.",
                "Sin análisis = Apuestas. Con análisis = Trading. Elige sabiamente.",
                "La disciplina separa a los traders exitosos de los apostadores.",
                "Cada operación sin análisis es un paso atrás en tu journey.",
                "El mercado premia la preparación, castiga la impulsividad."
        };

        int msgIndex = (int) (Math.random() * messages.length);
        tvMessage.setText(messages[msgIndex]);

        btnAccept.setOnClickListener(v -> {
            // Permitir abrir MetaTrader después del recordatorio
            finish();
        });
    }
}