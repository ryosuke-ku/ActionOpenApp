package com.example.actionopenapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BuppinDBHelper extends SQLiteOpenHelper {

    final static private int DB_VERSION = 1;
    public static final String DB_FILE_NAME = "buppin_data.db";

    public static final String DB_TABLE = "device_list_info";

    public static final String DB_COLUMN_ID = "id";
    public static final String DENPYO_NUMBER = "伝票管理番号";
    public static final String KANRI_TYPE = "管理種別";
    public static final String KANRI_NUMBER = "管理番号";
    public static final String USER_NAME = "利用者名";
    public static final String KANRI_NAME = "管理名称";
    public static final String BUILDING_NAME = "ビル名";
    public static final String LOCATION = "設置場所";
    public static final String DETAIL_LOCATION = "設置場所詳細";
    public static final String REMARKS = "備考";
    public static final String KANRI_STATUS = "資産状態";
    public static final String TYOUSA_RESULT = "調査結果";
    public static final String TYOUSA_DATE = "調査実施日";
    public static final String TYOUSA_DID_NAME = "調査実施者名";
    public static final String TYOUSA_DID_NAME_CODE = "調査実施者氏名コード";





    public BuppinDBHelper(Context context){
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL("create table " + DB_TABLE + "("
//                + DB_COLUMN_ID + " integer primary key autoincrement ,"
                        + DENPYO_NUMBER + ", "
                        + KANRI_TYPE + ", "
                        + KANRI_NUMBER + " INTEGER, " // 本来Longだが、Long型は存在しない
                        + USER_NAME + ", "
                        + KANRI_NAME + ", "
                        + BUILDING_NAME + ", "
                        + LOCATION + ", "
                        + DETAIL_LOCATION + ", "
                        + REMARKS + ", "
                        + KANRI_STATUS + ", "
                        + TYOUSA_RESULT + ", "
                        + TYOUSA_DATE + ", "
                        + TYOUSA_DID_NAME + ", "
                        + TYOUSA_DID_NAME_CODE + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //do nothing
    }
}

