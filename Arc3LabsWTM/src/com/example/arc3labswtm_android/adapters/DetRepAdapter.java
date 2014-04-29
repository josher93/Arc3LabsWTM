package com.example.arc3labswtm_android.adapters;

import com.example.arc3labswtm_android.R;
import com.example.arc3labswtm_android.model.Activities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class DetRepAdapter extends ArrayAdapter<Activities> 
{

	/**
	 * Adapter context
	 */
	Context mContext;

	/**
	 * Adapter View layout
	 */
	int mLayoutResourceId;

	public DetRepAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);

		mContext = context;
		mLayoutResourceId = layoutResourceId;
	}

	/**
	 * Returns the view for a specific item on the list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;

		final Activities currentItem = getItem(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
		}

		row.setTag(currentItem);
		final TextView textview = (TextView) row.findViewById(R.id.tvTituloAct);
		final TextView descripcion = (TextView) row.findViewById(R.id.tvDescrAct);
		textview.setText(currentItem.getTitle());
		descripcion.setText(currentItem.getDescription());


		return row;
	}
}