package br.com.naoki.pdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var arrayRgm = arrayOf("12345678", "20625308", "20613385", "20691564", "22255451")
    private var arrayPassword = arrayOf("123")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnConectar: Button = findViewById(R.id.btnConectar)
        btnConectar.setOnClickListener {
            val edtRA: EditText = findViewById(R.id.edtRA)
            val edtSenha: EditText = findViewById(R.id.edtSenha)
            val sRA = edtRA.text.toString()
            val sSenha = edtSenha.text.toString()

            // Limitador para while
            var contRgm = 0

            // Loop para verificar os dados de logins no array
            while(contRgm < arrayRgm.size) {
                // Verificar se o rgm e a senha estão no array salvo
                if(sRA == arrayRgm[contRgm] && sSenha == arrayPassword[0]){
                    Toast.makeText(applicationContext, "Login realizado com sucesso!", Toast.LENGTH_LONG).show()
                    // Criar Intent
                    val telaAreaDoAluno = Intent(this, AreaDoAluno::class.java)

                    // Passando parâmetros para a segunda tela
                    val parametros = Bundle()
                    parametros.putString("ra", "$sRA")
                    telaAreaDoAluno.putExtras(parametros)

                    // Limpar os campos
                    limpaCampos()

                    // Fazer a chamada
                    startActivity(telaAreaDoAluno)

                    // Parar a verificação
                    break
                }
                else{
                    limpaCampos()

                    // Mostrar mensagem de erro caso não digitar o RGM ou senha corretos
                    if(arrayRgm.indexOf(sRA) == -1 || sSenha != arrayPassword[0]){
                        Toast.makeText(applicationContext, "Login incorreto", Toast.LENGTH_SHORT).show()
                        break
                    }

                    // Aumentar uma contagem para loop de verificação
                    contRgm += 1
                }
            }
        }
    }

    private fun limpaCampos() {
        val edtRA: EditText = findViewById(R.id.edtRA)
        val edtSenha: EditText = findViewById(R.id.edtSenha)
        edtRA.setText("")
        edtSenha.setText("")
    }
}