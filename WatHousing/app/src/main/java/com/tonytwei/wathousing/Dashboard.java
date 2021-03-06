package com.tonytwei.wathousing;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Dashboard extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        int value = 0;
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            value = extras.getInt("role");
        }
        if(value == 0){
            Button but = (Button)findViewById(R.id.addlisting);
            Button but1 = (Button)findViewById(R.id.viewalllistings);
            but.setVisibility(View.INVISIBLE);
            but1.setVisibility(View.VISIBLE);
        }
        else if(value == 1){
            Button but = (Button)findViewById(R.id.viewalllistings);
            Button but1 = (Button)findViewById(R.id.addlisting);
            but.setVisibility(View.VISIBLE);
            but1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
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

    public void viewListing(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, HousingListActivity.class);
        getIntent().removeExtra("search");
        getIntent().removeExtra("searchkey");
        intent.putExtra("search", 0);
        startActivity(intent);
    }

    public void addListing(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, AddListingActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
