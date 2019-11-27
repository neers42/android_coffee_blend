package com.example.coffee_test_ai

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME : String = "User.db"
private const val DB_VERSION : Int = 1
private const val DB_TABLE_NAME : String = "UserData_Table"

class userDB_Helper ( var mContext : Context?) : SQLiteOpenHelper(mContext, DATABASE_NAME, null, DB_VERSION) {
    //  SQLiteOpenHelper
    // 第１引数 :
    // 第２引数 : データベースの名称
    // 第３引数 : null
    // 第４引数 : データベースのバージョン
    override fun onCreate(db: SQLiteDatabase?) {
        //テーブルがなかった時に呼ばれる
        // execSQLでクエリSQL文を実行→DBの構造が決定する
        db?.execSQL(
            "CREATE TABLE " + DB_TABLE_NAME + " ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "student_id text not null, " +
                    "firstname text not null, " +
                    "password text not null " +
                    ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //バージョンが変わったときに実行される
        db?.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_NAME + ";")
        onCreate(db)
    }
}


fun query_userdata(context : Context) : List<String>{
    //読み込み用のデータベースを開く
    val database = userDB_Helper(context).readableDatabase
    //データベースから全件検索する
    val cursor = database.query(
        "UserData_Table", null, null, null, null, null, "created_at DESC"
        )
    val user_datas = mutableListOf<String>()
    cursor.use{
            //カーソルで順次処理していく
        while(cursor.moveToNext()){
                //保存されているテキストを得る
            val user_data = cursor.getString(cursor.getColumnIndex("UserData_Table"))
            user_datas.add(user_data)
        }
    }
    database.close()
    return user_datas
}

//ユーザーデータをDBに挿入する
fun insert_userdata(context : Context, student_id : String, firstname : String, password : String){
    //書き込み可能なデータベースを開く
    val database = userDB_Helper(context).writableDatabase

    database.use{db ->
        //挿入するレコード
        val record = ContentValues().apply {
            put("student_id", student_id)
            put("firstname", firstname)
            put("password", password)
        }
        //データベースに挿入する
        db.insert("UserData_Table", null, record)
    }
}

//データベースにstudent_idが存在するかを判定するための関数
fun isRecord(context: Context, student_id: String): Boolean {
    val database = userDB_Helper(context).readableDatabase
    var flag : Boolean = false
    var index : String = ""
    val cursor = database.query(
        "UserData_Table", null, null, null, null, null, null
    )
    cursor.use{
        while(cursor.moveToNext()) {
            index = cursor.getString(cursor.getColumnIndex("student_id"))
            //検索が一致した場合fragをtrueにして返す
            if (index == student_id){
                flag = true
            }
        }
    }
    return flag
}
///ログインidとpasswordが一致しているかを判定する関数
fun loginOK(context : Context, login_id : String, login_password : String) : Boolean {
    val database = userDB_Helper(context).readableDatabase
    var flag: Boolean = false
    val cursor = database.query(
        "UserData_Table", null, null, null, null, null, null, null
    )
    cursor.use {
        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndex("student_id"))
            val pass = cursor.getString((cursor.getColumnIndex("password")))
            if (id == login_id && pass == login_password) {
                flag = true
            }
        }
    }
    return flag
}

fun serach_name(context : Context, login_id : String) : String{
    val database = userDB_Helper(context).readableDatabase
    var r_firstname : String = ""
    val cursor = database.query(
        "UserData_Table",null,null,null,null,null,null)
    cursor.use{
        while(cursor.moveToNext()){
            val id = cursor.getString(cursor.getColumnIndex("student_id"))
            val firstname = cursor.getString(cursor.getColumnIndex("firstname"))
            if (id == login_id){
                r_firstname = firstname
            }
        }
    }
    return r_firstname
}