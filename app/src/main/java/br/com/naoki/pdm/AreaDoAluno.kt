package br.com.naoki.pdm

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

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

        val date = Calendar.getInstance().time
        var horario = SimpleDateFormat("HH:mm", Locale.getDefault()) //Horario atual
        var data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) //Dia atual
        var diaDaSemana = SimpleDateFormat("EEE", Locale.getDefault()) //Dia da semana atual
        val diaLogin: TextView = findViewById(R.id.diaLogin)
        diaLogin.text = "Login feito no dia " + data.format(date) + " às " + horario.format(date)

        var arrayMaterias = arrayOf(
            "TRABALHO DE GRADUAÇÃO INTERDISCIPLINAR I",
            "MATÉRIA TESTE TERÇA",
            "PROGRAMAÇÃO PARA DISPOSITIVOS MÓVEIS",
            "FUNDAMENTOS DE INTELIGÊNCIA ARTIFICIAL",
            "LINGUAGENS FORMAIS E AUTÔMATOS")

        val materiaDoDia: TextView = findViewById(R.id.materiaDoDia)
        val situacaoAula: TextView = findViewById(R.id.situacaoAula)
        val presenca: TextView = findViewById(R.id.presenca)
        val btnPresenca: Button = findViewById(R.id.btnPresenca)

        var horarioAulaInicio = SimpleDateFormat("19:10", Locale.getDefault()) //Horário da aula 19:10 as 21:50
        var horarioAulaFim = SimpleDateFormat("21:50", Locale.getDefault()) //Horário da aula 19:10 as 21:50
        var latitudeSala = -23.5362 //Latitude da sala
        var longitudeSala = -46.5603 //Longitude da sala

        var registro = false
        var numMateria = 0

        if(diaDaSemana.format(date) == "Mon"){
            materiaDoDia.text = arrayMaterias[0]
            numMateria = 0
        }
        if(diaDaSemana.format(date) == "Tue"){
            materiaDoDia.text = arrayMaterias[1]
            numMateria = 1
        }
        if(diaDaSemana.format(date) == "Wed"){
            materiaDoDia.text = arrayMaterias[2]
            numMateria = 2
        }
        if(diaDaSemana.format(date) == "Thu"){
            materiaDoDia.text = arrayMaterias[3]
            numMateria = 3
        }
        if(diaDaSemana.format(date) == "Fri"){
            materiaDoDia.text = arrayMaterias[4]
            numMateria = 4
        }
        if(diaDaSemana.format(date) == "Sat"){
            materiaDoDia.text = "Hoje é sábado, dia de descansar!"
        }
        if(diaDaSemana.format(date) == "Sun"){
            materiaDoDia.text = "Hoje é domingo, dia de descansar!"
        }

        var latitude: Double
        var longitude: Double

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }

        try {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val locationListener: LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    //latitude = -23.5362
                    //longitude = -46.5603
                    situacaoAula.text = "lat: " + formatarGeopoint(latitude) + " long: " + formatarGeopoint(longitude)
                    if(((formatarGeopoint(latitude)) == formatarGeopoint(latitudeSala)) && (formatarGeopoint(longitude) == formatarGeopoint(longitudeSala))){
                        if((horario.format(date) >= horarioAulaInicio.format(date)) && (horario.format(date) <= horarioAulaFim.format(date)) && (registro == false)) {
                            situacaoAula.text = "Sua aula começou, boa aula! Você tem até as 21:50 para registrar sua presença!"
                            presenca.text = "Presença: DISPONÍVEL"
                            btnPresenca.setOnClickListener {
                                situacaoAula.text = "Localização: " + formatarGeopoint(latitude) + ", " + formatarGeopoint(longitude)
                                presenca.text = "Presença: REGISTRADA - Às: " + horario.format(date)
                                registro = true
                                Toast.makeText(
                                    applicationContext,
                                    "Presença registrada: " + arrayMaterias[numMateria] + " - Dia: " + data.format(date) + " às " + horario.format(date) +
                                            " - Localização: " + formatarGeopoint(latitude) + ", " + formatarGeopoint(longitude),
                                    Toast.LENGTH_LONG
                                ).show()
                                btnPresenca.setVisibility(View.INVISIBLE)
                            }
                        } else{
                            btnPresenca.setVisibility(View.INVISIBLE)
                        }
                    } else{
                        if((horario.format(date) >= horarioAulaInicio.format(date)) && (horario.format(date) <= horarioAulaFim.format(date)) && (registro == false)) {
                            situacaoAula.text = "Sua aula começou, boa aula! Você tem até as 21:50 para registrar sua presença!"
                            presenca.text = "Presença: Disponível"
                            btnPresenca.setOnClickListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Presença não registrado, motivo: Sua localização diz que você não está na sala | " + formatarGeopoint(latitude) + ", " + formatarGeopoint(longitude),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
        } catch (ex: SecurityException) {
            Toast.makeText(this, "Erro:" + ex.message, Toast.LENGTH_LONG).show()
        }

        val btnCronograma: Button = findViewById(R.id.btnCronograma)
        btnCronograma.setOnClickListener {
            val telaCronogramaDeAulas = Intent(this, CronogramaDeAulas::class.java)
            startActivity(telaCronogramaDeAulas)
            Toast.makeText(applicationContext, "Indo para o cronograma de aulas!", Toast.LENGTH_LONG).show()
        }

        val btnDeslogar: Button = findViewById(R.id.btnDeslogar)
        btnDeslogar.setOnClickListener {
            Toast.makeText(applicationContext, "Deslogado com sucesso!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun formatarGeopoint(valor: Double): String? {
        val decimalFormat = DecimalFormat("#.####")
        return decimalFormat.format(valor)
    }
}