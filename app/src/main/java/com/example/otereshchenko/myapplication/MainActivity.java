package com.example.otereshchenko.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelephonyManager my_number = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        setContentView(R.layout.main);
        String my_num;
        my_num = my_number.getSimOperator() + " " + my_number.getNetworkOperatorName();
        TextView my_operator = (TextView) findViewById(R.id.get_operator);
        TextView my_operator_name = (TextView) findViewById(R.id.sim_operator_name);
        TextView my_net_operator = (TextView) findViewById(R.id.net_operator);
        TextView my_net_operator_name = (TextView) findViewById(R.id.net_operator_name);
        TextView my_line1 = (TextView) findViewById(R.id.get_line1);
        TextView my_sim_country = (TextView) findViewById(R.id.get_sim_country);

        Button get_plan_btn = (Button)findViewById(R.id.get_plan_btn);


        my_operator.setText(my_number.getSimOperator());
        my_operator_name.setText(my_number.getSimOperatorName());
        my_net_operator.setText(my_number.getNetworkOperator());
        my_net_operator_name.setText(my_number.getNetworkOperatorName());
        my_sim_country.setText(my_number.getNetworkCountryIso());
        my_line1.setText(my_number.getLine1Number());



        String encodedHash = Uri.encode("*01*2#");
        String ussd = "*100" + encodedHash;
        startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)), 1);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
