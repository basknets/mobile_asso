package com.ndroid.projet_asso_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class get_animal extends AppCompatActivity {

    private Button btnModification;
    private Button btnDelete;
    private int animalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_animal);

        // Récupérer les données de l'intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Récupérer l'ID de l'animal
            String animal_id = extras.getString("animal_id");
            // Convertir la chaîne en entier
            animalId = Integer.parseInt(animal_id);

            // Récupérer les autres données de l'animal (nom, espèce, description)
            String nom = extras.getString("nom");
            String espece = extras.getString("espece");
            String description = extras.getString("description");

            // Afficher les données de l'animal dans les TextView correspondants
            TextView nomTextView = findViewById(R.id.nomTextView);
            TextView especeTextView = findViewById(R.id.especeTextView);
            TextView descriptionTextView = findViewById(R.id.descriptionTextView);

            nomTextView.setText(nom);
            especeTextView.setText(espece);
            descriptionTextView.setText(description);
        }

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Appeler la méthode pour supprimer l'animal
                deleteAnimalFromApi(animalId);
            }
        });

        btnModification = findViewById(R.id.btnModification);
        TextView nomTextView = findViewById(R.id.nomTextView);
        TextView especeTextView = findViewById(R.id.especeTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        btnModification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modificationAnimalIntent = new Intent(getApplicationContext(), ModificationAnimal.class);
                // Ajouter les données de l'animal à l'intent
                modificationAnimalIntent.putExtra("animal_id", animalId);
                modificationAnimalIntent.putExtra("nom", nomTextView.getText().toString());
                modificationAnimalIntent.putExtra("espece", especeTextView.getText().toString());
                modificationAnimalIntent.putExtra("description", descriptionTextView.getText().toString());
                startActivity(modificationAnimalIntent);
            }
        });

    }

    // Méthode pour supprimer l'animal de l'API
    private void deleteAnimalFromApi(int animalId) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("animal_id", animalId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.137:8080/animaux/delete.php", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(get_animal.this, "Animal supprimé avec succès", Toast.LENGTH_SHORT).show();
                        Intent les_animaux = new Intent(getApplicationContext(), les_animaux.class);
                        startActivity(les_animaux);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Gérer l'erreur de l'API si nécessaire
                Toast.makeText(get_animal.this, "Échec de la suppression de l'animal", Toast.LENGTH_SHORT).show();
            }
        });

        // Ajouter la requête à la file de requêtes
        queue.add(request);
    }

}