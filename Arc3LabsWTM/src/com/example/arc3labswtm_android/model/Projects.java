package com.example.arc3labswtm_android.model;

public class Projects 
{
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("name")
	private String mName;
	
	@com.google.gson.annotations.SerializedName("idCompany")
	private String mIdCompany;
	
	@Override
	public String toString()
	{
		return mName;
	}
	
//	********	GET		*************
	public String getId()
	{
		return mId;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public String getIdCompany()
	{
		return mIdCompany;
	}
	
//	********	SET		*************
	public final void setId(String id)
	{
		mId = id;
	}
	
	public final void setName(String name)
	{
		mName = name;
	}
	
	public final void setIdCompany(String idCompany)
	{
		mIdCompany = idCompany;
	}
}
