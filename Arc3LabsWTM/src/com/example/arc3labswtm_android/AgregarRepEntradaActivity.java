package com.example.arc3labswtm_android;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.example.arc3labswtm_android.adapters.RepEntradaSelectionAdapter;
import com.example.arc3labswtm_android.model.Activities;
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
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.os.Build;

public class AgregarRepEntradaActivity extends ActionBarActivity 
{
	private MobileServiceClient mClient;
	private MobileServiceTable<Activities> mActivitiesTable;
	private RepEntradaSelectionAdapter mAdapter;
	private ProgressBar mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_agregar_rep_entrada);
		
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar2);
	    
		try 
		{
			mClient = new MobileServiceClient(
					"https://arc3labswtm.azure-mobile.net/",
					"NWDqSfBULLlqUdVEhYpdvrnULGAYIx94", 
					this);

			mActivitiesTable = mClient.getTable(Activities.class);
		} 
		catch (MalformedURLException e) 
		{
			createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}
		
		mAdapter = new RepEntradaSelectionAdapter(this, R.layout.rep_rowlist_layout, R.id.titulo);
		ListView listViewToDo = (ListView) findViewById(R.id.listview_actividades);
		listViewToDo.setAdapter(mAdapter);
	
		refreshItemsFromTable();
		setupActionBar();
		
	}
	
	private void setupActionBar() 
	{
		ListView listView = (ListView) findViewById(R.id.listview_actividades);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() 
		{

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) 
			{
				if (checked) 
				{
					mAdapter.setNewSelection(position);
				} 
				else 
				{
					mAdapter.removeSelection(position);
				}

				mode.setTitle(mAdapter.getSelectionCount() + " items selected");
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) 
			{
				switch (item.getItemId()) 
				{
					case R.id.item_add_report:
						ArrayList<Activities> edicion = mAdapter.getSelectedItems();
						assignReport(edicion);
						Toast.makeText(AgregarRepEntradaActivity.this,
								mAdapter.getSelectionCount() + " actividades agregadas",
								Toast.LENGTH_LONG).show();
//						mAdapter.clearSelection();
//						mode.finish();
						return true;
					case R.id.item_send_report:
						
						
						Toast.makeText(AgregarRepEntradaActivity.this, "Reporte enviado",
						Toast.LENGTH_LONG).show();
						mAdapter.clearSelection();
						mode.finish();
						return true;
					default:
						return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) 
			{
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.contextual_menu, menu);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) 
			{
				mAdapter.clearSelection();
			}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) 
			{
				return false;
			}
		});
	}
	
	private void refreshItemsFromTable() 
	{
		mProgressBar.setVisibility(ProgressBar.VISIBLE);
		mActivitiesTable.execute(new TableQueryCallback<Activities>() 
		{

			public void onCompleted(List<Activities> result, int count, Exception exception, ServiceFilterResponse response) {
				if (exception == null) {
					mAdapter.clear();

					for (Activities item : result) {
						mAdapter.add(item);
					}
					mProgressBar.setVisibility(ProgressBar.GONE);

				} 
				else 
				{
					createAndShowDialog(exception, "Error");
				}
			}
		});
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
	
	
	private void assignReport(ArrayList<Activities> acts)
	{
		for(Activities act : acts)
		{
			act.setIdReport("IDREPORTE-android");
			mActivitiesTable.update(act, new TableOperationCallback<Activities>() 
			{
				public void onCompleted(Activities entity, Exception exception, ServiceFilterResponse response) 
				{
					if (exception == null) 
					{
					} 
					else 
					{
						createAndShowDialog(exception, "Error");
					}
				}
			});
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agregar_rep_entrada, menu);
		return true;
	}


}
