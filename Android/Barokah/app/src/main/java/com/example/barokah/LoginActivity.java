package com.example.barokah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView link_regist;
    private Button login_btn;
    private ProgressBar loading;
    private static String URL_LOGIN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loading = findViewById(R.id.loading);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);
        link_regist = findViewById(R.id.link_regist);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                String mPass = password.getText().toString().trim();

                if(!mEmail.isEmpty() || !mPass.isEmpty()){
                    Login(mEmail, mPass);
                }else{
                    email.setError("Tolong Masukan Email");
                    password.setError("Tolong Masukan Password");
                }
            }
        });

        link_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegist = new Intent(LoginActivity.this, RegisterActivity.class);
            }
        });
    }

    private void Login(final String email, final String password){

        loading.setVisibility(View.VISIBLE);
        login_btn.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL_LOGIN,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals('1')) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonObject.getJSONObject(String.valueOf(i));

                                    String name = object.getString("name").trim();
                                    String password = object.getString("password").trim();

                                    Toast.makeText(LoginActivity.this, "Login Berhasil!. Nama Anda : "
                                            +name+ "Email Anda : "
                                            +email,Toast.LENGTH_SHORT)
                                            .show();

                                    loading.setVisibility(View.GONE);


                                }
                            }
                        } catch (JSONException e){
                            loading.setVisibility(View.GONE);
                            login_btn.setVisibility(View.GONE);
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Error" .toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        loading.setVisibility(View.GONE);
                        login_btn.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Error" +error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map <String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params. put("email", email);
                params. put("password", password);
                return super.getParams();
        }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
