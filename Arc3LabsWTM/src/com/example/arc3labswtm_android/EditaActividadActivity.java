package com.example.arc3labswtm_android;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.Build;

public class EditaActividadActivity extends ActionBarActivity 
{
	private MobileServiceClient mClient;
	private MobileServiceTable<Activities> mActivitiesTable;
	private MobileServiceTable<Categories> mCategoriesTable;
	private MobileServiceTable<Projects> mProjectsTable;
	
	private CatSpinnerAdapter spAdapter;
	private ProSpinnerAdapter prAdapter;
	
	public final static String RETURN_PARAMETER = "com.example.arc3labswtm_android.RET_PARAMETER";
	
	Activities currentItem;
	
	private EditText mTxtTitulo;
	private EditText mTxtDescr;
	String categryId = null;
	String projectId = null;
	String parameter = null;
	Date pickedDate = null;
	DatePicker pickerDate;
	
	List<Categories> lista = new ArrayList<Categories>();
	
	String titulo = null;
	String descrp = null;
	String catId = null;
	String proId = null;

	Spinner spEstado;
	Spinner spProjects;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_edita_actividad);

		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    try 
		{
			mClient = new MobileServiceClient(
					"https://arc3labswtm.azure-mobile.net/",
					"NWDqSfBULLlqUdVEhYpdvrnULGAYIx94", 
					this);
			mActivitiesTable = mClient.getTable(Activities.class);
			mCategoriesTable = mClient.getTable(Categories.class);
			mProjectsTable = mClient.getTable(Projects.class);
		} 
	    catch (MalformedURLException e) 
	    {
	    	createAndShowDialogExc(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}
	    
	    parameter = getIntent().getStringExtra(DetalleActividadActivity.EDIT_PARAMETER);
	    
	    pickerDate = (DatePicker) findViewById(R.id.date_edita_Picker1);
	    
	    getDateFromDatePicket(pickerDate);
	    
	    mTxtTitulo = (EditText) findViewById(R.id.txt_edita_Titulo);
	    mTxtTitulo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Validation.hasText(mTxtTitulo);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
	    
	    mTxtDescr = (EditText)this.findViewById(R.id.txt_edita_Descripcion);
	    getItemFromTable();
	    getCategories();
	    getProjects();
	    
	    spAdapter = new CatSpinnerAdapter(this, R.layout.cat_spinner_row, R.id.category);
	    spEstado = (Spinner)this.findViewById(R.id.sp_edita_Estado);
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
	    spProjects = (Spinner)this.findViewById(R.id.sp_edita_Proyecto);
	    spProjects.setAdapter(prAdapter);
	    spProjects.setOnItemSelectedListener(new OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) 
	        {
	        	projectId = ((Projects) parentView.getItemAtPosition(position)).getId();
	        }

	        @Override
	        public void onNothingSelected(AdapterView<?> parentView) 
	        {
	            // your code here
	        }

	    });
	}
	
	public void updateActivity(Activities currentItem)
	{
		currentItem.setTitle(mTxtTitulo.getText().toString());
		currentItem.setDescription(mTxtDescr.getText().toString());
		currentItem.setIdCategoria(categryId);
		currentItem.setFechaFinalizacion(getDateFromDatePicket(pickerDate));
		currentItem.setIdProyecto(projectId);
		
		mActivitiesTable.update(currentItem, new TableOperationCallback<Activities>(){
			public void onCompleted(Activities entity, Exception exception, ServiceFilterResponse response)
			{
				if(exception == null)
				{
					AlertDialog alertDialog = new AlertDialog.Builder(EditaActividadActivity.this).create();
					alertDialog.setTitle("¡ÉXITO!");
					alertDialog.setMessage("Actividad editada exitosamente");
					alertDialog.setButton("OK", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which) {
							Intent myIntent = new Intent(EditaActividadActivity.this, DetalleActividadActivity.class);
							myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
							myIntent.putExtra(RETURN_PARAMETER, parameter);
							startActivity(myIntent);
							finish();
							return;
					}
					});
					alertDialog.show();
				}
				else
				{
					createAndShowDialogExc(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
				}
			}
		});
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
	
	private void getItemFromTable() 
	{
		mActivitiesTable.lookUp(parameter, new TableOperationCallback<Activities>()
				{
					public void onCompleted(Activities entity, Exception exception, ServiceFilterResponse response)
					{
						if(exception == null)
						{
							currentItem = entity;
//							
							titulo = entity.getTitle();
							descrp = entity.getDescription();
							
							catId = entity.getIdCategoria();
//							getItemCategory(catId);
							
							
							mTxtTitulo.setText(titulo);
							mTxtDescr.setText(descrp);
							
//							CatSpinnerAdapter myAdap = (CatSpinnerAdapter) spEstado.getAdapter();
//							int spinnerPosition = myAdap.getPosition(catId);

							//set the default according to value
//							spEstado.setSelection(spinnerPosition);
							
							
							
//							spEstado.setSelection(spAdapter.getPosition(entity));
							
//							mySpinner.setSelection(arrayAdapter.getPosition("Category 2"));
//							spAdapter adap =  
//									spEstado.getAdapter();
//							int spinnerPosition = adap.getPosition(catId);
//							spEstado.setSelection(spinnerPosition);
			            }
					}
				});
//		mToDoTable.lookUp("37BBF396-11F0-4B39-85C8-B319C729AF6D", new TableOperationCallback<ToDoItem>() {
//	        public void onCompleted(item entity, Exception exception,
//	                ServiceFilterResponse response) {
//	            if(exception == null){
//	                Log.i(TAG, "Read object with ID " + entity.id);    
//	            }
//	        }
//	    });
	}
	
	public void execute()
	{
		if(checkValidation())
		{
			updateActivity(currentItem);
		}
		else
        {
        	Toast.makeText(this, "Debe llenar los campos requeridos", Toast.LENGTH_LONG).show();
        }
	}
	
	private void getItemCategory(String id) 
	{
		mCategoriesTable.lookUp(id, new TableOperationCallback<Categories>()
				{
					public void onCompleted(Categories entity, Exception exception, ServiceFilterResponse response)
					{
						if(exception == null)
						{
							

							
//							int position = spAdapter.getPosition(entity);
//							spEstado.setSelection(position);

//							spEstado.setSelection(spAdapter.getPosition(entity));

			            }
					}
				});
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
//						lista.add(item);
//						result.indexOf(item.equals(catId));
//						int position = spAdapter.getPosition(item);
//						spEstado.setSelection(position);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.edita_actividad, menu);
		//return true;
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edita_actividad, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.save_editActividad_menu) 
		{
			execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static java.util.Date getDateFromDatePicket(DatePicker datePicker)
	{
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

}
