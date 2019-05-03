package com.example.dashboardmodern;


import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import de.nitri.gauge.Gauge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



import com.android.volley.Request;





public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    String fecha=new SimpleDateFormat("dd/MM/yyyy").format(new Date()).replace("/","").trim();

    TextView temp;



    public ArrayList<String> temperatura=new ArrayList<String>();
    public ArrayList<String> humedad=new ArrayList<String>();
    public ArrayList<String> radiacion=new ArrayList<String>();
    Gauge gaugeTemp;
    Gauge gaugeHum;
    Gauge gaugeRadi;
    TextView texHumedad ,texTemperatura,textUv ;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imagen= findViewById(R.id.actualizar);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"ACTUALIZANDO",Toast.LENGTH_SHORT).show();

                obtenerDatosTemperatura();
                obtenerDatoHumedad();
                obtenerDatosRadiacion();

            }
        });
/*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                obtenerDatosTemperatura();
                obtenerDatoHumedad();
                obtenerDatosRadiacion();


                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //    .setAction("Action", null).show();
            }
        });
        */
        queue = Volley.newRequestQueue(this);
        gaugeTemp=findViewById(R.id.gauge1);
        gaugeHum=findViewById(R.id.gauge2);
        gaugeRadi=findViewById(R.id.gauge3);

        obtenerDatosTemperatura();
        obtenerDatoHumedad();
        obtenerDatosRadiacion();
        texHumedad = findViewById(R.id.textHum);
        texTemperatura = findViewById(R.id.texTem);
        textUv = findViewById(R.id.textUv);
    }




    public float calcProm(ArrayList<String> array){
        float acumulado=0;
        int largo= array.size();
        float []arrayPars=new float[largo];
        for (int i = 0; i <largo ; i++) {
            arrayPars[i]=Float.parseFloat(array.get(i));
        }
        for (int i = 0; i <largo ; i++) {
            acumulado+=arrayPars[i];
        }

        return (acumulado/largo);
    }





    private void  obtenerDatosTemperatura(){

        String url="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/1Uhy9X3n7D/E1yGxKAcrg/"+fecha;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray myJsonArray = response.getJSONArray("data");

                    for (int i=0; i<myJsonArray.length();i++) {

                        JSONObject MyJsonObject = myJsonArray.getJSONObject(i);

                        String name =MyJsonObject.getString("valor");

                        temperatura.add(name);
                    }

                    float promedio =calcProm(temperatura);

                    gaugeTemp.setValue(promedio);
                    gaugeTemp.setUpperText("T°: "+temperatura.get(temperatura.size()-1));
                    texTemperatura.setText("La temperatura actual: "+temperatura.get(temperatura.size()-1)+"°C"+"\n"+
                            "El promedio del dia es: "+(int)promedio+"°C") ;



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"valor: no conecto",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"valor: error de conexion",Toast.LENGTH_SHORT).show();
            }
        });


        queue.add(request);
    }
    private void  obtenerDatosRadiacion(){

        String url="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/1Uhy9X3n7D/8IvrZCP3qa/"+fecha;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray myJsonArray = response.getJSONArray("data");

                    for (int i=0; i<myJsonArray.length();i++) {

                        JSONObject MyJsonObject = myJsonArray.getJSONObject(i);
                        String name =MyJsonObject.getString("valor");
                        radiacion.add(name);
                    }
                    gaugeRadi.setValue(calcProm(radiacion));
                    gaugeRadi.setUpperText("Rad: "+radiacion.get(radiacion.size()-1));
                    textUv.setText("La radiación actual es : "+radiacion.get(radiacion.size()-1)+"nm" +"\n"+
                            "El promedio del dia es: "+ (int) calcProm(radiacion)+"nm" );


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"valor: no conecto",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"valor: error de conexion",Toast.LENGTH_SHORT).show();
            }
        });


        queue.add(request);
    }


    private void  obtenerDatoHumedad(){

        String url="http://arrau.chillan.ubiobio.cl:8075/ubbiot/web/mediciones/medicionespordia/1Uhy9X3n7D/VIbSnGKyLW/"+fecha;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray myJsonArray = response.getJSONArray("data");

                    for (int i=0; i<myJsonArray.length();i++) {

                        JSONObject MyJsonObject = myJsonArray.getJSONObject(i);
                        String name =MyJsonObject.getString("valor");
                        humedad.add(name);
                    }

                    gaugeHum.setValue(calcProm(humedad));
                    gaugeHum.setUpperText("Hum: "+humedad.get(humedad.size()-1));
                    texHumedad.setText("La hum. rel. actual es : "+humedad.get(humedad.size()-1)+"%RH"+"\n"+
                                        "El promedio del dia es: "+(int) calcProm(humedad)+ "%RH" );

                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"valor: error de conexion",Toast.LENGTH_SHORT).show();
            }
        });


        queue.add(request);
    }

}
