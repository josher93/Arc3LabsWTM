package com.example.arc3labswtm_android.adapters;

import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.arc3labswtm_android.R;
import com.example.arc3labswtm_android.model.Activities;

public class ReporteEntradaActividadesAdapter extends ArrayAdapter<Activities>
{
	Context mContext;
	int mLayoutResourceId;
	
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	
	public ReporteEntradaActividadesAdapter(Context context, int layoutResourceId)
	{
		super(context, layoutResourceId);
		mContext = context;
		mLayoutResourceId = layoutResourceId;
	}
	
	public void setNewSelection(int position, boolean value) 
    {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }
	
	public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }
	
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		final Activities currentItem = getItem(position);
		
		if (row == null) 
		{
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
		}
		
		row.setTag(currentItem);
		final TextView tituloview = (TextView) row.findViewById(R.id.tituloAct);
		tituloview.setText(currentItem.getTitle());
		final TextView descrpview = (TextView) row.findViewById(R.id.descrAct);
		descrpview.setText(currentItem.getDescription());
		row.setBackgroundColor(mContext.getResources().getColor(android.R.color.background_light));
		
		if(mSelection.get(position) != null)
		{
			row.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
		}
		
		return row;
	}
}
