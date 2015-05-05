package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class TrainingAreaInicio extends ActionBarActivity {

    //Variable que controla la llamada al Activity Configuracion. La funcion startActivityForResults tiene dos parámetros: el intent y el codigo de petición,
    //con el cuál se identificarán los resultados devueltos por la Activity lanzada, en este caso, Configuracion.
    static final int CODIGO_PETICION_CONFIGURACION = 1;

    private Button boton_empezar;

    /*
        Variables para controlar el entrenamiento:
        Nivel: de 1 a 15, configurable por el usuario. El valor 0 equivale a aleatorio.
        Lista_a_preguntar: Es la lista que el usuario quiere que se le pregunte: soft, medium y hard (valores 1,2 y 3 respectivamente). El valor 0 equivale a aleatorio
        numero_verbos: El número de verbos que el usuario quiere que se le pregunte. 0 equivale a aleatoriol.
     */
    private int nivel=0, lista_a_preguntar=0,numero_verbos=0;
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


        //Implementamos el evento click del botón boton_empezar:
        boton_empezar.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(TrainingAreaInicio.this, JuegoTraining.class);
                        //Iniciamos la nueva actividad
                        intent.putExtra("lista",lista_a_preguntar);
                        intent.putExtra("nivel",nivel);
                        intent.putExtra("numero_verbos",numero_verbos);
                        startActivity(intent);
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_training_area_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch(item.getItemId()){
            //Usamos los identificadores de menu_training_area_inicio.xml (@+id) para definirles una acción.

            //Para ir a la configuracion
            case R.id.Menu_Tr_Opc1:
                intent  = new Intent(TrainingAreaInicio.this, Configuracion.class);
                //Iniciamos la nueva actividad
                startActivityForResult(intent, CODIGO_PETICION_CONFIGURACION);
                return true;

            //Para ir a las estadisticas
            case R.id.Menu_Tr_Opc2:
                intent = new Intent(TrainingAreaInicio.this, HistorialPuntuacionTraining.class);
                startActivity(intent);
                return true;

            case R.id.Menu_Tr_Opc3:
                String fichero= "puntuaciones.csv";
                FileOutputStream flujo_fichero;
                try {
                    flujo_fichero = openFileOutput(fichero, MODE_PRIVATE);
                    flujo_fichero.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            default:
                return super.onOptionsItemSelected(item);
        }
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
                System.out.println("Nivel: "+nivel);
                System.out.println("Lista verbos: "+lista_a_preguntar);
                System.out.println("Numero verbos: "+numero_verbos);
                this.nivel = datos.getIntExtra("nivel",0);
                this.lista_a_preguntar = datos.getIntExtra("lista",0);
                this.numero_verbos = datos.getIntExtra("numero_verbos",0);
                System.out.println("Nivel: "+nivel);
                System.out.println("Lista verbos: "+lista_a_preguntar);
                System.out.println("Numero verbos: "+numero_verbos);
            }
            if(codigo_resultado == RESULT_CANCELED){
                //DATOS INCORRECTOS! Las variables vuelven a sus valores por defecto (0)
                this.nivel=0;
                this.lista_a_preguntar=0;
                this.numero_verbos=0;
            }
        }
    }
}
