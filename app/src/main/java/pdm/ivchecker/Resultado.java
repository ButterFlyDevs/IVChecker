package pdm.ivchecker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import pdm.red.ConexionServidor;
import pdm.red.Jugador;


public class Resultado extends ActionBarActivity {


    private TextView textPosicion, textPuntos;
    private ConexionServidor miConexion = new ConexionServidor();
    private Button buttonSend;
    private EditText editTextNombreJugador;
    Context context = this;

    private int puntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        // ,### REFERENCIAS ###
        textPosicion=(TextView)findViewById(R.id.textPosicion);
        textPuntos=(TextView)findViewById(R.id.textPuntos);
        buttonSend=(Button)findViewById(R.id.buttonSend);
        editTextNombreJugador=(EditText)findViewById(R.id.editTextNombreJugador);


        puntos=getIntent().getIntExtra("puntos",0);

        //Acciones:
        textPuntos.setText(Integer.toString(puntos));
        textPosicion.setText(calcularPosicion(puntos)+" th");



        //Implementación del evento pulsar boton Send!
        buttonSend.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //1.: Enviamos nuestra posición al servidor:
                        miConexion.enviaPuntuacion(editTextNombreJugador.getText().toString(), puntos, context.getResources().getConfiguration().locale.getISO3Country().toString());

                        //2.: Vamos a la pantalla del resultados WCR:
                            //Creamos el Intent
                            Intent intent = new Intent(Resultado.this, wcr.class);
                            //Iniciamos la nueva actividad
                            startActivity(intent);
                    }
                }
        );


    }


    public int calcularPosicion(int puntuacion){

        System.out.println("Puntuación recibida" + puntuacion);

        int posicion=0;
        boolean esUltimo=true;

        //Obtenemos el ranking de usuarios
        List<Jugador> rankingJugadores = miConexion.pedirRankingNueva();

        for(Jugador jugador: rankingJugadores){
            if(puntuacion>=jugador.getPuntuacion()) {
                System.out.println("Puntuación del jugador "+rankingJugadores.indexOf(jugador)+" "+jugador.getPuntuacion());
                posicion = rankingJugadores.indexOf(jugador);
                esUltimo=false;
                break;
            }
        }

        if(!esUltimo)
            posicion++; //Sólo para evitar que salga posición cero si eres el primero
        if(esUltimo) {
            posicion = rankingJugadores.size();
            posicion++;
        }

        System.out.println("Woo ");
        return posicion;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos devuelve a la pantalla principal!.
        if(keyCode==KeyEvent.KEYCODE_BACK){

            Intent intent = new Intent(Resultado.this, ActividadPrincipal.class);
            startActivity(intent);

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
