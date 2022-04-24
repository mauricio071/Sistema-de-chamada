package br.com.naoki.pdm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class AreaDoAluno : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_do_aluno)

        // Recuperando os parâmetros passados
        val dados = intent
        val parametros = dados.extras
        val sNome = parametros!!.getString("ra")

        // Atualizando a tela
        val txtNome: TextView = findViewById(R.id.raAluno)
        txtNome.text = "RA: $sNome"

        val btnDeslogar: Button = findViewById(R.id.btnDeslogar)
        btnDeslogar.setOnClickListener {
            Toast.makeText(applicationContext, "Deslogado com sucesso!", Toast.LENGTH_LONG).show()
            finish()
        }

    }
}