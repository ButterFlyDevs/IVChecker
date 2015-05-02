package pdm.ivchecker;


import android.content.Intent;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import pdm.red.ConexionServidor;
import pdm.red.Jugador;


public class wcr extends ActionBarActivity {

    private TextView columnaPosicion, columnaNombre, columnaPuntos, columnaPais;

    private ConexionServidor miConexion = new ConexionServidor();

    List<Jugador>rankingJugadores;

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


    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wcr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //Al pulsar el botón actualizar recargamos la actividad par que vuelva a cargar la lista de jugadores:
        if (id == R.id.Actualize) {
            System.out.println("Actualizando");
            intent = new Intent(wcr.this, wcr.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
