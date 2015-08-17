package eric.mitapp.mitgpa;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class convertGPA extends ActionBarActivity {

    List<Course> classList = new ArrayList<>();
    ListView lvClasses;
    TextView tvGpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_gp);
        setTitle("Your GPA out of 4.0");

        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.fromLocalDatastore();

        query.findInBackground(new FindCallback<Course>() {
            @Override
            public void done(List<Course> courses, ParseException e) {
                classList.addAll(courses);

                Collections.reverse(classList);
                MyArrayAdapter mClassAdapter = new MyArrayAdapter(getApplicationContext(), classList);
                lvClasses = (ListView) findViewById(R.id.lvClasses);
                lvClasses.setAdapter(mClassAdapter);
                tvGpa = (TextView) findViewById(R.id.tvGpa);

                recalculate();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_convert_g, menu);
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

    public class MyArrayAdapter extends ArrayAdapter<Course> {
        private final Context context;
        private final List<Course> myClassList;

        public MyArrayAdapter(Context context, List<Course> classList1) {
            super(context, R.layout.class_item, classList1);
            this.context = context;
            this.myClassList = classList1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.class_item, parent, false);
            TextView tvCourseName = (TextView) rowView.findViewById(R.id.tvCourseName);
            TextView tvCourseUnits = (TextView) rowView.findViewById(R.id.tvCourseUnits);
            TextView tvCourseGrade = (TextView) rowView.findViewById(R.id.tvCourseGrade);
            Log.i("arrayAdapter", "name:" + myClassList.get(position).getName());
            tvCourseName.setText(myClassList.get(position).getName());
            tvCourseUnits.setText(myClassList.get(position).getUnitsStr());
            tvCourseGrade.setText(myClassList.get(position).getGrade());
            return rowView;
        }
    }


    public void recalculate() {
        //recalculate gpa
        if (classList.size()==0) {
            tvGpa.setText("GPA");
            return;
        }
        double total = 0.;
        double total_units = 0.;
        for (Course c: classList) {
            total+=(gradeToDouble(c.getGrade())*c.getUnits());
            total_units+=c.getUnits();
        }
        double gpa=total/total_units;
        DecimalFormat df = new DecimalFormat("0.00");
        tvGpa.setText(df.format(gpa));

    }

    public double gradeToDouble(String grade) {
        int intGrade;
        switch(grade) {
            case "A":
                intGrade= 4;
                break;
            case "B":
                intGrade= 3;
                break;
            case "C":
                intGrade= 2;
                break;
            case "D":
                intGrade= 1;
                break;
            case "E":
                intGrade= 0;
                break;
            case "F":
                intGrade= 0;
                break;
            default: intGrade= 0;
                break;
        }
        return intGrade;
    }
}
