package com.example.coffee_test_ai

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class loginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login_button = findViewById<Button>(R.id.login_button)
        val register_button = findViewById<Button>(R.id.register_button)
        login_button.setOnClickListener {
            val login_id = findViewById<EditText>(R.id.et_sid)
            val login_password = findViewById<EditText>(R.id.et_pass)
            val st_login_id = login_id.text.toString()
            val st_login_password = login_password.text.toString()

            //idとpasswordが一致していた場合、login_fragにtrueが格納される
            var login_flag = loginOK(this, st_login_id, st_login_password)
            if (login_flag) {
                val r_name: String = serach_name(this, st_login_id)
                AlertDialog.Builder(this)
                    .setTitle("Login Success!!")
                    .setMessage("ログインに成功しました!!")
                    .setPositiveButton("OK", { _, _ ->
                        intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("ID", st_login_id)
                        intent.putExtra("FIRSTNAME", r_name)
                        startActivity(intent)
                    })
                    .setIcon(R.mipmap.ic_launcher)
                    .show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Login Fail!")
                    .setMessage("IDかPasswordが違います!!")
                    .setPositiveButton("OK", { _, _ -> })
                    .setIcon(R.mipmap.ic_launcher)
                    .show()
            }
        }
        register_button.setOnClickListener{
            intent = Intent(this,new_registerActivity::class.java)
            startActivity(intent)
        }
    }
}
