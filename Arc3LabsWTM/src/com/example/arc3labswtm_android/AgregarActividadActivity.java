package com.example.arc3labswtm_android;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.arc3labswtm_android.adapters.ActivitiesAdapter;
import com.example.arc3labswtm_android.adapters.CatSpinnerAdapter;
import com.example.arc3labswtm_android.adapters.ProSpinnerAdapter;
import com.example.arc3labswtm_android.customs.ActivitiesFragment;
import com.example.arc3labswtm_android.customs.Validation;
import com.example.arc3labswtm_android.model.Activities;
import com.example.arc3labswtm_android.model.Categories;
import com.example.arc3labswtm_android.model.Projects;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
//import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class AgregarActividadActivity extends ActionBarActivity 
{
	
	private MobileServiceClient mClient;
	private MobileServiceTable<Activities> mActivitiesTable;
	private MobileServiceTable<Categories> mCategoriesTable;
	private MobileServiceTable<Projects> mProjectsTable;
	
	private CatSpinnerAdapter spAdapter;
	private ProSpinnerAdapter prAdapter;
	String parameter = null;
	Date pickedDate = null;
	
	DatePicker pickerDate;
	private MenuItem menuItem;
	private ProgressBar mProgressBar;
	
	private EditText mTxtTitulo;
	private EditText mTxtDescr;
	String categryId = null;
	String projectId = null;

	Spinner spEstado;
	Spinner spProjects;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_agregar_actividad);
		
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setCustomView(R.layout.actionview_progress_bar);
	    
	    mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
	    
	    
	    try 
		{
			mClient = new MobileServiceClient(
					"https://arc3labswtm.azure-mobile.net/",

					"NWDqSfBULLlqUdVEhYpdvrnULGAYIx94", 
					this)
//			.withFilter(new ProgressFilter())
			;
			mActivitiesTable = mClient.getTable(Activities.class);
			mCategoriesTable = mClient.getTable(Categories.class);
			mProjectsTable = mClient.getTable(Projects.class);
		} 
	    catch (MalformedURLException e) 
	    {
	    	createAndShowDialogExc(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}
	    
	    parameter = getIntent().getStringExtra(ActivitiesFragment.EXTRA_PARAMETER);
	    
	    pickerDate = (DatePicker) findViewById(R.id.datePicker1);
	    
	    getDateFromDatePicket(pickerDate);
	    
	    
	   

	    mTxtTitulo = (EditText) findViewById(R.id.txtTitulo);
	    mTxtTitulo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(mTxtTitulo);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
	    mTxtDescr = (EditText) findViewById(R.id.txtDescripcion);
	    getCategories();
	    getProjects();
	    
	    spAdapter = new CatSpinnerAdapter(this, R.layout.cat_spinner_row, R.id.category);
	    spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spEstado = (Spinner)this.findViewById(R.id.spEstado);
	    spEstado.setAdapter(spAdapter);
	    spEstado.setOnItemSelectedListener(new OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
	        {
	        	categryId = ((Categories) parentView.getItemAtPosition(position)).getId();
	        	
	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> parentView) {
	            // your code here
	        }

	    });
	    
	    prAdapter = new ProSpinnerAdapter(this, R.layout.pro_spinner_row, R.id.project);
	    prAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spProjects = (Spinner)this.findViewById(R.id.spProyecto);
	    spProjects.setAdapter(prAdapter);
	    spProjects.setOnItemSelectedListener(new OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
	        {
	        	projectId = ((Projects) parentView.getItemAtPosition(position)).getId();
	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> parentView) {
	            // your code here
	        }

	    });
	    
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.agregar_actividad, menu);

	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.agregarActividad_menu) 
		{
			menuItem = item;
//			menuItem.setActionView(R.layout.actionview_progress_bar);
//			menuItem.expandActionView();
			addActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void createAndShowDialogExc(Exception exception, String title) 
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
	
	public void addActivity()
	{
		if (checkValidation())
		{
			menuItem.setActionView(R.layout.actionview_progress_bar);
			menuItem.expandActionView();
			
			Activities item = new Activities();
			item.setTitle(mTxtTitulo.getText().toString());
			item.setDescription(mTxtDescr.getText().toString());
			item.setFechaFinalizacion(getDateFromDatePicket(pickerDate));
			item.setIdCategoria(categryId);
			item.setIdProyecto(projectId);
			item.setIdReport("idReporte");
			mActivitiesTable.insert(item, new TableOperationCallback<Activities>() {
		
				public void onCompleted(Activities entity, Exception exception, ServiceFilterResponse response) {
					
					if (exception == null) 
					{
//						createAndShowDialog("Actividad agregada con éxito", "¡Éxito!");
						AlertDialog alertDialog = new AlertDialog.Builder(AgregarActividadActivity.this).create();
						alertDialog.setTitle("¡ÉXITO!");
						alertDialog.setMessage("Actividad agregada exitosamente");
						alertDialog.setButton("OK", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which) {
								Intent myIntent = new Intent(AgregarActividadActivity.this, MainActivity.class);
								myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
								startActivity(myIntent);
								finish();
								return;
						}
						});
						alertDialog.show();
					} 
					else 
					{		
					}
				}
			});
		}
            
        else
        {
        	Toast.makeText(this, "Debe llenar los campos requeridos", Toast.LENGTH_LONG).show();
        }
            
	}

	private void getCategories()
	{
		mCategoriesTable.execute(new TableQueryCallback<Categories>() 
		{
			public void onCompleted(List<Categories> result, int count, Exception exception, ServiceFilterResponse response) 
			{
				if (exception == null) 
				{
					for (Categories item : result) 
					{
						spAdapter.add(item);
					}
				} 
				else 
				{
					createAndShowDialogExc(exception, "Error");
				}
			}
		});
	}
	
	private void getProjects()
	{
		mProjectsTable.execute(new TableQueryCallback<Projects>() 
		{
			public void onCompleted(List<Projects> result, int count, Exception exception, ServiceFilterResponse response) 
			{
				if (exception == null) 
				{
					for (Projects item : result) 
					{
						prAdapter.add(item);
					}
				} 
				else 
				{
					createAndShowDialogExc(exception, "Error");
				}
			}
		});
	}
	
	private boolean checkValidation() 
	{
        boolean ret = true;
 
        if (!Validation.hasText(mTxtTitulo)) ret = false;
        return ret;
    }
	
	private class ProgressFilter implements ServiceFilter 
	{
		
		@Override
		public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
				final ServiceFilterResponseCallback responseCallback) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mProgressBar.setVisibility(ProgressBar.VISIBLE);
				}
			});
			
			nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {
				
				@Override
				public void onResponse(ServiceFilterResponse response, Exception exception) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mProgressBar.setVisibility(ProgressBar.GONE);
						}
					});
					
					responseCallback.onResponse(response, exception);
				}
			});
		}
	}
	
	public static java.util.Date getDateFromDatePicket(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
	
}
