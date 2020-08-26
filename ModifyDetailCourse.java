package capstion.fluseo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import capstion.fluseo.Vo.Course;

public class ModifyDetailCourse extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Course selectedCourse;
    TextView txtCourseName,txtCourseProname, txtCoursePlace;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_detail_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 그거 뭐야 그거 정보 넣어서 데이터베이스에 넣기

                selectedCourse.setCourseProfessor(txtCourseProname.getText().toString());
                selectedCourse.setCoursePlace(txtCoursePlace.getText().toString());

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String uid = firebaseUser.getUid();
                DatabaseReference myRef =
                        database.getReference("Users").child(uid).child("CourseList").child(selectedCourse.getCourseName());
                myRef.setValue(selectedCourse);
                Toast.makeText(ModifyDetailCourse.this, "수정 완료!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        selectedCourse = (Course)getIntent().getSerializableExtra("vo");

        txtCourseName = findViewById(R.id.modifyName);
        txtCourseProname = findViewById(R.id.modifyPro);
        txtCoursePlace = findViewById(R.id.modifyPlace);

        txtCourseName.setText(selectedCourse.getCourseName());

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
        getMenuInflater().inflate(R.menu.modify_detail_course, menu);
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
            Intent timeTableIntent = new Intent(ModifyDetailCourse.this,TimetableActivity.class);
            startActivity(timeTableIntent);
        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_community) {
            Intent community1Intent = new Intent(ModifyDetailCourse.this, CommuntiyActivity.class);
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
