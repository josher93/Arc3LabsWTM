package com.example.arc3labswtm_android.adapters;


import com.example.arc3labswtm_android.R;
import com.example.arc3labswtm_android.model.Categories;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CatSpinnerAdapter extends ArrayAdapter<Categories>
{
	Context mContext;
	int mResource;
	int mTextViewReosurceId;
	
	public CatSpinnerAdapter(Context context, int resource, int textViewResourceId)
	{
		super(context, resource, textViewResourceId);
		mContext = context;
		mResource = resource;
		mTextViewReosurceId = textViewResourceId;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		return getCustomView(position, convertView, parent);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return getCustomView(position, convertView, parent);
	}
	
	public View getCustomView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		View row = convertView;
		final Categories currentItem = getItem(position);
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mResource, parent, false);
		}
		
		row.setTag(currentItem);
		row.setTag(position);
		final TextView label=(TextView)row.findViewById(R.id.category);
		label.setText(currentItem.getName());
		
//		label.setText(DayOfWeek[position]);
//		ImageView icon=(ImageView)row.findViewById(R.id.icon);
		
//		if (DayOfWeek[position]=="Sunday")
//		{
//			icon.setImageResource(R.drawable.icon);
//		}
//		else
//		{
//			icon.setImageResource(R.drawable.icongray);
//		}
		
		return row;
	}	
}
