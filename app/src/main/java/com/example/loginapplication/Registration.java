package com.example.loginapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private static final String API_SIGNIN = "https://ia219vugx9.execute-api.us-east-1.amazonaws.com/production/user/signup";
    private static final String API_Verify = "https://ia219vugx9.execute-api.us-east-1.amazonaws.com/production/user/confirmation";

    private Button registerBtn,signInBtn;
    private EditText username,password,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        registerBtn = (Button) findViewById(R.id.register);
        signInBtn = (Button) findViewById(R.id.loginIn);
        registerBtn.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                String username1 = username.getText().toString();
                String password1 = password.getText().toString();
                String email1 = email.getText().toString();
                String[] paramenters = new String[4];
                paramenters[0] = username1;
                paramenters[1] = password1;
                paramenters[2] = email1;
                paramenters[3] = API_SIGNIN;
                Registration.WebService temp = new Registration.WebService();
                temp.execute(paramenters);
                break;

            case R.id.loginIn:
                shouLoginPage();
                break;
        }

    }

    private void openOverlay() {

        View overlayView = getLayoutInflater().inflate(R.layout.overlay, null);

        // Find the EditText and Button views in the overlay layout
        EditText editText = overlayView.findViewById(R.id.email_code);
        Button submitButton = overlayView.findViewById(R.id.button_submit);

        // Create a dialog to show the overlay view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(overlayView);
        AlertDialog dialog = builder.create();

        // Set an OnClickListener on the submit button to handle text submission
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textInput = editText.getText().toString();
                String[] paramenters = new String[3];
                paramenters[0] = username.getText().toString();
                paramenters[1] = textInput;
                paramenters[3] = API_Verify;
                Registration.WebServiceNew temp = new Registration.WebServiceNew();
                temp.execute(paramenters);

                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void shouLoginPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class WebService extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
                if(jsonobj.getString("message").equals("Success")){
                    System.out.println("Hi   Success");
                    openOverlay();
                    //findViewById(R.id.hidden_layout).setVisibility(View.VISIBLE);

                }else{
                    System.out.println("Please Try again");
                    //openOverlay();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        protected String doInBackground(String... inputs) {
            JSONObject json = new JSONObject();
            try {
                String username = inputs[0];
                String password = inputs[1];
                String email = inputs[2];
                String apiUrl = inputs[3];

                json.put("username",username);
                json.put("password",password);
                json.put("email",email);
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
                    //bread.readLine();
                    String temp,responseString = "";

                    while ((temp = bread.readLine()) != null){
                        responseString+= temp;
                    }
                     return responseString;

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

    private class WebServiceNew extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
                if(jsonobj.getString("message").equals("Success")){
                    System.out.println("Confim   Success");
                    //openOverlay();
                    //findViewById(R.id.hidden_layout).setVisibility(View.VISIBLE);
                    shouLoginPage();
                }else{
                    System.out.println("Please Try again");
                    //openOverlay();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        protected String doInBackground(String... inputs) {
            JSONObject json = new JSONObject();
            try {
                String username = inputs[0];
                String code = inputs[1];
                String apiUrl = inputs[2];

                json.put("username",username);
                json.put("code",code);
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
                    //bread.readLine();
                    String temp,responseString = "";

                    while ((temp = bread.readLine()) != null){
                        responseString+= temp;
                    }
                    return responseString;

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