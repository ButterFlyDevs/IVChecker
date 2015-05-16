package pdm.ivchecker;


import android.content.Intent;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import pdm.red.ConexionServidor;
import pdm.red.Jugador;


public class wcr extends ActionBarActivity {

    private TextView columnaPosicion, columnaNombre, columnaPuntos, columnaPais;

    private ConexionServidor miConexion = new ConexionServidor();

    List<Jugador>rankingJugadores;


    //Publicidad
    private AdView adView;

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


        rankingJugadores = miConexion.pedirRankingNueva();

        System.out.println("Recibidos "+rankingJugadores.size()+" elementos");

        if(rankingJugadores!=null) {

            int n = 1;

            for (Jugador jugador : rankingJugadores) {
                columnaPosicion.append(Integer.toString(n) + "\n");
                columnaNombre.append(jugador.getNombre() + "\n");
                columnaPuntos.append(Integer.toString(jugador.getPuntuacion()) + "\n");
                columnaPais.append(jugador.getPais() + "\n");
                n++;
            }
        }


        //Publicidad:
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //Cualquier emulador
                .addTestDevice("9011F9E7CEC921EF1BB8A17A36B24813") //El telefono de Juan
                .build();
        adView.loadAd(adRequest);

    }//Fin onCreate

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos devuelve a la pantalla principal!.
        if(keyCode==KeyEvent.KEYCODE_BACK){

            Intent intent = new Intent(wcr.this, ActividadPrincipal.class);
            startActivity(intent);

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }


}
