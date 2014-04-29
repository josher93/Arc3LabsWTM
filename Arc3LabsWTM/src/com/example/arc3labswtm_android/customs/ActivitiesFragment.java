package com.example.arc3labswtm_android.customs;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.example.arc3labswtm_android.AgregarActividadActivity;
import com.example.arc3labswtm_android.AgregarRepEntradaActivity;
import com.example.arc3labswtm_android.DetalleActividadActivity;
import com.example.arc3labswtm_android.MainActivity;
import com.example.arc3labswtm_android.R;
//import com.example.arc3labswtm_android.ReporteEntradaActivity;
import com.example.arc3labswtm_android.ReporteSalidaActivity;
import com.example.arc3labswtm_android.adapters.ActivitiesAdapter;
import com.example.arc3labswtm_android.model.Activities;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponseCallback;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class ActivitiesFragment extends Fragment 
{
	protected static final String TAG = "ActivitiesFragmment";
	private MobileServiceClient mClient;
	private MobileServiceTable<Activities> mActivitiesTable;
	private ActivitiesAdapter mAdapter;
	public final static String EXTRA_PARAMETER = "com.example.arc3labswtm_android.PARAMETER";
	private ProgressBar mProgressBar;

	
/*	::::::	PRIMER EVENTO	::::::::::::. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		try 
		{
			mClient = new MobileServiceClient("https://arc3labswtm.azure-mobile.net/",
					"NWDqSfBULLlqUdVEhYpdvrnULGAYIx94", 
					getActivity().getApplicationContext())
//			.withFilter(new ProgressFilter())
			;
			mActivitiesTable = mClient.getTable(Activities.class);
		} 
	  
		catch (MalformedURLException e) 
		{
			createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
		}
		 
	}

	/*	::::::	SEGUNDO EVENTO	::::::::::::. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.act_listfragment, container, false);
		return view;
	}

	/*	::::::	TERCER EVENTO (DESPUES DE QUE LA ACTIVIDAD HA TERMINADO DE CREARSE)	::::::::::::. */
	@Override
    public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState); 
		
		mProgressBar = (ProgressBar) getView().findViewById(R.id.loadingProgressBar);
		mAdapter = new ActivitiesAdapter(getActivity(), R.layout.act_itemlist, R.id.tvTituloAct);
		  ListView listViewToDo = (ListView) getView().findViewById(R.id.activities_fragment_list);
		  listViewToDo.setAdapter(mAdapter);
		  listViewToDo.setOnItemClickListener(new OnItemClickListener(){
			    public void onItemClick(AdapterView<?> parent, View view, int position,long id)
			    {
			    	final Activities currentItem = mAdapter.getItem(position);
			    	String pid = currentItem.getId();
			    	Intent newActivity = new Intent(view.getContext(), DetalleActividadActivity.class);
					newActivity.putExtra(EXTRA_PARAMETER, pid);
					startActivity(newActivity);
			    }
			});
		  
		  refreshItemsFromTable();
		  setupActionBar();
    }
	
	
	private void setupActionBar() 
	{
		ListView listView = (ListView) getView().findViewById(R.id.activities_fragment_list);
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

				mode.setTitle(mAdapter.getSelectionCount() + " seleccionada(s)");
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) 
			{
				switch (item.getItemId()) 
				{
					case R.id.delete_acts:
						ArrayList<Activities> eliminacion = mAdapter.getSelectedItems();
						deleteActivities(eliminacion);
						Toast.makeText(getActivity(),
								mAdapter.getSelectionCount() + " actividad(es) eliminada(s)",
								Toast.LENGTH_LONG).show();
						mAdapter.clearSelection();
						for(Activities act : eliminacion)
						{
							mAdapter.remove(act);
						}
						mode.finish();
//						refreshItemsFromTable();
						return true;
						
					case R.id.item_send_report:
					default:
						return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) 
			{
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.activities_contextual_menu, menu);
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

	
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
	    inflater.inflate(R.menu.fragment_activities_menu, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
    {
    	 switch (item.getItemId()) 
	        {
	        case R.id.action_pendientes:
	        	getActividadesPendientes();
	            return true;
	        case R.id.action_realizadas:
	        	getActividadesRealizadas();
	            return true;
	        case R.id.action_progreso:
	        	getActividadesProgreso();
	            return true;
	        case R.id.action_todas:
	        	refreshItemsFromTable();
	        	return true;
	        }
    	 
 	        return super.onOptionsItemSelected(item);

    }
	
	//::::::::::	DIALOG	::::::::::::::::
	
	private void createAndShowDialog(Exception exception, String title) 
	{
		createAndShowDialog(exception.toString(), title);
	}

	private void createAndShowDialog(String message, String title) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
		builder.setMessage(message);
		builder.setTitle(title);
		builder.create().show();
	}

	
	//::::::::::::::::		METHODS		::::::::::::::::::::::::
	
	public class ProgressFilter implements ServiceFilter 
	{
		
		@Override
		public void handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback,
				final ServiceFilterResponseCallback responseCallback) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					
				}
			});
			
			nextServiceFilterCallback.onNext(request, new ServiceFilterResponseCallback() {
				
				@Override
				public void onResponse(ServiceFilterResponse response, Exception exception) {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							
						}
					});
					
					responseCallback.onResponse(response, exception);
				}
			});
		}
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
						Log.i(TAG, "Titulo: " + item.getTitle());
					}
					mProgressBar.setVisibility(ProgressBar.GONE);

				} else {
					createAndShowDialog(exception, "Error");
				}
			}
		});
		
	}
	
	private void getActividadesPendientes()
	{
		mProgressBar.setVisibility(ProgressBar.VISIBLE);
		mActivitiesTable.where().field("idCategoria").eq("9136F9E0-C93B-49EA-9FE5-A5D8EA140EA4").execute(new TableQueryCallback<Activities>() 
		{
			public void onCompleted(List<Activities> result, int count, Exception exception, ServiceFilterResponse response) 
			{
				if (exception == null) 
				{
					mAdapter.clear();
					for (Activities item : result) 
					{
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
	
	private void getActividadesRealizadas()
	{
		mProgressBar.setVisibility(ProgressBar.VISIBLE);
		mActivitiesTable.where().field("idCategoria").eq("3561AC66-31C4-4975-BB56-771BCF29CBD6").execute(new TableQueryCallback<Activities>() 
		{
			public void onCompleted(List<Activities> result, int count, Exception exception, ServiceFilterResponse response) 
			{
				if (exception == null) 
				{
					mAdapter.clear();
					for (Activities item : result) 
					{
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
	
	private void getActividadesProgreso()
	{
		mActivitiesTable.where().field("idCategoria").eq("746DB24B-270A-468B-A51C-655A1C89C751").execute(new TableQueryCallback<Activities>() 
		{
			public void onCompleted(List<Activities> result, int count, Exception exception, ServiceFilterResponse response) 
			{
				if (exception == null) 
				{
					mAdapter.clear();
					for (Activities item : result) 
					{
						mAdapter.add(item);
					}
				} 
				else 
				{
					createAndShowDialog(exception, "Error");
				}
			}
		});
	}
	
	private void deleteActivities(ArrayList<Activities> acts)
	{
		for(Activities act : acts)
		{
			mActivitiesTable.delete(act, new TableDeleteCallback()
			{
				public void onCompleted(Exception exception, ServiceFilterResponse response)
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

}

