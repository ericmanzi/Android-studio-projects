package eric.mitapp.mitgpa;

import android.content.Context;
import android.content.Intent;
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


public class TechnicalGPA extends ActionBarActivity {

    List<Course> classList = new ArrayList<>();
    ListView lvClasses;
    TextView tvGpa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_gp);
        Intent i = getIntent();
        final String major = i.getStringExtra("major");
        setTitle("Technical GPA: Course "+major);

        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
//        ParseQuery<Course> query18 = ParseQuery.getQuery(Course.class);
        query.fromLocalDatastore();
//        query18.fromLocalDatastore();
        query.whereEqualTo("department", major);
        if (major.equals("6")) {
//            query18.whereEqualTo("department", "18");
        }
        query.findInBackground(new FindCallback<Course>() {
            @Override
            public void done(List<Course> courses, ParseException e) {
                classList.addAll(courses);

                if (major.equals("6")) {

                    ParseQuery<Course> query8 = ParseQuery.getQuery(Course.class);
                    query8.fromLocalDatastore();
                    query8.whereEqualTo("department", "8");
                    query8.findInBackground(new FindCallback<Course>() {
                        @Override
                        public void done(List<Course> courses, ParseException e) {
                            classList.addAll(courses);

                            ParseQuery<Course> query18 = ParseQuery.getQuery(Course.class);
                            query18.fromLocalDatastore();
                            query18.whereEqualTo("department", "18");
                            query18.findInBackground(new FindCallback<Course>() {
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
                    });


                } else {
                    Collections.reverse(classList);
                    MyArrayAdapter mClassAdapter = new MyArrayAdapter(getApplicationContext(), classList);
                    lvClasses = (ListView) findViewById(R.id.lvClasses);
                    lvClasses.setAdapter(mClassAdapter);
                    tvGpa = (TextView) findViewById(R.id.tvGpa);

                    recalculate();
                }




            }
        });
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
                intGrade= 5;
                break;
            case "B":
                intGrade= 4;
                break;
            case "C":
                intGrade= 3;
                break;
            case "D":
                intGrade= 2;
                break;
            case "E":
                intGrade= 1;
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
