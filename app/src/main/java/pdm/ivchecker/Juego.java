package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Random;


public class Juego extends ActionBarActivity {

    //Matriz donde se almacenan los verbos:
    private String [][] verbos;
    //Flujo de entrada para la lectura de fichero CSV:
    private InputStream inputStream;

    //Botón de siguiente verbo
    private Button btnNext;
    private EditText txtVerbo;
    private TextView infinitivo, pasado, participio, puntos;

    private ImageView vida1, vida2, vida3, vida4;

    private int puntuacionJugada=0;

    private int puntuacionPartida=0;

    //Número de vidas
    private int nVidas=4;

    private int numPartida=0, numPartidas=5;

    private int numVerbo, numForma, numLetrasForma;
    private String misterio="";

    @Override
    //Método llamada cuando se crea por primera vez la actividad
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Obtenemos la referencia a ese botón de la vista
        btnNext=(Button)findViewById(R.id.nextButton);





        //Al campo de texto donde el jugador introduce el verbo
        txtVerbo=(EditText)findViewById(R.id.formaMisteriosa);
        txtVerbo.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        // Para cerrar el teclado al pulsar intro
        txtVerbo.setOnKeyListener(new View.OnKeyListener()
        {
            /**
             * This listens for the user to press the enter button on
             * the keyboard and then hides the virtual keyboard
             */
            public boolean onKey(View arg0, int arg1, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ( (event.getAction() == KeyEvent.ACTION_DOWN  ) &&
                        (arg1           == KeyEvent.KEYCODE_ENTER)   )
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtVerbo.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        } );




        infinitivo=(TextView)findViewById(R.id.infinitivo);
        pasado=(TextView)findViewById(R.id.pasado);
        participio=(TextView)findViewById(R.id.participio);

        //Al campo de los puntos
        puntos=(TextView)findViewById(R.id.viewPuntos);
        puntos.setText(Integer.toString(0));

        //A las imágenes de las vidas:
        vida1=(ImageView)findViewById(R.id.vida1);
        vida2=(ImageView)findViewById(R.id.vida2);
        vida3=(ImageView)findViewById(R.id.vida3);
        vida4=(ImageView)findViewById(R.id.vida4);




        //Implementamos el evento click del botón next:
        btnNext.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {

                        comprobarVerbo();
                        numPartida++;
                        //Si se han completado todos los verbos vamos a otra activity.
                        if (numPartida == numPartidas) {
                            //Creamos el intent:
                            Intent intent = new Intent(Juego.this, Resultados.class);

                            //Creamos la información a pasar entre actividades:
                            Bundle b = new Bundle();
                            b.putString("PUNTOS", String.valueOf(puntuacionJugada));

                            //Añadimos la información al intent:
                            intent.putExtras(b);

                            //Nos vamos al activity resultados:
                            startActivity(intent);
                        }
                        //Si no se han completado jugamos!
                        jugar();


                    }
                }
        );


        //Abrimos el flujo del fichero almacenado en la carpeta denro de res llamada raw con el nombre iv
        inputStream=getResources().openRawResource(R.raw.ivsoft);

        //Abrimos el flujo con un buffer.
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;



        verbos = new String [50][3];

        //Cargamos los verbos en una matriz para manejarlos mejor durante el juego.
        try {
            //Saltamos la primera linea
            line=reader.readLine();
            //String line;
            int fila=0;
            while(true){
                line=reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                verbos[fila][0] = RowData[0];
                verbos[fila][1] = RowData[1];
                verbos[fila][2] = RowData[2];
                fila++;
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Después de cargar los datos comienza el juego:
        this.jugar();


    }

    //Metodo utilizado para guardar la puntuacion en un fichero local
    private void salvar_puntuacion_local(){

        String fichero= "puntuaciones.csv";

            try {

                     //Apertura del fichero.
                             /* ######################## COMO BORRAR EL FICHERO DE PUNTUACIONES:

                             this.flujo_fichero = openFileOutput(fichero, MODE_PRIVATE);
                             flujo_fichero.close();
                            */


            //    this.flujo_fichero = openFileOutput(fichero, MODE_APPEND);
            //    String prueba = "ESTO_ES_UNA_PRUEBA\n";
            //    flujo_fichero.write(prueba.getBytes());
            //    flujo_fichero.close();

                inputStream = openFileInput(fichero);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while(true) {

                    String line;
                    System.out.println("Lectura");
                    line = reader.readLine();
                    if (line == null) break;
                    String[] RowData = line.split(",");
                    System.out.println(RowData[0]);

                }
                inputStream.close();
            }
         catch (IOException ioe){
             ioe.printStackTrace();
             System.out.println("ERROR: No ha sido posible abrir el fichero de puntuaciones");
             }
         }



    public void jugar(){
        //Sección de la lógica del juego

        //Asociamos los objetos de la lógica a los de la vista.


        //Para la generación de números:
        Random rnd = new Random();


        //Generamos el verbo a mostrar:
        numVerbo=(int)(rnd.nextDouble() * 50 + 0);

        System.out.println("Verbo elegido: "+numVerbo);

        //Generamos la forma que no aparecerá (por la que preguntaremos)
        numForma=(int)(rnd.nextDouble() * 3 + 0);

        System.out.println("Forma elegida: "+numForma);

        //Obtenermos el verbo que falta en forma de rallitas:
        numLetrasForma=verbos[numVerbo][numForma].length();
        System.out.println("forma elegida: "+verbos[numVerbo][numForma]+ "  tam: "+numLetrasForma);
        for(int i=0; i<numLetrasForma; i++){
            misterio+=" _ ";
        }

        //Escribimos en la pantalla:
        if(numForma==0)
            infinitivo.setText(misterio);
        else
            infinitivo.setText(verbos[numVerbo][0]);

        if(numForma==1)
            pasado.setText(misterio);
        else
            pasado.setText(verbos[numVerbo][1]);

        if(numForma==2)
            participio.setText(misterio);
        else
            participio.setText(verbos[numVerbo][2]);

        //Después misterio vuelve a estar vacía
        misterio="";


    }

    public void perderVida(){

        //Si le quedan 4 vidas:
        if(this.nVidas==4) {
            //Se rompe el corazón 4:
            vida4.setImageResource(R.drawable.corazonmuerto);
            //Se le resta una:
            nVidas--;
            //Queda con 3 vidas!
        }
        else if(this.nVidas==3) {
            //Se rompe el corazón 3:
            vida3.setImageResource(R.drawable.corazonmuerto);
            //Se le resta una:
            nVidas--;
        }
        //Si le quedan 2 vidas:
        else if(this.nVidas==2) {
            //Se rompe el corazón 2:
            vida2.setImageResource(R.drawable.corazonmuerto);
            //Se le resta una:
            nVidas--;
        }
        //Si le queda 1 vidas:
        else if(this.nVidas==1) {
            //Se rompe el corazón 1:
            vida1.setImageResource(R.drawable.corazonmuerto);
            //Se le resta una:
            nVidas--;
        }
        //Si no le queda ninguna vida:
        else if(this.nVidas==0) {
            //Nos vamos a la pantalla de fin de partida: YOU ARE DEAD!
            System.out.println("Eres malo malo");
        }


    }

    public void comprobarVerbo(){
        System.out.println("Texto introducido: "+txtVerbo.getText());
        System.out.println("Respuesta correcta: "+verbos[numVerbo][numForma]);

        if(txtVerbo.getText().toString().equals(verbos[numVerbo][numForma])){
            puntuacionJugada++;
        }else{
            //Pierde una vida: ohhh!!
            perderVida();
        }
        System.out.println(puntuacionJugada);

        puntos.setText(Integer.toString(puntuacionJugada));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_juego, menu);
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
