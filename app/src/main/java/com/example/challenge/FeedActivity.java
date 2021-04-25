package com.example.challenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FeedActivity extends AppCompatActivity {

    private FeedRecViewAdapter adapter;
    private RecyclerView recView;
    private Button sendMsg;
    private ImageButton lonOutBtn;
    private SwipeRefreshLayout ref;

    private String token, serverResponse;

    private static final String URL = "https://intern-hackathon.mready.net/api/posts";

    private OkHttpClient client;
    private Request request;

    List<Message> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initViews();

        Intent intent = getIntent();
        token = intent.getStringExtra("token");


        adapter = new FeedRecViewAdapter(this);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(this));

        client = new OkHttpClient();
        request = get();

        sendRequest();

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedActivity.this, AddPostActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });

        lonOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FeedActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


        //La efectuarea unui swipeRefresh se face fetch din nou din baza de date
        ref.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
                ref.setRefreshing(false);
            }
        });



    }

    public void initViews(){
        recView = findViewById(R.id.recyclerView);
        sendMsg = findViewById(R.id.sendBtn);
        lonOutBtn = findViewById(R.id.logOutButton);
        ref = findViewById(R.id.refreshLayout);
    }

    private Request get() {
        Request request = new Request.Builder()
                .url(URL)
                .get()
                .build();
        return request;
    }


    //metoda de trimitere a cererii catre server
    private void sendRequest(){

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONArray jsonArray;
                try {

                    JSONObject jsonObj = new JSONObject(response.body().string());

                    jsonArray = jsonObj.getJSONArray("data");
                    System.out.println(jsonArray.length());
                    for(int i=0; i<jsonArray.length(); i++){
                        list.add(new Message(jsonArray.getJSONObject(i).getString("display_name"), jsonArray.getJSONObject(i).getString("message"), jsonArray.getJSONObject(i).getString("created_at")));
                        FeedActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setMessages(list);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}