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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interface);

        btnAnimaux = findViewById(R.id.btnAnimaux);
        btnAjout = findViewById(R.id.btnAjout);

        btnAnimaux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent les_animaux = new Intent(getApplicationContext(), les_animaux.class);
                startActivity(les_animaux);
                finish();
            }
        });

        btnAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ajout_animaux = new Intent(getApplicationContext(), ajout_animaux.class);
                startActivity(ajout_animaux);
                finish();
            }
        });
    }
}