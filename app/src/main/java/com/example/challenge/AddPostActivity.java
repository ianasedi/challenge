package com.example.challenge;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

public class AddPostActivity extends AppCompatActivity {

    private EditText addTextTxt;
    private Button addTextBtn;
    private ImageButton backBtn;

    private static final String URL = "https://intern-hackathon.mready.net/api/posts";

    private OkHttpClient client;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        initViews();

        Intent intent = getIntent();
        token = intent.getStringExtra("token");


        addTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                RequestBody json = registerJson(addTextTxt.getText().toString());
                Request request = post(URL, json);

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(response.isSuccessful()){
                            System.out.println(response.body().string());
                            addTextTxt.setText("");
                        }
                    }
                });
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callParent = new Intent(AddPostActivity.this, FeedActivity.class);
                startActivity(callParent);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews(){
        addTextTxt = findViewById(R.id.addTextTxt);
        addTextBtn = findViewById(R.id.sendTextBtn);
        backBtn = findViewById(R.id.backBtn);
        addTextTxt.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    private Request post(String url, RequestBody rb) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .post(rb)
                .build();
        return request;
    }

    //Create body with specified fields
    private RequestBody registerJson(String message) {
        RequestBody rb = new FormBody.Builder().add("message", message).add("token", token).build();
        return rb;
    }
}