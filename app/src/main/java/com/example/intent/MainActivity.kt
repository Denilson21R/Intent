package com.example.intent

import android.Manifest
import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.intent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        val PARAMETRO = "PARAMETRO"
        val RETORNO = "RETORNO"
        val OUTRA_ACTIVITY_REQUEST_CODE = 0
        val RECEBER_RETORNAR_ACTION = "RECEBER_RETORNAR_ACTION"
    }

    private val activityMainBinding: ActivityMainBinding by lazy { //quando usa o lazy ele so instancia quando necessario
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var outraActivityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var requisisaoPermissaoArl: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        supportActionBar?.title = "Tratando Intents"
        supportActionBar?.subtitle = "Principais Tipos"

        /*outraActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result:ActivityResult?){
                if(result?.resultCode == RESULT_OK){
                    val retorno = String = result.data?.getStringExtra(RETORNO)
                    activityMainBinding.retornoTv.text = retorno
                }
            }
        )*/

        requisisaoPermissaoArl = registerForActivityResult(ActivityResultContracts.RequestPermission()){ permissaoConcedida ->
            if (permissaoConcedida){
                discarTelefone(Intent.ACTION_CALL)
            }else{
                //requisitarPermissaoLigacao()
                Toast.makeText(this, "Permissão Necessária", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.outraActivityMi -> {
                //declara intent
                //val outraActivityIntent: Intent = Intent(this, OutraActivity::class.java)
                val outraActivityIntent = Intent(RECEBER_RETORNAR_ACTION)

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
                //url precisa conter http ou https
                val url = activityMainBinding.parametroEt.text.toString().let {
                    if(!it.lowercase().contains("http[s]?".toRegex())) "https://" + it else it
                }
                val siteIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
                )
                startActivity(siteIntent)
                true
            }
            R.id.callMi -> {
                //android >= M, verifica se tem permissao e solicita se necessario
                //android < M, executa a discagem
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    //android >= M
                    if(checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){//verifica se tem permissao
                        //tenho a permissao
                        discarTelefone(Intent.ACTION_CALL)
                    }else{
                        //nao tenho a permissao
                        requisitarPermissaoLigacao()
                    }
                }else{
                    //android < M
                    discarTelefone(Intent.ACTION_CALL)
                }

                true
            }
            R.id.dialMi -> {
                discarTelefone(Intent.ACTION_DIAL)
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

    private fun discarTelefone(action: String){
        val numero = activityMainBinding.parametroEt.text.toString()
        val chamadaIntent = Intent(
            action,
            Uri.parse("tel:" + numero)
        )
        startActivity(chamadaIntent)
    }

    private fun requisitarPermissaoLigacao() {
        requisisaoPermissaoArl.launch(CALL_PHONE)
    }
}