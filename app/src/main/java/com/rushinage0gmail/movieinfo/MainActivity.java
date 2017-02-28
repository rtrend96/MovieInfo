package com.rushinage0gmail.movieinfo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText etquery;
    Button btnsearch;
    TextView tvdata;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etquery = (EditText) findViewById(R.id.etquery);
        btnsearch = (Button) findViewById(R.id.btnsearch);
        tvdata = (TextView) findViewById(R.id.tvdata);


        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MovieName = etquery.getText().toString();
                if (MovieName.length()== 0){
                    Toast.makeText(getApplicationContext(),"Name cannot be empty",Toast.LENGTH_LONG).show();
                    etquery.requestFocus();
                    return ;

                }
                else {
                    Task1 t1= new Task1();
                    t1.execute("http://www.omdbapi.com/?s=" + MovieName);
                }
            }
        });

    }

    class Task1 extends AsyncTask<String, Void, String > {
             String jsonData = "", line="", data = "";
        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url= new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();

                InputStream is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine())!= null){
                    jsonData += line +"\n";
                }

                if(jsonData != null){
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray movieArray = jsonObject.getJSONArray("Search");
                    for(int i=0; i<movieArray.length(); i++){
                        JSONObject movie = movieArray.getJSONObject(i);
                        String title = movie.getString("Title");
                        String year = movie.getString("Year");

                        data += title +"  " +year + "\n";
                    }

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
                 return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvdata.setText(s);
        }
    }

}
