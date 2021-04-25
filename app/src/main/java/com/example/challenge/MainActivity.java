package com.example.challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView logoImg;
    private TextView welcomeTxt, loginTxt, createTxt, errorTxt;
    private EditText usernameTxt, passswordTxt;
    private Button loginBtn;
    private String token, serverResponse;

    private static final String URL = "https://intern-hackathon.mready.net/api/auth/login";

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        token = "";

        //Incepere activitate CreareAccount noua
        createTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorTxt.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);

            }
        });

        //Trimite cerere de login catre server cad este apasat butonul
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorTxt.setVisibility(View.GONE);
                client = new OkHttpClient();
                RequestBody json = registerJson(usernameTxt.getText().toString(), passswordTxt.getText().toString());
                Request request = post(URL, json);        //Se creeaza cererea in functie de datele introduse

                client.newCall(request).enqueue(new Callback() {        //Se asteapta raspunsul de la server
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            serverResponse = response.body().string();              //Daca se primeste tokenul se realizeaza autentificarea
                            System.out.println(serverResponse);                     //si incepe o activitate noua
                            try {                                                   //In caz contrar este afisat un mesaj de eroare
                                JSONObject jsonObj = new JSONObject(serverResponse);
                                try {
                                    token = jsonObj.getJSONObject("data").getString("token");
                                }catch(JSONException e) {
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            errorTxt.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                                if(!token.equals("") && !usernameTxt.getText().toString().isEmpty()){
                                    Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                                    intent.putExtra("token", token);
                                    startActivity(intent);
                                }else{
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            errorTxt.setVisibility(View.VISIBLE);
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

            }
        });
    }

    private Request post(String url, RequestBody rb) {
        Request request = new Request.Builder()
                .url(url)
                .post(rb)
                .build();
        return request;
    }

    //Create request body with specified fields
    private RequestBody registerJson(String username, String password) {
        RequestBody rb = new FormBody.Builder().add("username", username).add("password", password).build();
        return rb;
    }

    private void initViews(){
        logoImg = findViewById(R.id.logoImg);
        welcomeTxt = findViewById(R.id.welcomeTxt);
        loginTxt = findViewById(R.id.loginTxt);
        createTxt = findViewById(R.id.createTxt);
        usernameTxt = findViewById(R.id.usernameTxt);
        passswordTxt = findViewById(R.id.passwordTxt);
        loginBtn = findViewById(R.id.loginBtn);
        errorTxt = findViewById(R.id.errorTxt);
    }

}