package com.ndroid.projet_asso_mobile;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class les_animaux extends AppCompatActivity {

    private ListView listView;
    private List<Animal> animalList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_les_animaux);
        listView = findViewById(R.id.listView);
        animalList = new ArrayList<>();
        new FetchData().execute();
    }

    private class FetchData extends AsyncTask<Void, Void, Void> {

        @Override

        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://172.20.10.13:8080/animaux/get.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                String jsonString = stringBuilder.toString().trim(); // Supprimez les espaces supplémentaires

                JSONObject jsonObject = new JSONObject(jsonString);
                String animauxJsonString = jsonObject.getString("animaux"); // Obtenez la chaîne JSON associée à la clé "animaux"

                JSONArray jsonArray = new JSONArray(animauxJsonString); // Convertissez cette chaîne JSON en tableau JSON

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject animalObject = jsonArray.getJSONObject(i);

                    String nom = animalObject.getString("nom");
                    String espece = animalObject.getString("espece");
                    String description = animalObject.getString("description");
                    String animal_id = animalObject.getString("animal_id");
                    animalList.add(new Animal(animal_id, nom, espece, description));
                }

                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CustomAdapter adapter = new CustomAdapter(les_animaux.this, R.layout.list_item, animalList);
            listView.setAdapter(adapter);

            // Définir un écouteur d'événements sur la ListView pour gérer les clics sur les éléments de la liste
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Récupérez l'élément de la liste sur lequel l'utilisateur a cliqué
                    Animal animal = animalList.get(position);
                    // Vous pouvez utiliser les données de l'animal pour passer des informations à votre nouvelle activité, par exemple, l'ID ou le nom de l'animal
                    Intent getAnimalIntent = new Intent(les_animaux.this, get_animal.class);
                    getAnimalIntent.putExtra("animal_id", animal.getId()); // Passer l'ID de l'animal à la nouvelle activité
                    getAnimalIntent.putExtra("nom", animal.getNom()); // Passer le nom de l'animal à la nouvelle activité
                    getAnimalIntent.putExtra("espece", animal.getEspece());
                    getAnimalIntent.putExtra("description", animal.getDescription() );
                    startActivity(getAnimalIntent);
                }
            });
        }

        private class CustomAdapter extends ArrayAdapter<Animal> {

            private List<Animal> animals;
            private int layoutResourceId;
            private Context context;

            public CustomAdapter(Context context, int layoutResourceId, List<Animal> animals) {
                super(context, layoutResourceId, animals);
                this.layoutResourceId = layoutResourceId;
                this.context = context;
                this.animals = animals;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = convertView;
                ViewHolder holder;

                if (row == null) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    row = inflater.inflate(layoutResourceId, parent, false);

                    holder = new ViewHolder();
                    holder.nomTextView = row.findViewById(R.id.nomTextView);
                    holder.especeTextView = row.findViewById(R.id.especeTextView);
                    holder.idTextView = row.findViewById(R.id.idTextView);

                    row.setTag(holder);
                } else {
                    holder = (ViewHolder) row.getTag();
                }

                Animal animal = animals.get(position);
                holder.nomTextView.setText(animal.getNom());
                holder.especeTextView.setText(animal.getEspece());
                holder.idTextView.setText(String.valueOf(animal.getId())); // Afficher l'ID

                return row;
            }


            class ViewHolder {
                TextView nomTextView;
                TextView especeTextView;
                TextView idTextView;
            }
        }
    }
}
