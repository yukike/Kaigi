package com.example.yukike.kaigi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class AsyncHttpRequest extends AsyncTask<Uri.Builder, Void, String> {

    private Activity mainActivity;
    private HashMap<String, Object> jsonMap;
    private String url_str;
    private HttpURLConnection urlConnection;
    private String dialog_str;
    private String type;
    private ReserveLogsAdapter adapter;
    private ListView listView;
    private ArrayList<ReserveLogs> list;
    private ArrayList<UseLogs> useList;
    private ProgressDialog m_ProgressDialog;

    public AsyncHttpRequest(Activity activity) {

        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }

    // 実行前の事前処理
    @Override
    protected void onPreExecute() {

        // プログレスダイアログの生成
        this.m_ProgressDialog = new ProgressDialog(this.mainActivity);
        // プログレスダイアログの設定
        this.m_ProgressDialog.setMessage(this.mainActivity.getResources().getString(R.string.loading));  // メッセージをセット
        // プログレスダイアログの表示
        this.m_ProgressDialog.show();

        return;
    }

    // このメソッドは必ずオーバーライドする必要があるよ
    // ここが非同期で処理される部分みたいたぶん。
    @Override
    protected String doInBackground(Uri.Builder... builder) {
        // httpリクエスト投げる処理を書く。
        // ちなみに私はHttpClientを使って書きましたー
        String resultData = "";
        try {

            //ステップ1.接続URLを決める。
            URL url = new URL(getUrl());

            //ステップ2.URLへのコネクションを取得する。
            urlConnection = (HttpURLConnection) url.openConnection();

            //ステップ3.接続設定(メソッドの決定,タイムアウト値,ヘッダー値等)を行う。
            //接続タイムアウトを設定する。
            urlConnection.setConnectTimeout(100000);
            //レスポンスデータ読み取りタイムアウトを設定する。
            urlConnection.setReadTimeout(100000);
            //ヘッダーにUser-Agentを設定する。
            urlConnection.setRequestProperty("User-Agent", "Android");
            //ヘッダーにAccept-Languageを設定する。
            urlConnection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
            //ヘッダーにContent-Typeを設定する
            urlConnection.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
            //HTTPのメソッドをPOSTに設定する。
            urlConnection.setRequestMethod("POST");
            //リクエストのボディ送信を許可する
            urlConnection.setDoOutput(true);
            //レスポンスのボディ受信を許可する
            urlConnection.setDoInput(true);

            //ステップ4.コネクションを開く
            urlConnection.connect();
            Log.d("execute", "connet end");

            //ステップ5:リクエストボディの書き出しを行う。
            OutputStream outputStream = urlConnection.getOutputStream();
            if (jsonMap.size() > 0) {
                //JSON形式の文字列に変換する。
                JSONObject responseJsonObject = new JSONObject(jsonMap);
                String jsonText = responseJsonObject.toString();
                PrintStream ps = new PrintStream(urlConnection.getOutputStream());
                ps.print(jsonText);
                Log.d("execute", "jsonText = " + jsonText);
                ps.close();
            }
            outputStream.close();

            //結果確認
            int responseCode = urlConnection.getResponseCode();
            if(responseCode != HttpsURLConnection.HTTP_OK) {
                // 予期せぬエラー
                Log.d("execute", "HttpResult Error");
            } else {
                Log.d("execute", "HttpResult OK");
            }

            resultData = convertToString(urlConnection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                //7.コネクションを閉じる。
                urlConnection.disconnect();
                Log.d("execute", "disconnect end");
            }
        }
        return resultData;
    }

    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {
        // 応答解析
        Log.d("execute", "URL:" + getUrl());
        Log.d("execute", "Type:" + getType());
        Log.d("execute", "ResponseData:" + result);

        // プログレスダイアログを閉じる
        if (this.m_ProgressDialog != null && this.m_ProgressDialog.isShowing()) {
            this.m_ProgressDialog.dismiss();
        }

        if(getType().equals(this.mainActivity.getResources().getString(R.string.functype_reserve))) {
            // 予約処理
            responseReserve(result);
        } else if(getType().equals(this.mainActivity.getResources().getString(R.string.functype_reserveLogs))) {
            // 予約履歴取得処理
            responseReserveLogs(result);
        } else if(getType().equals(this.mainActivity.getResources().getString(R.string.functype_useLogs))) {
            // 使用履歴取得処理
            responseUseLogs(result);
        } else {

        }

    }

    /*
     * キャンセル時の処理
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();

        Log.d("AsyncTaskProgressDialog", "onCancelled()");

        if (this.m_ProgressDialog != null) {

            Log.d("this.m_ProgressDialog", String.valueOf(this.m_ProgressDialog.isShowing()));

            // プログレスダイアログ表示中の場合
            if (this.m_ProgressDialog.isShowing()) {

                // プログレスダイアログを閉じる
                this.m_ProgressDialog.dismiss();
            }
        }

        return;
    }

    public String convertToString(InputStream stream) throws IOException {
        StringBuffer sb = new StringBuffer();
        String line = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        try {
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    // 予約処理の応答処理
    void responseReserve(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String returnCode = json.getString("returnCode");
            Log.d("execute", "returnCode = " + returnCode);

            if (returnCode.equals("0")) {
                // 予約成功
                // トーストを出す
                //Toast toast = Toast.makeText(this.mainActivity, this.mainActivity.getResources().
                //        getString(R.string.dialog_toast_success), Toast.LENGTH_SHORT);
                //toast.show();

                // ダイアログを出す
                new AlertDialog.Builder(this.mainActivity)
                        .setTitle(this.mainActivity.getResources().getString(R.string.dialog_toast_success))
                        .setMessage(getDialog_str())
                        .setPositiveButton("OK", null)
                        .show();

            } else {
                // 予約失敗
                // トーストを出す
                if(returnCode.equals("-1")) {
                    // 開始時間が終了時間よりもあとに設定されている
                    Toast toast = Toast.makeText(this.mainActivity, this.mainActivity.getResources().
                            getString(R.string.dialog_toast_err_zengo), Toast.LENGTH_SHORT);
                    toast.show();
                } else if(returnCode.equals("-2")) {
                    // 過去の日付
                    Toast toast = Toast.makeText(this.mainActivity, this.mainActivity.getResources().
                            getString(R.string.dialog_toast_err_kako), Toast.LENGTH_SHORT);
                    toast.show();
                } else if(returnCode.equals("-3")) {
                    // 予約時間2時間オーバ
                    Toast toast = Toast.makeText(this.mainActivity, this.mainActivity.getResources().
                            getString(R.string.dialog_toast_err_over), Toast.LENGTH_SHORT);
                    toast.show();
                } else if(returnCode.equals("-4")) {
                    // 重複予約
                    Toast toast = Toast.makeText(this.mainActivity, this.mainActivity.getResources().
                            getString(R.string.dialog_toast_err_jufuku), Toast.LENGTH_SHORT);
                    toast.show();
                } else if(returnCode.equals("-909")) {
                    Toast toast = Toast.makeText(this.mainActivity, this.mainActivity.getResources().
                            getString(R.string.dialog_toast_err_noregist), Toast.LENGTH_SHORT);
                    toast.show();
                } else  {
                    // 予期せぬエラー
                    Toast toast = Toast.makeText(this.mainActivity, this.mainActivity.getResources().
                            getString(R.string.dialog_toast_err_yoki), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

        } catch (JSONException e) {
            // 予期せぬエラー
            Toast toast = Toast.makeText(this.mainActivity, this.mainActivity.getResources().
                    getString(R.string.dialog_toast_err_nores), Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        } finally {

        }
    }

    // 予約履歴取得処理の応答処理
    void responseReserveLogs(String result) {
        Log.d("excute","ReserveLogs Response " + result);
        try {

            ListView listView = (ListView) this.mainActivity.findViewById(R.id.listView1);

            //ArrayList<ReserveLogs> list = new ArrayList<>();
            ReserveLogsAdapter reserveLogsAdapter = new ReserveLogsAdapter(this.mainActivity);

            reserveLogsAdapter.setReserveLogsList(list);
            listView.setAdapter(reserveLogsAdapter);
            //JSONObject json = new JSONObject(result);
            //JSONArray datas = json.getJSONArray("");
            JSONArray datas = new JSONArray(result);

            ArrayList<ReserveLogs> list = getList();

            for(int i=0; i < datas.length(); i++) {
                // JSONパース
                JSONObject data = datas.getJSONObject(i);
                String roomNo = data.getString("room");
                String startTime = data.getString("start");
                String endTime = data.getString("end");
                String status = data.getString("status");
                // データコンバート
                String chgRoomNo = convertRoomNo(roomNo);
                String chgDate = convertDate(startTime);
                String chgStartTime = convertTime(startTime);
                String chgEndTime = convertTime(endTime);
                String chgStatus = convertStatus(status);
                Log.d("excute", "roomNo = " + chgRoomNo);
                Log.d("excute", "chgDate = " + chgDate);
                Log.d("excute", "chgStartTime = " + chgStartTime);
                Log.d("excute", "chgEndTime = " + chgEndTime);
                Log.d("excute", "status = " + chgStatus);
                // listViewにセット
                ReserveLogs res = new ReserveLogs();
                res.setRoomNo(chgRoomNo);
                res.setYyyymmdd(chgDate);
                res.setStartHhmm(chgStartTime);
                res.setEndHhmm(chgEndTime);
                res.setStatus(chgStatus);
                list.add(res);
            }
            reserveLogsAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            // 予期せぬエラー
            Toast toast = Toast.makeText(this.mainActivity, this.mainActivity.getResources().
                    getString(R.string.dialog_toast_err_nores), Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        } finally {

        }
    }

    // 使用履歴取得処理の応答処理
    void responseUseLogs(String result) {
        try {
            ListView listView = (ListView) this.mainActivity.findViewById(R.id.listView2);

            //ArrayList<ReserveLogs> list = new ArrayList<>();
            UseLogsAdapter useLogsAdapter = new UseLogsAdapter(this.mainActivity);

            useLogsAdapter.setUseLogsList(useList);
            listView.setAdapter(useLogsAdapter);
            //JSONObject json = new JSONObject(result);
            //JSONArray datas = json.getJSONArray("");
            JSONArray datas = new JSONArray(result);

            ArrayList<UseLogs> list = getUseList();

            for (int i = 0; i < datas.length(); i++) {
                // JSONパース
                JSONObject data = datas.getJSONObject(i);
                String roomNo = data.getString("room");
                String startTime = data.getString("start");
                String endTime = data.getString("end");
                // データコンバート
                String chgRoomNo = convertRoomNo(roomNo);
                String chgDate = convertDate(startTime);
                String chgStartTime = convertTime(startTime);
                Log.d("excute", "roomNo = " + chgRoomNo);
                Log.d("excute", "chgDate = " + chgDate);
                Log.d("excute", "chgStartTime = " + chgStartTime);
                // listViewにセット
                UseLogs res = new UseLogs();
                res.setRoomNo(chgRoomNo);
                res.setYyyymmdd(chgDate);
                res.setStartHhmm(chgStartTime);
                list.add(res);
            }
            useLogsAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            // 予期せぬエラー
            Toast toast = Toast.makeText(this.mainActivity, this.mainActivity.getResources().
                    getString(R.string.dialog_toast_err_nores), Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        } finally {

        }
    }

    // 会議室名変換
    String convertRoomNo(String str) {
        switch (str) {
            case "1":
                return this.mainActivity.getResources().getString(R.string.roomNo1);
            case "2":
                return this.mainActivity.getResources().getString(R.string.roomNo2);
            case "3":
                return this.mainActivity.getResources().getString(R.string.roomNo3);
             default:
                return str;
        }

    }

    // Unixタイムスタンプから日付変換
    String convertDate(String str) {
        Long unixtime = Long.parseLong(str) * 1000;
        Date date = new Date(unixtime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(date);
    }

    // Unixタイムスタンプから時間変換
    String convertTime(String str) {
        Long unixtime = Long.parseLong(str) * 1000;
        Date date = new Date(unixtime);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    // ステータスを文字に変換
    String convertStatus(String str) {
        switch (str) {
            case "1":
                return this.mainActivity.getResources().getString(R.string.status_yet);
            case "2":
                return this.mainActivity.getResources().getString(R.string.status_unlock);
            case "3":
                return this.mainActivity.getResources().getString(R.string.status_end);
            default:
                return str;
        }

    }

    void setJSONMap(HashMap<String, Object> json) {
        jsonMap = json;
    }

    HashMap<String, Object> getJSONMap() {
        return jsonMap;
    }

    void setUrl(String str) {
        url_str = str;
    }

    String getUrl() {
        return url_str;
    }

    void setDialog_str(String str) {
        dialog_str = str;
    }

    String getDialog_str() {
        return dialog_str;
    }

    void setType(String str) {
        type = str;
    }

    String getType() {
        return type;
    }

    void setList(ArrayList<ReserveLogs> str) {
        list = str;
    }

    ArrayList<ReserveLogs> getList() {
        return list;
    }

    public ArrayList<UseLogs> getUseList() {
        return useList;
    }

    public void setUseList(ArrayList<UseLogs> useList) {
        this.useList = useList;
    }
}
