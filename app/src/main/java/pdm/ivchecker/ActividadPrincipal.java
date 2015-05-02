package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


public class ActividadPrincipal extends ActionBarActivity {

    //Necesitamos dos variables para tener referenciados a los botones.
    private Button btnJugar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Obtenemos una referencia a los controles de la interfaz.
        btnJugar=(Button)findViewById(R.id.BtnJugar);


        //Implementamos el evento click del botón btnJugar:
        btnJugar.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, juego_show_level.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );

    }


    /*
     Esta función crea el meńu de la actividad, es decir el que aparece cuando pulsamos menú
      en nuestro terminal. Su configuración la tenemos en res/menu/menu_actividad_principal.
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            //Usamos los identificadores de menu_actividad_principal.xml (@+id) para definirles una acción.

            case R.id.TrainingArea:
                intent = new Intent(ActividadPrincipal.this, TrainingAreaInicio.class);
                startActivity(intent);
                return true;

            case R.id.ShowVerbLists:
                intent = new Intent(ActividadPrincipal.this, ListaVerbos.class);
                //Iniciamos la nueva actividad
                startActivity(intent);
                return true;

            case R.id.ShowWorldChallenge:
                intent = new Intent(ActividadPrincipal.this, wcr.class);
                startActivity(intent);
                return true;

            //Para ir al la actividad About
            case R.id.About:
                intent = new Intent(ActividadPrincipal.this, Resultado.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
