package com.example.arc3labswtm_android.customs;

import java.util.ArrayList;




import com.example.arc3labswtm_android.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class NavigationAdapter extends BaseAdapter 
{
    private Activity activity;  
	ArrayList<Item_objct> arrayitms; 

   public NavigationAdapter(Activity activity,ArrayList<Item_objct>  listarry) 
   {  
       super();  
       this.activity = activity;  
       this.arrayitms=listarry;
   }     

   @Override
   public Object getItem(int position) 
   {       
       return arrayitms.get(position);
   }   
    public int getCount() 
    {  
        return arrayitms.size();  
    }    
    @Override
    public long getItemId(int position) 
    {
        return position;
    }   
    
    public static class Fila  
    {  
    		TextView titulo_itm;
    		
    }  
   public View getView(int position, View convertView, ViewGroup parent) 
   {  
      
	   Fila view;  
       LayoutInflater inflator = activity.getLayoutInflater();  
      if(convertView==null)  
       {  
           view = new Fila();
           Item_objct itm=arrayitms.get(position);
           convertView = inflator.inflate(R.layout.drawer_list_item, null);
           view.titulo_itm = (TextView) convertView.findViewById(R.id.item_title);
           view.titulo_itm.setText(itm.getTitulo());           
             
           convertView.setTag(view);  
        }  
        else  
        {  
           view = (Fila) convertView.getTag();  
        }  
        return convertView;  
    }
}
