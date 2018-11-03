package com.example.yukike.kaigi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.HashMap;

public class OfferDialogFlagment extends DialogFragment {
    private String dialog_str;
    private String url_str;
    private HashMap<String, Object> jsonMap;

    // ダイアログが生成された時に呼ばれるメソッド ※必須
    public Dialog onCreateDialog(Bundle savedInstanceState){

        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // タイトル設定
        dialogBuilder.setTitle(R.string.reserve_button);
        dialogBuilder.setMessage(getDialog_str());

        // YESボタン作成
        dialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // POST送信
                //PostSend postSend = new PostSend();
                //String res = postSend.execute(getUrl(),getJSONMap());

                Uri.Builder builder = new Uri.Builder();
                AsyncHttpRequest task = new AsyncHttpRequest(getActivity());
                task.setType(getResources().getString(R.string.functype_reserve));
                task.setUrl(getUrl());
                task.setJSONMap(getJSONMap());
                task.setDialog_str(getDialog_str());
                task.execute(builder);

            }
        });

        // NOボタン作成
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 何もしないで閉じる
            }
        });

        // dialogBulderを返す
        return dialogBuilder.create();
    }

    void setDialog_str(String str) {
        dialog_str = str;
    }

    String getDialog_str() {
        return dialog_str;
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

}
