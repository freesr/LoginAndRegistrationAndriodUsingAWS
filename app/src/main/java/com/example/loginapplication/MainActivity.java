package com.example.loginapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String API_SIGNIN = "https://9qcp5y13z2.execute-api.us-east-1.amazonaws.com/prod/user/signin";
    private Button loginBtn;
    private EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                String username1 = username.getText().toString();
                String password1 = password.getText().toString();
                String[] paramenters = new String[3];
                paramenters[0] = username1;
                paramenters[1] = password1;
                paramenters[2] = API_SIGNIN;
                WebService temp = new WebService();
                temp.execute(paramenters);
                break;
        }

    }

    private class WebService extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... inputs) {
           //String apiUrl = "https://9qcp5y13z2.execute-api.us-east-1.amazonaws.com/prod/user/signin";
            JSONObject json = new JSONObject();
            try {
                String username = inputs[0];
                String password = inputs[1];
                String apiUrl = inputs[2];

                json.put("username",username);
                json.put("password",password);
                String jsonstring = json.toString();
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.getDoOutput();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(),"utf-8"));
                writer.write(jsonstring);
                writer.flush();
                writer.close();

                if (urlConnection.getResponseCode() == 200){
                    BufferedReader bread = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8"));
                    bread.readLine();
                    String temp,responseString = "";

                    while ((temp = bread.readLine()) != null){
                        responseString+= temp;
                    }
                    JSONObject readObj =  new JSONObject();
                    readObj.put("Content",responseString);
                    //Toast.makeText(, "Logged In", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        }
    }
}