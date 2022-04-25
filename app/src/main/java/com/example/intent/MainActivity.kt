package com.example.intent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.intent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object{
        val PARAMETRO = "PARAMETRO"
        val RETORNO = "RETORNO"
        val OUTRA_ACTIVITY_REQUEST_CODE = 0
    }

    private val activityMainBinding: ActivityMainBinding by lazy { //quando usa o lazy ele so instancia quando necessario
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        supportActionBar?.title = "Tratando Intents"
        supportActionBar?.subtitle = "Principais Tipos"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.outraActivityMi -> {
                //declara intent
                val outraActivityIntent: Intent = Intent(this, OutraActivity::class.java)

                //criacao do bundle que vai para a proxima activity
                val parametrosBundle = Bundle()
                val parametro : String = activityMainBinding.parametroEt.text.toString()
                parametrosBundle.putString(PARAMETRO, parametro)

                //coloca o bundle na intent e envia pra outra activity
                outraActivityIntent.putExtras(parametrosBundle)
                //startActivity(outraActivityIntent)
                startActivityForResult(outraActivityIntent, OUTRA_ACTIVITY_REQUEST_CODE)
                true
            }
            R.id.viewMi -> {
                true
            }
            R.id.callMi -> {
                true
            }
            R.id.dialMi -> {
                true
            }
            R.id.pickMi -> {
                true
            }
            R.id.chooserMi -> {
                true
            }
            else -> return false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OUTRA_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            val retorno:String = data?.getStringExtra(RETORNO)?: ""
            activityMainBinding.retornoTv.text = retorno
        }
    }
}