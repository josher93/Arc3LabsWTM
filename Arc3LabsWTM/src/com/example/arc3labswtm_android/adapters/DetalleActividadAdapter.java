package com.example.arc3labswtm_android.adapters;


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

public class DetalleActividadAdapter extends ArrayAdapter<Activities>
{
	Context mContext;
	int mLayoutResourceId;
	
	public DetalleActividadAdapter(Context context, int layoutResourceId)
	{
		super(context, layoutResourceId);
		mContext = context;
		mLayoutResourceId = layoutResourceId;
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
		final TextView tituloview = (TextView) row.findViewById(R.id.lblTitulo);
		tituloview.setText(currentItem.getTitle());
//		final TextView proyecto = (TextView) row.findViewById(R.id.lblProyecto);
//		proyecto.setText(currentItem.getIdProyecto());
		final TextView lblFechaFin = (TextView) row.findViewById(R.id.lblFechaFin);
		lblFechaFin.setText(currentItem.getFechaFinalizacion().toString());
		final TextView lblEstado = (TextView) row.findViewById(R.id.lblEstado);
		if(currentItem.getIdCategoria().equals("746DB24B-270A-468B-A51C-655A1C89C751"))
		{
			lblEstado.setText("Pendiente");
		}
		if(currentItem.getIdCategoria().equals("746DB24B-270A-468B-A51C-655A1C89C751"))
		{
			lblEstado.setText("En Progreso");
		}
		if(currentItem.getIdCategoria().equals("3561AC66-31C4-4975-BB56-771BCF29CBD6"))
		{
			lblEstado.setText("Finalizada");
		}
		final TextView descrpview = (TextView) row.findViewById(R.id.lblDescripcion);
		descrpview.setText(currentItem.getDescription());
		
		return row;
	}
}
