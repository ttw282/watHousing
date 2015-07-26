package com.tonytwei.wathousing;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class AddListingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void postData(String valueIWantToSend) {
        HttpClient httpClient = new DefaultHttpClient();
        // replace with your url
        HttpPost httpPost = new HttpPost(valueIWantToSend);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            // write response to log
            Log.d("Http Post Response:", response.toString());
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
    }
    public void addListing(View view) {
        EditText address1 = (EditText)findViewById(R.id.address);
        EditText contact1 = (EditText)findViewById(R.id.contact);
        EditText name1 = (EditText)findViewById(R.id.name);
        EditText pcode1 = (EditText)findViewById(R.id.pcode);
        EditText rent1 = (EditText)findViewById(R.id.rent);
        String address = address1.getText().toString();
        String contact = contact1.getText().toString();
        String name = name1.getText().toString();
        String pcode = pcode1.getText().toString();
        String rent = rent1.getText().toString();

        new MyAsyncTask().execute("http://mdguo.com/api/postListing.php?addr=" + address + "&contact=" + contact + "&name="+name + "&pcode=" + pcode + "&rent="+rent );
        finish();
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData(params[0]);
            return null;
        }

        protected void onPostExecute(Double result) {
            Toast.makeText(getApplicationContext(), "Listing Posted!", Toast.LENGTH_LONG).show();
        }
    }
}
