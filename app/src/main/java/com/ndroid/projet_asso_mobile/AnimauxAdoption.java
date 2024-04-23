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

public class AnimauxAdoption extends AppCompatActivity {

    private ListView listView;
    private List<Animal> animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animaux_adoption);
        listView = findViewById(R.id.listView);
        animalList = new ArrayList<>();
        new FetchData().execute();
    }

    private class FetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.43.137:8080/animaux/AnimauxAdopte.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                String jsonString = stringBuilder.toString().trim();

                JSONObject jsonObject = new JSONObject(jsonString);
                String animauxJsonString = jsonObject.getString("animaux");

                JSONArray jsonArray = new JSONArray(animauxJsonString);

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
            CustomAdapter adapter = new CustomAdapter(AnimauxAdoption.this, R.layout.list_item, animalList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Animal animal = animalList.get(position);
                    Intent getAnimalIntent = new Intent(AnimauxAdoption.this, get_animal.class);
                    getAnimalIntent.putExtra("animal_id", animal.getId());
                    getAnimalIntent.putExtra("nom", animal.getNom());
                    getAnimalIntent.putExtra("espece", animal.getEspece());
                    getAnimalIntent.putExtra("description", animal.getDescription());
                    startActivity(getAnimalIntent);
                }
            });
        }
    }

    private static class CustomAdapter extends ArrayAdapter<Animal> {

        private List<Animal> animals;
        private int layoutResourceId;
        private LayoutInflater inflater;

        public CustomAdapter(Context context, int layoutResourceId, List<Animal> animals) {
            super(context, layoutResourceId, animals);
            this.layoutResourceId = layoutResourceId;
            this.animals = animals;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.nomTextView = convertView.findViewById(R.id.nomTextView);
                holder.especeTextView = convertView.findViewById(R.id.especeTextView);
                holder.idTextView = convertView.findViewById(R.id.idTextView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Animal animal = animals.get(position);
            holder.nomTextView.setText(animal.getNom());
            holder.especeTextView.setText(animal.getEspece());
            holder.idTextView.setText(String.valueOf(animal.getId()));

            return convertView;
        }

        static class ViewHolder {
            TextView nomTextView;
            TextView especeTextView;
            TextView idTextView;
        }
    }
}
