package com.tonytwei.wathousing;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tonytwei.wathousing.dummy.DummyContent;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * An activity representing a single Housing detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link HousingListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link HousingDetailFragment}.
 */    
public class HousingDetailActivity extends ActionBarActivity {

    private DummyContent.DummyItem mItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housing_detail);

        // Show the Up button in the action bar.
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().hide();

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(HousingDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(HousingDetailFragment.ARG_ITEM_ID));
            HousingDetailFragment fragment = new HousingDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.housing_detail_container, fragment)
                    .commit();
        }


        new HttpAsyncTask().execute("http://mdguo.com/api/getReview.php");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, HousingListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void WriteReview(View view) {
        Intent i = new Intent(this, WriteReview.class);
        TextView txt = (TextView)findViewById(R.id.housing_detail);
        String listingid = txt.getText().toString().split(":")[0];
        getIntent().removeExtra("listingid");
        i.putExtra("listingid", listingid);
        startActivity(i);
    }


    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //((TextView)findViewById(R.id.reviews)).setText(result);

            try {
                String reviews = "";
                String avg_rating = "";
                String rating = "";
                JSONArray mJsonArray = new JSONArray(result);

                TextView frag = (TextView)findViewById(R.id.housing_detail);
                String id = frag.getText().toString().split(":")[0];
                float total = 0;
                float count = 0;
                float avg = 0;

                for(int i = 0; i < mJsonArray.length(); i++) {
                    JSONObject obj = mJsonArray.getJSONObject(i);
                    if(obj.getInt("listingId") == Integer.parseInt(id)) {
                        for(int j = 0;  j <5; j++)
                        {
                            if (j >= (5 - obj.getInt("rating"))){
                                rating += "*";
                            }
                            else {
                                //rating += "_";
                                rating += "  ";
                            }
                        }
                        reviews += rating + " :    " + obj.getString("comments") + "\n";
                        count++;
                        total += obj.getInt("rating");
                    }
                    rating = "";
                }
                reviews += "\n";
                avg = total/count;

                RatingBar ratingBar = (RatingBar) findViewById(R.id.avg_rating);
                ratingBar.setRating(avg);

                TextView txt = (TextView)findViewById(R.id.reviews);
                txt.setText(reviews);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here

        new HttpAsyncTask().execute("http://mdguo.com/api/getReview.php");

    }
}
