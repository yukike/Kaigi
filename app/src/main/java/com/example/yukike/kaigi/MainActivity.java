package com.example.yukike.kaigi;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private ConstraintLayout yoyaku_layout;
    private ConstraintLayout yoyakuLogs_layout;
    private ConstraintLayout useLogs_layout;
    private NfcAdapter mAdapter;
    private User user;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    yoyaku_layout.setVisibility(View.VISIBLE);
                    yoyakuLogs_layout.setVisibility(View.INVISIBLE);
                    useLogs_layout.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    yoyaku_layout.setVisibility(View.INVISIBLE);
                    yoyakuLogs_layout.setVisibility(View.VISIBLE);
                    useLogs_layout.setVisibility(View.INVISIBLE);
                    createReserveLogs();
                    return true;
                //case R.id.navigation_notifications:
                //    yoyaku_layout.setVisibility(View.INVISIBLE);
                //    yoyakuLogs_layout.setVisibility(View.INVISIBLE);
                //    useLogs_layout.setVisibility(View.VISIBLE);
                //    createUseLogs();
                //    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("excute", "onCreate");
        user = new User();

        yoyaku_layout = (ConstraintLayout) findViewById(R.id.yoyaku_layout);
        yoyakuLogs_layout = (ConstraintLayout) findViewById(R.id.yoyakuLogs_layout);
        useLogs_layout = (ConstraintLayout) findViewById(R.id.useLogs_layout);

        yoyaku_layout.setVisibility(View.VISIBLE);
        yoyakuLogs_layout.setVisibility(View.INVISIBLE);
        useLogs_layout.setVisibility(View.INVISIBLE);

        createSpinner();

        // セレクトボックスを選択したら日付のセレクトボックスの値を更新する　月ボックス
        Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // セレクトボックスを選択したら日付のセレクトボックスの値を更新する 年ボックス
        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // ボタンのイベント
        Button dialogBtn = (Button) findViewById(R.id.button);
        // clickイベント追加
        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            // クリックしたらダイアログを表示する処理
            public void onClick(View v) {
                Spinner spinner1 = (Spinner)findViewById(R.id.spinner);
                Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
                Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
                Spinner spinner4 = (Spinner)findViewById(R.id.spinner4);
                Spinner spinner5 = (Spinner)findViewById(R.id.spinner5);
                Spinner spinner6 = (Spinner)findViewById(R.id.spinner6);
                Spinner spinner7 = (Spinner)findViewById(R.id.spinner7);
                Spinner spinner8 = (Spinner)findViewById(R.id.spinner8);

                String item1 = (String)spinner1.getSelectedItem();
                String item2 = (String)spinner2.getSelectedItem();
                String item3 = (String)spinner3.getSelectedItem();
                String item4 = (String)spinner4.getSelectedItem();
                String item5 = (String)spinner5.getSelectedItem();
                String item6 = (String)spinner6.getSelectedItem();
                String item7 = (String)spinner7.getSelectedItem();
                String item8 = (String)spinner8.getSelectedItem();

                int idx = spinner1.getSelectedItemPosition();

                // 表示する文章設定
                String dialog_str =
                        item1 + "\n" +
                        item2 + getResources().getString(R.string.year) + item3 +
                        getResources().getString(R.string.month) + item4 +
                        getResources().getString(R.string.day) + "\n" +
                        getResources().getString(R.string.start_time) +"　"+ item5 +
                        getResources().getString(R.string.hour) + item6 +
                        getResources().getString(R.string.min) + "\n" +
                        getResources().getString(R.string.end_time) + "　"+ item7  +
                        getResources().getString(R.string.hour) + item8 +
                        getResources().getString(R.string.min);

                // 日付情報作成ANDROID_ID
                //String startTimeDate = item2 + item3 + item4 + item5 + item6;
                //String endTimeDate = item2 + item3 + item4 + item7 + item8;
                String startTimeDate = item2 + "/" + item3 + "/" + item4 + " " + item5 + ":" + item6 + ":" + "00";
                String endTimeDate = item2 + "/" + item3 + "/" + item4 + " " + item7 + ":" + item8 + ":" + "00";

                // Android IDの取得
                //String uuid =
                //        Settings.Secure.getString(getContentResolver(), Settings.System.ANDROID_ID);
                // 端末に設定したuseridを使用
                String uuid = user.getUserId();

                if(!uuid.equals("0")) {
                    // jsonデータ作成
                    Log.d("startTimeDate", startTimeDate);
                    Log.d("startTimeDate_Unix", String.valueOf(getEpochTime(startTimeDate)));
                    Log.d("endTimeDate", endTimeDate);
                    Log.d("endTimeDate_Unix", String.valueOf(getEpochTime(endTimeDate)));

                    HashMap<String, Object> jsonMap = new HashMap<>();
                    jsonMap.put(getResources().getString(R.string.chaincodeId), getResources().getString(R.string.chaincode));
                    jsonMap.put(getResources().getString(R.string.funcId), getResources().getString(R.string.func_offer));
                    jsonMap.put(getResources().getString(R.string.userId), String.valueOf(uuid));
                    jsonMap.put(getResources().getString(R.string.roomNo), String.valueOf(idx + 1));
                    jsonMap.put(getResources().getString(R.string.startTime), String.valueOf(getEpochTime(startTimeDate)));
                    jsonMap.put(getResources().getString(R.string.endTime), String.valueOf(getEpochTime(endTimeDate)));

                    // ダイアログクラスをインスタンス化
                    OfferDialogFlagment dialog = new OfferDialogFlagment();
                    dialog.setDialog_str(dialog_str);
                    dialog.setUrl(getResources().getString(R.string.invoke_url));
                    dialog.setJSONMap(jsonMap);
                    // 表示  getFagmentManager()は固定、sampleは識別タグ
                    dialog.show(getSupportFragmentManager(), "yoyaku");
                } else {
                    // ユーザIDが登録されていません
                    Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.dialog_toast_error_nouserid), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // DB作成
        DBHelper dBHelper = null;
        try {
            dBHelper = new DBHelper(getApplicationContext());
            // ユーザID取得
            Cursor cursor = dBHelper.query(true,"userdata", new String[] {"userid"}, null, null, null, null, null, null);
            Boolean mov = cursor.moveToFirst();
            String _id = "0";
            user.setUserId(_id);
            if(mov) {
                Log.d("excute", "userid registed");
                // ユーザＩＤあり
                while (mov) {
                    //useridを取得する
                    _id = cursor.getString(cursor.getColumnIndex("userid"));
                    mov = cursor.moveToNext();
                }
                Log.d("excute", "usrid = " + _id);
                user.setUserId(_id);
                TextView textView = (TextView) findViewById(R.id.textView15);
                textView.setText(user.getUserId());
            } else {
                Log.d("excute", "userid no registed");
                // ユーザID取得できなかった
                // NFCタグをかざすようにダイアログを表示
                user.setNfcflag(true);
                NfcTagDialogFlagment dialog = new NfcTagDialogFlagment();
                dialog.setUser(user);
                dialog.show(getSupportFragmentManager(), "nfctag");

            }
        } catch(Exception e) {
            Log.d("excute", e.getMessage());
        }

        // NFCアダプター設定
        Log.d("excute","mAdapter set");
        mAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("excute","onResume");

        //▼NFCの機能判定
        //NFC機能なし機種
        if(mAdapter == null){
            Toast.makeText(getApplicationContext(), "no Nfc feature", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //NFC通信OFFモード
        if(!mAdapter.isEnabled()){
            Toast.makeText(getApplicationContext(), "off Nfc feature", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        //▲NFCの機能判定

        //NFCを見つけたときに反応させる
        //PendingIntent→タイミング（イベント発生）を指定してIntentを発生させる
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
        //        new Intent(this,getClass()), 0);
        Intent intent = new Intent(MainActivity.this , MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //タイミングは、タグ発見時とする。
        IntentFilter[] intentFilter = new IntentFilter[]{
                new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        };

        //反応するタグの種類を指定。
        String[][] techList = new String[][]{
                {
                        android.nfc.tech.NfcA.class.getName(),
                        android.nfc.tech.NfcB.class.getName(),
                        android.nfc.tech.IsoDep.class.getName(),
                        android.nfc.tech.MifareClassic.class.getName(),
                        android.nfc.tech.MifareUltralight.class.getName(),
                        android.nfc.tech.NdefFormatable.class.getName(),
                        android.nfc.tech.NfcV.class.getName(),
                        android.nfc.tech.NfcF.class.getName(),
                }
        };
        mAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, techList);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("excute","onPause");
        // Activityがバックグラウンドになったときは、受け取らない
        mAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.d("excute","onNewIntent");
        //インテントを受け取ったら

        Log.d("excute", "nfcflag is " + user.getNfcflag());
        if (intent != null && user.getNfcflag()) {
            Log.d("excute","Get Action is " + intent);
            resolveIntent(intent);
        }
    }

    // NFCタグ読み取り処理
    public void resolveIntent(Intent intent){
        String action = intent.getAction();
        String techStr1 = "";
        String techStr2 = "";
        if (isNFCAction(action)) {

            byte[] rowId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            String text = bytesToText(rowId);
            //タグのIDを表示
            Log.d("excute","text = " + text.trim());

            //Tag tag1 = (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            //for (String tech : tag1.getTechList()) {
            //    Log.d("excute","tech = " + tech);
            //    techStr1 = techStr1 + tech + "\n";
            //}
            //if (techStr1.equals("")) {
            //    //techStr = "no techList.";
            //    Log.d("excute", "no techList");
            //    return;
            //}
            //TechListを表示
            //Log.d("excute","techStr1 = " + techStr1);

            //UserIdをセット
            user.setUserId(text.trim());
            //NFCの読み取りを終了する
            user.setNfcflag(false);

            // デバック用
            //_id = 100;
            // ユーザID登録
            ContentValues values = new ContentValues();
            // テスト用に100をユーザIDとして入力。ほんとうはＮＦＣタグの値を入れる
            values.put("userid", user.getUserId());
            DBHelper dBHelper = new DBHelper(this.getApplicationContext());
            dBHelper.insert("userdata", null, values);

            Log.d("excute", "usrid = " + user.getUserId());
            TextView textView = (TextView) findViewById(R.id.textView15);
            textView.setText(user.getUserId());

            // ユーザ登録完了のダイアログを出す
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.nfctag_end_title))
                    .setMessage("ユーザID: " + user.getUserId())
                    .setPositiveButton("OK", null)
                    .show();

        }

    }

    //NFCのアクションかを判断する
    private boolean isNFCAction(String action){
        Log.d("excute", "action is " + action);
        if (action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)
                || action.equals(NfcAdapter.ACTION_TAG_DISCOVERED)
                || action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED) ) {
            Log.d("excute", "isNFCAction = true");
            return true;
        }
        Log.d("excute", "isNFCAction = false");
        return false;
    }

    //バイト値の変換
    private String bytesToText(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02X", b);
            buffer.append(hex);
        }
        String text = buffer.toString().trim();
        return text;
    }

    private void createSpinner() {
        // 月の値によって、日にちのスピナーの値を設定する
        Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
        Spinner spinner4 = (Spinner)findViewById(R.id.spinner4);

        //int idx = spinner3.getSelectedItemPosition();
        String item = (String)spinner3.getSelectedItem();

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item);
        String[] mArray;

        switch (item) {
            case "01":
            case "03":
            case "05":
            case "07":
            case "08":
            case "10":
            case "12":
                // 月末31日
                mArray = getResources().getStringArray(R.array.dd31List);
                for(int i=0; i<mArray.length; i++) {
                    mAdapter.add(mArray[i]);
                }

                mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner4.setAdapter(mAdapter);
                return;
            case "02":
                Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
                String yyyy_str = (String)spinner2.getSelectedItem();
                int yyyy_int = Integer.parseInt(yyyy_str);

                if( yyyy_int %4 == 0) {
                    // うるう年　月末29日
                    mArray = getResources().getStringArray(R.array.dd29List);
                } else {
                    // うるう年ではない　月末28日
                    mArray = getResources().getStringArray(R.array.dd28List);
                }
                for(int i=0; i<mArray.length; i++) {
                    mAdapter.add(mArray[i]);
                }

                mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner4.setAdapter(mAdapter);
                return;

            case "04":
            case "06":
            case "09":
            case "11":
                // 月末30日
                mArray = getResources().getStringArray(R.array.dd30List);
                for(int i=0; i<mArray.length; i++) {
                    mAdapter.add(mArray[i]);
                }

                mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner4.setAdapter(mAdapter);
                return;

        }

    }

    //予約履歴表示用
    private void createReserveLogs() {
        ListView listView = (ListView) findViewById(R.id.listView1);

        ArrayList<ReserveLogs> list = new ArrayList<>();
        ReserveLogsAdapter reserveLogsAdapter = new ReserveLogsAdapter(this);

        reserveLogsAdapter.setReserveLogsList(list);
        listView.setAdapter(reserveLogsAdapter);

        //見出し
        ReserveLogs res0 = new ReserveLogs();
        res0.setRoomNo(getResources().getString(R.string.midashi_roomName));
        res0.setYyyymmdd(getResources().getString(R.string.midashi_date));
        res0.setStartHhmm(getResources().getString(R.string.midashi_startHhmm));
        res0.setEndHhmm(getResources().getString(R.string.midashi_EndHhmm));
        res0.setStatus(getResources().getString(R.string.midashi_status));

        list.add(res0);
        reserveLogsAdapter.notifyDataSetChanged();

        //データ取得処理
        // Android IDの取得
        //String udid =
        //        Settings.Secure.getString(getContentResolver(), Settings.System.ANDROID_ID);
        // 端末に設定したuseridを使用
        String uuid = String.valueOf(user.getUserId());

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(getResources().getString(R.string.chaincodeId) , getResources().getString(R.string.chaincode));
        jsonMap.put(getResources().getString(R.string.funcId) , getResources().getString(R.string.func_queryReserveLogs));
        jsonMap.put(getResources().getString(R.string.userId) , String.valueOf(uuid));

        Uri.Builder builder = new Uri.Builder();
        AsyncHttpRequest task = new AsyncHttpRequest(this);
        task.setType(getResources().getString(R.string.functype_reserveLogs));
        task.setUrl(getResources().getString(R.string.query_url));
        task.setJSONMap(jsonMap);
        task.setList(list);
        task.execute(builder);

        //test
        //ReserveLogs res1 = new ReserveLogs();
        //res1.setRoomNo("会議室１");
        //res1.setYyyymmdd("2018/10/19");
        //res1.setStartHhmm("09:01");
        //res1.setEndHhmm("12:30");
        //list.add(res1);

        //ReserveLogs res2 = new ReserveLogs();
        //res2.setRoomNo("会議室２");
        //res2.setYyyymmdd("2018/11/19");
        //res2.setStartHhmm("09:02");
        //res2.setEndHhmm("12:35");
        //list.add(res2);

        //reserveLogsAdapter.notifyDataSetChanged();
    }

    //使用履歴表示用
    private void createUseLogs() {
        ListView listView = (ListView) findViewById(R.id.listView2);

        ArrayList<UseLogs> list = new ArrayList<>();
        UseLogsAdapter useLogsAdapter = new UseLogsAdapter(this);

        useLogsAdapter.setUseLogsList(list);
        listView.setAdapter(useLogsAdapter);

        //見出し
        UseLogs res0 = new UseLogs();
        res0.setRoomNo(getResources().getString(R.string.midashi_roomName));
        res0.setYyyymmdd(getResources().getString(R.string.midashi_date));
        res0.setStartHhmm(getResources().getString(R.string.midashi_startHhmm));
        list.add(res0);
        useLogsAdapter.notifyDataSetChanged();

        // 端末に設定したuseridを使用
        String uuid = String.valueOf(user.getUserId());

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put(getResources().getString(R.string.chaincodeId) , getResources().getString(R.string.chaincode));
        jsonMap.put(getResources().getString(R.string.funcId) , getResources().getString(R.string.func_queryUseLogs));
        jsonMap.put(getResources().getString(R.string.userId) , String.valueOf(uuid));

        Uri.Builder builder = new Uri.Builder();
        AsyncHttpRequest task = new AsyncHttpRequest(this);
        task.setType(getResources().getString(R.string.functype_useLogs));
        task.setUrl(getResources().getString(R.string.query_url));
        task.setJSONMap(jsonMap);
        task.setUseList(list);
        task.execute(builder);
    }

    // Date型→Unixタイムスタンプ
    public static long getEpochTime(String strDate){

        Date lm = new Date(strDate);
        return (lm.getTime() / 1000);

    }

}
