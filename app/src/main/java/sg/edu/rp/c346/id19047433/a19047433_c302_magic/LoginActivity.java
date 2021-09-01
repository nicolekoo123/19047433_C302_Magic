
package sg.edu.rp.c346.id19047433.a19047433_c302_magic;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.*;

import org.json.JSONObject;

import cz.msebera.android.httpclient.*;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText etLoginID, etPassword;
    private Button btnSubmit;
    private AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginID = (EditText)findViewById(R.id.editTextLoginID);
        etPassword = (EditText)findViewById(R.id.editTextPassword);
        btnSubmit = (Button)findViewById(R.id.buttonSubmit);
        client = new AsyncHttpClient();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etLoginID.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Login failed. Please enter username.", Toast.LENGTH_LONG).show();

                } else if (password.equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Login failed. Please enter password.", Toast.LENGTH_LONG).show();

                } else {
					// TODO: call doLogin web service to authenticate user
					//save the apikey into SharedPreference
                    OnLogin(v);
                }
            }
        });
    }
    private void OnLogin(View v) {
        // Point X - TODO: call doLogin web service to authenticate user
        RequestParams params = new RequestParams();
        params.add("username", etLoginID.getText().toString());
        params.add("password", etPassword.getText().toString());

        client.post("http://10.0.2.2/C302_Magic/doLogin.php",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response.getBoolean("authenticated")){
                        Toast.makeText(LoginActivity.this, "response.getBoolean(authenticated)", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, "login", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, BuyActivity.class);
                        String id = response.getString("id");
                        String apikey = response.getString("apikey");
                        String role = response.getString("role");
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("loginID", id);
                        editor.putString("apiKey", apikey);
                        editor.putString("role", role);
                        System.out.println(id);
                        editor.commit();
                        startActivity(i);
                    }else{
                        Toast.makeText(LoginActivity.this, "Login failed, Please Check your login credentials", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,"Login failed, Please Check your login credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


