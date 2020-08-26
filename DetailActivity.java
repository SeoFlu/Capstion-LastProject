package capstion.fluseo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import capstion.fluseo.Vo.BoardMenu;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback{

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference communityBoard = database.getReference("community"); // 이거 위치 변경
    private TextView detailName,detailContext, detailDate, detailAuther,detailHitCount;
    private FloatingActionButton modifyFab, deleteFab;
    private Intent intent;
    private Boolean delBool = false;
    private int boardNum;
    private BoardMenu boardMenu;
    private Context context;

    private GoogleMap mMap;
    private Geocoder geocoder;
    private MarkerOptions selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context = this;

        detailName = findViewById(R.id.tvDetailName);
        detailContext = findViewById(R.id.tvDetailContext);
        detailDate = findViewById(R.id.tvDetailDate);
        detailHitCount = findViewById(R.id.tvDetailHitCount);
        detailAuther = findViewById(R.id.tvDetailAuthor);

        modifyFab  = findViewById(R.id.fabModifiy);
        deleteFab = findViewById(R.id.fabDelete);

        intent = getIntent();

        boardMenu = (BoardMenu)intent.getSerializableExtra("boardDetail");
        boardNum = intent.getExtras().getInt("boardNum");
        detailName.setText(boardMenu.getBoardName());
        detailContext.setText(boardMenu.getBoardContent());
        int hitCount = boardMenu.getBoardHitCount();
        String strHitCount = String.valueOf(hitCount);
        detailHitCount.setText(strHitCount);
        detailDate.setText(boardMenu.getBoardDate());
        detailAuther.setText(boardMenu.getBoardAuthor());

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.detailMap);
        mapFragment.getMapAsync(this);
        if(boardNum!=2){
            // 이거 따로 할까? 동적으로 안될꺼같은데
            LinearLayout linearLayout = findViewById(R.id.mapLayout);
            linearLayout.removeAllViews();
        }


        modifyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modifyIntent = new Intent(DetailActivity.this,WriteCommnuityActivity.class);
                BoardMenu modifyBoard = boardMenu;
                modifyIntent.putExtra("modiData",modifyBoard);
                modifyIntent.putExtra("boardCommitNum",boardNum);
                startActivity(modifyIntent);
                finish();
            }
        });


        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delBool){
                    database.getReference("community"+boardNum+"/"+boardMenu.getBoardKey()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                finish();
                        }
                    });
                    delBool = false;
                }else{
                    Toast.makeText(DetailActivity.this, "정말로 삭제하시겠습니까?\n정말로 삭제하실려면 한번더 눌러주세요.", Toast.LENGTH_SHORT).show();
                    delBool = true;
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if(boardMenu.getxPosition()==null){
            LinearLayout linearLayout = findViewById(R.id.mapLayout);
            linearLayout.removeAllViews();
        }else{
            LatLng location = new LatLng(boardMenu.getxPosition(),boardMenu.getyPosition());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title("지정된 위치");
            googleMap.addMarker(markerOptions);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,5));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }


    }



}
