package com.example.arc3labswtm_android.model;

import java.util.Date;

public class Activities 
{
	@com.google.gson.annotations.SerializedName("Id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("titulo")
	private String mTitulo;
	
	@com.google.gson.annotations.SerializedName("idReporte")
	private String mIdReporte;
	
	@com.google.gson.annotations.SerializedName("idProyecto")
	private String mIdProyecto;
	
	@com.google.gson.annotations.SerializedName("fechaFinalizacion")
	private Date mFechaFinalizacion;
	
	@com.google.gson.annotations.SerializedName("idCategoria")
	private String mIdCategoria;
	
	@com.google.gson.annotations.SerializedName("descripcion")
	private String mDescripcion;
	
	public Activities()
	{}
	
	
//	***********		CONSTRUCTORS	***********
	
//	public String toString()
//	{
//		return getTitle();
//	}
	
	public Activities(String titulo, String Id, String descripcion, String idReporte, Date fechaFinalizacion, String idProyecto, String idCategoria)
	{
		this.setTitle(titulo);
		this.setId(Id);
		this.setDescription(descripcion);
		this.setFechaFinalizacion(fechaFinalizacion);
		this.setIdReport(idReporte);
		this.setIdProyecto(idProyecto);
		this.setIdCategoria(idCategoria);
	}

//	********	GET		*************
	
	public String getId()
	{
		return mId;
	}
	
	public String getTitle() 
	{
		return mTitulo;
	}
	
	public String getIdReport()
	{
		return mIdReporte;
	}
	
	public Date getFechaFinalizacion()
	{
		return mFechaFinalizacion;
	}
	
	public String getIdProyecto()
	{
		return mIdProyecto;
	}
	
	public String getIdCategoria()
	{
		return mIdCategoria;
	}
	
	public String getDescription() 
	{
		return mDescripcion;
	}

//	********	SET		*************
	
	public final void setId(String Id)
	{
		mId = Id;
	}
	
	public final void setTitle(String titulo)
	{
		mTitulo = titulo;
	}
	
	public final void setFechaFinalizacion(Date fechaFinalizacion)
	{
		mFechaFinalizacion = fechaFinalizacion;
//		mFechaFinalizacion = new Date();
	}
	
	public final void setIdReport(String idReporte)
	{
		mIdReporte = idReporte;
	}
	
	public final void setIdProyecto(String idProyecto)
	{
		mIdProyecto = idProyecto;
	}
	
	public final void setIdCategoria(String idCategoria)
	{
		mIdCategoria = idCategoria;
	}
	
	public final void setDescription(String descripcion)
	{
		mDescripcion = descripcion;
	}
}
