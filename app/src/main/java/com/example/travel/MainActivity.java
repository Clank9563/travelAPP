package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextSelection;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private ListView listView;
    private SimpleAdapter adapter;
    private LinkedList<HashMap<String,String>> data = new LinkedList<>();
    private String[] from = {"name","tel","address"};
    private  int[] to ={R.id.item_name,R.id.item_tel,R.id.item_address};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.list);
        initlist();
        fetchOpendata();

    }
    private void  initlist(){
        adapter = new SimpleAdapter(this,data,R.layout.item,from,to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("name",data.get(i).get("name"));
                intent.putExtra("intro",data.get(i).get("intro"));
                intent.putExtra("photo",data.get(i).get("photo"));
                startActivity(intent);
            }
        });
    }

    private void  fetchOpendata(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAttractions.aspx" ,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.v("test",response);
                parseJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("test",error.toString());
            }
        }
        );
        queue.add(request);
    }
    private void parseJSON(String json){
        data.clear();
        try {
            JSONArray root = new JSONArray(json);
            for ( int i=0;i<root.length();i++){
                JSONObject row = root.getJSONObject(i);
                String name = row.getString("Name");
                String tel = row.getString("Tel");
                String address = row.getString("Address");

                HashMap<String,String> rowdata = new HashMap<>();
                rowdata.put(from[0],name);
                rowdata.put(from[1],tel);
                rowdata.put(from[2],address);
                rowdata.put("intro",row.getString("Introduction"));
                rowdata.put("photo",row.getString("Photo"));

                data.add(rowdata);
            }
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            Log.v("text",e.toString());
        }

    }
}