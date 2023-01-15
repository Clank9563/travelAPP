package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class DetailActivity extends AppCompatActivity {
    private TextView name,intro;
    private ImageView photo;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        name = findViewById(R.id.detail_name);
        intro = findViewById(R.id.detail_Introduction);
        photo = findViewById(R.id.detail_photo);

        Intent intent  = getIntent();
        name.setText(intent.getStringExtra("name"));
        intro.setText(intent.getStringExtra("intro"));
        queue = Volley.newRequestQueue(this);
        fetchPhoto(intent.getStringExtra("photo"));
    }

    private void fetchPhoto(String url){
        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        photo.setImageBitmap(response);

                    }
                },
                0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(request);
    }
}