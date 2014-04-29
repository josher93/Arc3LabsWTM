package com.example.arc3labswtm_android.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.arc3labswtm_android.R;
import com.example.arc3labswtm_android.model.Categories;

public class CatEditSpinnerAdapter extends ArrayAdapter<Categories>
{
	Context mContext;
	int mResource;
	int mTextViewReosurceId;
	
	public CatEditSpinnerAdapter(Context context, int resource, int textViewResourceId)
	{
		super(context, resource, textViewResourceId);
		mContext = context;
		mResource = resource;
		mTextViewReosurceId = textViewResourceId;
	}
	
//	@Override
//	public View getDropDownView(int position, View convertView, ViewGroup parent)
//	{
//		return getCustomView(position, convertView, parent);
//	}
//	
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent)
//	{
//		return getCustomView(position, convertView, parent);
//	}
//	
//	public View getCustomView(int position, View convertView, ViewGroup parent) 
//	{
//		// TODO Auto-generated method stub
//		View row = convertView;
//		final Categories currentItem = getItem(position);
//		
//		if(row == null)
//		{
//			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
//			row = inflater.inflate(mResource, parent, false);
//		}
//		
//		row.setTag(currentItem);
//		final TextView label=(TextView)row.findViewById(R.id.category);
//		label.setText(currentItem.getName());
//		
//		return row;
//	}
}