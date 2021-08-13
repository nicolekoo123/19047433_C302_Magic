package sg.edu.rp.c346.id19047433.a19047433_c302_magic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CreateCardActivity_19047433 extends AppCompatActivity {

    private SharedPreferences preferences;
    private String apikey;
    private String id;
    private String role;
    private AsyncHttpClient client;

    private String name, colourId, typeId, price, quantity;
    private EditText etName, etColourId, etTypeId, etPrice, etQuantity;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card_19047433);

        etName = findViewById(R.id.editTextName);
        etColourId = findViewById(R.id.editTextColourID);
        etTypeId = findViewById(R.id.editTextTypeID);
        etPrice = findViewById(R.id.editTextPrice);
        etQuantity = findViewById(R.id.editTextQuantity);
        btnAdd = findViewById(R.id.buttonAdd);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        apikey = preferences.getString("apiKey", "").trim();
        id = preferences.getString("loginID", "").trim();
        role = preferences.getString("role", "").trim();

        client = new AsyncHttpClient();

        //System.out.println(id + " , " + apikey + " , " + name + " , " + colourId + " , " + typeId + " , " + price + " , " + quantity);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                colourId = etColourId.getText().toString();
                typeId = etTypeId.getText().toString();
                price = etPrice.getText().toString();
                quantity = etQuantity.getText().toString();

                RequestParams params = new RequestParams();
                params.add("id", id);
                params.add("apikey", apikey);
                params.add("cardName", name);
                params.add("colourId", colourId);
                params.add("typeId", typeId);
                params.add("price", price);
                params.add("quantity", quantity);

                if (name.isEmpty() == true){
                    Toast.makeText(CreateCardActivity_19047433.this,"Name cannot be blank", Toast.LENGTH_SHORT).show();
                } else if (colourId.isEmpty() == true){
                    Toast.makeText(CreateCardActivity_19047433.this,"Colour ID cannot be blank", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(colourId) <= 0 || Integer.parseInt(colourId) >= 6){
                    Toast.makeText(CreateCardActivity_19047433.this,"Colour ID must be 1 to 5", Toast.LENGTH_SHORT).show();
                } else if (typeId.isEmpty() == true){
                    Toast.makeText(CreateCardActivity_19047433.this,"Type ID cannot be blank", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(typeId) <= 0 || Integer.parseInt(typeId) >= 5){
                    Toast.makeText(CreateCardActivity_19047433.this,"Type ID must be 1 to 4", Toast.LENGTH_SHORT).show();
                } else if (price.isEmpty() == true){
                    Toast.makeText(CreateCardActivity_19047433.this,"Price cannot be blank", Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(price) <= 0.0){
                    Toast.makeText(CreateCardActivity_19047433.this,"Price should be zero or higher", Toast.LENGTH_SHORT).show();
                } else if (quantity.isEmpty() == true){
                    Toast.makeText(CreateCardActivity_19047433.this,"Quantity cannot be blank", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(quantity) < 0){
                    Toast.makeText(CreateCardActivity_19047433.this,"Quantity should be zero or higher", Toast.LENGTH_SHORT).show();
                } else {
                    client.post("http://10.0.2.2/C302_Magic/19047433_createCard.php", params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                if (response.getString("success").equals("true")){
                                    Intent intent = new Intent(CreateCardActivity_19047433.this, MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(CreateCardActivity_19047433.this,"Card created successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CreateCardActivity_19047433.this,"Card created successfully.", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(CreateCardActivity_19047433.this,"Card created unsuccessfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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