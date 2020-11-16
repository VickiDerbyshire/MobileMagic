package com.example.umar1_mdproject_mtg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MTG_Search extends AppCompatActivity {

    //https://api.scryfall.com/cards/search?q=vampire&unique=cards&as=full&order=name

    //https://api.scryfall.com/cards/search?q=[CARD+NAME]&unique=cards&as=full&order=name

    public String BroadCardSearch_apiURL = "https://api.scryfall.com/cards/search?q=";
    public String BCS_endof_apiURL = "&unique=cards&as=full&order=name";
    public String Search;
    String query = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mtg_search);

    }

    public void MTG_Search(View view) {

        EditText txtSearch = (findViewById(R.id.txtSearch));
        Search = txtSearch.getText().toString();

        try {
            query = BroadCardSearch_apiURL + URLEncoder.encode(Search, "UTF-8") + BCS_endof_apiURL;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        JSONTask myTask = new JSONTask();
        myTask.execute(query);
    }


    class JSONTask extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        private LinearLayout Imagesss;

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.connect();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    InputStream stream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    return buffer.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                conn.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "No country specified", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONArray CardArray;

                if (jsonObject.isNull("data")) {
                    return;
                }

                CardArray = jsonObject.getJSONArray("data");
                TextView currentView = findViewById(R.id.lblViewCards);
                currentView.setText("");

                this.Imagesss = (LinearLayout) findViewById(R.id.Imagess);

                for (int i = 0; i < CardArray.length(); i++) {

                    JSONObject obj = CardArray.getJSONObject(i);

                    currentView.append("Card Name: " + String.valueOf(obj.getString("name")));
                    currentView.append("\nCard ID: " + String.valueOf(obj.getString("id")));
                    currentView.append("\nSet Name: " + String.valueOf(obj.getString("set_name")));
                    currentView.append("\nColor: " + String.valueOf(obj.getString("border_color")));
                    currentView.append("\nUSD Price: $" + String.valueOf(obj.getJSONObject("prices").getDouble("usd")));
                    currentView.append("\nimg URL: " + String.valueOf(obj.getJSONObject("image_uris").getString("small")) + "\n\n");

                    /*
                    //int drawable = getResources().getIdentifier("a"+i, "drawable", getPackageName());
                    ImageView myImageView = new ImageView(MTG_Search.this);
                    InputStream URLcontent = (InputStream) new URL(String.valueOf(obj.getJSONObject("image_uris").getString("small"))).getContent();
                    Drawable imagez = Drawable.createFromStream(URLcontent, "cards");
                    myImageView.setImageDrawable(imagez);
                    Imagesss.addView(myImageView);
                     */

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void Home_View(View view) {
        Intent openHome = new Intent(getApplicationContext(), Home.class);
        startActivity(openHome);
    }
}


