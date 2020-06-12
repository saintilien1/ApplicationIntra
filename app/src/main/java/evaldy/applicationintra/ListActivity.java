package evaldy.applicationintra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    myAdapter adapter;
    ArrayList<listejson> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RequestQueue requestQueue;

// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();

        String url = "http://www.innov-haiti.org/leguide/guide.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        arrayList = new ArrayList<>();
                        try {

                            JSONArray array = response.getJSONArray("result");
                            for(int i=0; i < array.length(); i++){
                                listejson listejson = new listejson();
                                listejson.setId(array.getJSONObject(i).getInt("guide_id"));
                                listejson.setNom(array.getJSONObject(i).getString("name"));
                                listejson.setAdresse(array.getJSONObject(i).getString("adress"));
                                System.out.println(listejson.getId() + listejson.getNom() + listejson.getAdresse());
                                arrayList.add(listejson);
                            }
                            listView = (ListView) findViewById(R.id.liste);
                            adapter = new myAdapter(ListActivity.this,arrayList);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(ListActivity.this,ShowActivity.class);
                                    intent.putExtra("ID",arrayList.get(position).getId());
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println("Erreur " + error.toString());
                        Toast.makeText(ListActivity.this, "Erreur Volley : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

// Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);


    }
}
