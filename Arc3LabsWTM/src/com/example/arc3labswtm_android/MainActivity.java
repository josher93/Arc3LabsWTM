package com.example.arc3labswtm_android;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.example.arc3labswtm_android.R.menu;
import com.example.arc3labswtm_android.adapters.ActivitiesAdapter;
import com.example.arc3labswtm_android.customs.ActivitiesFragment;
import com.example.arc3labswtm_android.customs.Item_objct;
import com.example.arc3labswtm_android.customs.NavigationAdapter;
import com.example.arc3labswtm_android.customs.ReportsFragment;
import com.example.arc3labswtm_android.model.Activities;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity 
{
	private MobileServiceClient mClient;
	private MobileServiceTable<Activities> mActivitiesTable;
	private ActivitiesAdapter mAdapter;
//	public final static String EXTRA_PARAMETER = "com.example.arc3labswtm_android.PARAMETER";

	
	private String[] titulos;
    private DrawerLayout NavDrawerLayout;
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private TypedArray NavIcons;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    NavigationAdapter NavAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		
		NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavList = (ListView) findViewById(R.id.lista);
        View header = getLayoutInflater().inflate(R.layout.drawer_header, null);
        NavList.addHeaderView(header);
		
        titulos = getResources().getStringArray(R.array.menu_array);
        ArrayList<Item_objct> NavItms = new ArrayList<Item_objct>();
        NavItms.add(new Item_objct(titulos[0]));
        NavItms.add(new Item_objct(titulos[1]));     	       
        NavAdapter= new NavigationAdapter(this,NavItms);
        NavList.setAdapter(NavAdapter);
		
        mTitle = mDrawerTitle = getTitle();
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                NavDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* Icono de navegacion*/
                R.string.app_name,  /* "open drawer" description */
                R.string.hello_world  /* "close drawer" description */) 
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) 
            {
            	invalidateOptionsMenu();
            	Log.e("Cerrado completo", "!!");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) 
            {
            	invalidateOptionsMenu();
                Log.e("Apertura completa", "!!");
            }
        };	        
        
        
        NavDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
            {
            	MostrarFragment(position);
            }
        });
        
        MostrarFragment(1);
		
		
	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) 
	{
        // if nav drawer is opened, hide the action items
//        boolean drawerOpen = NavDrawerLayout.isDrawerOpen(NavList);
//        menu.findItem(R.id.action_add_activity).setVisible(!drawerOpen);
//        menu.findItem(R.id.action_pendientes).setVisible(!drawerOpen);
//        menu.findItem(R.id.action_progreso).setVisible(!drawerOpen);
//        menu.findItem(R.id.action_realizadas).setVisible(!drawerOpen);
//        menu.findItem(R.id.action_todas).setVisible(!drawerOpen);
//        menu.findItem(R.id.action_reporte_salida).setVisible(!drawerOpen);
//        menu.findItem(R.id.action_reporte_entrada).setVisible(!drawerOpen);
        
        return super.onPrepareOptionsMenu(menu);
    }
	
	private void MostrarFragment(int position) 
	 {
	        Fragment fragment = null;
	        switch (position) {
	        case 1:
	            fragment = new ActivitiesFragment();
	            break;
	        case 2:
	            fragment = new ReportsFragment();
	            break;
	     
	        default:
	        	Toast.makeText(getApplicationContext(),"Opcion "+titulos[position-1]+"no disponible!", Toast.LENGTH_SHORT).show();
	            fragment = new ActivitiesFragment();
	            position=1;
	            break;
	        }
	        if (fragment != null) 
	        {
	            FragmentManager fragmentManager = getFragmentManager();
	            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

	            NavList.setItemChecked(position, true);
	            NavList.setSelection(position);
	            setTitle(titulos[position-1]);
	            NavDrawerLayout.closeDrawer(NavList);
	        } 
	        else 
	        {
	            Log.e("Error  ", "MostrarFragment"+position);
	        }
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	 protected void onPostCreate(Bundle savedInstanceState) 
	 {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        mDrawerToggle.syncState();
	    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if (mDrawerToggle.onOptionsItemSelected(item)) 
	        {
	            Log.e("mDrawerToggle pushed", "x");
	          return true;
	        }
	 switch (item.getItemId()) 
        {
        case R.id.action_add_activity:
        	Intent addAct = new Intent(this, AgregarActividadActivity.class);            
        	addAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
            startActivity(addAct);            
            return true;
        case R.id.action_reporte_entrada:
        	Intent repEntr = new Intent(this, AgregarRepEntradaActivity.class);            
        	repEntr.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
            startActivity(repEntr);            
            return true;
        case R.id.action_reporte_salida:
        	Intent repSalida = new Intent(this, ReporteSalidaActivity.class);            
        	repSalida.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
            startActivity(repSalida);            
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
	
	private void refreshItemsFromTable() 
	{
		mActivitiesTable.execute(new TableQueryCallback<Activities>() 
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
	

}
