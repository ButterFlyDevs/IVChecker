package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


public class ActividadPrincipal extends ActionBarActivity {

    //Necesitamos dos variables para tener referenciados a los botones.
    private Button botonJugar, botonAreaEntrenamiento, botonListaVerbos, botonRankingMundial, botonAyuda;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Obtenemos una referencia a los controles de la interfaz.
        botonJugar=(Button)findViewById(R.id.botonJugar);
        botonAreaEntrenamiento=(Button)findViewById(R.id.botonAreaEntrenamiento);
        botonListaVerbos=(Button)findViewById(R.id.botonListaVerbos);
        botonRankingMundial=(Button)findViewById(R.id.botonRankingMundial);
        botonAyuda=(Button)findViewById(R.id.botonAyuda);



        //Cargamos la animacion desde R.anim folder
        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.animacionbotonplay);
        //Lanzamos la animacion:
        botonJugar.startAnimation(animacion);


        // ### Implementación de los eventos de botones: ###

        //Implementamos el evento click del botón btnJugar:
        botonJugar.setOnClickListener(

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
        botonAreaEntrenamiento.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, TrainingAreaInicio.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );

        botonListaVerbos.setOnClickListener(

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
        botonRankingMundial.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, wcr.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );
        botonAyuda.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, About.class);
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
        // Inflamos el menú:

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
                intent = new Intent(ActividadPrincipal.this, About.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
