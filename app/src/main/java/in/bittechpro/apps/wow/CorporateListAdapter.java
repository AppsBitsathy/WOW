package in.bittechpro.apps.wow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CorporateListAdapter  extends ArrayAdapter<String> {

    private Activity activity;
    private String[] m_name;
    String[] date;
    private int[] current;
    private int[] full;
    private double[] capacity;

    CorporateListAdapter(Activity activity, String[] m_name, int[] full,int[] current,double[] capacity) {
        super(activity, R.layout.adapter_corporate, m_name);
        this.activity = activity;
        this.m_name = m_name;
        this.current = current;
        this.full = full;
        this.capacity = capacity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("ViewHolder") View list = inflater.inflate(R.layout.adapter_corporate, null, true);

        TextView name = list.findViewById(R.id.cor_m_name);
        TextView txt_capacity = list.findViewById(R.id.cor_m_capacity);

        name.setText(m_name[position]);
        String txt = String.valueOf(capacity[position])+"% FILLED\nTotal Capacity : "+full[position]+" Kg\nCurrent : "+current[position]+" Kg";
        txt_capacity.setText(txt);
        if(capacity[position]>74.9){
            txt_capacity.setTextColor(ColorStateList.valueOf(Color.RED));
        }
        else if (capacity[position]>49.9){
            txt_capacity.setTextColor(ColorStateList.valueOf(Color.parseColor("#2979FF")));
        }


        return list;
    }
}
