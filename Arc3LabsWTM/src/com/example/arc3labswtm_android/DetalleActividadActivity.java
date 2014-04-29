package com.example.arc3labswtm_android;

import java.net.MalformedURLException;
import java.util.List;

import com.example.arc3labswtm_android.adapters.ActivitiesAdapter;
import com.example.arc3labswtm_android.adapters.DetalleActividadAdapter;
import com.example.arc3labswtm_android.customs.ActivitiesFragment;
import com.example.arc3labswtm_android.customs.ActivitiesFragment.ProgressFilter;
import com.example.arc3labswtm_android.model.Activities;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.os.Build;

public class DetalleActividadActivity extends ActionBarActivity 
{
	private MobileServiceClient mClient;
	private MobileServiceTable<Activities> mActivitiesTable;
	private DetalleActividadAdapter mAdapter;
	String master = null;
	String parameter = null;
	String parameter2 = null;
	public final static String EDIT_PARAMETER = "com.example.arc3labswtm_android.EDIT";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_detalle_actividad);

		try 
		{
			mClient = new MobileServiceClient("https://arc3labswtm.azure-mobile.net/",
					"NWDqSfBULLlqUdVEhYpdvrnULGAYIx94", 
					this);
			mActivitiesTable = mClient.getTable(Activities.class);
		} 
	  
		catch (MalformedURLException e) 
		{
			createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}
		
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
	    parameter = getIntent().getStringExtra(ActivitiesFragment.EXTRA_PARAMETER);
	    if(parameter != null)
	    {
	    	master = parameter;
	    }
	    
	    parameter2 = getIntent().getStringExtra(EditaActividadActivity.RETURN_PARAMETER);
	    if(parameter2 != null)
	    {
	    	master = parameter2; 
	    }
	    
	    
	    mAdapter = new DetalleActividadAdapter(this, R.layout.act_detalle);
	    ListView listDetalle = (ListView) findViewById(R.id.detalleActividad);
	    listDetalle.setAdapter(mAdapter);
	    
	    getData();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{

		
//		getMenuInflater().inflate(R.menu.detalle_actividad, menu);
//		return true;
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.detalle_actividad, menu);
		return super.onCreateOptionsMenu(menu);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
        {
        case R.id.editaActividad_menu:
        	Intent intent = new Intent(this, EditaActividadActivity.class);
    		intent.putExtra(EDIT_PARAMETER, parameter);
    		startActivity(intent);     
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

	private void createAndShowDialog(Exception exception, String title) 
	{
		createAndShowDialog(exception.toString(), title);
	}

	private void createAndShowDialog(String message, String title) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}
	
	private void getData() 
	{
		mActivitiesTable.where().field("Id").eq(master).execute(new TableQueryCallback<Activities>() 
		{

			public void onCompleted(List<Activities> result, int count, Exception exception, ServiceFilterResponse response) {
				if (exception == null) 
				{
					mAdapter.clear();

					for (Activities item : result) 
					{
						mAdapter.add(item);
//						Log.i(TAG, "Titulo: " + item.getTitle());
					}
				} 
				else 
				{
					createAndShowDialog(exception, "Error");
				}
			}
		});
	}
	
	public void editaAct(View view)
	{
		Intent intent = new Intent(this, EditaActividadActivity.class);
		intent.putExtra(EDIT_PARAMETER, parameter);
		startActivity(intent);
	}

}
