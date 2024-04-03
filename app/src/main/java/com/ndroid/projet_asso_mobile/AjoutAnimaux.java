package com.ndroid.projet_asso_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AjoutAnimaux extends AppCompatActivity {

    private EditText createNomEditText;
    private EditText createDateNaissanceEditText;
    private EditText createEspeceEditText;
    private EditText createRaceEditText;
    private EditText createSexeEditText;
    private EditText createLieuEditText;
    private EditText createDescriptionEditText;
    private Button createAnimalBtn;
    private TextView errorCreateAnimalTextView;
    private String nom;
    private String date_naissance;
    private String espece;
    private String race;
    private String sexe;
    private String lieu;
    private String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_animaux);

        createNomEditText = findViewById(R.id.createNomEditText);
        createDateNaissanceEditText = findViewById(R.id.createDateNaissanceEditText);
        createEspeceEditText = findViewById(R.id.createEspeceEditText);
        createRaceEditText = findViewById(R.id.createRaceEditText);
        createSexeEditText = findViewById(R.id.createSexeEditText);
        createLieuEditText = findViewById(R.id.createLieuEditText);
        createDescriptionEditText = findViewById(R.id.createDescriptionEditText);
        createAnimalBtn = findViewById(R.id.createAnimalBtn);
        errorCreateAnimalTextView = findViewById(R.id.errorCreateAnimalTextView);

        createAnimalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom = createNomEditText.getText().toString();
                date_naissance = createDateNaissanceEditText.getText().toString();
                espece = createEspeceEditText.getText().toString();
                race = createRaceEditText.getText().toString();
                sexe = createSexeEditText.getText().toString();
                lieu = createLieuEditText.getText().toString();
                description = createDescriptionEditText.getText().toString();

                createAnimal(nom, date_naissance, espece, race, sexe, lieu, description);

            }
        });

    }

    public void onApiResponse(JSONObject response) {
        boolean success = response.optBoolean("success", false); // Defaults to false if "success" is not a boolean.
        if (success) {
            Intent interfaceActivityIntent = new Intent(getApplicationContext(), InterfaceActivity.class);
            interfaceActivityIntent.putExtra("nom", nom);
            startActivity(interfaceActivityIntent);
        } else {
            String error = response.optString("error", "Unknown error"); // Provide a default error message.
            errorCreateAnimalTextView.setVisibility(View.VISIBLE);
            errorCreateAnimalTextView.setText(error);
        }
    }

    public void createAnimal(String nom, String date_naissance, String espece, String race, String sexe, String lieu, String description) {
        String url = "http://172.20.10.13:8080/animaux/ajout.php";

        Map<String, String> params = new HashMap<>();
        params.put("nom", nom);
        params.put("date_naissance", date_naissance);
        params.put("espece", espece);
        params.put("race", race);
        params.put("sexe", sexe);
        params.put("lieu", lieu);
        params.put("description", description);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                response -> {
                    onApiResponse(response);
                    Toast.makeText(AjoutAnimaux.this, response.toString(), Toast.LENGTH_LONG).show();
                },
                error -> {
                    Toast.makeText(AjoutAnimaux.this, "Erreur animal pas ajouté", Toast.LENGTH_SHORT).show();
                }
        );

        // Ajouter la requête à la file d'attente de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


}
