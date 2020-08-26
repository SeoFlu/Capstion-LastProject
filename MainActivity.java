package capstion.fluseo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import capstion.fluseo.Vo.BoardMenu;
import capstion.fluseo.Vo.NoticeList;
import capstion.fluseo.dataController.NoticesListAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //왼쪽 메뉴 리스트
//    private ExpandableListView listView;

    private Context context;
    private String[][] crollingValues;
    //인증
    private FirebaseAuth auth;

    //데이터베이스
    private FirebaseDatabase database;
    // 공지사항 리스트
    private NoticesListAdapter adapter;
    private List<NoticeList> noticeList;


    // 크롤링 변수
    private String htmlpageURL =
            "https://www.dju.ac.kr/kor/html/subp.htm?page_code=01050100&type=info&code=dn_whole&codeno=1&searchkey=&searchfield=&absoluteimgpath=&page=01";
    private String detailURL;
    int valueIndex = 5;
    private int noticeNum;
    private String crollingDetailStr[];
    private TextView titletxt, authortxt, datetxt, contexttxt;
    private Dialog dialog;
    private WindowManager.LayoutParams params1;
    private Boolean waitCrollingBool=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        //인증
        auth = FirebaseAuth.getInstance();
        if (auth == null) {

            Toast.makeText(MainActivity.this, "인증하세요", Toast.LENGTH_SHORT).show();
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        crollingDetailStr = new String[5];
        noticeList = new ArrayList<NoticeList>();
        // Change Title name
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Deajeon Uni App");

        //데이터 베이스 변수 초기화
        database = FirebaseDatabase.getInstance();
        getDatabaseNotice();

        // 크롤링 -> 데이터베이스
        JsoupAsyncTask JsoupAsyncTask = new JsoupAsyncTask();
        JsoupAsyncTask.execute();


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
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent timeTableIntent = new Intent(MainActivity.this,TimetableActivity.class);
            startActivity(timeTableIntent);
        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_community) {
            Intent community1Intent = new Intent(MainActivity.this, CommuntiyActivity.class);
            startActivity(community1Intent);
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getDatabaseNotice() {
        database.getReference("Notice").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                NoticeList newNotice = dataSnapshot.getValue(NoticeList.class);
                newNotice.setKey(dataSnapshot.getKey());
                noticeList.add(newNotice);
                Collections.sort(noticeList);
                Collections.reverse(noticeList);
                displayNotice(noticeList);
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


    //데이터베이스 삽입
    private void setDatabaseValue() {
        for (int i = 0; i < 10; i++) {
            NoticeList newLIst = new NoticeList(crollingValues[i][0], crollingValues[i][1], crollingValues[i][2], crollingValues[i][4],crollingValues[i][6]);
            database.getReference("Notice")
                    .child(crollingValues[i][0])
                    .setValue(newLIst);
        }
    }

    //출력
    private void displayNotice(final List<NoticeList> noticeList) {
        ListView noticeListView;
        noticeListView = (ListView) findViewById(R.id.noticeListView);
        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                int width = (int) (dm.widthPixels * 0.9);
                int height = (int) (dm.heightPixels * 0.9);
                getWindow().getAttributes().width = width;
                getWindow().getAttributes().height = height;

                detailURL = crollingValues[position][6];

                dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.notice_layout);

                params1 = dialog.getWindow().getAttributes();
                params1.width = width;
                params1.height = height;
                dialog.getWindow().setAttributes(params1);

                crollingDetailNotice crollingDetailNotice = new crollingDetailNotice();
                crollingDetailNotice.execute();

                while(!waitCrollingBool){
                    titletxt = dialog.findViewById(R.id.tvNoticeName);
                    authortxt = dialog.findViewById(R.id.tvNoticeAuthor);
                    datetxt = dialog.findViewById(R.id.tvNoticedDate);
                    contexttxt = dialog.findViewById(R.id.tvContext);

                    titletxt.setText(crollingDetailStr[0]);
                    authortxt.setText(crollingDetailStr[1]);
                    datetxt.setText(crollingDetailStr[2]);
                    contexttxt.setText(crollingDetailStr[3]);

                    dialog.show();
                }

                waitCrollingBool=false;

            }
        });
        adapter = new NoticesListAdapter(getApplicationContext(), noticeList);
        noticeListView.setAdapter(adapter);
    }




//    //크롤링 코드
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(htmlpageURL).get();

                Elements titles = doc.select("table.bbs tbody tr td");

                int index = 0;

                crollingValues = new String[10][7];
                for (Element e : titles) {
                    crollingValues[index / 6][index % 6] = e.text().trim();
                    if(index%6==1){
                        String myURL = e.select("a").attr("abs:href");
                        crollingValues[index / 6][6] = myURL;
                    }
                    index++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            setDatabaseValue();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //textviewHtmlDocument.setText(htmlContentInStringFormat);
            //텍스트 검사
        }
    }


    private class crollingDetailNotice extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document doc = Jsoup.connect(detailURL).get();

                Elements titles = doc.select("table.bbs").select("tr");
                int index = 0;
                for(Element e : titles){
                    switch (index){
                        case 0:
                            crollingDetailStr[index] = e.select("b").text();
                            break;
                        case 1:
                            crollingDetailStr[index] = e.select("td").text();
                            break;
                        case 4:
                            crollingDetailStr[2] = e.select("td").text();
                            break;
                    }
                    index++;
                }

                titles = doc.select("div.core").select("p");
                crollingDetailStr[3]="";
                for(Element e : titles){
                        crollingDetailStr[3] += e.text().trim()+"\n";
                }

                waitCrollingBool=true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}
