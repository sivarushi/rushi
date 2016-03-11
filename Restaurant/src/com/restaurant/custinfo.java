package com.restaurant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class custinfo extends Activity {
	EditText cust_name,cust_email,cust_phone,cust_address; 
	Button save;
	ModelClass mClass;
	DatabaseHelper cust_Helper;
	public static String c_name,c_mail, c_phone ;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.custinfo);
		try{
			cust_Helper = new DatabaseHelper(custinfo.this);
			//cust_Helper.getWritableDatabase();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		//load form widget
		mClass = new ModelClass();
		save=(Button)findViewById(R.id.Save);
		cust_name = (EditText)findViewById(R.id.details_name_edit);
		cust_email =(EditText)findViewById(R.id.details_email_edit);
		cust_phone =(EditText)findViewById(R.id.details_phone_edit);




		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				c_name = cust_name.getText().toString();
				//System.out.println(c_name);
				c_mail = cust_email.getText().toString();
				c_phone = cust_phone.getText().toString();
				;
				mClass.setName(c_name);
				mClass.setEmail(c_mail);
				mClass.setPhone(c_phone);

				if(c_name.equals("")||c_mail.equals("")||c_phone.equals("") ){
					Toast.makeText(custinfo.this, "All fields are Mandatory", Toast.LENGTH_LONG).show(); 
				}
				else{
					Intent i =new Intent(custinfo.this,Tabview.class);
					Bundle bundle = new Bundle();
					bundle.putString("cust_name", c_name);
					i.putExtras(bundle);
					startActivity(i);
					//Intent intent = new Intent(this, Startervegpoc.class);

					String data1 = cust_name.getText().toString();
					String data2 = cust_email.getText().toString();
					String data3 = cust_phone.getText().toString();

					File folder = getCacheDir();
					File myfile = new File(folder,"mydata.txt");
					FileOutputStream fileoutputstream = null;
					try {
						fileoutputstream = new FileOutputStream(myfile);
						fileoutputstream.write(data1.getBytes());
						fileoutputstream.write(data2.getBytes());
						fileoutputstream.write(data3.getBytes());
						Toast.makeText(getApplicationContext(), "data written to file",
								Toast.LENGTH_SHORT).show();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally
					{
						if(fileoutputstream != null)
						{
							try {
								fileoutputstream.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}		
						}
					}
				}

			}

		});


	}
	public void onResume(){
		super.onResume();
		cust_Helper = new DatabaseHelper(custinfo.this);
		//cust_Helper.getWritableDatabase();
	}
	public void onStop(){
		super.onStop();
		cust_Helper.close();

	}

}
