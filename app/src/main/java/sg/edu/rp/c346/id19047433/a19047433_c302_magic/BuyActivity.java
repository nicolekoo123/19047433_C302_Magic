package sg.edu.rp.c346.id19047433.a19047433_c302_magic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
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

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

//Student Name: Nicole Koo Jia Ying
//Student ID: 19047433
//Class Day: Day 5 class

public class BuyActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<CardItem> adapter;
    private ArrayList<CardItem> list;
    private AsyncHttpClient client;
    private SharedPreferences preferences;
    private String apikey;
    private String id;
    private String userId;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        listView = (ListView) findViewById(R.id.listViewCards);
        list = new ArrayList<CardItem>();
        adapter = new ArrayAdapter<CardItem>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        client = new AsyncHttpClient();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        apikey = preferences.getString("apiKey", "").trim();
        id = preferences.getString("loginID", "").trim();
        role = preferences.getString("role", "").trim();

        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("apikey", apikey);

        client.post("http://10.0.2.2/C302_Magic/getAllCards.php", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject item = response.getJSONObject(i);
                        if (item.getInt("quantity") != 0){
                            CardItem card = new CardItem(item.getString("cardId"), item.getString("colourId"), item.getString("typeId"), item.getString("cardName"), item.getDouble("price"), item.getInt("quantity"));
                            list.add(card);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(BuyActivity.this,"Can't get the Cards.", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardItem currentItem = list.get(position);
                String name = currentItem.getCardName();
                Double price = currentItem.getPrice();
                int quantity = (int) currentItem.getQuantity();
                String cardId = currentItem.getCardId();

                Double total = price * quantity;

                userId = preferences.getString("loginID", "").trim();

                RequestParams params = new RequestParams();
                params.add("id",userId);
                params.add("apikey", apikey);
                params.add("userId", userId);
                params.add("total", String.valueOf(total));
                params.add("quantity", String.valueOf(0));
                params.add("cardId", cardId);

                client.post("http://10.0.2.2/C302_Magic/buyCard.php", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getString("success").equals("true")){
                                Intent intent = new Intent(BuyActivity.this, BuyActivity.class);
                                startActivity(intent);
                                Toast.makeText(BuyActivity.this,"you brought " + quantity + "copies of " + name + "for $" + total, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(BuyActivity.this,"Fail to buy.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
