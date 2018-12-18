package in.bittechpro.apps.wow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RewardListAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private String[] m_name;
    String[] date;
    int[] reward;
    private int[] bottle;



    RewardListAdapter(Activity c, String[] m_name, String[] date,int[] reward,int[] bottle){
        super(c, R.layout.adapter_reward_list, m_name);
        this.activity = c;
        this.m_name = m_name;
        this.date = date;
        this.reward = reward;
        this.bottle = bottle;
    }

    @NonNull
    public View getView(final int position, View view, @NonNull final ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();

        @SuppressLint("ViewHolder") View list = inflater.inflate(R.layout.adapter_reward_list, null, true);

        TextView txt_machine = list.findViewById(R.id.machine_name);
        TextView txt_date = list.findViewById(R.id.reward_date);
        TextView txt_reward = list.findViewById(R.id.reward_count);
        TextView txt_bottle = list.findViewById(R.id.bottle_count);

        txt_machine.setText(m_name[position]);
        txt_date.setText(date[position]);
        txt_reward.setText(String.valueOf(reward[position]));
        txt_bottle.setText(String .valueOf(bottle[position]));

        return list;
    }
}

