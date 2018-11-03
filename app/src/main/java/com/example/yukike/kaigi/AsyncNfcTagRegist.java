package com.example.yukike.kaigi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncNfcTagRegist extends AsyncTask<Void, Void, String> {
    private Activity mainActivity;
    private ProgressDialog m_ProgressDialog;
    private User user;

    public AsyncNfcTagRegist(Activity activity) {
        // 呼び出し元のアクティビティ
        this.mainActivity = activity;
    }

    // 実行前の事前処理
    @Override
    protected void onPreExecute() {

        // プログレスダイアログの生成
        this.m_ProgressDialog = new ProgressDialog(this.mainActivity);
        // プログレスダイアログの設定
        this.m_ProgressDialog.setMessage(this.mainActivity.getResources().getString(R.string.nfctag_please));  // メッセージをセット
        // プログレスダイアログの表示
        this.m_ProgressDialog.show();

        return;
    }

    @Override
    protected String doInBackground(Void... voids) {
        // フラグがfalseになるまで、プログレスダイアログを出す
        try {
            // フラグをtrue
            User user = getUser();
            while (user.getNfcflag()) {
                Thread.sleep(1000);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        Log.d("excute","doInBackground is end");
        return null;
    }

    // このメソッドは非同期処理の終わった後に呼び出されます
    @Override
    protected void onPostExecute(String result) {
        // 応答解析
        Log.d("execute", "ResponseData:" + result);

        // プログレスダイアログを閉じる
        if (this.m_ProgressDialog != null && this.m_ProgressDialog.isShowing()) {
            this.m_ProgressDialog.dismiss();
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

    public void setUser(User usr) {
        user = usr;
    }

    public User getUser() {
        return user;
    }

}
