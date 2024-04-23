package com.ndroid.projet_asso_mobile;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.health.connect.datatypes.units.Length;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private TextView errorConnectAccountTextView;
    private EditText mailEditText;
    private EditText passwordEditText;
    private Button connectBtn;
    private String mail;
    private String password;

    private DatabaseManager databaseManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorConnectAccountTextView = findViewById(R.id.errorConnectAccountTextView);
        mailEditText = findViewById(R.id.MailEditText);
        passwordEditText = findViewById(R.id.PasswordEditText);
        connectBtn = findViewById(R.id.connectBtn);

        databaseManager = new DatabaseManager(getApplicationContext());

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail = mailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                connectUser(mail,password);

            }
        });

    }
    public void onApiResponse(JSONObject response){
        Boolean success = null;
        String error = "";
        try {
            success = response.getBoolean("success");
            if (success == true){
                Intent interfaceActivity = new Intent(getApplicationContext(), InterfaceActivity.class);
                interfaceActivity.putExtra("mail", mail);
                startActivity(interfaceActivity);
                finish();
            }else{
                error = response.getString("error");
                errorConnectAccountTextView.setVisibility(View.VISIBLE);
                errorConnectAccountTextView.setText(error);
            }

        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    public void connectUser(String mail, String password) {
        String url = "http://192.168.43.137:8080/user/login.php";

        Map<String, String> params = new HashMap<>();
        params.put("mail", mail);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                this::onApiResponse, // This assumes onApiResponse handles login similar to registration.
                error -> Toast.makeText(MainActivity.this, "Erreur lors de la connexion, vous n'avez pas les autorisations requise ou vos identifiants sont incorrects", Toast.LENGTH_SHORT).show()
        );

        databaseManager.queue.add(jsonObjectRequest);
    }
}
