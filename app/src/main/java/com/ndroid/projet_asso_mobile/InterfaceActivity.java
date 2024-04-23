package com.ndroid.projet_asso_mobile;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InterfaceActivity extends AppCompatActivity {

    private Button btnAnimaux;
    private Button btnAjout;

    private  Button btnAnimauxAdopte;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        btnAnimaux = findViewById(R.id.btnAnimaux);
        btnAjout = findViewById(R.id.btnAjout);
        btnAnimauxAdopte = findViewById(R.id.btnAnimauxAdopte);

        btnAnimaux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent les_animaux = new Intent(getApplicationContext(), les_animaux.class);
                startActivity(les_animaux);
            }
        });

        btnAnimauxAdopte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AnimauxAdoption = new Intent(getApplicationContext(), AnimauxAdoption.class);
                startActivity(AnimauxAdoption);
            }
        });

        btnAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AjoutAnimaux = new Intent(getApplicationContext(), AjoutAnimaux.class);
                startActivity(AjoutAnimaux);
            }
        });
    }
}
