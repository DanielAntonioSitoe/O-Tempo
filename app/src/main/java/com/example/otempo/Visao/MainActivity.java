package com.example.otempo.Visao;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otempo.Modelo.Cidade;
import com.example.otempo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

//
//Classe que Preenche a tela Principal
//
public class MainActivity extends AppCompatActivity {

    ListView listView;
    List<Cidade> cidades;
    String cidadeAtual;
    private boolean adicionar =false;

    //Instanciando a classe Custom Adapter
    CustomAdapter customAdapter = new CustomAdapter();

    //
    //    Classe que Busca o estado do tempo de uma determinada cidade, instancia a classe cidade e adiciona na lista de Cidades
    //
    class buscarTemperatura extends AsyncTask<String,Void,String> {
        //
        //    faz a conexao com o link recebido e retorna uma string com a informacao obtida
        //
        @Override
        protected String doInBackground(String... caminhos){
            StringBuilder resultado = new StringBuilder();
            try
            {
                URL url = new URL(caminhos[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new BufferedReader(new InputStreamReader(inputStream)));

                String linha = "";
                while ((linha = reader.readLine())!=null)
                {
                    resultado.append(linha).append("\n");
                }
                return resultado.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        //
        //    Pega o resultado retornado na conexao, converte-o em um ficheiro json e extrai os dados da cidade
        //
        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            JSONObject jsonObject;
            Cidade cidade = new Cidade();
            try {
                jsonObject = new JSONObject(resultado);

                JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                cidade.setTemp((int) (jsonObjectMain.getDouble("temp")-273.15));
                cidade.setMax((int) (jsonObjectMain.getDouble("temp_max") -273.15));
                cidade.setMin((int) (jsonObjectMain.getDouble("temp_min") -273.15));
                cidade.setPrecisao(jsonObjectMain.getInt("pressure"));
                cidade.setHumidade(jsonObjectMain.getInt("humidity"));

                //Dados da Temperatura
                JSONObject jsonObjectWind =jsonObject.getJSONObject("wind");
                cidade.setVelocidade(jsonObjectWind.getString("speed"));

                //Dados das Nuvens
                JSONArray jsonArrayweather =jsonObject.getJSONArray("weather");
                JSONObject jsonObjectweather =jsonArrayweather.getJSONObject(0);
                cidade.setCeu(jsonObjectweather.getString("description"));

                //Dados da zona
                JSONObject jsonObjectSys= jsonObject.getJSONObject("sys");
                cidade.setPais(jsonObjectSys.getString("country"));
                cidade.setNome(jsonObject.getString("name"));
                cidades.add(cidade);
                if(cidades.size()>10){
                    customAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "A sua Localizacao '"+cidadeAtual+"' foi Adicionada!",Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //
    //    Classe que Busca a localizacao atual 'cidade' do utilizador atraves do seu endereco ip usando a api do http://ip-api.com/json/
    //
    class Localizacao extends AsyncTask<String,Void,String> {
        //
        //    faz a conexao com o link recebido e retorna uma string com a informacao obtida
        //
        @Override
        protected String doInBackground(String... caminhos){
            StringBuilder resultado = new StringBuilder();
            try
            {
                URL url = new URL(caminhos[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new BufferedReader(new InputStreamReader(inputStream)));

                String linha = "";
                while ((linha = reader.readLine())!=null)
                {
                    resultado.append(linha).append("\n");
                }
                return resultado.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        //
        //    Pega o resultado retornado na conexao, converte-o em um ficheiro json e extrai os dados da cidade
        //
        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(resultado);
                cidadeAtual = jsonObject.getString("city");
                adicionar =true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }



    //
    //    Metodo que inicia a tela principal
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runtimer();
        cidadeAtual = "";

        //
        //    Instanciando a classe localizacao e buscadondo a localizacao atual do usuario
        //
        Localizacao local = new Localizacao();
        try {
            final String s = local.execute("http://ip-api.com/json/").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //
        //    Chamando o metodo que preenche as 10 primeiras cidades
        //
        buscarDados();


        cidades = new ArrayList<>();
        listView = findViewById(R.id.listview);
        listView.setAdapter(customAdapter);


        //    adiciona o metodo que
        //    envia os dados da cidade selecionada para a tela de detalhes
        //    ao ser clicada uma cidade
        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), VerDetalhesActivity.class);
                intent.putExtra("max", cidades.get(i).getMax()+"");
                intent.putExtra("nome", cidades.get(i).getNome());
                intent.putExtra("min", cidades.get(i).getMin()+"");
                intent.putExtra("pais", cidades.get(i).getPais()+"");
                intent.putExtra("name", cidades.get(i).getNome());
                intent.putExtra("temp", cidades.get(i).getTemp()+"");
                intent.putExtra("humidade", cidades.get(i).getHumidade()+"");
                intent.putExtra("velocidade", cidades.get(i).getVelocidade()+"");
                intent.putExtra("ceu", cidades.get(i).getCeu());
                intent.putExtra("precisao", cidades.get(i).getPrecisao()+"");
                startActivity(intent);

            }
        });
    }


    //    Classe Usada para preencher a lista de cidades na tela principal
    private class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return cidades.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        //  preenche dados de uma cidade como uma item da lista
        //
        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
           View view1 = getLayoutInflater().inflate(R.layout.row_data,null);
            TextView max = view1.findViewById(R.id.tempMax);
            TextView min = view1.findViewById(R.id.tempMin);
            TextView tempAtual = view1.findViewById(R.id.tempAtual);
            TextView nomeCidade = view1.findViewById(R.id.nomeCidade);
            TextView estado = view1.findViewById(R.id.estado);
            ImageView imagem = view1.findViewById(R.id.imagem);
            max.setText(cidades.get(i).getMax()+"°");
            min.setText(cidades.get(i).getMin()+"°");
            tempAtual.setText(cidades.get(i).getTemp()+"°C");
            nomeCidade.setText(cidades.get(i).getNome()+","+cidades.get(i).getPais());
            estado.setText(cidades.get(i).getCeu());
            if(cidades.get(i).getCeu().equalsIgnoreCase("clear sky")){
                imagem.setImageResource(R.drawable.sun);
            }else if(cidades.get(i).getCeu().equalsIgnoreCase("thunderstorm")){
                imagem.setImageResource(R.drawable.storm);
            }else if(cidades.get(i).getCeu().equalsIgnoreCase("smoke")){
                imagem.setImageResource(R.drawable.storm);
            }else if(cidades.get(i).getCeu().equalsIgnoreCase("shower rain")) {
                imagem.setImageResource(R.drawable.cloudyrain);
            }else if(cidades.get(i).getCeu().equalsIgnoreCase("moderate rain")){
                imagem.setImageResource(R.drawable.cloudyrain);
            }else if(cidades.get(i).getCeu().equalsIgnoreCase("light rain")){
                imagem.setImageResource(R.drawable.cloudyrain);
            }else if(cidades.get(i).getCeu().equalsIgnoreCase("rain")){
                imagem.setImageResource(R.drawable.rain);
            }else
                {
                imagem.setImageResource(R.drawable.cloudy);
            }
            return view1;
        }
    }

    //   Metodo que Busca estado do tempo das 10 primeiras cidades solicitadas
    public void buscarDados() {
        String[] strings = {"Lisboa","Madrid","Paris","Berlim","Copenhaga","Roma","Londres","Dublin","Praga","Viena"};
        for (int i=0;i<strings.length;i++){
            try {
                final String[] temp = {""};
                String caminho = "https://api.openweathermap.org/data/2.5/weather?q=" + strings[i] + "&appid=63a1112e7af664c4ea181356e24b0f38";
                buscarTemperatura tarefa = new buscarTemperatura();
                temp[0] = tarefa.execute(caminho).get();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    //
    //    Metodo que mostra a mensagem de caregamento da cidade atual do Utilizador
    //    Assim que a Cidade atual e caregada o metodo busca estado do tempo da cidade atual
    //
    private void runtimer(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(cidadeAtual == ""){
                    Toast.makeText(MainActivity.this, "Carregando Localizacao Atual!",Toast.LENGTH_SHORT).show();
                }else{
                    if(adicionar){
                        try {
                            final String[] temp = {""};
                            String caminho = "https://api.openweathermap.org/data/2.5/weather?q=" + cidadeAtual + "&appid=63a1112e7af664c4ea181356e24b0f38";
                            buscarTemperatura tarefa = new buscarTemperatura();
                            temp[0] = tarefa.execute(caminho).get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adicionar = false;
                    }
                }
                handler.postDelayed(this,1000);

            }
        });

    }



}
