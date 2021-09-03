package com.example.otempo.Visao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.otempo.R;

//
//Classe que Preenche a tela de detalhes do tempo de uma cidade
//
public class VerDetalhesActivity extends AppCompatActivity {
    TextView nome;
    TextView max;
    TextView min;
    TextView temp;
    TextView humidade;
    TextView velocidade;
    TextView precisao;
    ImageView image;

    //
    // ao ser carregada a tela de detalhes, preenche-a com os dados da cidade
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdata);
        nome = findViewById(R.id.nomeCidade2);
        max = findViewById(R.id.tempMaxima);
        min = findViewById(R.id.tempMinima);
        temp = findViewById(R.id.tempAtual2);
        image = findViewById(R.id.imagem2);
        humidade = findViewById(R.id.textHumidade2);
        velocidade = findViewById(R.id.textVelocidade2);
        precisao = findViewById(R.id.textPrecisao2);
        Intent intent = getIntent();
        velocidade.setText(intent.getStringExtra("velocidade"));
        precisao.setText(intent.getStringExtra("precisao"));
        humidade.setText(intent.getStringExtra("humidade"));
        nome.setText(intent.getStringExtra("nome")+","+intent.getStringExtra("pais"));
        max.setText(intent.getStringExtra("max")+"°");
        min.setText(intent.getStringExtra("min")+"°");
        String ceu = intent.getStringExtra("ceu");
        String temp1 = intent.getStringExtra("temp")+"° "+ceu;
        temp.setText(temp1);
        image.setImageResource(intent.getIntExtra("image",0));
        if(ceu.equalsIgnoreCase("clear sky")){
            image.setImageResource(R.drawable.sun);
        }else if(ceu.equalsIgnoreCase("thunderstorm")){
            image.setImageResource(R.drawable.storm);
        }else if(ceu.equalsIgnoreCase("smoke")){
            image.setImageResource(R.drawable.storm);
        }else if(ceu.equalsIgnoreCase("shower rain")) {
            image.setImageResource(R.drawable.cloudyrain);
        }else if(ceu.equalsIgnoreCase("moderate rain")){
            image.setImageResource(R.drawable.cloudyrain);
        }else if(ceu.equalsIgnoreCase("light rain")){
            image.setImageResource(R.drawable.cloudyrain);
        }else if(ceu.equalsIgnoreCase("rain")){
            image.setImageResource(R.drawable.rain);
        }else
        {
            image.setImageResource(R.drawable.cloudy);
        }
    }
}
