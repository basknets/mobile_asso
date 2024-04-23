package com.ndroid.projet_asso_mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModificationAnimal extends AppCompatActivity {
    private int animalId;
    private EditText nomEditText;
    private EditText especeEditText;
    private EditText descriptionEditText;
    private Button buttonModifier;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_animal);

        // Récupérer les données de l'intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Récupérer les données de l'animal
            animalId = extras.getInt("animal_id");
            String nom = extras.getString("nom");
            String espece = extras.getString("espece");
            String description = extras.getString("description");

            // Récupérer les EditText correspondants dans le layout
            nomEditText = findViewById(R.id.nomEditText);
            especeEditText = findViewById(R.id.especeEditText);
            descriptionEditText = findViewById(R.id.descriptionEditText);
            buttonModifier = findViewById(R.id.buttonModifier);

            // Pré-remplir les champs du formulaire avec les données de l'animal
            nomEditText.setText(nom);
            especeEditText.setText(espece);
            descriptionEditText.setText(description);

            // Afficher l'ID de l'animal dans la TextView idTextView
            TextView idTextView = findViewById(R.id.idTextView);
            idTextView.setText(String.valueOf(animalId));

            buttonModifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nom = nomEditText.getText().toString();
                    String espece = especeEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();

                    modifAnimal(nom, espece, description);
                }
            });
        }
    }

    public void onApiResponse(JSONObject response){

        boolean success = response.optBoolean("success", false); // Defaults to false if "success" is not a boolean.
        if (success) {
            Intent interfaceActivityIntent = new Intent(getApplicationContext(), InterfaceActivity.class);
            startActivity(interfaceActivityIntent);
        } else {
            String error = response.optString("error", "Unknown error"); // Provide a default error message.

        }
    }

    public void modifAnimal(String nom, String espece, String description) {
        String url = "http://192.168.43.137:8080/animaux/modification.php";

        Map<String, String> params = new HashMap<>();
        params.put("animal_id", String.valueOf(animalId));
        params.put("nom", nom);
        params.put("espece", espece);
        params.put("description", description);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                response -> {
                    onApiResponse(response);
                    Toast.makeText(ModificationAnimal.this, response.toString(), Toast.LENGTH_LONG).show();
                },
                error -> {
                    Toast.makeText(ModificationAnimal.this, "Erreur lors de la modification : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Ajouter la requête à la file d'attente de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
