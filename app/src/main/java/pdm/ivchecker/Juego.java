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

    //Matrices donde se almacenan los verbos:
    private String [][] verbosSoft;
    private String [][] verbosMedium;
    private String [][] verbosHard;

    //Elementos de la vista:
    private Button btnNext;
    private EditText txtVerbo;
    private TextView infinitivo, pasado, participio, puntos;
    private ImageView vida1, vida2, vida3, vida4;

    private Random rnd;

    //Número de verbos de las listas
    private int numVerbosListaSoft, numVerbosListaMedium, numVerbosListaHard;






    // ## VARIABLES GLOBALES DE PARTIDA que van modificandose conforme avanza esta y que todas las funciones necesitan. ## //

    //Respecto a los datos del jugador:
    private int nVidas; //Número de vidas del jugador.
    private int puntuacionPartida; //Puntuación del jugador.

    //Respecto a la lógica del juego:
    private int numVerbo, numFormaA, numFormaB, numLetrasForma; //Variables para crear la jugada
    private String formaMisteriosa; //Forma que el usuario debe introducir.
    private int nivel; //Nivel por el que el usuario va.
    private int jugadaEnNivel; //Jugada (de 10) por la que va dentro del nivel.


    // Fin variables globales de partida.

    //Constructor:
    public Juego(){
        numVerbosListaSoft=numVerbosListaMedium=numVerbosListaHard=0;
        formaMisteriosa="";
        puntuacionPartida=0;
        nVidas=4;
        nivel=1;
        jugadaEnNivel=1;
        //Inicializamos  el objeto de tipo Random().
        rnd = new Random();
    }



    //Función para cargar en las matrices los verbos desde los .csv
    private void cargarVerbos(){


        //Abrimos el flujo necesario para leer desde los tres ficheros que estań en la carpeta raw..
        InputStream inputStream=getResources().openRawResource(R.raw.ivsoft);


        //Abrimos el flujo con un buffer.
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        //Necesario para la lectura:
        String line;


        // ## SOFT ## //
        try {
            /*
            Extraemos la primera linea del fichero que es la que indica el número de verbos que este
            contiene y que nose será util para crear la matriz del tamaño exacto. Hacemos esto pensando
            en el momento que se tenga que modifcar la lista sin tener que modificar ningún valor en
            el programa. Es uso de try-catch es obligatorio.
             */
            line=reader.readLine();
            System.out.println("El fichero ivsoft contiene " + line + " verbos.");
            numVerbosListaSoft=Integer.parseInt(line);
        } catch (IOException e) {
            //Para que no falle el programa si no se cargase el número lo ponemos nosotros.
            numVerbosListaSoft=50;
            e.printStackTrace();
        }
        this.verbosSoft = new String [numVerbosListaSoft][3];

        //Cargamos los verbos en una matriz para manejarlos mejor durante el juego.
        try {
            int fila=0;
            while(true){
                line=reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                verbosSoft[fila][0] = RowData[0];
                verbosSoft[fila][1] = RowData[1];
                verbosSoft[fila][2] = RowData[2];
                fila++;
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // ## MEDIUM ## //

        //CAmbiamos el flujo a la lista medium
        inputStream=getResources().openRawResource(R.raw.ivmedium);

        //Abrimos el nuevo flujo
        reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            /*
            Extraemos la primera linea del fichero que es la que indica el número de verbos que este
            contiene y que nose será util para crear la matriz del tamaño exacto. Hacemos esto pensando
            en el momento que se tenga que modifcar la lista sin tener que modificar ningún valor en
            el programa. Es uso de try-catch es obligatorio.
             */
            line=reader.readLine();
            System.out.println("El fichero ivmedium contiene " + line + " verbos.");
            numVerbosListaMedium=Integer.parseInt(line);
        } catch (IOException e) {
            //Para que no falle el programa si no se cargase el número lo ponemos nosotros.
            numVerbosListaMedium=88;
            e.printStackTrace();
        }
        this.verbosMedium = new String [numVerbosListaMedium][3];

        //Cargamos los verbos en una matriz para manejarlos mejor durante el juego.
        try {
            int fila=0;
            while(true){
                line=reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                verbosMedium[fila][0] = RowData[0];
                verbosMedium[fila][1] = RowData[1];
                verbosMedium[fila][2] = RowData[2];
                fila++;
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // ## HARD ## //

        //Cambiamos el flujo a la lista hard
        inputStream=getResources().openRawResource(R.raw.ivhard);

        //Abrimos el nuevo flujo
        reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            /*
            Extraemos la primera linea del fichero que es la que indica el número de verbos que este
            contiene y que nose será util para crear la matriz del tamaño exacto. Hacemos esto pensando
            en el momento que se tenga que modifcar la lista sin tener que modificar ningún valor en
            el programa. Es uso de try-catch es obligatorio.
             */
            line=reader.readLine();
            System.out.println("El fichero ivmedium contiene " + line + " verbos.");
            numVerbosListaHard=Integer.parseInt(line);
        } catch (IOException e) {
            //Para que no falle el programa si no se cargase el número lo ponemos nosotros.
            numVerbosListaHard=182;
            e.printStackTrace();
        }
        this.verbosHard = new String [numVerbosListaHard][3];

        //Cargamos los verbos en una matriz para manejarlos mejor durante el juego.
        try {
            int fila=0;
            while(true){
                line=reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                verbosHard[fila][0] = RowData[0];
                verbosHard[fila][1] = RowData[1];
                verbosHard[fila][2] = RowData[2];
                fila++;
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }



    @Override
    //Método llamada cuando se crea por primera vez la actividad
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        //Para que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Para que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        /* ## Referencias a los elementos del layout (vista) ## */

        //Obtenemos la referencia al botón de la vista "Next"
        btnNext=(Button)findViewById(R.id.nextButton);

        //Al campo de texto donde el jugador introduce el verbo
        txtVerbo=(EditText)findViewById(R.id.formaMisteriosa);
        txtVerbo.setInputType(InputType.TYPE_TEXT_VARIATION_URI);



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

        /* Fin de las referencias*/


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


        //Implementamos el evento click del botón next:
        btnNext.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {


                        //Comprobamos el verbo:
                        comprobarVerbo();

                        //Limpiamos el contenido del editText:
                        txtVerbo.setText("");

                        //Aumentamos el número de la jugada en el nivel (10 verbos por nivel)
                        jugadaEnNivel++;

                        /*
                        10 jugadas por nivel!
                         */
                        if(jugadaEnNivel>10) { //Si hemos completado las diez jugadas por nivel pasamos de nivel.
                            nivel++; //Pasamos de nivel
                            jugadaEnNivel=1; //Reiniciamos.
                        }


                        /*
                        Hay que cambiar el flujo de ejecución...
                        La implementación del botón no debería ser la que controlase el fin de la partida
                        sino la lógica del juego.
                        EL botón solo debe de llamar a jugar y que sea en jugar donde se desarrolle
                        la lógica del juego.
                         */


                        //Si se han completado todos los niveles (se ha acabado el juego) vamos a una pantalla de resultados:.
                        if (nivel > 15) {
                            //Creamos el intent:
                            Intent intent = new Intent(Juego.this, Resultados.class);

                            //Creamos la información a pasar entre actividades:
                            Bundle b = new Bundle();
                            b.putString("PUNTOS", String.valueOf(puntuacionPartida));

                            //Añadimos la información al intent:
                            intent.putExtras(b);

                            //Nos vamos al activity resultados:
                            startActivity(intent);
                        }
                        //Si no se han completado seguimos jugando!
                        crearJugada();


                    }
                }
        ); //Fin del manejador del botón next.

        this.cargarVerbos();


        //Después de cargar los datos comienza el juego:
        this.crearJugada();


    }






/*
    //Metodo utilizado para guardar la puntuacion en un fichero local
    private void salvar_puntuacion_local(){

        String fichero= "puntuaciones.csv";

            try {

                     //Apertura del fichero.
                              ######################## COMO BORRAR EL FICHERO DE PUNTUACIONES:

                             this.flujo_fichero = openFileOutput(fichero, MODE_PRIVATE);
                             flujo_fichero.close();



            //    this.flujo_fichero = openFileOutput(fichero, MODE_APPEND);
            //    String prueba = "ESTO_ES_UNA_PRUEBA\n";
            //    flujo_fichero.write(prueba.getBytes());
            //    flujo_fichero.close();

             //   inputStream = openFileInput(fichero);
              //  BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while(true) {

                    String line;
                    System.out.println("Lectura");
               //     line = reader.readLine();
         //           if (line == null) break;
                 //   String[] RowData = line.split(",");
                  //  System.out.println(RowData[0]);

                }
              //  inputStream.close();
            }
         catch (IOException ioe){
             ioe.printStackTrace();
             System.out.println("ERROR: No ha sido posible abrir el fichero de puntuaciones");
             }
         }
*/

    // ## LÓGICA DEL JUEGO ## //
    public void crearJugada(){


        String verboInfinitivo="";
        String verboPasado="";
        String verboParticipio="";


        System.out.println("### Creando jugada "+this.jugadaEnNivel+" en nivel "+nivel+" ###");

        /* 1º
           GENERAMOS EL VERBO A PREGUNTAR según lo que mida la lista de verbos que dependerá del nivel en el que estemos.:
           el número "numVerbosLista" se coge desde el propio fichero que se carga.
           **Mejora: que no pueda volver a preguntar por el mismo verbo.
        */

        //Ademas lo cargarmos:


        if(nivel>=1 && nivel<=3) { //Si estamos en los niveles 1-5

            //Elegimos el verbo de forma aleatoria:
            numVerbo = (int) (rnd.nextDouble() * numVerbosListaSoft + 0);
            //Cargamos el verbo en las variables locales:
            verboInfinitivo=verbosSoft[numVerbo][0];
            verboPasado=verbosSoft[numVerbo][1];
            verboParticipio=verbosSoft[numVerbo][2];

        }else
            if(nivel>=6 && nivel<=10) { //Si estamos en los niveles 6-10

                //Elegimos el verbo de forma aleatoria:
                numVerbo = (int) (rnd.nextDouble() * numVerbosListaMedium + 0);
                //Cargamos el verbo en las variables locales:
                verboInfinitivo=verbosMedium[numVerbo][0];
                verboPasado=verbosMedium[numVerbo][1];
                verboParticipio=verbosMedium[numVerbo][2];


        } if(nivel>=11 && nivel<=15) { //Si estamos en los niveles 6-10

            //Elegimos el verbo de forma aleatoria:
            numVerbo = (int) (rnd.nextDouble() * numVerbosListaHard + 0);
            //Cargamos el verbo en las variables locales:
            verboInfinitivo=verbosHard[numVerbo][0];
            verboPasado=verbosHard[numVerbo][1];
            verboParticipio=verbosHard[numVerbo][2];

        }


        System.out.println("Verbo elegido nº: "+numVerbo+"\nInfinitivo: "+verboInfinitivo+" Pasado: "+verboPasado+" Participio: "+verboParticipio);






        //2º GENERAMOS LA FORMA O FORMAS dependiendo del nivel que no aparecerán (por la que preguntaremos)

        if( (nivel>=1 && nivel<=3) || (nivel>=6 && nivel<=8) || (nivel>=11 && nivel<=13) )
         //Elegimos una forma a preguntar.
         numFormaA=(int)(rnd.nextDouble() * 3 + 0);
        else if( (nivel>=4 && nivel<=5) || (nivel>=9 && nivel<=10) || (nivel>=14 && nivel<=15)    ) {
            //Elegimos dos formas a preguntar que no podrán ser nunca la misma.
            numFormaA=(int)(rnd.nextDouble() * 3 + 0);
            if( (int)(rnd.nextDouble() *2 +0) ==0 )
                numFormaB=(numFormaA+1)%3;
            else{
                numFormaB=(numFormaA-1)%3;
                if (numFormaB<0)
                    numFormaB+=3;
            }
        }

        if((nivel>=1 && nivel<=3) || (nivel>=6 && nivel<=8) || (nivel>=11 && nivel<=13))
            System.out.println("Forma elegida: "+numFormaA);
        else if((nivel>=4 && nivel<=5) || (nivel>=9 && nivel<=10) || (nivel>=14 && nivel<=15))
            System.out.println("Formas elegidas: "+numFormaA+" y "+numFormaB);




        //3º Construimos la forma misteriosa para los niveles 1, 6 y 11,


            //Si estamos en el nivel 1, 6 u 11, construimos un string de rallitas en la forma que queremos ocultar:
            if( nivel==1 || nivel==6 || nivel==11) {
                //Obtenemos el número de letras que tiene la forma:


                if(numFormaA==0)
                    numLetrasForma=verboInfinitivo.length();
                if(numFormaA==1)
                    numLetrasForma=verboPasado.length();
                if(numFormaA==2)
                    numLetrasForma=verboParticipio.length();

                //Se construye un string de rallitas para mostrar como pista al jugador
                for (int i = 0; i < numLetrasForma; i++) {
                    formaMisteriosa += " _ ";
                }
            }
            else
                //Si está en los niveles 2 6 o 12 no hay forma misteriosa, es una linea
                formaMisteriosa="";



        //4º Seteamos la información en los textView de pantalla.


            //Si se trata del nivel 1,2, 6,7, 11 o 12 la forma misteriosa se pasa directamente:
            if(nivel==1 || nivel==2 || nivel==6 || nivel==7 || nivel==11 || nivel==12) {
                if (numFormaA == 0)
                    infinitivo.setText(formaMisteriosa);
                else{
                    //Da igual en el nivel que estemos porque se ha cargado el verbo correspondiente al nivel antes.
                    infinitivo.setText(verboInfinitivo);
                }
                if (numFormaA == 1)
                    pasado.setText(formaMisteriosa);
                else{
                        pasado.setText(verboPasado);
                }
                if (numFormaA == 2)
                    participio.setText(formaMisteriosa);
                else{
                    participio.setText(verboParticipio);
                }

                //Después la forma misteriosa vuelve a estar vacía
                formaMisteriosa = "";
            }


        else

            if(nivel==3 || nivel==8 || nivel==13) {
                //Entonces se colocan las tres formas verbales de forma no ordenada

            //Envío el infinitivo a:
                int lugar = (int) (rnd.nextDouble() * 3 + 0); //(0 - 1- 2)

                if(lugar==0) {
                    infinitivo.setText(verboInfinitivo);
                    //Quedan dos huecos:
                    if( (int)(rnd.nextDouble()*2+0) == 0  ) {
                        pasado.setText(verboPasado);
                        participio.setText(verboParticipio);
                    }else {
                        pasado.setText(verboParticipio);
                        participio.setText(verboPasado);
                    }


                }
                if(lugar==1){

                    pasado.setText(verboInfinitivo);
                    //Quedan dos huecos;
                    if( (int)(rnd.nextDouble()*2+0) == 0  ) {
                        infinitivo.setText(verboPasado);
                        participio.setText(verboParticipio);
                    }else {
                        participio.setText(verboPasado);
                        infinitivo.setText(verboParticipio);
                    }

                }
                if(lugar==2){


                    participio.setText(verboInfinitivo);

                    //Quedan dos huecos;
                    if( (int)(rnd.nextDouble()*2+0) == 0  ) {
                        infinitivo.setText(verboPasado);
                        pasado.setText(verboParticipio);
                    }else {
                        participio.setText(verboPasado);
                        infinitivo.setText(verboParticipio);
                    }

                }




            } //Fin nivel 2, 8 y 14
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

        if(nivel>=1 && nivel<=5)
            System.out.println("Respuesta correcta: "+verbosSoft[numVerbo][numFormaA]);
        else if(nivel>=6 && nivel<=10)
            System.out.println("Respuesta correcta: "+verbosMedium[numVerbo][numFormaA]);
        else if(nivel>=11 && nivel <=15)
            System.out.println("Respuesta correcta: "+verbosHard[numVerbo][numFormaA]);





        if(nivel>=1 && nivel<=5) {
            if (txtVerbo.getText().toString().equals(verbosSoft[numVerbo][numFormaA])) {
                puntuacionPartida++;
            } else {
                //Pierde una vida: ohhh!!
                perderVida();
            }
        }
        else if(nivel>=6 && nivel<=10) {
            if (txtVerbo.getText().toString().equals(verbosMedium[numVerbo][numFormaA])) {
                puntuacionPartida++;
            } else {
                //Pierde una vida: ohhh!!
                perderVida();
            }
        }else if(nivel>=11 && nivel<=15) {
            if (txtVerbo.getText().toString().equals(verbosMedium[numVerbo][numFormaA])) {
                puntuacionPartida++;
            } else {
                //Pierde una vida: ohhh!!
                perderVida();
            }
        }






        System.out.println("Puntucion: " +puntuacionPartida);

        puntos.setText(Integer.toString(puntuacionPartida));
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
