package pdm.ivchecker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import pdm.red.ConexionServidor;


public class wcr extends ActionBarActivity {

    private TextView rankingUsuarios;
    private ConexionServidor miConexion = new ConexionServidor();
    private  List<String> ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wcr);
        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Enlace conexión con elemento de la vista:
        rankingUsuarios=(TextView)findViewById(R.id.rankingUsuarios);
        rankingUsuarios.append("\n\n\n\n\n");

        System.out.println("Antes");



        //Introducción de datos de prueba:
        // ######### BORRAME ##############
        miConexion.enviaPuntuacion("superprueba",20000000);
        // ######### BORRAME ##############


        //Petición a la función pedirRanking de la clase ConexionServidor.java
        ranking=miConexion.pedirRanking();

        System.out.println("Despues");

        System.out.println("Recibidos "+ranking.size()+" elementos");

        if(ranking!=null) {

            int n = 1;

            for (String dato : ranking) {
                rankingUsuarios.append("\n\t\t");
                if (!dato.equals(""))
                    rankingUsuarios.append(n + " " + dato);
                n++;
            }
        }else{
            String mensaje="Lo sentimos. No se ha podido establacer conexión con el servidor. Inténtelo de nuevo más tarde.";
            rankingUsuarios.append(mensaje);
        }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
