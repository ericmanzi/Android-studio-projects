package eric.com.tipcalculator;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;


public class Result extends ActionBarActivity {

    double tipamount;
    double totalamount;

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mp = MediaPlayer.create(this, R.raw.merci);

        Bundle extras = getIntent().getExtras();
        if (extras == null) return;
        int tip = extras.getInt("tip");
        double bill = extras.getDouble("bill");
        int people = extras.getInt("people");
        Log.i("people", String.valueOf(people));
        Log.i("tip", String.valueOf(tip));
        Log.i("bill", String.valueOf(bill));

        double tipsum = (tip*bill)/100.0;
        tipamount = tipsum/people;

        Log.i("tipamount", String.valueOf(tipamount));
        double sumtotal = bill+tipsum;
        Log.i("sumtotal", String.valueOf(sumtotal));


        totalamount=sumtotal/people;
        Log.i("totalamount", String.valueOf(totalamount));

        DecimalFormat df = new DecimalFormat("#.##");

        TextView tvTip = (TextView) findViewById(R.id.tvTip);
        TextView tvTotal = (TextView) findViewById(R.id.tvTotal);

        String tipText = df.format(tipamount);
        Log.i("tipText", String.valueOf(tipText));

        tvTip.setText(tipText);

        String totalText = df.format(totalamount);
        Log.i("totalText", String.valueOf(totalText));

        tvTotal.setText(totalText);


        //so jogcall is this app that helps you and your partner spend more time together by tracking
        //the amount of time you spend doing activities together

    }

    public void recalculate(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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

    public void merci(View v) {
        mp.start();
    }
}
