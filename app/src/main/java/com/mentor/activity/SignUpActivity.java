package com.mentor.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mentor.R;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class SignUpActivity extends AppCompatActivity {

    EditText first_name, last_name, email, password, role;
    Button sign_up_btn, to_home_btn, to_sign_in_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        role = findViewById(R.id.role);

        String fName = first_name.toString();
        String lastName = last_name.toString();
        String emailString = email.toString();
        String passwordString = password.toString();
        String roleString = role.toString();

        sign_up_btn = findViewById(R.id.sign_up_btn);


        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    sign_up(fName, lastName, emailString, passwordString, roleString);
                }  catch (JSONException e) {
                    throw new RuntimeException(e);
                }



            }
        });
    }

    /*public void processFormFields() {
        String url = "";
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Toast.makeText(SignUpActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener);


        RequestQueue requestQueue;

// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();

        String urll = "http://www.example.com";



        requestQueue.add(stringRequest);
        }*/

    public void sign_up(String fName, String lName, String email, String password, String role ) throws JSONException {
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject newUser = new JSONObject();
        client.addHeader("Accept", "application/json");
        newUser.put("firstName", fName);
        newUser.put("lastName", lName);
        newUser.put("email", email);
        newUser.put("password", password);
        newUser.put("role", role);

        StringEntity entity = new StringEntity(newUser.toString(), "UTF_8");
        client.post(getApplicationContext(), "http://78.90.22.16/auth/sign-up", entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Intent mainActivity = new Intent(SignUpActivity.this, MainActivity.class);

                try{
                    JSONObject response = new JSONObject(new String(responseBody));
                    Bundle bundle = new Bundle();
                    bundle.putString("current_user", response.toString());
                    startActivity(mainActivity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void request() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("param1", "TEST");
        client.post("http://78.90.22.16/recipes/all", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void goHome(View view) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void toSignIn(View view) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}