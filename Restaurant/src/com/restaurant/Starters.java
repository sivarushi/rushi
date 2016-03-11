package com.restaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class Starters extends Fragment {
	
	Food[] fd;
	private static final String tag = "Startvegpoc";
	private String jsonResult;
	private String url = "http://10.0.2.2:2020/restraunt/itemlist.php";
	
	ListView lv;
	ListView lv2;
	TextView tv1;
	TextView tv2;
	
	ArrayList<Food> foodList = new ArrayList<Food>();
	ArrayList<Food> foodList2 = new ArrayList<Food>();
	
	ArrayList<String> starterordr1 = new ArrayList<String>();
	//ArrayList<String> starterordr2 = new ArrayList<String>();
	
	String str[];
	int count=0;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		View v = super.onCreateView(inflater, container, savedInstanceState);
		lv =(ListView)v.findViewById(R.id.listView1);
		lv2 = (ListView) v.findViewById(R.id.listView2);
		accessWebService();
		tv1= (TextView)v.findViewById(R.id.textView1);
		tv2= (TextView)v.findViewById(R.id.textView2);
		tv1.setText("VEGETARIAN");
		tv2.setText("Non-VEGETARIAN");
		Button btn = (Button)v.findViewById(R.id.button1);
		//accessWebServicenonveg();
		Log.i("finished", "access web service");
		//fd = new Food[5];
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				/*Food country = (Food) parent.getItemAtPosition(position);
				Toast.makeText(getApplicationContext(),
						"Clicked on Row: " + country.getName(), 
						Toast.LENGTH_LONG).show();*/
				String item=foodList.get(position).getName();
				Log.i("List1","items added"+item);
				Toast.makeText(getActivity(), "items"+item, Toast.LENGTH_SHORT).show();
				starterordr1.add(item);
				Toast.makeText(getActivity(),
						"Clicked on Row: " + item, 
						Toast.LENGTH_LONG).show();
			}
		});
		
		lv2.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				//Food country = (Food) parent.getItemAtPosition(position);
				String item=foodList2.get(position).getName();
				//starterordr2.add(item);
				Toast.makeText(getActivity(),
						"Clicked on Row: " + item, 
						Toast.LENGTH_LONG).show();
			}
		});
		
		 btn.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
            	 Log.i("gona","startactivity final order");
            	 Toast.makeText(getActivity(), "Stratr"+starterordr1.size(), Toast.LENGTH_SHORT).show();
                 // TODO Auto-generated method stub
                 Intent i = new Intent(getActivity(),Finalorder.class);
                 i.putStringArrayListExtra("strings",starterordr1 );
                 Toast.makeText(getActivity(), "Stratr"+starterordr1.size(), Toast.LENGTH_SHORT).show();
                 startActivity(i);
                 Log.i("started activity","final order");
             }
         });
		return v;
	}
private void displayListView() {
		
		Log.i(tag, "displayListView");
		
		
	
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("item");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				String name = jsonChildNode.optString("veg");
				String name2 = jsonChildNode.optString("non-veg");
				
				
				//for veg list
				Food menu = new Food(name,0);
				Log.i(tag, "displayListViewcompleted");
				foodList.add(menu);
				//Toast.makeText(getApplicationContext(), "items"+foodList, Toast.LENGTH_SHORT).show();
				
				//for non-veg list
				Food non_veg_menu = new Food(name2,0);
				Log.i(tag, "displayListView non-veg completed");
				foodList2.add(non_veg_menu);
				//Toast.makeText(getApplicationContext(), "items"+foodList2.toString(), Toast.LENGTH_SHORT).show();
				
				
			}
		} catch (JSONException e) {
			Toast.makeText(getActivity(), "Error" + e.toString(),
					Toast.LENGTH_SHORT).show();
		}


		//create an ArrayAdaptar from the String Array
		CustomAdapter ca= new CustomAdapter(getActivity(),foodList);
		
		// Assign adapter to ListView
		lv.setAdapter(ca);
		
		
		CustomAdapter2 ca2= new CustomAdapter2(getActivity(),foodList2);
		
		// Assign adapter to ListView
		lv2.setAdapter(ca2);


	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Async Task to access the web
		private class JsonReadTask extends AsyncTask<String, Void, String> {
			@Override
			protected String doInBackground(String... params) {
				Log.i(tag, "doInBackground");
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(params[0]);
				try {
					HttpResponse response = httpclient.execute(httppost);
					jsonResult = inputStreamToString(
							response.getEntity().getContent()).toString();
				}

				catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			private StringBuilder inputStreamToString(InputStream is) {
				String rLine = "";
				StringBuilder answer = new StringBuilder();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));

				try {
					while ((rLine = rd.readLine()) != null) {
						answer.append(rLine);
					}
				}

				catch (IOException e) {
					// e.printStackTrace();
					Toast.makeText(getActivity(),
							"Error..." + e.toString(), Toast.LENGTH_LONG).show();
				}
				return answer;
			}

			@Override
			protected void onPostExecute(String result) {
				displayListView();
			}
		}// end async task
		public void accessWebService() {
			Log.i(tag, "accessWebService");
			JsonReadTask task = new JsonReadTask();
			
			task.execute(new String[] { url });
		}


		

		// build hash set for list view
		
		
}