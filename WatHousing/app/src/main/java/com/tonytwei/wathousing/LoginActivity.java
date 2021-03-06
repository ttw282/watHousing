package com.tonytwei.wathousing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.media.tv.TvInputService;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.kmiller.facebookintegration.SessionStore;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor> {

    public static final String mAPP_ID = "389012777962684";
    public Facebook mFacebook = new Facebook(mAPP_ID);
    /**
     * A dummy authentication store containing known user names and passwords..
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        ((Button)findViewById(R.id.LoginButton)).setOnClickListener(loginButtonListener);
        SessionStore.restore(mFacebook, this);
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password) || TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);


           new HttpAsyncTask().execute("http://mdguo.com/api/authenticate.php?username=" + email + "&password=" + password);


            /*TextView txt = ((TextView)findViewById(R.id.hidden));
            String result = txt.getText().toString();
            if(result == "1") {
                Intent intent = new Intent(this, Dashboard.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }*/
        }
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
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
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
            if(result == "1") {
                Toast.makeText(getBaseContext(), "Login Successful!", Toast.LENGTH_LONG).show();
            }
            else if(result == "0") {
                Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_LONG).show();
            }
            Message msg = Message.obtain();
            msg.obj = result;
            msg.setTarget(myHandler);
            msg.sendToTarget();
        }
    }

    Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int success = Integer.parseInt(msg.obj.toString().split(" ")[0]);
            int role = Integer.parseInt(msg.obj.toString().split(" ")[1]);
            if(success == 0) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                getIntent().removeExtra("role");
                startActivity(intent);
                finish();
            }
            else if(success == 1){
                Intent intent1 = new Intent(getBaseContext(), Dashboard.class);
                getIntent().removeExtra("role");
                intent1.putExtra("role", role);
                startActivity(intent1);
                finish();
            }
        }
    };


    public void signUp(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    //----------------------------------------------
    // loginButtonListener
    //----------------------------------------------
    private OnClickListener loginButtonListener = new OnClickListener() {
        public void onClick( View v ) {


            if( !mFacebook.isSessionValid() ) {
                Toast.makeText(LoginActivity.this, "Authorizing", Toast.LENGTH_SHORT).show();
                //mFacebook.authorize(LoginActivity.this, new String[]{""}, new LoginDialogListener());

                mFacebook.authorize(LoginActivity.this, new String[]{""}, new Facebook.DialogListener() {
                    @Override
                    public void onComplete(Bundle values) {
                        try {
                            //The user has logged in, so now you can query and use their Facebook info
                            JSONObject json = Util.parseJson(mFacebook.request("me"));
                            String facebookID = json.getString("id");
                            String firstName = json.getString("first_name");
                            String lastName = json.getString("last_name");
                            Toast.makeText( LoginActivity.this, "Thank you for Logging In, " + firstName + " " + lastName + "!", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getBaseContext(), Dashboard.class);
                            startActivity(intent1);
                            finish();
                            //SessionStore.save(mFacebook, LoginActivity.this);
                        }
                        catch( Exception error ) {
                            Toast.makeText( LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                        catch( FacebookError error ) {
                            Toast.makeText( LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFacebookError(FacebookError e) {
                        Toast.makeText( LoginActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(DialogError e) {
                        Toast.makeText( LoginActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
            else {
                Toast.makeText( LoginActivity.this, "Has valid session", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject json = Util.parseJson(mFacebook.request("me"));
                    String facebookID = json.getString("id");
                    String firstName = json.getString("first_name");
                    String lastName = json.getString("last_name");
                    Toast.makeText(LoginActivity.this, "You already have a valid session, " + firstName + " " + lastName + ". No need to re-authorize.", Toast.LENGTH_SHORT).show();

                }
                catch( Exception error ) {
                    Toast.makeText( LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
                catch( FacebookError error ) {
                    Toast.makeText( LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    //***********************************************************************
    //***********************************************************************
    // LoginDialogListener
    //***********************************************************************
    //***********************************************************************
    public final class LoginDialogListener implements Facebook.DialogListener {
        public void onComplete(Bundle values) {
            try {
                //The user has logged in, so now you can query and use their Facebook info
                JSONObject json = Util.parseJson(mFacebook.request("me"));
                String facebookID = json.getString("id");
                String firstName = json.getString("first_name");
                String lastName = json.getString("last_name");
                Toast.makeText( LoginActivity.this, "Thank you for Logging In, " + firstName + " " + lastName + "!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getBaseContext(), Dashboard.class);
                startActivity(intent1);
                finish();
                //SessionStore.save(mFacebook, LoginActivity.this);
            }
            catch( Exception error ) {
                Toast.makeText( LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
            catch( FacebookError error ) {
                Toast.makeText( LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        public void onFacebookError(FacebookError error) {
            Toast.makeText( LoginActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
        }

        public void onError(DialogError error) {
            Toast.makeText( LoginActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
        }

        public void onCancel() {
            Toast.makeText( LoginActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
        }
    }
}

