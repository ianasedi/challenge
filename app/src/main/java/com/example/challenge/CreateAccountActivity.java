package com.example.challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateAccountActivity extends AppCompatActivity {



    private TextView detailsTxt;
    private EditText txtName, txtUser, txtPass, txtRepPass;
    private Button createBtn;
    private ImageButton backBtn;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String URL = "https://intern-hackathon.mready.net/api/auth/register";
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initiViews();

        //La apasarea butonului se incearca trimiterea unei cereri catre server
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtName.getText().toString().isEmpty() || txtUser.getText().toString().isEmpty() || txtPass.getText().toString().isEmpty() || txtRepPass.getText().toString().isEmpty()) {
                    Toast.makeText(CreateAccountActivity.this, "Toate campurile trebuie completate", Toast.LENGTH_SHORT).show();
                } else if (!txtPass.getText().toString().equals(txtRepPass.getText().toString())) {
                    Toast.makeText(CreateAccountActivity.this, "Parolele nu coincid!", Toast.LENGTH_SHORT).show();  //Daca toate campurile sunt completate
                } else {                                                                                                         //corect se trimite cererea, daca nu se afiseaza un Toast cu mesajul corespunzator

                    client = new OkHttpClient();
                    RequestBody json = registerJson(txtUser.getText().toString(), txtPass.getText().toString(), txtName.getText().toString());
                    Request request = post(URL, json);

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                System.out.println(response.body().string());
                                CreateAccountActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CreateAccountActivity.this, "User creat cu succes", Toast.LENGTH_SHORT).show();        //Daca se reuseste crearea unui nou user este afisat un mesaj
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //create a post request with specified at specified URL
    private Request post(String url, RequestBody rb) {
        Request request = new Request.Builder()
                .url(url)
                .post(rb)
                .build();
        return request;
    }

    //Create body with specified fields
    private RequestBody registerJson(String username, String password, String display_name) {
        RequestBody rb = new FormBody.Builder().add("username", username).add("password", password).add("display_name", display_name).build();
        return rb;
    }

    public void initiViews(){
        detailsTxt = findViewById(R.id.detailsTxt);
        txtName = findViewById(R.id.fullNameTxt);
        txtUser = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPwd);
        txtRepPass = findViewById(R.id.txtRepPwd);
        createBtn = findViewById(R.id.createBtn);
        backBtn = findViewById(R.id.imageButtonBack);
    }
}