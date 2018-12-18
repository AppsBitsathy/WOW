package in.bittechpro.apps.wow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
    int[] free;
    private int[] bottle;

    CorporateListAdapter(Activity activity, String[] m_name, int[] free) {
        super(activity, R.layout.adapter_corporate, m_name);
        this.activity = activity;
        this.m_name = m_name;
        this.free = free;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("ViewHolder") View list = inflater.inflate(R.layout.adapter_corporate, null, true);

        TextView name = list.findViewById(R.id.cor_m_name);
        TextView capacity = list.findViewById(R.id.cor_m_capacity);

        name.setText(m_name[position]);
        capacity.setText("Free : " + free[position]);

        return list;
    }
}
