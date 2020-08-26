package capstion.fluseo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.accounttransfer.AccountTransfer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import capstion.fluseo.Vo.Account;
import capstion.fluseo.Vo.Course;
import capstion.fluseo.Vo.NoticeList;
import capstion.fluseo.Vo.SupplyCourse;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class TimetableActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TableLayout tableLayout;

    ArrayList<Course> courseArrayList;
    ArrayList<Course> courseDataList = new ArrayList<Course>();

    private Dialog dialog;
    private Context context;
    private WindowManager.LayoutParams params1;
    private TextView nameCourse, date1Course, timeStart1Course, timeEnd1Course, date2Course, timeStart2Course, timeEnd2Course, proCourse, placeCourse;
    private TextView txtModify, txtSupply;
    private Course selecetedCourse;
    private int courseTxtID;
    private String lastTime;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private String uid;
    private int startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.getReference("Users").child(uid).child("CourseList").removeValue();
                courseArrayList = Excel2();
                tableLayout.removeAllViews();
                drawTimeTable(courseArrayList);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        context = this;
        tableLayout = findViewById(R.id.tableLayout);

        courseDataList = new ArrayList<Course>();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getUid();

        startDate = settingActionBar();

        getSupplyTimeTableDatabase(startDate+1);
        getTimetableDatabase();


        // 강의실 정보 따로 입력해야되고. 나머지는 교수명정도.
        // 근데 눌러서 보강정보도 뜨게 해야됨
        // 보강정보 알고리즘은 현재 시간이랑 보강 시간이랑  불러올때 비교해서 넣어줍시다.

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        courseDataList.clear();
        getSupplyTimeTableDatabase(startDate+1);
        getTimetableDatabase();
    }

    private void getLastTIme(){
        database.getReference("LastTime").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                lastTime = dataSnapshot.getValue();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //보강 내역 불러올꺼임
    private void getSupplyTimeTableDatabase(int startDate) {
        for (int i = 0; i < 7; i++) {
            database.getReference("SupplyCourse").child(String.valueOf(startDate + i)).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Course supplyCourse = dataSnapshot.getValue(Course.class);
                    if (supplyCourse.getCourseName() == null) {

                    } else {
                        courseDataList.add(supplyCourse);
                        tableLayout.removeAllViews();
                        drawTimeTable(courseDataList);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Course supplyCourse = dataSnapshot.getValue(Course.class);
                    if (supplyCourse.getCourseName() == null) {

                    } else {
                        courseDataList.add(supplyCourse);
                        tableLayout.removeAllViews();
                        drawTimeTable(courseDataList);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Course supplyCourse = dataSnapshot.getValue(Course.class);
                    if (supplyCourse.getCourseName() == null) {

                    } else {
                        courseDataList.add(supplyCourse);
                        tableLayout.removeAllViews();
                        drawTimeTable(courseDataList);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Course supplyCourse = dataSnapshot.getValue(Course.class);
                    if (supplyCourse.getCourseName() == null) {

                    } else {
                        courseDataList.add(supplyCourse);
                        tableLayout.removeAllViews();
                        drawTimeTable(courseDataList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void getTimetableDatabase() {
        database.getReference("Users").child(uid).child("CourseList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Course newTimeTable = dataSnapshot.getValue(Course.class);
                courseDataList.add(newTimeTable);
                tableLayout.removeAllViews();
                drawTimeTable(courseDataList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Course newTimeTable = dataSnapshot.getValue(Course.class);
                tableLayout.removeAllViews();
                drawTimeTable(courseDataList);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Course newTimeTable = dataSnapshot.getValue(Course.class);
                tableLayout.removeAllViews();
                drawTimeTable(courseDataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //해당 일반 출력단
    private void drawTimeTable(List<Course> list) {
        //첫번째 날짜
        TableRow tableDate = new TableRow(this);
        tableDate.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        for (int i = 0; i < 7; i++) {
            TextView textView = new TextView(this);
            String date = changeNumtoDate(i);
            textView.setText(date);
//            textView.setBackgroundResource(R.drawable.border);
            textView.setPadding(20, 20, 20, 20);
            tableDate.addView(textView);
        }

        tableLayout.addView(tableDate, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        for (int c = 1; c < 11; c++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));

            TextView textView = new TextView(this);
            textView.setText(c + "교시");
//            textView.setBackgroundResource(R.drawable.border);
            textView.setPadding(20, 20, 20, 20);
            tableRow.addView(textView);


            for (int r = 1; r < 7; r++) {
                TextView txtView = new TextView(this);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getCheck2Time()) {
                        if ((changeTimetoInt(list.get(i).getCourseStartTime2()) <= c && changeTimetoInt(list.get(i).getCourseEndTime2()) >= c) && Integer.parseInt(list.get(i).getCourseDate2()) == r) {
                            txtView.setText(list.get(i).getCourseName());
                            String strrow = String.valueOf(r);
                            String strcol = String.valueOf(c);
                            int id = Integer.parseInt(strrow.concat(strcol));
                            txtView.setId(id);
                            txtView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TextView txt = (TextView) v;
                                    String txtname = txt.getText().toString();
                                    Course selectedCourse = searchCourseList(courseDataList, txtname);
                                    showTableDialog(selectedCourse,v.getId());
                                }
                            });
                            txtView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    TextView txt = (TextView) v;
                                    String txtname = txt.getText().toString();
                                    Course selectedCourse = searchCourseList(courseDataList, txtname);
                                    showTableSelectDialog(selectedCourse,v.getId());
                                    return true;
                                }
                            });
                        }
                    }
                    if (list.get(i).getCheck1Time()) {
                        if ((changeTimetoInt(list.get(i).getCourseStartTime1()) <= c && changeTimetoInt(list.get(i).getCourseEndTime1()) >= c) && Integer.parseInt(list.get(i).getCourseDate1()) == r) {
                            txtView.setText(list.get(i).getCourseName());
                            String strrow = String.valueOf(r);
                            String strcol = String.valueOf(c);
                            int id = Integer.parseInt(strrow.concat(strcol));
                            txtView.setId(id);
                            txtView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TextView txt = (TextView) v;
                                    String txtname = txt.getText().toString();
                                    Course selectedCourse = searchCourseList(courseDataList, txtname);
                                    showTableDialog(selectedCourse,v.getId());
                                }
                            });
                            txtView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    TextView txt = (TextView) v;
                                    String txtname = txt.getText().toString();
                                    Course selectedCourse = searchCourseList(courseDataList, txtname);
                                    showTableSelectDialog(selectedCourse,v.getId());
                                    return true;
                                }
                            });
                        }
                    }
                }
//                txtView.setBackgroundResource(R.drawable.border);
                txtView.setPadding(20, 20, 20, 20);
                tableRow.addView(txtView);
            }

            tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        }
    }


    //다이얼로그 선택시 뜨는거
    private void showTableSelectDialog(Course course,int id) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9);
        int height = (int) (dm.heightPixels * 0.2);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
        dialog = new Dialog(context);

        selecetedCourse = course;
        courseTxtID = id;

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.course_select_dialog);

        params1 = dialog.getWindow().getAttributes();
        params1.width = width;
        params1.height = height;
        dialog.getWindow().setAttributes(params1);


        txtModify = (TextView) dialog.findViewById(R.id.selectModify);
        txtSupply = (TextView) dialog.findViewById(R.id.selectSupply);

        // 수정 버튼 클릭 리스너
        txtModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modifyCourseIntent = new Intent(TimetableActivity.this, ModifyDetailCourse.class);
                modifyCourseIntent.putExtra("vo", selecetedCourse);
                startActivity(modifyCourseIntent);
            }
        });
        // 보강 버튼 클릭 리스너
        txtSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addCourseIntent = new Intent(TimetableActivity.this, AddSupplyCourse.class);
                addCourseIntent.putExtra("id", courseTxtID);
                addCourseIntent.putExtra("vo", selecetedCourse);
                startActivity(addCourseIntent);
            }
        });

        dialog.show();
    }

    // 누르면 나오는 다이얼로그
    private void showTableDialog(Course course) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9);
        int height = (int) (dm.heightPixels * 0.9);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.course_display_detail);

        params1 = dialog.getWindow().getAttributes();
        params1.width = width;
        params1.height = height;
        dialog.getWindow().setAttributes(params1);

        nameCourse = dialog.findViewById(R.id.courseName);
        timeStart1Course = dialog.findViewById(R.id.courseStartTime1);
        timeStart2Course = dialog.findViewById(R.id.courseStartTime2);
        date1Course = dialog.findViewById(R.id.courseDate1);
        date2Course = dialog.findViewById(R.id.courseDate2);
        timeEnd1Course = dialog.findViewById(R.id.courseEndTime1);
        timeEnd2Course = dialog.findViewById(R.id.courseEndTime2);
        proCourse = dialog.findViewById(R.id.coursePro);
        placeCourse = dialog.findViewById(R.id.coursePlace);

        if (!course.getCheck2Time()) {
            nameCourse.setText(course.getCourseName());
            timeStart1Course.setText(course.getCourseStartTime1() + " ~ ");
            timeEnd1Course.setText(course.getCourseEndTime1());
            date1Course.setText(changeNumtoDate(Integer.parseInt(course.getCourseDate1())).concat(" "));
            proCourse.setText(course.getCourseProfessor());
            placeCourse.setText(course.getCoursePlace());
        } else {
            nameCourse.setText(course.getCourseName());
            timeStart1Course.setText(course.getCourseStartTime1() + " ~ ");
            timeStart2Course.setText(course.getCourseStartTime2() + " ~ ");
            timeEnd1Course.setText(course.getCourseEndTime1());
            timeEnd2Course.setText(course.getCourseEndTime2());
            date1Course.setText(changeNumtoDate(Integer.parseInt(course.getCourseDate1())).concat(" "));
            date2Course.setText(changeNumtoDate(Integer.parseInt(course.getCourseDate2())).concat(" "));
            proCourse.setText(course.getCourseProfessor());
            placeCourse.setText(course.getCoursePlace());
        }
        dialog.show();
    }

    private void showTableDialog(Course course,int id) {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.9);
        int height = (int) (dm.heightPixels * 0.9);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.course_display_detail);

        params1 = dialog.getWindow().getAttributes();
        params1.width = width;
        params1.height = height;
        dialog.getWindow().setAttributes(params1);

        nameCourse = dialog.findViewById(R.id.courseName);
        timeStart1Course = dialog.findViewById(R.id.courseStartTime1);
        timeStart2Course = dialog.findViewById(R.id.courseStartTime2);
        date1Course = dialog.findViewById(R.id.courseDate1);
        date2Course = dialog.findViewById(R.id.courseDate2);
        timeEnd1Course = dialog.findViewById(R.id.courseEndTime1);
        timeEnd2Course = dialog.findViewById(R.id.courseEndTime2);
        proCourse = dialog.findViewById(R.id.coursePro);
        placeCourse = dialog.findViewById(R.id.coursePlace);

        TextView txt = dialog.findViewById(R.id.testid);
        txt.setText(String.valueOf(id));

        if (!course.getCheck2Time()) {
            nameCourse.setText(course.getCourseName());
            timeStart1Course.setText(course.getCourseStartTime1() + " ~ ");
            timeEnd1Course.setText(course.getCourseEndTime1());
            date1Course.setText(changeNumtoDate(Integer.parseInt(course.getCourseDate1())).concat(" "));
            proCourse.setText(course.getCourseProfessor());
            placeCourse.setText(course.getCoursePlace());
        } else {
            nameCourse.setText(course.getCourseName());
            timeStart1Course.setText(course.getCourseStartTime1() + " ~ ");
            timeStart2Course.setText(course.getCourseStartTime2() + " ~ ");
            timeEnd1Course.setText(course.getCourseEndTime1());
            timeEnd2Course.setText(course.getCourseEndTime2());
            date1Course.setText(changeNumtoDate(Integer.parseInt(course.getCourseDate1())).concat(" "));
            date2Course.setText(changeNumtoDate(Integer.parseInt(course.getCourseDate2())).concat(" "));
            proCourse.setText(course.getCourseProfessor());
            placeCourse.setText(course.getCoursePlace());
        }
        dialog.show();
    }

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
        getMenuInflater().inflate(R.menu.timetable, menu);
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

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_community) {
            Intent community1Intent = new Intent(TimetableActivity.this, CommuntiyActivity.class);
            startActivity(community1Intent);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public ArrayList<Course> Excel2() {
        ArrayList<Course> courseLIst = new ArrayList<Course>();

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("test.xls");
            WorkbookSettings settings = new WorkbookSettings();
            settings.setEncoding("EUC-KR");
            Workbook wb = Workbook.getWorkbook(is, settings);

            if (wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if (sheet != null) {
                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = 11;

                    String str, nowCourseName = "";
                    String nowName, preName = "";
                    boolean secBool = false;
                    int nowCol = 0;

                    for (int col = 1; col < colTotal; col++) {
                        Course course = new Course();
                        for (int row = rowIndexStart; row < rowTotal; row++) {
//                            str = sheet.getCell(col,row).getContents(); // 엑셀 텍스트 읽기
                            nowName = sheet.getCell(col, row).getContents();

                            if (searchNameList(courseLIst, nowName) || secBool) {
                                // 리스트 안에 강의가 있다면
                                if (!course.getCheck2Time()) {
                                    course = searchCourseList(courseLIst, nowName);
                                    secBool = true;
                                }
                                if (preName.equals(nowName)) {
                                    //연강시
                                    course.setCourseEndTime2(changeTimeStringtoTime(String.valueOf(row)));
                                } else {
                                    //아닐시
                                    if (course.getCheck2Time()) {
                                        if (course.getCourseEndTime2() == null) {
                                            course.setCourseEndTime2(changeTimeStringtoTime(String.valueOf(row - 1)));
                                        }
                                        preName = "";
                                        courseLIst.add(course);
//                                        database.getReference("Users").child(uid).child("CourseList").child(course.getCourseName()).setValue(course);
                                        database.getReference("Users/" + uid + "/CourseList").child(course.getCourseName()).setValue(course);
                                        secBool = false;
                                        course = new Course();
                                    }
                                    preName = nowName;
                                    course.setCourseDate2(String.valueOf(col));
                                    course.setCourseStartTime2(changeTimeStringtoTime(String.valueOf(row)));
                                    course.setCheck2Time(true);
                                }
                            } else {
                                //리스트 안에 강의가 없다면?
                                if (preName.equals(nowName)) {
                                    //연강시
                                    course.setCourseEndTime1(changeTimeStringtoTime(String.valueOf(row)));
                                } else {
                                    //연강이 아닐시
                                    if (course.getCheck1Time()) {
                                        if (course.getCourseEndTime1() == null) {
                                            course.setCourseEndTime1(changeTimeStringtoTime(String.valueOf(row - 1)));
                                        }
                                        preName = "";
                                        courseLIst.add(course);
                                        database.getReference("Users").child(uid).child("CourseList").child(course.getCourseName()).setValue(course);
                                    }
                                    course = new Course();
                                    preName = nowName;
                                    course.setCourseName(nowName);
                                    course.setCourseDate1(String.valueOf(col));
                                    course.setCourseStartTime1(changeTimeStringtoTime(String.valueOf(row)));
                                    course.setCheck1Time(true);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        return courseLIst;
    }

    private Course searchCourseList(ArrayList<Course> list, String name) {
        Course course = new Course();

        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getCourseName())) {
                course = list.get(i);
            }
        }
        return course;
    }

    private boolean searchNameList(ArrayList<Course> list, String name) {
        boolean returnbool = false;

        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getCourseName())) {
                returnbool = true;
            }
        }
        return returnbool;
    }


    public int settingActionBar() {
        ActionBar actionBar = getSupportActionBar();

        Calendar c1 = Calendar.getInstance();

        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, 1);

        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);

        //last day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);

        int year7 = c1.get(Calendar.YEAR);
        int month7 = c1.get(Calendar.MONTH) + 1;
        int day7 = c1.get(Calendar.DAY_OF_MONTH);

        actionBar.setTitle(year1 + "년 " + month1 + "월 " + day1 + "일 / " + year7 + "년 " + month7 + "월 " + day7 + "일");

        String startDate = String.valueOf(year1).concat(String.valueOf(month1)).concat(String.valueOf(day1));
        String endTime = String.valueOf(year7).concat(String.valueOf(month7)).concat(String.valueOf(day7));

        database.getReference().child("LastTime").setValue(endTime);
        int startintDate = Integer.parseInt(startDate);

        return startintDate;
    }


    private String changeNumtoDate(int index) {
        String str = new String();

        switch (index) {
            case 0:
                str = "";
                break;
            case 1:
                str = "Mon";
                break;
            case 2:
                str = "Tue";
                break;
            case 3:
                str = "Wen";
                break;
            case 4:
                str = "Thu";
                break;
            case 5:
                str = "Fri";
                break;
            case 6:
                str = "Sat";
                break;
        }
        return str;

    }

    private String changeDate(String date) {
        String returnstr = "";

        switch (date) {
            case "월":
                returnstr = "1";
                break;
            case "화":
                returnstr = "2";
                break;
            case "수":
                returnstr = "3";
                break;
            case "목":
                returnstr = "4";
                break;
            case "금":
                returnstr = "5";
                break;
            case "토":
                returnstr = "6";
                break;
        }

        return returnstr;
    }

    private String changeTimeStringtoTime(String time) {
        String returnTime = "";

        switch (time) {
            case "1":
                returnTime = "0900";
                break;
            case "2":
                returnTime = "1000";
                break;
            case "3":
                returnTime = "1100";
                break;
            case "4":
                returnTime = "1200";
                break;
            case "5":
                returnTime = "1300";
                break;
            case "6":
                returnTime = "1400";
                break;
            case "7":
                returnTime = "1500";
                break;
            case "8":
                returnTime = "1600";
                break;
            case "9":
                returnTime = "1700";
                break;
            case "10":
                returnTime = "1800";
                break;
        }
        return returnTime;
    }

    private int changeTimetoInt(String time) {
        int indexTime = 0;

        switch (time) {
            case "0900":
                indexTime = 1;
                break;
            case "1000":
                indexTime = 2;
                break;
            case "1100":
                indexTime = 3;
                break;
            case "1200":
                indexTime = 4;
                break;
            case "1300":
                indexTime = 5;
                break;
            case "1400":
                indexTime = 6;
                break;
            case "1500":
                indexTime = 7;
                break;
            case "1600":
                indexTime = 8;
                break;
            case "1700":
                indexTime = 9;
                break;
            case "1800":
                indexTime = 10;
                break;
        }

        return indexTime;
    }
}
