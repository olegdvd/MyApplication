package com.example.otereshchenko.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import com.example.otereshchenko.myapplication.USSD;

public class MainActivity extends ActionBarActivity {
    Button get_plan_btn;
    String first_card_plan_check;
    String secret_code;
    String first_card_balance_check;
    String provider_index;
    String message_string;

    public static final String TAG = MainActivity.class.getSimpleName();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelephonyManager my_number = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        setContentView(R.layout.main);

        TextView my_operator = (TextView) findViewById(R.id.get_operator);
        TextView my_operator_name = (TextView) findViewById(R.id.sim_operator_name);
        TextView my_net_operator = (TextView) findViewById(R.id.net_operator);
        TextView my_net_operator_name = (TextView) findViewById(R.id.net_operator_name);
        TextView my_line1 = (TextView) findViewById(R.id.get_line1);
        TextView my_sim_country = (TextView) findViewById(R.id.get_sim_country);
        // TextView get_current_plan = (TextView) findViewById(R.id.get_current_plan);
        final TextView message = (TextView) findViewById(R.id.message);


        my_operator.setText(my_number.getSimOperator());
        my_operator_name.setText(my_number.getSimOperatorName());
        my_net_operator.setText(my_number.getNetworkOperator());
        my_net_operator_name.setText(my_number.getNetworkOperatorName());
        my_sim_country.setText(my_number.getNetworkCountryIso());
        my_line1.setText(my_number.getLine1Number());

        Button get_plan_btn = (Button) findViewById(R.id.get_plan_btn);
        Button dial_code_btn = (Button) findViewById(R.id.dial_code);

        InputStream inputStream = getResources().openRawResource(R.raw.mccmnc);
        CSVFile csvFile = new CSVFile(inputStream);
        List<String[]> scoreList = csvFile.read();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               // updateUI (intent);
                //intent.getIntExtra()
                System.out.println ("Intent: "+ intent);
            }
        };


        String catname = "";
        for (int i = 0; i < scoreList.size(); i++) {
            String[] strings = scoreList.get(i);
            for (int j = 0; j < 1; j++) {
                if (strings[1].contains(my_number.getSimOperator())) {
                    provider_index = strings[1];
                    first_card_plan_check = strings[2];
                    first_card_balance_check = strings[3];
                }
            }
            System.out.println();
        }
        message.setText("Запрос тарифа: " + first_card_plan_check + " Запрос баланса: " + first_card_balance_check);

        first_card_plan_check = "*100*01*2#";
        secret_code = "*#06#";


        get_plan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_card_balance_check.contains("*")) {
                    String encodedHash = Uri.encode(first_card_plan_check);
                    String ussd = "" + encodedHash;
                    startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)), 1);
                } else {
                    message.setText(getString(R.string.ussd_not_found) + "\n" + getString(R.string.you_can_help_ussd)
                    );
                }

            }

            //@Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                USSD ussd = new USSD(4000, 4000); // передается два параметра, задержка до и после (ms) создания сообщения
                if (ussd.IsFound())
                    message.setText("\n" + ussd.getMsg());
                else
                    message.setText("" + R.string.error_ussd_msg);
            }
        });
        dial_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_card_balance_check.contains("*")) {
                    String encodedHash = Uri.encode(secret_code);
                    String ussd = "" + encodedHash;
                    startActivityForResult(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + ussd)), 1);
                } else {
                    message.setText(getString(R.string.ussd_not_found) + "\n" + getString(R.string.you_can_help_ussd)
                    );
                }

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
       // registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_GET_CONTENT));
    }
    @Override
    public void onDestroy() {
        Log.i(TAG, "unregister");
       // unregisterReceiver(broadcastReceiver);
        super.onDestroy();
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
