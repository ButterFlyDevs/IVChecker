package pdm.ivchecker;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Collections;
import java.util.List;

import pdm.red.ConexionServidor;
import pdm.red.Jugador;


public class wcr extends ActionBarActivity {

    private TextView columnaPosicion, columnaNombre, columnaPuntos, columnaPais;

    private ConexionServidor miConexion = new ConexionServidor();

    List<Jugador>rankingJugadores;


    PopupWindow popUp;

    //Publicidad
    private AdView adView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wcr);
        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        columnaPosicion=(TextView)findViewById(R.id.columnaPosicion);
        columnaNombre=(TextView)findViewById(R.id.columnaNombre);
        columnaPuntos=(TextView)findViewById(R.id.columnaPuntos);
        columnaPais=(TextView)findViewById(R.id.columnaPais);

        columnaPosicion.setText("");
        columnaNombre.setText("");
        columnaPuntos.setText("");
        columnaPais.setText("");




        //Poner dialog a funcionar
        progressDialog = ProgressDialog.show(wcr.this, "", "Cargando lista de jugadores...");
        //Ejecutar la hebra para obtener los datos

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Llamada clave de la clase

                rankingJugadores = miConexion.pedirRankingNueva();
                if (rankingJugadores != null) {
                    System.out.println("Recibidos " + rankingJugadores.size() + " elementos");

                    int n = 1;
                     String columnaPosicion_temporal="";
                     String columnaNombre_temporal="";
                     String columnaPuntos_temporal="";
                     String columnaPais_temporal="";
                    for (Jugador jugador : rankingJugadores) {
                        columnaPosicion_temporal += (Integer.toString(n) + "\n");
                        columnaNombre_temporal +=(jugador.getNombre() + "\n");
                        columnaPuntos_temporal += (Integer.toString(jugador.getPuntuacion()) + "\n");
                        columnaPais_temporal += (jugador.getPais() + "\n");

                        n++;

                    }
                    final String columnaNombre_final = columnaNombre_temporal;
                    final String columnaPuntos_final = columnaPuntos_temporal;
                    final String columnaPosicion_final = columnaPosicion_temporal;
                    final String columnaPais_final = columnaPais_temporal;
                    columnaNombre.post(new Runnable() {
                        @Override
                        public void run() {
                            columnaNombre.setText(columnaNombre_final);
                        }
                    });
                    columnaPosicion.post(new Runnable() {
                        @Override
                        public void run() {
                            columnaPosicion.setText(columnaPosicion_final);
                        }
                    });
                    columnaPais.post(new Runnable() {
                        @Override
                        public void run() {
                            columnaPais.setText(columnaPais_final);
                        }
                    });
                    columnaPuntos.post(new Runnable() {
                        @Override
                        public void run() {
                            columnaPuntos.setText(columnaPuntos_final);
                        }
                    });
                    progressDialog.dismiss();
                } else {
                    //Mostramos una ventana emergenente avisando de que ha ocurrido un error y que se revise la conexión a la red:
                    progressDialog.dismiss();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alertaFalloInternet();
                        }
                    });

                }

            }
        }).start();


        //Publicidad:
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //Cualquier emulador
                // .addTestDevice("xxx") //El telefono de desarrollo
                .build();
        adView.loadAd(adRequest);

    }//Fin onCreate


    private void alertaFalloInternet(){

        try{

            Context context = wcr.this;
            String title = getString(R.string.tituloAlerta);
            String message = getString(R.string.mensajeAlerta);
            String button1String = getString(R.string.botonAAlerta);
            String button2String = getString(R.string.boton2Alerta);

            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            ad.setTitle(title);
            ad.setMessage(message);
            ad.setCancelable(false);

            ad.setPositiveButton(
                    button1String,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = new Intent(wcr.this, ActividadPrincipal.class);
                            startActivity(intent);
                        }
                    }
            );

            ad.setNegativeButton(
                    button2String,
                    new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = new Intent(wcr.this, wcr.class);
                            startActivity(intent);
                        }
                    }
            );

            ad.show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos devuelve a la pantalla principal!.
        if(keyCode==KeyEvent.KEYCODE_BACK){

            Intent intent = new Intent(wcr.this, ActividadPrincipal.class);
            startActivity(intent);

            //Aplicacion de transicion animada entre activities:
            overridePendingTransition(R.anim.entrada_abajo2, R.anim.salida_abajo2);

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }


}
