package com.example.arc3labswtm_android.customs;


import com.example.arc3labswtm_android.DetalleActividadActivity;
import com.example.arc3labswtm_android.DetalleReporteActivity;
import com.example.arc3labswtm_android.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

public class ReportsFragment extends Fragment 
{
	CalendarView calendar;
//	public void onCreate(Bundle savedInstanceState) 
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.fragment_reports);
//		calendar = (CalendarView)findViewById(R.id.calendar_reports);
//		calendar.setOnDateChangeListener(new OnDateChangeListener()
//		{
//
//			@Override
//			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) 
//			{
//				Toast.makeText(getApplicationContext(),
//				dayOfMonth +"/"+month+"/"+ year,Toast.LENGTH_LONG).show();
//			}
//		});
//	}
	
     
    public ReportsFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) 
    {
    	setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_reports, container, false);
        calendar = (CalendarView) rootView.findViewById(R.id.calendar_reports);
        calendar.setOnDateChangeListener( new OnDateChangeListener() 
        {
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) 
			{
				Intent newActivity = new Intent(view.getContext(), DetalleReporteActivity.class);
				startActivity(newActivity);
//				Toast.makeText(getActivity().getApplicationContext(),
//				dayOfMonth +"/"+month+"/"+ year,Toast.LENGTH_LONG).show();
			}
		});
        
        return rootView;
    }
    

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.fragment_reports_menu, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
}