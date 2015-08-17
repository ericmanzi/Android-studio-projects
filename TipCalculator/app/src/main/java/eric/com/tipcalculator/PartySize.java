package eric.com.tipcalculator;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;


public class PartySize extends ActionBarActivity {
    NumberPicker numPeople;
    NumberPicker tip;
    Button next;
    Button back;
    double bill;
    int people=1;
    int tipVal=15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_size);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        bill = extras.getDouble("bill");


        numPeople = (NumberPicker) findViewById(R.id.numPeople);
        tip = (NumberPicker) findViewById(R.id.tip);
        next = (Button) findViewById(R.id.nextbtn);
        back = (Button) findViewById(R.id.backbtn);
        numPeople.setMaxValue(100);
        numPeople.setMinValue(1);
        numPeople.setValue(1);
        numPeople.setOnValueChangedListener( new NumberPicker.
                OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int
                    oldVal, int newVal) {
                people=newVal;
            }
        });
        tip.setMaxValue(100);
        tip.setMinValue(0);
        tip.setValue(15);
        tip.setOnValueChangedListener( new NumberPicker.
                OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int
                    oldVal, int newVal) {
                tipVal=newVal;
            }
        });


    }

    public void back(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void next(View v) {
        Intent i = new Intent(this, Result.class);
        i.putExtra("people", people);
        i.putExtra("tip", tipVal);
        i.putExtra("bill", bill);
        Log.i("people", String.valueOf(people));
        Log.i("tip", String.valueOf(tipVal));
        Log.i("bill", String.valueOf(bill));
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_party_size, menu);
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
