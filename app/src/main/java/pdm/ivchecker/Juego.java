package pdm.ivchecker;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText campoVerboIntroducidoA, campoVerboIntroducidoB;

    private TextView infinitivo, pasado, participio, puntos, textNivel;
    private ImageView vida1, vida2, vida3; // vida4, vida5;


    //Botones de niveles superiores
    private CheckBox botonInfinitivoAzul, botonInfinitivoVerde;
    private CheckBox botonPasadoAzul, botonPasadoVerde;
    private CheckBox botonParticipioAzul, botonParticipioVerde;

    //Layouts:
    private LinearLayout layoutCampoVerboIntroducidoA, layoutCampoVerboIntroducidoB;

    private RelativeLayout layoutInfinitivo, layoutPasado, layoutParticipio;

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
    Intent intent;


    // Fin variables globales de partida.



    SharedPreferences prefe;

    //Constructor:
    public Juego(){
        System.out.println("Constructor de JUEGO");
        numVerbosListaSoft=numVerbosListaMedium=numVerbosListaHard=0;
        formaMisteriosa="";
        puntuacionPartida=0;
        nVidas=3;
        nivel=1;

        int defecto=0;

//        nivel=Integer.parseInt(getIntent().getStringExtra("nivel"));
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

        //Recibido desde otra actividad
        intent = getIntent();

        nivel=intent.getIntExtra("nivel", 0); //El nivel que nos dice la activity anterior.


        System.out.println("Nivel recibido en Juego: "+nivel);

        /* ## Referencias a los elementos del layout (vista) ## */

        //Obtenemos la referencia al botón de la vista "Next"
        btnNext=(Button)findViewById(R.id.nextButton);

        //Campos de texto donde el jugador introduce los verbos:
        campoVerboIntroducidoA=(EditText)findViewById(R.id.formaMisteriosaA);
        campoVerboIntroducidoA.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        campoVerboIntroducidoB=(EditText)findViewById(R.id.formaMisteriosaB);
        campoVerboIntroducidoB.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        //Referencias a los layouts de las cajas de texto EditText:
        layoutCampoVerboIntroducidoA=(LinearLayout)findViewById(R.id.LinearLayoutA);
        layoutCampoVerboIntroducidoB=(LinearLayout)findViewById(R.id.LinearLayoutB);



        infinitivo=(TextView)findViewById(R.id.infinitivo);
        pasado=(TextView)findViewById(R.id.pasado);
        participio=(TextView)findViewById(R.id.participio);





        //Al campo de los puntos
        puntos=(TextView)findViewById(R.id.viewPuntos);

        //El texto del nivel:
        textNivel=(TextView)findViewById(R.id.textLevel);


        //Setemos el textView de nivel para que aparezca el nivel en el que estamos jugando:
        textNivel.setText("Level "+nivel);

       // puntos.setText(Integer.toString(0));

        //A las imágenes de las vidas:
        vida1=(ImageView)findViewById(R.id.vida1);
        vida2=(ImageView)findViewById(R.id.vida2);
        vida3=(ImageView)findViewById(R.id.vida3);
      //  vida4=(ImageView)findViewById(R.id.vida4);
      //  vida5=(ImageView)findViewById(R.id.vida5);


        //Sólo se verán en un principio las primeras tres.
      //  vida4.setVisibility(ImageView.INVISIBLE);
        //    vida5.setVisibility(ImageView.VISIBLE);


        /* Fin de las referencias*/


        //Recuperación de datos almacenados:

        //Recuperación de datos almacenados:
        prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);

        System.out.println("Recibido del Prefe getInt: "+prefe.getInt("puntos",0));
        //Almacenamos en la variable global el valor de la puntuación que teníamos:
        puntuacionPartida=prefe.getInt("puntos",0); //Los puntos de la partida
        nVidas=prefe.getInt("vidas",3); //El número de vidas de la partida. Si no devuelve nada es que acaba de empezar y es 3.
        //Insteramos esta puntuación en TextViewPuntos:
        puntos.setText(Integer.toString(puntuacionPartida));

        System.out.println("Recibido del Prefe getInt: "+puntuacionPartida+" puntos "+nVidas+" vidas");

        //Cuando se llega al nivel 4 y 9 se gana una vida.
        if(nivel==4 || nivel==9 )
            ganarVida();

        ajustarVidas(nVidas);


        //Ajustamos los elmentos de la vista según el nivel:

         if(nivel!=4 && nivel!=5 && nivel!=9 && nivel!=10 && nivel!=14 && nivel!=15) {
             campoVerboIntroducidoB.setVisibility(TextView.INVISIBLE);
             layoutCampoVerboIntroducidoB.setVisibility(LinearLayout.INVISIBLE);
             layoutCampoVerboIntroducidoA.setBackgroundColor(Color.TRANSPARENT);

         }else{
             layoutCampoVerboIntroducidoA.setBackgroundColor(Color.rgb(49,193,255));

             campoVerboIntroducidoB.setVisibility(TextView.VISIBLE);
             layoutCampoVerboIntroducidoB.setVisibility(LinearLayout.VISIBLE);
         }






        //Esta clase mola.

        // Para cerrar el teclado al pulsar intro
        campoVerboIntroducidoA.setOnKeyListener(new View.OnKeyListener() {
            /**
             * This listens for the user to press the enter button on
             * the keyboard and then hides the virtual keyboard
             */
            public boolean onKey(View arg0, int arg1, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (arg1 == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(campoVerboIntroducidoA.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });



        // Para cerrar el teclado al pulsar intro
        campoVerboIntroducidoB.setOnKeyListener(new View.OnKeyListener() {
            /**
             * This listens for the user to press the enter button on
             * the keyboard and then hides the virtual keyboard
             */
            public boolean onKey(View arg0, int arg1, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (arg1 == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(campoVerboIntroducidoB.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        //Implementamos el evento click del botón next:
        btnNext.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {


                        //Comprobamos el verbo:
                        comprobarVerbo();

                        //Limpiamos el contenido de los editText aunque dependiendo del nivel no se verán los dos:
                        campoVerboIntroducidoA.setText("");
                        campoVerboIntroducidoB.setText("");

                        //Aumentamos el número de la jugada en el nivel (10 verbos por nivel)
                        jugadaEnNivel++;

                        /*
                        10 jugadas por nivel!
                         */
                        if(jugadaEnNivel>2) { //Si hemos completado las diez jugadas por nivel pasamos de nivel.
                            nivel++; //Pasamos de nivel
                            jugadaEnNivel=1; //Reiniciamos.
                            //Nos vamos a la actividad que muestra el nivel:
                            Intent intent = new Intent(Juego.this, juego_show_level.class);

                            System.out.println("Vamos a show_level pasando nivel  "+nivel);
                            //Vamos a la activity juego con el nivel 1
                            intent.putExtra("nivel",nivel);

                            //Antes de pasar de actividad guardamos los datos para que al volver a la actividad los tengamos disponibles:
                            SharedPreferences.Editor editor=prefe.edit();
                            editor.putInt("puntos",puntuacionPartida); //La puntuación de la partida
                            editor.putInt("vidas",nVidas); //El número de vidas
                            editor.commit();

                            System.out.println("Grabamos nivel: "+nivel+" y vidas: "+nVidas);


                            startActivity(intent);
                        }


                        /*
                        Hay que cambiar el flujo de ejecución...
                        La implementación del botón no debería ser la que controlase el fin de la partida
                        sino la lógica del juego.
                        EL botón solo debe de llamar a jugar y que sea en jugar donde se desarrolle
                        la lógica del juego.
                         */


                        //Si se han completado todos los niveles (se ha acabado el juego) vamos a una pantalla de resultados:.
                        else if (nivel > 15) {
                            //Creamos el intent:
                            Intent intent = new Intent(Juego.this, Resultado.class);

                            //Creamos la información a pasar entre actividades:
                            Bundle b = new Bundle();
                            b.putString("PUNTOS", String.valueOf(puntuacionPartida));

                            //Añadimos la información al intent:
                            intent.putExtras(b);

                            //Nos vamos al activity resultados:
                            startActivity(intent);
                        }
                        //Si no se han completado seguimos jugando!
                        else
                            //Si no estuviera dentro de la estructura else nos crearía una jugada antes de pasar de nivel.
                            crearJugada();


                    }
                }
        ); //Fin del manejador del botón next.

        this.cargarVerbos();


        //Después de cargar los datos comienza el juego:
        this.crearJugada();


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos devuelve a la pantalla principal perdiendo todos los puntos.
        if(keyCode==KeyEvent.KEYCODE_BACK){
            System.out.println("pulsado boton back");
            SharedPreferences.Editor editor=prefe.edit();
            //Escribimos 0 en puntos
            editor.putInt("puntos",0);
            editor.putInt("vidas",3); //El número de vidas
            //Realizamos la escritura
            editor.commit();
            finish();


            Intent intent = new Intent(Juego.this, ActividadPrincipal.class);
            startActivity(intent);

            return true;
        }

        /*
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                System.out.println("pulsado boton back");
                Toast.makeText(this, "Abandonas cobarde",Toast.LENGTH_SHORT);
                SharedPreferences.Editor editor=prefe.edit();
                editor.putInt("puntos",0);
                editor.commit();
                return true;
        }*/
        return super.onKeyDown(keyCode, event);

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

        /*
        Para evitar que modificaciones del color de las cajas queden en posteriores jugadas resetamos las tres:
         */
        infinitivo.setBackgroundColor(Color.TRANSPARENT);
        pasado.setBackgroundColor(Color.TRANSPARENT);
        participio.setBackgroundColor(Color.TRANSPARENT);


       // infinitivo.setLayoutParams(ActionBar.LayoutParams.MATCH_PARENT);





        System.out.println("### Creando jugada "+this.jugadaEnNivel+" en nivel "+nivel+" ###");

        /* 1º
           GENERAMOS EL VERBO A PREGUNTAR según lo que mida la lista de verbos que dependerá del nivel en el que estemos.:
           el número "numVerbosLista" se coge desde el propio fichero que se carga.
           **Mejora: que no pueda volver a preguntar por el mismo verbo.
        */

        //Ademas lo cargarmos:


        if(nivel>=1 && nivel<=5) { //Si estamos en los niveles 1-5

            System.out.println("Elegimos verbo de la lista SOFT");

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

                System.out.println("Elegimos verbo de la lista MEDIUM");

        } if(nivel>=11 && nivel<=15) { //Si estamos en los niveles 6-10

            //Elegimos el verbo de forma aleatoria:
            numVerbo = (int) (rnd.nextDouble() * numVerbosListaHard + 0);
            //Cargamos el verbo en las variables locales:
            verboInfinitivo=verbosHard[numVerbo][0];
            verboPasado=verbosHard[numVerbo][1];
            verboParticipio=verbosHard[numVerbo][2];

            System.out.println("Elegimos verbo de la lista HARD");

        }


        System.out.println("Verbo elegido nº: "+numVerbo+"\nInfinitivo: "+verboInfinitivo+" Pasado: "+verboPasado+" Participio: "+verboParticipio);

        String verbos[]={verboInfinitivo,verboPasado,verboParticipio};




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
            //Entonces se colocan las tres formas verbales de forma no ordenada donde sigue habiendo una sin pista
            if(nivel==3 || nivel==8 || nivel==13) {



                //El lugar donde poner la forma misteriosa
                int lugar = (int) (rnd.nextDouble() * 3 + 0); //(0 - 1- 2)

                /*
                Si el lugar decidido para poner la forma misteriosa es 0 ahí se coloca la forma misteriosa.
                 */
                if(lugar==0) { //Lugar = 0 -> en el infinitivo se pone la forma misteriosa.

                    //Colocamos la forma misteriosa:
                    infinitivo.setText(formaMisteriosa);

                    //Quedan dos huecos:

                    /*
                    El número de la fomra misteriosa era la fomra elegida: numFormaA
                     */


                    if( (int)(rnd.nextDouble()*2+0) == 0  ) { //Cara o cruz:
                        //Cara
                        pasado.setText(verbos[(numFormaA+1)%3]);
                        participio.setText(verbos[(numFormaA+2)%3]);
                        /*
                        pasado.setText(verboPasado);
                        participio.setText(verboParticipio);
                        */
                    }else {
                        participio.setText(verbos[(numFormaA+1)%3]);
                        pasado.setText(verbos[(numFormaA+2)%3]);

                        /*
                        pasado.setText(verboParticipio);
                        participio.setText(verboPasado);
                        */
                    }


                }
                if(lugar==1){

                    pasado.setText(formaMisteriosa);


                    //Quedan dos huecos;
                    if( (int)(rnd.nextDouble()*2+0) == 0  ) {


                        //Cara
                        infinitivo.setText(verbos[(numFormaA+1)%3]);
                        participio.setText(verbos[(numFormaA+2)%3]);

                        /*
                        infinitivo.setText(verboPasado);
                        participio.setText(verboParticipio);
                        */
                    }else {
                        participio.setText(verbos[(numFormaA+1)%3]);
                        infinitivo.setText(verbos[(numFormaA+2)%3]);
                        /*
                        participio.setText(verboPasado);
                        infinitivo.setText(verboParticipio);
                        */
                    }

                }
                if(lugar==2){


                    participio.setText(formaMisteriosa);

                    //Quedan dos huecos;
                    if( (int)(rnd.nextDouble()*2+0) == 0  ) {

                        //Cara
                        infinitivo.setText(verbos[(numFormaA+1)%3]);
                        pasado.setText(verbos[(numFormaA + 2) % 3]);

                        /*
                        infinitivo.setText(verboPasado);
                        pasado.setText(verboParticipio);
                        */
                    }else {
                        //Cara
                        pasado.setText(verbos[(numFormaA+1)%3]);
                        infinitivo.setText(verbos[(numFormaA+2)%3]);
                        /*
                        participio.setText(verboPasado);
                        infinitivo.setText(verboParticipio);
                        */
                    }

                }




            }else{
                System.out.println("Resto de niveles");

                /*
                Si se trata del nivel 4 se marcar de colores los cuadros donde irán las formas y no se introduce nada:
                 */
                if(nivel==4 || nivel==9 || nivel==14){

                    //Ponemos las tres formas en su orden:
                    infinitivo.setText(verboInfinitivo);
                    pasado.setText(verboPasado);
                    participio.setText(verboParticipio);


                    System.out.println("Configurando nivel 4, 9 o 14");

                    //Pintamos de azul el verbo que corresponda la forma A
                    if(numFormaA==0) {
                        infinitivo.setBackgroundColor(Color.rgb(49, 193, 255));
                        infinitivo.setText("");
                    }if(numFormaA==1) {
                        pasado.setBackgroundColor(Color.rgb(49, 193, 255));
                        pasado.setText("");
                    }if(numFormaA==2) {
                        participio.setBackgroundColor(Color.rgb(49, 193, 255));
                        participio.setText("");
                    }

                    //Pintamos de verde el verbo que corresponda la forma B
                    if(numFormaB==0) {
                        infinitivo.setBackgroundColor(Color.rgb(48, 255, 173));
                        infinitivo.setText("");
                    }if(numFormaB==1) {
                        pasado.setBackgroundColor(Color.rgb(48, 255, 173));
                        pasado.setText("");
                    }if(numFormaB==2) {
                        participio.setBackgroundColor(Color.rgb(48, 255, 173));
                        participio.setText("");
                    }

                }else if(nivel==5 || nivel==10 || nivel==15){

                    System.out.println("Config nivel 5. ");

                    int formaVisible=3-(numFormaA+numFormaB);


                    //El lugar donde poner la forma visible
                    int lugar = (int) (rnd.nextDouble() * 3 + 0); //(0 - 1- 2)
                    System.out.println("Lugar de la forma visible: "+lugar+" Forma visible: "+formaVisible);

                    if(lugar==0){ //Si la forma visible se coloca en hueco del infinitivo

                        //Dependiendo de la forma verbal que se vaya a mostrar se mete un texto u otro en ##ESE HUECO##.
                        if(formaVisible==0)
                            infinitivo.setText(verboInfinitivo);
                        if(formaVisible==1)
                            infinitivo.setText(verboPasado);
                        if(formaVisible==2)
                            infinitivo.setText(verboParticipio);


                        //Quedan dos huecos:

                        if( (int)(rnd.nextDouble()*2+0) == 0  ) { //Cara o cruz:
                            //Cara


                            //Colocalción de la formaA en el pasado.
                            if(numFormaA==0)
                                pasado.setText("INFINITIVO");
                            if(numFormaA==1)
                                pasado.setText("PASADO");
                            if(numFormaA==2)
                                pasado.setText("PARTICIPIO");
                            //Sea como sea pones el pasado azul (de la forma verboIntroducidoA:
                            pasado.setBackgroundColor(Color.rgb(49,193,255));

                            //Colocación de la fomra B en el participio
                            if(numFormaB==0)
                                participio.setText("INFINITIVO");
                            if(numFormaB==1)
                                participio.setText("PASADO");
                            if(numFormaB==2)
                                participio.setText("PARTICIPIO");

                            participio.setBackgroundColor(Color.rgb(48,255,173));

                        }else {
                            //Cruz

                            //Colocalción de la formaA en el participio.
                            if(numFormaA==0)
                                participio.setText("INFINITIVO");
                            if(numFormaA==1)
                                participio.setText("PASADO");
                            if(numFormaA==2)
                                participio.setText("PARTICIPIO");
                            //Sea como sea pones el pasado azul (de la forma verboIntroducidoA:
                            participio.setBackgroundColor(Color.rgb(49,193,255));

                            //Colocación de la fomra B en el pasado
                            if(numFormaB==0)
                                pasado.setText("INFINITIVO");
                            if(numFormaB==1)
                                pasado.setText("PASADO");
                            if(numFormaB==2)
                                pasado.setText("PARTICIPIO");

                            pasado.setBackgroundColor(Color.rgb(48,255,173));


                        }

                    }

                    if(lugar==1){//Si la forma visible se coloca en el hueco del pasado

                        if(formaVisible==0)
                            pasado.setText(verboInfinitivo);
                        if(formaVisible==1)
                            pasado.setText(verboPasado);
                        if(formaVisible==2)
                            pasado.setText(verboParticipio);


                        //Quedan dos huecos:

                        if( (int)(rnd.nextDouble()*2+0) == 0  ) { //Cara o cruz:
                            //Cara


                            //Colocalción de la formaA en el infinitivo.
                            if(numFormaA==0)
                                infinitivo.setText("INFINITIVO");
                            if(numFormaA==1)
                                infinitivo.setText("PASADO");
                            if(numFormaA==2)
                                infinitivo.setText("PARTICIPIO");
                            //Sea como sea pones el pasado azul (de la forma verboIntroducidoA:
                            infinitivo.setBackgroundColor(Color.rgb(49,193,255));

                            //Colocación de la fomra B en el participio
                            if(numFormaB==0)
                                participio.setText("INFINITIVO");
                            if(numFormaB==1)
                                participio.setText("PASADO");
                            if(numFormaB==2)
                                participio.setText("PARTICIPIO");

                            participio.setBackgroundColor(Color.rgb(48,255,173));

                        }else {
                            //Cruz

                            //Colocalción de la formaA en el participio.
                            if(numFormaA==0)
                                participio.setText("INFINITIVO");
                            if(numFormaA==1)
                                participio.setText("PASADO");
                            if(numFormaA==2)
                                participio.setText("PARTICIPIO");
                            //Sea como sea pones el pasado azul (de la forma verboIntroducidoA:
                            participio.setBackgroundColor(Color.rgb(49,193,255));

                            //Colocación de la fomra B en el infinitivo
                            if(numFormaB==0)
                                infinitivo.setText("INFINITIVO");
                            if(numFormaB==1)
                                infinitivo.setText("PASADO");
                            if(numFormaB==2)
                                infinitivo.setText("PARTICIPIO");

                            infinitivo.setBackgroundColor(Color.rgb(48,255,173));


                        }
                    }

                    if(lugar==2){

                        if(formaVisible==0)
                            participio.setText(verboInfinitivo);
                        if(formaVisible==1)
                            participio.setText(verboPasado);
                        if(formaVisible==2)
                            participio.setText(verboParticipio);


                        //Quedan dos huecos:

                        if( (int)(rnd.nextDouble()*2+0) == 0  ) { //Cara o cruz:
                            //Cara


                            //Colocalción de la formaA en el pasado.
                            if(numFormaA==0)
                                pasado.setText("INFINITIVO");
                            if(numFormaA==1)
                                pasado.setText("PASADO");
                            if(numFormaA==2)
                                pasado.setText("PARTICIPIO");
                            //Sea como sea pones el pasado azul (de la forma verboIntroducidoA:
                            pasado.setBackgroundColor(Color.rgb(49,193,255));

                            //Colocación de la fomra B en el infinitivo
                            if(numFormaB==0)
                                infinitivo.setText("INFINITIVO");
                            if(numFormaB==1)
                                infinitivo.setText("PASADO");
                            if(numFormaB==2)
                                infinitivo.setText("PARTICIPIO");

                            infinitivo.setBackgroundColor(Color.rgb(48,255,173));

                        }else {
                            //Cruz

                            //Colocalción de la formaA en el infinitivo.
                            if(numFormaA==0)
                                infinitivo.setText("INFINITIVO");
                            if(numFormaA==1)
                                infinitivo.setText("PASADO");
                            if(numFormaA==2)
                                infinitivo.setText("PARTICIPIO");
                            //Sea como sea pones el pasado azul (de la forma verboIntroducidoA:
                            infinitivo.setBackgroundColor(Color.rgb(49,193,255));

                            //Colocación de la fomra B en el pasado
                            if(numFormaB==0)
                                pasado.setText("INFINITIVO");
                            if(numFormaB==1)
                                pasado.setText("PASADO");
                            if(numFormaB==2)
                                pasado.setText("PARTICIPIO");

                            pasado.setBackgroundColor(Color.rgb(48,255,173));


                        }


                    }



                }//Fin nivel 5,10,15


            }
        }


    public void ganarVida(){

        //Configuramos el mensaje de vida ganada:
        Context context = getApplicationContext();
        CharSequence text = "You win one life!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);

        //Si no le queda ninguna vida
        if(this.nVidas==0) {
            vida1.setImageResource(R.drawable.corazonvivo);
            nVidas++;
        }else if(this.nVidas==1){ //Si le queda una vida
            vida2.setImageResource(R.drawable.corazonvivo);
            nVidas++;
        }else if(this.nVidas==2){
            vida3.setImageResource(R.drawable.corazonvivo);
            nVidas++;
        }
        toast.show();



    }

    public void ajustarVidas(int numero){
        if(numero==3) {
            vida3.setImageResource(R.drawable.corazonvivo);
            vida2.setImageResource(R.drawable.corazonvivo);
            vida1.setImageResource(R.drawable.corazonvivo);
        }
        if(numero==2){
            vida3.setImageResource(R.drawable.corazonmuerto);
            vida2.setImageResource(R.drawable.corazonvivo);
            vida1.setImageResource(R.drawable.corazonvivo);
        }
        if(numero==1){
            vida3.setImageResource(R.drawable.corazonmuerto);
            vida2.setImageResource(R.drawable.corazonmuerto);
            vida1.setImageResource(R.drawable.corazonvivo);

        }
        if(numero==0){
            vida3.setImageResource(R.drawable.corazonmuerto);
            vida2.setImageResource(R.drawable.corazonmuerto);
            vida1.setImageResource(R.drawable.corazonmuerto);
        }



    }


    public void perderVida(){

        if(this.nVidas==3) {
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

            //Mostramos un mensaje toast! YOU ARE DEAD!
            Context context = getApplicationContext();
            CharSequence text = "YOU ARE DEAD!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            //Vamos a la clase Resultados.class
            Intent intent = new Intent(Juego.this, Resultado.class);
            startActivity(intent);

        }


    }

    public void comprobarVerbo(){


        //INFO DE DEPURACIÓN:

        if(nivel!=4 && nivel!=5 && nivel!=9 && nivel!=10 && nivel!=14 && nivel!=15)
            System.out.println("Verbo introducido: "+campoVerboIntroducidoA.getText());
        else{
            System.out.println("Verbo Introducido azul: "+campoVerboIntroducidoA.getText());
            System.out.println("Verbo Introducido verde: "+campoVerboIntroducidoB.getText());
        }

        if(nivel>=1 && nivel<=5)
            if(nivel==1 || nivel==2 || nivel==3)
                System.out.println("Respuesta correcta: "+verbosSoft[numVerbo][numFormaA]);
            else {
                System.out.println("Respuesta correcta azul: "+verbosSoft[numVerbo][numFormaA]);
                System.out.println("Respuesta correcta verde: "+verbosSoft[numVerbo][numFormaB]);
            }
        else if(nivel>=6 && nivel<=10)
            System.out.println("Respuesta correcta: "+verbosMedium[numVerbo][numFormaA]);
        else if(nivel>=11 && nivel <=15)
            System.out.println("Respuesta correcta: "+verbosHard[numVerbo][numFormaA]);




        //Comprobación de verbos:

        if(nivel>=1 && nivel<=5) {

            if(nivel==1 || nivel==2 || nivel==3) {
                if (campoVerboIntroducidoA.getText().toString().equals(verbosSoft[numVerbo][numFormaA])) {
                    /*
                    En el nivel 1 cada acierto suma 1 en el nivel 2 suma 2 y así con el resto de niveles
                     */
                    puntuacionPartida=puntuacionPartida+nivel;

                } else {
                    //Pierde una vida: ohhh!!
                    perderVida();
                }
            }
            else{

                if(nivel==4 || nivel==5) {
                    if (campoVerboIntroducidoA.getText().toString().equals(verbosSoft[numVerbo][numFormaA]))
                        puntuacionPartida=puntuacionPartida+nivel;
                    else
                        perderVida();
                    if (campoVerboIntroducidoB.getText().toString().equals(verbosSoft[numVerbo][numFormaB]))
                        puntuacionPartida=puntuacionPartida+nivel;
                    else
                        perderVida();
                }

            }
        }
        else if(nivel>=6 && nivel<=10) {

            if(nivel==6 || nivel==7 || nivel==8) {
                if (campoVerboIntroducidoA.getText().toString().equals(verbosMedium[numVerbo][numFormaA])) {
                    puntuacionPartida=puntuacionPartida+nivel;

                } else {
                    //Pierde una vida: ohhh!!
                    perderVida();
                }
            }
            else{

                if(nivel==9 || nivel==10) {
                    if (campoVerboIntroducidoA.getText().toString().equals(verbosMedium[numVerbo][numFormaA]))
                        puntuacionPartida=puntuacionPartida+nivel;
                    else
                        perderVida();
                    if (campoVerboIntroducidoB.getText().toString().equals(verbosMedium[numVerbo][numFormaB]))
                        puntuacionPartida=puntuacionPartida+nivel;
                    else
                        perderVida();
                }

            }

        }else if(nivel>=11 && nivel<=15) {


            if(nivel==11 || nivel==12 || nivel==13) {
                if (campoVerboIntroducidoA.getText().toString().equals(verbosHard[numVerbo][numFormaA])) {
                    puntuacionPartida=puntuacionPartida+nivel;

                } else {
                    //Pierde una vida: ohhh!!
                    perderVida();
                }
            }
            else{

                if(nivel==14 || nivel==15) {
                    if (campoVerboIntroducidoA.getText().toString().equals(verbosHard[numVerbo][numFormaA]))
                        puntuacionPartida=puntuacionPartida+nivel;
                    else
                        perderVida();
                    if (campoVerboIntroducidoB.getText().toString().equals(verbosHard[numVerbo][numFormaB]))
                        puntuacionPartida=puntuacionPartida+nivel;
                    else
                        perderVida();
                }

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
