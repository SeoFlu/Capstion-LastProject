package capstion.fluseo;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import capstion.fluseo.Vo.BoardMenu;
import capstion.fluseo.dataController.BoardListViewAdapter;

public class CommunityActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference communityBoard = database.getReference("community2");
    ListView boardListView;
    private List<BoardMenu> boardList;
    private BoardListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commnutiy);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("주변 맛집_텍티비티 2");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commitCommunutiyIntent = new Intent(CommunityActivity2.this,WriteCommnuityActivity.class);
                commitCommunutiyIntent.putExtra("boardNum",2);
                startActivity(commitCommunutiyIntent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        boardListView = (ListView)findViewById(R.id.boardList);
        boardList = new ArrayList<BoardMenu>();

        getDatabaseBorad();

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
        getMenuInflater().inflate(R.menu.commnutiy, menu);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        boardList.clear();
        getDatabaseBorad();
    }

    private void getDatabaseBorad(){
        communityBoard.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                BoardMenu boardMenu = dataSnapshot.getValue(BoardMenu.class);
                boardMenu.setBoardKey(dataSnapshot.getKey());
                boardList.add(boardMenu);
                displayNotice(boardList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                BoardMenu boardMenu = dataSnapshot.getValue(BoardMenu.class);
                boardMenu.setBoardKey(dataSnapshot.getKey());
//                boardList.add(boardMenu);
                displayNotice(boardList);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                BoardMenu boardMenu = dataSnapshot.getValue(BoardMenu.class);
                boardMenu.setBoardKey(dataSnapshot.getKey());
                displayNotice(boardList);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                BoardMenu boardMenu = dataSnapshot.getValue(BoardMenu.class);
                boardMenu.setBoardKey(dataSnapshot.getKey());
                displayNotice(boardList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayNotice(List<BoardMenu> boardMenuList){

        adapter = new BoardListViewAdapter(boardMenuList,getApplicationContext());
        boardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoardMenu boardMenu = boardList.get(position);
                int hitCount = boardMenu.getBoardHitCount();
                boardMenu.setBoardHitCount(hitCount+1);
                DatabaseReference communityBoardModify = database.getReference("community2/"+boardMenu.getBoardKey());
                communityBoardModify.setValue(boardMenu);
                Intent detailIntent = new Intent(view.getContext(),DetailActivity.class);
                detailIntent.putExtra("boardNum",2);
                detailIntent.putExtra("boardDetail",boardMenu);
                startActivity(detailIntent);
            }
        });
        boardListView.setAdapter(adapter);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_community1) {
            Intent intent = new Intent(CommunityActivity2.this,CommuntiyActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_community2) {
            Intent intent = new Intent(CommunityActivity2.this,CommunityActivity2.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_community3) {
            Intent intent = new Intent(CommunityActivity2.this,CommunityActivity3.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
