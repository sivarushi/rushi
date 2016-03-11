package com.restaurant;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.restaurant.parser.JSONParser;

public class MainActivity extends Activity implements OnClickListener {
	private static String url = "http://10.251.163.5:8088/MFRPServices/login";

	ProgressDialog pDialog;
	EditText etusername,etpassword;
	Button btlogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		etusername=(EditText)findViewById(R.id.username_edit);
		etpassword=(EditText)findViewById(R.id.password_edit);
		
		btlogin=(Button)findViewById(R.id.login_button);
		btlogin.setOnClickListener(this);
	}

	

	

	/**
	 * Background Async Task to Create new product
	 * */
	class login extends AsyncTask<String, String, Boolean> {
		private static final String TAG_SUCCESS = "registered";
		JSONParser jsonParser = new JSONParser();

		private static final String TAG = "login";

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Checking please wait..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected Boolean doInBackground(String... args) {
			try{
				String username = etusername.getText().toString();
				String password=etpassword.getText().toString();

				String json = "";

				// 3. build jsonObject
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("user_id", username);
				jsonObject.accumulate("password", password);


				// 4. convert JSONObject to JSON to String
				json = jsonObject.toString();

				// getting JSON Object
				// Note that create product url accepts POST method
				JSONObject json1 = jsonParser.makeHttpRequest(url,
						"POST", json);

				// check log cat fro response
				Log.d("Create Response", json1.toString());

				Log.i(TAG, ""+json1.toString());
				String success = json1.getString("status");
				if(success.equals("Success"))
					return true;

			}
			catch(Exception e)
			{

			}

			return false;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(Boolean result) {
			// dismiss the dialog once done
			pDialog.dismiss();

			if(result)
			{
				Intent i=new Intent(MainActivity.this,custinfo.class);
				startActivity(i);

			}
			else
			{
				Toast.makeText(getApplicationContext(), "Wrong id or password", Toast.LENGTH_SHORT).show();
			}


		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		new login().execute();

	}
}
