package com.tradeguardian.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.tradeguardian.R;

public class AnalysisCheckActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_check);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        Button btnYes = findViewById(R.id.btn_yes);
        Button btnNo = findViewById(R.id.btn_no);
        TextView tvQuestion1 = findViewById(R.id.tv_question1);
        TextView tvQuestion2 = findViewById(R.id.tv_question2);

        // Personalizar preguntas
        String[] analysisQuestions = {
                "¿Ya realizaste tu análisis en TradingView?",
                "¿Ya marcaste mínimos y máximos clave?",
                "¿Identificaste zonas de soporte/resistencia?",
                "¿Tienes definidos tu stop loss y take profit?"
        };

        // Seleccionar preguntas aleatorias
        int q1Index = (int) (Math.random() * analysisQuestions.length);
        int q2Index;
        do {
            q2Index = (int) (Math.random() * analysisQuestions.length);
        } while (q2Index == q1Index);

        tvQuestion1.setText(analysisQuestions[q1Index]);
        tvQuestion2.setText(analysisQuestions[q2Index]);

        // Botón SI - Operación permitida
        btnYes.setOnClickListener(v -> {
            // Mostrar frase motivacional final
            String[] successPhrases = {
                    "¡Excelente preparación! La disciplina paga.",
                    "Analizado y listo. Ejecuta con confianza.",
                    "Plan + Disciplina = Consistencia. ¡Adelante!",
                    "Has hecho tu tarea. Ahora opera sin emociones."
            };

            int phraseIndex = (int) (Math.random() * successPhrases.length);
            showMotivationalDialog(successPhrases[phraseIndex]);
        });

        // Botón NO - Mostrar advertencia
        btnNo.setOnClickListener(v -> {
            Intent intent = new Intent(this, DisciplineReminderActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showMotivationalDialog(String message) {
        // Puedes implementar un dialog bonito aquí
        // Por ahora solo cerramos y permitimos MT
        finish();
    }
}