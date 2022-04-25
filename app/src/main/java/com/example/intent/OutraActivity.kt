package com.example.intent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.intent.MainActivity.Companion.PARAMETRO
import com.example.intent.MainActivity.Companion.RETORNO
import com.example.intent.databinding.ActivityOutraBinding

class OutraActivity : AppCompatActivity() {
    private val activityOutraBinding: ActivityOutraBinding by lazy {
        ActivityOutraBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityOutraBinding.root)

        supportActionBar?.title = "Outra Activity"
        supportActionBar?.subtitle = "Recebe e retorna um valor"

        val value_intent : String = intent.extras?.getString(PARAMETRO)?: ""

        activityOutraBinding.recebidoTv.text = value_intent

        activityOutraBinding.retornarBt.setOnClickListener {
            val retornoIntent: Intent = Intent()
            val retorno : String = activityOutraBinding.retornoEt.text.toString()
            retornoIntent.putExtra(RETORNO, retorno)
            setResult(RESULT_OK, retornoIntent)
            finish()
        }
    }
}