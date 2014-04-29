package com.example.arc3labswtm_android.adapters;

import java.util.ArrayList;

import com.example.arc3labswtm_android.R;
import com.example.arc3labswtm_android.model.Activities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivitiesAdapter extends ArrayAdapter<Activities>
{
	private ArrayList<Integer> mSelection = new ArrayList<Integer>();
	private ArrayList<Activities> ItemsSeleccionados = new ArrayList<Activities>();
	Context mContext;
	int mResource;
	int mTextViewReosurceId;
	
	public ActivitiesAdapter(Context context, int resource, int textViewReosurceId)
	{
		super(context, resource, textViewReosurceId);

		mContext = context;
		mResource = resource;
		mTextViewReosurceId = textViewReosurceId;
	}
	
	/*	ITEMS CON POSICION	*/
	public void setNewSelection(int position)
	{
		final Activities currentItem = getItem(position);
		ItemsSeleccionados.add(currentItem);
		mSelection.add(position);
		notifyDataSetChanged();
	}
	
	public ArrayList<Integer> getCurrentCheckedPosition()
	{
		return mSelection;
	}
	
	public void removeSelection(int position) 
	{
		final Activities currentItem = getItem(position);
		ItemsSeleccionados.remove(currentItem);
		
		mSelection.remove(Integer.valueOf(position));
		notifyDataSetChanged();
	}
	
	public void clearSelection() 
	{
		mSelection = new ArrayList<Integer>();
		ItemsSeleccionados = new ArrayList<Activities>();
		notifyDataSetChanged();
	}
	
	public ArrayList<Activities> getSelectedItems()
	{
		return ItemsSeleccionados;
	}
	
	public int getSelectionCount() 
	{
		return mSelection.size();
	}
	
	/*	GETTER DEL VIEW */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		
		final Activities currentItem = getItem(position);
		
		if(row == null)
		{
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mResource, parent, false);
		}
		
		row.setTag(currentItem);
		final TextView title = (TextView) row.findViewById(R.id.tvTituloAct);
		final TextView idreporte = (TextView) row.findViewById(R.id.tvDescrAct);
		final TextView fechafin = (TextView) row.findViewById(R.id.tvFechaFin);
		title.setText(currentItem.getTitle());
		idreporte.setText(currentItem.getDescription());
		fechafin.setText(currentItem.getFechaFinalizacion().toLocaleString());
		final ImageView icon = (ImageView) row.findViewById(R.id.icon);
		if(currentItem.getIdCategoria().equals("3561AC66-31C4-4975-BB56-771BCF29CBD6"))
		{
			icon.setImageResource(R.drawable.done);
		}
		else
		{
			icon.setImageResource(R.drawable.pendiente);
		}

		row.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
		
		if(mSelection.contains(position))
		{
			row.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_blue_light));
		}
		
		return row;
	}

}