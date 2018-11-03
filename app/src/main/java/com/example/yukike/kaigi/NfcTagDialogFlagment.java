package com.example.yukike.kaigi;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.HashMap;

public class NfcTagDialogFlagment extends DialogFragment {
    private User user;

    // ダイアログが生成された時に呼ばれるメソッド ※必須
    public Dialog onCreateDialog(Bundle savedInstanceState){

        // ダイアログ生成  AlertDialogのBuilderクラスを指定してインスタンス化します
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // タイトル設定
        dialogBuilder.setTitle(R.string.nfctag_title);
        dialogBuilder.setMessage(R.string.nfctag_message);

        // YESボタン作成
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // NFCをリードしてuseridを取得
                AsyncNfcTagRegist task = new AsyncNfcTagRegist(getActivity());
                task.setUser(getUser());
                task.execute();
            }
        });

        // NOボタン作成
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 何もしないで閉じる
            }
        });

        // dialogBulderを返す
        return dialogBuilder.create();
    }

    public void setUser(User usr) {
        user = usr;
    }

    public User getUser() {
        return user;
    }

}