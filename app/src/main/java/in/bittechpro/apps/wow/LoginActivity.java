package in.bittechpro.apps.wow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Fragment fragment = null;

    Button btn_register,btn_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        SharedPreferences sharedpreferences = getSharedPreferences(SPrefManager.PREF_NAME, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(SPrefManager.LOGGED)){
            if (sharedpreferences.getInt(SPrefManager.LOGGED, 0) == 1) {
                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }

        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new RegisterFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
                ft.replace(R.id.content_frame, fragment,"Register");
                ft.commit();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new LoginFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out);
                ft.replace(R.id.content_frame, fragment,"Select");
                ft.commit();
            }
        });

    }

}
