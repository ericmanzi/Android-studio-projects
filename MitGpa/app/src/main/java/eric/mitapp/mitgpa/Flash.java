package eric.mitapp.mitgpa;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

//maroon color: #632523
public class Flash extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        Object actionBar = getSupportActionBar();
        android.support.v7.internal.app.WindowDecorActionBar bar = (android.support.v7.internal.app.WindowDecorActionBar) actionBar;
        bar.hide();

        setContentView(R.layout.activity_flash);
        new CountDownTimer(3000,1000) {
            public void onTick(long millisUntilFinished) {
                //do nothing
            }
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flash, menu);
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
