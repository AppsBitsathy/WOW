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
import android.support.design.widget.Snackbar;
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

public class ExchangeAdapter extends ArrayAdapter<String> {

        private Activity activity;

        private int[] img;
        private String[] name;


        ExchangeAdapter(Activity c, String[] name, int[] img){
            super(c, R.layout.adapter_exchange, name);
            this.img = img;
            this.name = name;
            this.activity = c;
        }

        @NonNull
        public View getView(final int position, final View view, @NonNull final ViewGroup parent) {
            LayoutInflater inflater = activity.getLayoutInflater();

            @SuppressLint("ViewHolder") View spez = inflater.inflate(R.layout.adapter_exchange, null, true);

            ImageView img_view = spez.findViewById(R.id.img_spez);
            TextView txt = spez.findViewById(R.id.txt_spez_name);


            img_view.setImageResource(img[position]);
            txt.setText(name[position]);

            CardView card = spez.findViewById(R.id.card_spez);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(view,"Will be added soon",Snackbar.LENGTH_LONG);
                }
            });

            return spez;
        }


}

