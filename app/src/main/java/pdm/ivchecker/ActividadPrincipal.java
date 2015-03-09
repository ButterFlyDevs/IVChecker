package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ActividadPrincipal extends ActionBarActivity {

    //Necesitamos dos variables para tener referenciados a los botones.
    private Button btnJugar;
    private Button btnListarVerbos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);

        //Obtenemos una referencia a los controles de la interfaz.
        btnJugar=(Button)findViewById(R.id.BtnJugar);
        btnListarVerbos=(Button)findViewById(R.id.BtnListarVerbos);

        //Implementamos el evento click del botón btnJugar:
        btnJugar.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, Juego.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );

        //Implementamos el evento click del botón btnListarVerbos:
        btnListarVerbos.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, ListaVerbos.class);
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
        switch (item.getItemId()) {
            //Usamos los identificadores de menu_actividad_principal.xml (@+id) para definirles una acción.
            case R.id.MnuOpc1:
                Intent intent = new Intent(ActividadPrincipal.this, ListaVerbos.class);
                //Iniciamos la nueva actividad
                startActivity(intent);
                return true;
            case R.id.MnuOpc2:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
