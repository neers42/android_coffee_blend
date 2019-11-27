package com.example.coffee_test_ai

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class postActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        var coffee_taste: Int = 0
        var richness: Int = 0
        var sweetness: Int = 0
        var acidity: Int = 0
        var bitterness: Int = 0
        var fragrance: Int = 0
        var easy_to_drink: Int = 0
        var coffee_taste_flag = false
        var richness_flag = false
        var sweetness_flag = false
        var acidity_flag = false
        var bitterness_flag = false
        var fragrance_flag = false
        var easy_to_drink_flag = false
        val coffee_taste_group: RadioGroup = findViewById(R.id.coffee_taste)
        val name = intent.getStringExtra("NAME")
        val URL = intent.getStringExtra("URL")
        val context: Context = this

        // ラジオグループのチェック状態が変更された時にチェック状態が変更されたラジオボタンのIDが渡される
        coffee_taste_group.setOnCheckedChangeListener { _, checkedId: Int ->
            coffee_taste = when (checkedId) {
                R.id.rb1 -> 2
                R.id.rb2 -> 1
                R.id.rb3 -> 0
                else -> throw IllegalArgumentException("Not supported!") as Throwable
            }
            val coffee_taste_button = findViewById<View>(checkedId) as RadioButton
            if (coffee_taste_button.isChecked == true) {
                coffee_taste_flag = true
            }

        }

        val richness_group: RadioGroup = findViewById(R.id.richness)
        richness_group.setOnCheckedChangeListener { _, checkedId: Int ->
            richness = when (checkedId) {
                R.id.rb4 -> 1
                R.id.rb5 -> 2
                R.id.rb6 -> 3
                else -> throw IllegalArgumentException("Not supported!")
            }
            val richness_button = findViewById<View>(checkedId) as RadioButton
            if (richness_button.isChecked == true) {
                richness_flag = true
            }
        }
        val sweetness_group: RadioGroup = findViewById(R.id.sweetness)
        sweetness_group.setOnCheckedChangeListener { _, checkedId: Int ->
            sweetness = when (checkedId) {
                R.id.rb7 -> 1
                R.id.rb8 -> 2
                R.id.rb9 -> 3
                else -> throw IllegalArgumentException("Not supported!")
            }
            val sweetness_button = findViewById<View>(checkedId) as RadioButton
            if (sweetness_button.isChecked == true) {
                sweetness_flag = true
            }
        }
        val acidity_group: RadioGroup = findViewById(R.id.acidity)
        acidity_group.setOnCheckedChangeListener { _, checkedId: Int ->
            acidity = when (checkedId) {
                R.id.rb10 -> 1
                R.id.rb11 -> 2
                R.id.rb12 -> 3
                else -> throw IllegalArgumentException("Not supported!")
            }
            val acidity_button = findViewById<View>(checkedId) as RadioButton
            if (acidity_button.isChecked == true) {
                acidity_flag = true
            }
        }
        val bitterness_group: RadioGroup = findViewById(R.id.bitterness)
        bitterness_group.setOnCheckedChangeListener { _, checkedId: Int ->
            bitterness = when (checkedId) {
                R.id.rb13 -> 1
                R.id.rb14 -> 2
                R.id.rb15 -> 3
                else -> throw IllegalArgumentException("Not supported!")
            }
            val bitterness_button = findViewById<View>(checkedId) as RadioButton
            if (bitterness_button.isChecked == true) {
                bitterness_flag = true
            }
        }
        val fragrance_group: RadioGroup = findViewById(R.id.fragrance)
        fragrance_group.setOnCheckedChangeListener { _, checkedId: Int ->
            fragrance = when (checkedId) {
                R.id.rb16 -> 1
                R.id.rb17 -> 2
                R.id.rb18 -> 3
                else -> throw IllegalArgumentException("Not supported!")
            }
            val fragrance_button = findViewById<View>(checkedId) as RadioButton
            if (fragrance_button.isChecked == true) {
                fragrance_flag = true
            }
        }
        val easy_to_drink_group: RadioGroup = findViewById(R.id.easy_to_drink)
        easy_to_drink_group.setOnCheckedChangeListener { _, checkedId: Int ->
            easy_to_drink = when (checkedId) {
                R.id.rb19 -> 2
                R.id.rb20 -> 3
                else -> throw IllegalArgumentException("Not supported!")
            }
            val easy_to_drink_button = findViewById<View>(checkedId) as RadioButton
            if (easy_to_drink_button.isChecked == true) {
                easy_to_drink_flag = true
            }
        }
        val postButton = findViewById<Button>(R.id.button3)
        postButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (coffee_taste_flag == true && richness_flag == true &&
                    sweetness_flag == true && acidity_flag == true &&
                    bitterness_flag == true && fragrance_flag == true && easy_to_drink_flag == true
                ) {
                    onParallelPostButtonClick(
                        context, name, URL,
                        coffee_taste = coffee_taste,
                        richness = richness,
                        sweetness = sweetness,
                        acidity = acidity,
                        bitterness = bitterness,
                        fragrance = fragrance,
                        easy_to_drink = easy_to_drink
                    )
                } else {
                    val snackbar = Snackbar.make(view, "全て入力してください", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                    snackbar.view.setBackgroundColor(Color.RED)
                    snackbar.setActionTextColor(Color.WHITE)
                    snackbar.setAction("OK", { snackbar.dismiss() })
                    snackbar.show()
                }
            }
        })
    }

    fun onParallelPostButtonClick(context : Context, name : String, URL: String, coffee_taste: Int, richness: Int,
                                  sweetness: Int, acidity: Int, bitterness: Int,
                                  fragrance: Int, easy_to_drink: Int)
            = GlobalScope.launch(Dispatchers.Main) {
        val http = HttpUtil()
        //Mainスレッドでネットワーク関連処理を実行するとエラーになるためBackgroundで実行
        async(Dispatchers.Default) {
            http.httpPOST(URL, coffee_taste, richness, sweetness, acidity, bitterness, fragrance, easy_to_drink)
        }.await().let {
            println(it)
            if (it == 200) {
                AlertDialog.Builder(context)
                    .setTitle("Success!!")
                    .setMessage("フィードバックが完了しました!!\nメニューに戻りますか?")
                    .setPositiveButton("メニューに戻る") { _, _ ->
                        val _intent = Intent(context, MainActivity::class.java)
                        _intent.putExtra("FIRSTNAME", name)
                        startActivity(_intent)
                    }
                    .setNeutralButton("アプリを終了する"){ _, _ ->
                        val _intent = Intent(context, loginActivity::class.java)
                        startActivity(_intent)
                        moveTaskToBack(true)
                    }
                    .setIcon(R.mipmap.ic_launcher)
                    .show()
            }
        }
        }
    }
