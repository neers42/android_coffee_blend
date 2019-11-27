package com.example.coffee_test_ai
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.eclipsesource.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val id = intent.getStringExtra("ID")
        val URL = "http://160.16.210.86:55555/blend?user_id=" + id
        val name = intent.getStringExtra("FIRSTNAME")
        val tv_name = findViewById(R.id.textView) as TextView
        tv_name.setText(name + "さん、こんにちは！\nコーヒーはいかがですか？")

        //getButtonでボタンの設定を決定
        val getButton = findViewById(R.id.button) as Button
        getButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                onParallelGetButtonClick(URL)
            }
        })
        val feedbackbutton = findViewById(R.id.button2) as Button
        feedbackbutton.setOnClickListener {
            val intent = Intent(this, postActivity::class.java)
            intent.putExtra("NAME", name)
            intent.putExtra("id", id)
            intent.putExtra("URL", URL)
            startActivity(intent)
        }
    }

    //非同期処理でHTTP GETを実行します。
    fun onParallelGetButtonClick(URL : String) = GlobalScope.launch(Dispatchers.Main) {
        val http = HttpUtil()
        //Mainスレッドでネットワーク関連処理を実行するとエラーになるためBackgroundで実行
        async(Dispatchers.Default) { http.httpGET(URL) }.await().let {
            val result = Json.parse(it).asObject()
            val textView = findViewById(R.id.textView) as TextView
            val body_temperature : String = result.get("BodyTemperature").asString()
            val sleep_time : String = result.get("SleepTime").asString()
            val wakeup_time : String = result.get("WakeupTime").asString()
            val pressure : String = result.get("Pressure").asString()
            val thi : String = result.get("DiscomfortIndex").asString()
            val drinktime : String = result.get("DrinkTime").asString()
            val main_blend : String = result.get("MainBlend").asString()
            val sub_blend1 : String = result.get("SubBlend1").asString()
            val sub_blend2 : String = result.get("SubBlend2").asString()
            textView.setText("ユーザーの身体データ\n\n" +
                    "体温  :  " + body_temperature + "\n" +
                    "睡眠時間  :  " + sleep_time + "\n" +
                    "起床時間  :  " + wakeup_time + "\n" +
                    "気圧  :  " + pressure + "\n" +
                    "不快指数  :  " + thi + "\n" +
                    "喫飲時間帯  :  " + drinktime + "\n" +
                    "メインブレンド  : " + main_blend + "%\n" +
                    "サブブレンド1  :  " + sub_blend1 + "%\n" +
                    "サブブレンド2  :  " + sub_blend2 + "%\n")
        }
    }
}
