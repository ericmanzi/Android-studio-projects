package eric.mitapp.mitgpa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ImageButton newClass;
    Spinner gradesSpinner;
    ListView lvClasses;
    EditText courseName;
    EditText courseUnits;
    TextView tvGpa;
    List<Course> classList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);

        Object actionBar = getSupportActionBar();
        android.support.v7.internal.app.WindowDecorActionBar bar = (android.support.v7.internal.app.WindowDecorActionBar) actionBar;
//        bar.hide();
        setContentView(R.layout.activity_main);
        newClass = (ImageButton) findViewById(R.id.newClass);
        newClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewClassDialog();
            }
        });
        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.fromLocalDatastore();
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Course>() {
            @Override
            public void done(List<Course> courses, ParseException e) {
                classList.addAll(courses);
                Collections.reverse(classList);

                MyArrayAdapter mClassAdapter = new MyArrayAdapter(getApplicationContext(), classList);
                lvClasses = (ListView) findViewById(R.id.lvClasses);
                lvClasses.setAdapter(mClassAdapter);
                setDismissView(mClassAdapter);
                tvGpa = (TextView) findViewById(R.id.tvGpa);

                recalculate();
            }
        });
    }

   public void showNewClassDialog() {
       LayoutInflater inflater = getLayoutInflater();
       final View alert_layout = inflater.inflate(R.layout.dialog_layout, null);
       courseName = (EditText) alert_layout.findViewById(R.id.etCourseName);
       courseUnits = (EditText) alert_layout.findViewById(R.id.etUnits);
       gradesSpinner = (Spinner) alert_layout.findViewById(R.id.spGrade);
       ArrayAdapter<CharSequence> gradesAdapter = ArrayAdapter.createFromResource(this,
               R.array.grades, R.layout.spinner_dropdown_item);
       gradesSpinner.setAdapter(gradesAdapter);
       new AlertDialog.Builder(this)
               .setTitle("Add new class")
               .setView(alert_layout)
               .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int whichButton) {
                       String newCourseName = courseName.getText().toString();
                       String tempCourseUnits= courseUnits.getText().toString();
                       String newCourseGrade= gradesSpinner.getSelectedItem().toString();
                       if (newCourseName.equals("") || newCourseGrade.equals("") || tempCourseUnits.equals("")) return;
                       int newCourseUnits = Integer.parseInt(tempCourseUnits);
                       Course newCourse = new Course();
                       newCourse.setCourseName(newCourseName);
                       newCourse.setClassUnits(newCourseUnits);
                       newCourse.setClassGrade(newCourseGrade);
                       classList.add(0, newCourse);
                       newCourse.pinInBackground(new SaveCallback() {
                           @Override
                           public void done(ParseException e) {
                               recalculate();
                           }
                       });
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int whichButton) {
                   }
               })
               .show();

   }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {launchTechnicalGPADialog();}
        if (id == R.id.action_settings1) {
            Intent i = new Intent(getApplicationContext(), convertGPA.class);
            startActivity(i);
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
            Log.i("arrayAdapter", "name:"+myClassList.get(position).getName());
            tvCourseName.setText(myClassList.get(position).getName());
            tvCourseUnits.setText(myClassList.get(position).getUnitsStr());
            tvCourseGrade.setText(myClassList.get(position).getGrade());
            return rowView;
        }
    }

    public void setDismissView(MyArrayAdapter mAdapter1) {
        final MyArrayAdapter mAdapter = mAdapter1;
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(lvClasses,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {return true;}
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Toast.makeText(getApplicationContext(), classList.get(position).getName()
                                            +" was removed from your classes", Toast.LENGTH_SHORT).show();
                                    Course deletedCourse = mAdapter.getItem(position);
                                    deletedCourse.unpinInBackground();
                                    mAdapter.remove(mAdapter.getItem(position));
                                }
                                mAdapter.notifyDataSetChanged();
                                recalculate();
                            }
                        });
        lvClasses.setOnTouchListener(touchListener);
        lvClasses.setOnScrollListener(touchListener.makeScrollListener());
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

    public void launchTechnicalGPADialog() {
        final EditText major = new EditText(getApplicationContext());
        major.setTextColor((Color.parseColor("#555555")));
        major.setInputType(InputType.TYPE_CLASS_NUMBER);
        major.setGravity(Gravity.CENTER_HORIZONTAL);
        new AlertDialog.Builder(this)
                .setTitle("Select your major")
                .setView(major)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(getApplicationContext(), TechnicalGPA.class);
                        i.putExtra("major", major.getText().toString());
                        startActivity(i);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }
}
