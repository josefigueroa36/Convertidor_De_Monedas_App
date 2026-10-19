package com.example.convertidormoneda;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText campoCantidad;
    private RadioGroup radioGroupOrigen, radioGroupDestino;
    private Button btnConvertir;
    private TextView tvResultado;

    private static final double TASA_USD = 1.0;
    private static final double TASA_COP = 3100.0;
    private static final double TASA_JPY = 160.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        campoCantidad = findViewById(R.id.campoCantidad);
        radioGroupOrigen = findViewById(R.id.radioGroupOrigen);
        radioGroupDestino = findViewById(R.id.radioGroupDestino);
        btnConvertir = findViewById(R.id.btnConvertir);
        tvResultado = findViewById(R.id.tvResultado);

        btnConvertir.setOnClickListener(v -> convertir());
    }

    private void convertir() {
        String textoCantidad = campoCantidad.getText().toString().trim();

        if (textoCantidad.isEmpty()) {
            Toast.makeText(this, "Ingresa una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioGroupOrigen.getCheckedRadioButtonId() == -1
                || radioGroupDestino.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecciona moneda origen y destino", Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(textoCantidad);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        double tasaOrigen = obtenerTasa(radioGroupOrigen.getCheckedRadioButtonId());
        double tasaDestino = obtenerTasa(radioGroupDestino.getCheckedRadioButtonId());

        double enUSD = cantidad / tasaOrigen;
        double resultado = enUSD * tasaDestino;

        String destinoText ;

        if (radioGroupDestino.getCheckedRadioButtonId() == R.id.radioCOP || radioGroupDestino.getCheckedRadioButtonId() == R.id.radioCOPDestino) {
            destinoText = "COP";
        } else if (radioGroupDestino.getCheckedRadioButtonId() == R.id.radioJPY || radioGroupDestino.getCheckedRadioButtonId() == R.id.radioJPYDestino) {
            destinoText = "JPY";
        } else {
            destinoText = "USD";;
        }
        tvResultado.setText(String.format("Resultado: %.2f "+ destinoText, resultado));
    }

    private double obtenerTasa(int radioButtonId) {
        if (radioButtonId == R.id.radioCOP || radioButtonId == R.id.radioCOPDestino) {
            return TASA_COP;
        } else if (radioButtonId == R.id.radioJPY || radioButtonId == R.id.radioJPYDestino) {
            return TASA_JPY;
        } else {
            return TASA_USD;
        }
    }
}