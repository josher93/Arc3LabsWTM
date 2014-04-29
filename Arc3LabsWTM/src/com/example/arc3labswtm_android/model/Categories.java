package com.example.arc3labswtm_android.model;

public class Categories 
{
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("name")
	private String mName;
	
	public Categories()
	{}
	
	public Categories(String id, String name)
	{
		this.setId(id);
		this.setName(name);
	}
	
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

//	********	SET		*************
	
	public final void setId(String id)
	{
		mId = id;
	}
	
	public final void setName(String name)
	{
		mName = name;
	}
	
	
}
