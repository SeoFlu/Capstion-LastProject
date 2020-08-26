package capstion.fluseo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import capstion.fluseo.Vo.BoardMenu;

public class WriteCommnuityActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference communityBoard;

    private Button commitButton;
    private EditText boardNmaeEdit,boardContentEdt, boardPasswdEdit,boardAuther;
    private LinearLayout linearLayout;
    private int boardNum;
    private boolean modifyBool = false;
    private BoardMenu boardMenu;
    private Double xPosition,yPosition;

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Dialog dialog;
    private Context context;
    private WindowManager.LayoutParams params;

// 순서 정해줘야겠네

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_commnuity);

        context = this;

        linearLayout = findViewById(R.id.writeLiner);
        boardNmaeEdit = findViewById(R.id.editBoardName);
        boardAuther = findViewById(R.id.editBoardAuther);
        boardPasswdEdit = findViewById(R.id.editBoardPasswd);
        boardContentEdt = findViewById(R.id.editBoardContext);
        commitButton = findViewById(R.id.btnCommitbtn);

        Intent intent = getIntent();
        boardMenu = (BoardMenu)intent.getSerializableExtra("modiData");

        boardNum = intent.getExtras().getInt("boardCommitNum");
        if(boardNum==0){
            boardNum = intent.getExtras().getInt("boardNum");
        }
        communityBoard = database.getReference("community"+boardNum);

        if(boardMenu!=null){
            // 원래 글 있을때 말하는거 아닌가
            boardNmaeEdit.setText(boardMenu.getBoardName());
            boardAuther.setText(boardMenu.getBoardAuthor());
            boardPasswdEdit.setText(boardMenu.getBoardPasswd());
            boardContentEdt.setText(boardMenu.getBoardContent());
            modifyBool = true;
        }

        if(boardMenu==null){
            boardMenu = new BoardMenu();
        }


        // 보드넘버 2면 지도정보도 불러와야됨
        if(boardNum==2){
            Button button = new Button(this);
            button.setText("위치 추가");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                    int width = (int) (dm.widthPixels * 0.9);
                    int height = (int) (dm.heightPixels * 0.9);
                    getWindow().getAttributes().width = width;
                    getWindow().getAttributes().height = height;
                    dialog = new Dialog(context);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.write_map_context);

                    params = dialog.getWindow().getAttributes();
                    params.width = width;
                    params.height = height;
                    dialog.getWindow().setAttributes(params);

                    final EditText searchEditText = dialog.findViewById(R.id.searchPlaceEdit);
                    Button searchButton = dialog.findViewById(R.id.searchButton);
                    Button commitButton = dialog.findViewById(R.id.resultButton);

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(final GoogleMap googleMap) {

                            mMap = googleMap;
                            geocoder = new Geocoder(dialog.getContext());

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    MarkerOptions mOptions = new MarkerOptions();

                                    mOptions.title("마커 좌표");
                                    Double latitude = latLng.latitude;
                                    Double longitude = latLng.longitude;
                                    mOptions.snippet(latitude.toString() + ", " + longitude.toString());

                                    mOptions.position(new LatLng(latitude,longitude));
                                    boardMenu.setPosition(latitude,longitude);

                                    googleMap.clear();
                                    googleMap.addMarker(mOptions);

                                }
                            });
                            LatLng sydney = new LatLng(-34, 151);
                            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        }
                    });


                    searchButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String str = searchEditText.getText().toString();
                            List<Address> addressList = null;
                            try{
                                addressList = geocoder.getFromLocationName(str,10);
                            }catch (IOException e){
                                e.printStackTrace();
                            }

                            String[] splitStr = addressList.get(0).toString().split(",");
                            String address = splitStr[0].substring(splitStr[0].indexOf("\"")+1,splitStr[0].length()-2);


                            String latitude = splitStr[10].substring(splitStr[10].indexOf("=")+1);
                            String longitude = splitStr[12].substring(splitStr[12].indexOf("=")+1);

                            boardMenu.setPosition(Double.parseDouble(latitude),Double.parseDouble(longitude));

                            LatLng point = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

                            MarkerOptions mOptions2 = new MarkerOptions();
//                            mOptions2.title("search result");

                            mOptions2.snippet(address);
                            mOptions2.position(point);

                            // 마커 추가
                            mMap.addMarker(mOptions2);
                            // 해당 좌표로 화면 줌
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
                        }
                    });

                    commitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 뭘 보내야되지? 안에 더블 두개 해서 넣어야 되고
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
            linearLayout.addView(button);
        }


        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(modifyBool){
////
////                }else{
////                    boardMenu = new BoardMenu();
////                }

                boardMenu.setBoardName(boardNmaeEdit.getText().toString());
                boardMenu.setBoardAuthor(boardAuther.getText().toString());
                boardMenu.setBoardPasswd(boardPasswdEdit.getText().toString());
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd");
                String formatDate = sdfNow.format(date);
                boardMenu.setBoardDate(formatDate);
                boardMenu.setBoardContent(boardContentEdt.getText().toString());
                setDatabase(boardMenu,boardNum);
                modifyBool=false;
                finish();

            }
        });

    }


    private void setDatabase(BoardMenu newMenu,int menu){
        if(modifyBool){
            DatabaseReference modifiyRef = database.getReference("community"+menu+"/"+newMenu.getBoardKey());
            modifiyRef.setValue(newMenu);
        }else{
            communityBoard.push().setValue(newMenu);
        }

    }
}
