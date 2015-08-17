package eric.mitapp.mitgpa;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by ericmanzi on 5/18/15.
 */
@ParseClassName("Course")
public class Course extends ParseObject {
    public Course() {}

    public void setCourseName(String courseName) {
        put("courseName", courseName);
        String department = courseName.split("\\.")[0];
        put("department", department);
    }
    public String getName() { return getString("courseName");}
    public String getDepartment() { return getString("department");}

    public void setClassUnits(int classUnits) { put("classUnits", classUnits);}
    public int getUnits() { return getInt("classUnits");}
    public String getUnitsStr() { return String.valueOf(getInt("classUnits"));}

    public void setClassGrade(String classGrade) { put("classGrade", classGrade);}
    public String getGrade() { return getString("classGrade");}

}
