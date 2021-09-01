package sg.edu.rp.c346.id19047433.a19047433_c302_magic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private AsyncHttpClient client;
    private SharedPreferences preferences;
    private String apikey;
    private String id;
    private String role;
    private ArrayList<String> colour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        client = new AsyncHttpClient();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        apikey = preferences.getString("apiKey", "").trim();
        id = preferences.getString("loginID", "").trim();
        role = preferences.getString("role", "").trim();

        colour = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, colour);
        listView.setAdapter(adapter);
 
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("apikey", apikey);

        System.out.println(id + " , " + apikey);

        client.post("http://10.0.2.2/C302_Magic/19047433_getColours.php", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject item = response.getJSONObject(i);
                        colour.add(item.getString("colourName"));
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"Can't get the colour.", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int selectedColourId = position + 1;
                System.out.println(selectedColourId);
                Intent i = new Intent(getBaseContext(), CardActivity_19047433.class);
                i.putExtra("colourId", selectedColourId);
                startActivity(i);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        if (role.equalsIgnoreCase("admin")) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        else if (role.equalsIgnoreCase("customer")) {
            getMenuInflater().inflate(R.menu.main2, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            preferences.edit().clear();
            preferences.edit().commit();
            startActivity(intent);
            return true;
        }else if (id == R.id.menu_cardByColour) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }else if (id == R.id.menu_add) {
            Intent intent = new Intent(getApplicationContext(), CreateCardActivity_19047433.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}