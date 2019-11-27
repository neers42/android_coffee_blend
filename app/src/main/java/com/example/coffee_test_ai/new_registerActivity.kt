package com.example.coffee_test_ai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class new_registerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_register_avtivity)
        val register_button = findViewById<Button>(R.id.new_register_button)
        register_button.setOnClickListener {

            //未入力判定のフラグメント
            var isValid = true
            //入力欄の値を取得し変数に格納
            val student_id = findViewById<EditText>(R.id.et_sid2)
            val firstname = findViewById<EditText>(R.id.et_firstname)
            val password = findViewById<EditText>(R.id.et_pass2)
            val st_student_id = student_id.text.toString()
            val st_firstname = firstname.text.toString()
            val st_password = password.text.toString()

            if (st_student_id.isEmpty()) {
                student_id.error = getString(R.string.et_sid2_error)
                isValid = false
            }else if(st_firstname.isEmpty()) {
                firstname.error = getString(R.string.et_firstname_error)
                isValid = false
            }else if(st_password.isEmpty()){
                password.error = getString(R.string.et_pass2_error)
                isValid = false
            }
            //全部(student_is,firstname,password)が入力されている場合に以下の処理を行う
            if(isValid){

                //student_idが存在するかをisrecode_flagにboolean値として代入する
                var isrecode_flag : Boolean = isRecord(this, st_student_id)
                //すでにstudent_idが存在していた場合再度ログイン画面に戻る
                if (isrecode_flag == true){
                    //Dialogのインスタンス生成
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("すでに" + st_student_id + "は登録されています")
                        .setPositiveButton("OK", { _, _ -> })
                        .setIcon(R.mipmap.ic_launcher)
                        .show()
                }else{
                    //ユーザーデータをDBに登録する
                    insert_userdata(this, st_student_id, st_firstname, st_password)
                    AlertDialog.Builder(this)
                        .setTitle("Register Success!!")
                        .setMessage("登録が完了しました")
                        .setPositiveButton("OK", {_, _ ->
                            Log.i("new_registerActivity", "setPositiveButton(register)が押されました")
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("FIRSTNAME", st_firstname)
                            intent.putExtra("ID", st_student_id)
                            startActivity(intent)
                        })
                        .setIcon(R.mipmap.ic_launcher)
                        .show()
                }
            }
        }
    }
}


