package in.bittechpro.apps.wow;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment {


    public ExchangeFragment() {
        // Required empty public constructor
    }

    View view;

    int[] image = {
            R.drawable.amazon_pay,
            R.drawable.freecharge,
            R.drawable.jio_money,R.drawable.phonepe,
            R.drawable.paytm
    };

    String[] name = {
            "AMAZON PAY",
            "FREECHARGE",
            "JIO MONEY",
            "PHONEPE",
            "PAYTM"
    };

    GridView gridView;

    TextView ex_count;

    SharedPreferences sharedpreferences ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_exchange, container, false);

        ex_count = view.findViewById(R.id.ex_count);
        sharedpreferences = view.getContext().getSharedPreferences(SPrefManager.PREF_NAME, Context.MODE_PRIVATE);
        ex_count.setText(String.valueOf(sharedpreferences.getInt(SPrefManager.TOTAL_REWARD, 0)));


        gridView = view.findViewById(R.id.exchange_grid);
        gridView.setAdapter(new ExchangeAdapter(getActivity(),name,image));
        return view;
    }

}
