package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class TrainingAreaInicio extends ActionBarActivity {

    //Variable que controla la llamada al Activity Configuracion. La funcion startActivityForResults tiene dos parámetros: el intent y el codigo de petición,
    //con el cuál se identificarán los resultados devueltos por la Activity lanzada, en este caso, Configuracion.
    static final int CODIGO_PETICION_CONFIGURACION = 1;

    private Button boton_empezar, botonAjustes, botonEstadisticas, botonEliminar;

    /*
        Variables para controlar el entrenamiento:
        smartVerb: . El valor 0 equivale a SI.
        Lista_a_preguntar: Es la lista que el usuario quiere que se le pregunte: soft, medium y hard (valores 1,2 y 3 respectivamente). El valor 0 equivale a aleatorio
        numero_verbos: El número de verbos que el usuario quiere que se le pregunte. 0 equivale a aleatoriol.
     */
    private int smartVerb=0, lista_a_preguntar=0,numero_verbos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_area_inicio);

        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Obtenemos una referencia a los controles de la interfaz.
        boton_empezar=(Button)findViewById(R.id.botonGO);
        botonAjustes=(Button)findViewById(R.id.botonAjustes);
        botonEstadisticas=(Button)findViewById(R.id.botonEstadisticas);
        botonEliminar=(Button)findViewById(R.id.botonEliminarEstadisticas);

        Animation loopParpadeante = AnimationUtils.loadAnimation(this, R.anim.animacionbotonplay);
        boton_empezar.startAnimation(loopParpadeante);


        //Implementamos el evento click del botón boton_empezar:
        boton_empezar.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(TrainingAreaInicio.this, JuegoTraining.class);
                        //Iniciamos la nueva actividad
                        intent.putExtra("lista",lista_a_preguntar);
                        intent.putExtra("smartVerb",smartVerb);
                        intent.putExtra("numero_verbos",numero_verbos);
                        startActivity(intent);


                        //Aplicacion de transicion animada entre activities:
                        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                    }
                }
        );

        botonAjustes.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent  = new Intent(TrainingAreaInicio.this, Configuracion.class);
                        //Iniciamos la nueva actividad
                        startActivityForResult(intent, CODIGO_PETICION_CONFIGURACION);
                    }
                }
        );
        botonEstadisticas.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(TrainingAreaInicio.this, HistorialPuntuacionTraining.class);
                        startActivity(intent);
                    }
                }
        );

        botonEliminar.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String fichero= "puntuaciones.csv";
                        FileOutputStream flujo_fichero;
                        try {
                            flujo_fichero = openFileOutput(fichero, MODE_PRIVATE);
                            flujo_fichero.close();
                            Toast.makeText(getApplicationContext(), getString(R.string.ficheroBorrado), Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );





    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos devuelve a la pantalla principal!
        if(keyCode==KeyEvent.KEYCODE_BACK){

            Intent intent = new Intent(TrainingAreaInicio.this, ActividadPrincipal.class);
            startActivity(intent);
            //Aplicacion de transicion animada entre activities:
            overridePendingTransition(R.anim.zoom_back_in2, R.anim.zoom_back_out2);

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }



    /*

        Funcion onActivityResult: Esta funcion es llamada automáticamente por el sistema cuando la actividad creada
        en la funcion startACtivityforResult termina. Recoge 3 valores:
        @codigo_peticion: es el codigo que devuelve la Activity acabada, junto con los datos. Por así decirlo, es el identificador de los datos devueltos
        @codigo_resultado: Puede ser RESULT_OK si los resultados son correctos, o RESULT_CANCELED si ha ocurrido algo en la segunda actividad que nos obligue a ignorar los datos devueltos
        o a tratarlos de otra forma..
        @datos: Es el intent con todos los datos devueltos por el activity.
     */
    protected void onActivityResult(int codigo_peticion, int codigo_resultado, Intent datos) {
        //Comprobar la peticion que estamos manejando (en este caso solo hay 1, pero si esta actividad tuviera más de una
        //llamada a startACtivityForResult para cosas distintas, habría más de un if, a modo de "switch" (PERO NO SE USA!)

        if(codigo_peticion == CODIGO_PETICION_CONFIGURACION){ // Tratamiento de la info de configuración devuelta
            //Comprobar que la petición fue correcta
            if(codigo_resultado == RESULT_OK){
                //DATOS CORRECTOS! Se cambian las variables de esta Actividad a las descritas por el usuario en la configuracion
                System.out.println("smartVerb: "+smartVerb);
                System.out.println("Lista verbos: "+lista_a_preguntar);
                System.out.println("Numero verbos: "+numero_verbos);
                this.smartVerb = datos.getIntExtra("smartVerb",0);
                this.lista_a_preguntar = datos.getIntExtra("lista",0);
                this.numero_verbos = datos.getIntExtra("numero_verbos",0);
                System.out.println("smartVerb: "+smartVerb);
                System.out.println("Lista verbos: "+lista_a_preguntar);
                System.out.println("Numero verbos: "+numero_verbos);
            }
            if(codigo_resultado == RESULT_CANCELED){
                //DATOS INCORRECTOS! Las variables vuelven a sus valores por defecto (0)
                this.smartVerb=0;
                this.lista_a_preguntar=0;
                this.numero_verbos=0;
            }
        }
    }
}
