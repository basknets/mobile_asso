package com.ndroid.projet_asso_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class ModificationAnimal extends AppCompatActivity {
    private int animalId;
    private EditText nomEditText;
    private EditText especeEditText;
    private EditText descriptionEditText;

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

            // Pré-remplir les champs du formulaire avec les données de l'animal
            nomEditText.setText(nom);
            especeEditText.setText(espece);
            descriptionEditText.setText(description);
        }
    }

    public void modifierAnimal(View view) {
        String url = "http://172.20.10.13:8080/animaux/modification.php"; // L'URL de modification doit être utilisée ici

        // Récupérer les valeurs des champs modifiés
        String nom = nomEditText.getText().toString();
        String espece = especeEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        // Appeler la méthode pour modifier l'animal
        modifierAnimal(url, animalId, nom, espece, description);
    }

    private void modifierAnimal(String url, int animalId, String nom, String espece, String description) {
        Map<String, String> params = new HashMap<>();
        params.put("animal_id", String.valueOf(animalId)); // Envoyer l'ID de l'animal à modifier
        params.put("nom", nom);
        params.put("espece", espece);
        params.put("description", description);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                response -> {
                    // Gérer la réponse de la requête
                    Toast.makeText(ModificationAnimal.this, "Animal modifié avec succès", Toast.LENGTH_SHORT).show();
                    // Vous pouvez rediriger ou faire d'autres actions après la modification réussie
                },
                error -> {
                    // Gérer l'erreur de la requête
                    Toast.makeText(ModificationAnimal.this, "Erreur lors de la modification de l'animal", Toast.LENGTH_SHORT).show();
                }
        );

        // Ajouter la requête à la file d'attente de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
