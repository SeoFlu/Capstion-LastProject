package capstion.fluseo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import capstion.fluseo.Vo.Course;
import capstion.fluseo.Vo.ExistingCourse;
import capstion.fluseo.Vo.SupplyCourse;

public class AddSupplyCourse extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView nameSupply, dateSupply, startTimeSupply, endTimeSupply, placeSupply;

    private Context context;
    private Course selectedCourse;
    private Course supplyCourse;
    private String txtdate;
    //    private SupplyCourse supplyCourse;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private int id;
    private String date;
    private String uid;
    private Course existingCourse;
    private ArrayList<Course> courseDataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supply_course);

        courseDataList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (placeSupply.getText().equals("")) {
                    Toast.makeText(context, "장소를 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {

                    //데이터베이스 입력
                    supplyCourse.setCoursePlace(placeSupply.getText().toString());
                    supplyCourse.setCheck1Time(true);

                    int dayNum=0;
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                    try {
                        txtdate = supplyCourse.getCourseDate1();
                        Date date = formatter.parse(supplyCourse.getCourseDate1());
                        date = new Date(date.getTime() + (1000 * 60 * 60 * 24 * +1));
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        dayNum = cal.get(Calendar.DAY_OF_WEEK)-2; // 요일을 구해온다.
                        if(dayNum==-1){
                            supplyCourse.setCourseDate1("6");
                        }else{
                            supplyCourse.setCourseDate1(String.valueOf(dayNum));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    DatabaseReference myRef = database.getReference("SupplyCourse/" + txtdate + "/" + supplyCourse.getCourseName());
                    myRef.setValue(supplyCourse);

                    String getrow = String.valueOf(id).substring(0,1);

//                    existingCourse = searchCourseList(courseDataList,supplyCourse.getCourseName());
                    modifiyCourseCheck(selectedCourse,getrow);
                    database.getReference("Users").child(uid).child("CourseList").child(supplyCourse.getCourseName()).setValue(selectedCourse);
                    finish();
                }
            }

        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        selectedCourse = (Course) getIntent().getSerializableExtra("vo");

        database = FirebaseDatabase.getInstance();

        supplyCourse = new Course();

        context = this;

        nameSupply = findViewById(R.id.supplyName);
        dateSupply = findViewById(R.id.supplyDate);
        startTimeSupply = findViewById(R.id.supplyStartTime);
        endTimeSupply = findViewById(R.id.supplyEndTime);
        placeSupply = findViewById(R.id.supplyPlace);

        nameSupply.setText(selectedCourse.getCourseName());

        Button commitDateSupply = (Button) findViewById(R.id.supplyDateCommitBtn);
        Button commitTImeStartSupply = (Button) findViewById(R.id.supplyTimeStartCommitbtn);
        Button commitTimeEndSupply = (Button) findViewById(R.id.supplyTimeEndCommitbtn);

        supplyCourse.setCourseName(nameSupply.getText().toString());


        // 날짜 입력
        commitDateSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, datelistener, 2019, 11, 10);
                dialog.show();
            }
        });

        // 보강 시작 시간 입력
        commitTImeStartSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context, starttimelistener, 12, 0, true);
                dialog.show();
            }
        });

        // 보강 끝나는 시간 입력
        commitTimeEndSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context, endtimelistener, 12, 0, true);
                dialog.show();
            }
        });

        database.getReference("Users").child(uid).child("CourseList").child("0").removeValue();

        id = this.getIntent().getIntExtra("id",0);
//        String getrow = String.valueOf(id).substring(0,1);
//        getTimetableDatabase();
//        existingCourse = searchCourseList(courseDataList,supplyCourse.getCourseName());
//        modifiyCourseCheck(existingCourse,getrow);

    }


    // 달력 다이얼로그 날짜 설정
    private DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String stryear, strmonth, strdate;
            stryear = String.valueOf(year);
            strmonth = String.valueOf(month + 1);
            strdate = stryear + strmonth + changeDayStr(dayOfMonth);
            supplyCourse.setCourseDate1(strdate);
//            supplyCourse.setSupplyDate(strdate);
            dateSupply.setText(year + "년 " + strmonth + "월 " + dayOfMonth + "일");
        }
    };

    // 타임다이얼로그 시간 설정
    private TimePickerDialog.OnTimeSetListener starttimelistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hour = changeTimeto24(hourOfDay);
            String min = changeMin(minute);
            supplyCourse.setCourseStartTime1(String.valueOf(hourOfDay).concat(min));
//            supplyCourse.setSupplyStartTime(hour.concat(min));
            startTimeSupply.setText(hour + " : " + min);
        }
    };

    private TimePickerDialog.OnTimeSetListener endtimelistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hour = changeTimeto24(hourOfDay);
            String min = changeMin(minute);
            supplyCourse.setCourseEndTime1(String.valueOf(hourOfDay).concat(min));
//            supplyCourse.setSupplyEndTime(hour.concat(min));
            endTimeSupply.setText(hour + " : " + min);
        }
    };


    // 기존 강의 내역을 불러옵니다.
    private void getTimetableDatabase() {
        database.getReference("Users").child(uid).child("CourseList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Course newTimeTable = dataSnapshot.getValue(Course.class);
                courseDataList.add(newTimeTable);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Course newTimeTable = dataSnapshot.getValue(Course.class);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Course newTimeTable = dataSnapshot.getValue(Course.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //탐색 함수
    private Course searchCourseList(ArrayList<Course> list, String name) {
        Course course = new Course();

        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getCourseName())) {
                course = list.get(i);
            }
        }
        return course;
    }

    private void modifiyCourseCheck(Course course, String date){
        if(course.getCourseDate1().equals(date)){
            course.setCheck1Time(false);
        }else if(course.getCourseDate2().equals(date)){
            course.setCheck2Time(false);
        }
    }

    // 변수들 변경 함수
    private String changeTimeto24(int hour) {
        String strhour24;
        if (hour < 10) {
            strhour24 = "0".concat(String.valueOf(hour));
        } else {
            strhour24 = String.valueOf(hour);
        }
        return strhour24;
    }

    private String changeDayStr(int day) {
        String strday;
        if (day < 10) {
            strday = "0".concat(String.valueOf(day));
        } else {
            strday = String.valueOf(day);
        }
        return strday;
    }

    private String changeMin(int min) {
        String strMin;
        if (min == 0) {
            strMin = "00";
        } else {
            strMin = String.valueOf(min);
        }
        return strMin;
    }


    //기타 여러가지
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_supply_course, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notic) {

        } else if (id == R.id.nav_timetable) {
            Intent timeTableIntent = new Intent(AddSupplyCourse.this, TimetableActivity.class);
            startActivity(timeTableIntent);
        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_community) {
            Intent community1Intent = new Intent(AddSupplyCourse.this, CommuntiyActivity.class);
            startActivity(community1Intent);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
