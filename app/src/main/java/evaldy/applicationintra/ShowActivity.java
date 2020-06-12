package evaldy.applicationintra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShowActivity extends AppCompatActivity {

    TextView textView;
    TextView textView1;
    ImageView imageView;
    Button button;
    int ID;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView2);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView2);
        button.setVisibility(View.INVISIBLE);

        final Intent intent = getIntent();
        ID = intent.getIntExtra("ID",12345);

        RequestQueue requestQueue;

// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();

        String url = "http://www.innov-haiti.org/leguide/guide.php?id="+ID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("result");

                            textView.setText(array.getJSONObject(0).getString("name"));
                            textView1.setText(array.getJSONObject(0).getString("website"));
                            Picasso.get().load(array.getJSONObject(0).getString("image")).into(imageView);
                            longitude = array.getJSONObject(0).getDouble("longitude");
                            latitude = array.getJSONObject(0).getDouble("latitude");
                            button.setVisibility(View.VISIBLE);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent1 = new Intent(ShowActivity.this,MapsActivity.class);
                                    intent1.putExtra("longitude",longitude);
                                    intent1.putExtra("latitude",latitude);
                                    startActivity(intent1);
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
                        Toast.makeText(ShowActivity.this, "Erreur Volley : " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

// Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);


    }
}
