package pdm.ivchecker;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.reflect.Field;
import java.util.Random;

import com.google.android.gms.ads.*;



public class Juego extends ActionBarActivity {



    //Variables del fragment:

    String id = "IdQueNecesitaMyFragment";
//    MyDialogFragment frag;



    Bundle arguments = new Bundle();





    //private juego_show_level showerLevels;


    //Matrices donde se almacenan los verbos:
    private String [][] verbosSoft;
    private String [][] verbosMedium;
    private String [][] verbosHard;

    //Elementos de la vista:
    private Button btnNext;
    private EditText campoVerboIntroducidoA, campoVerboIntroducidoB;

    private TextView infinitivo, pasado, participio, puntos, textNivel;
    private ImageView vida1, vida2, vida3; // vida4, vida5;

    //Layouts:
    private LinearLayout layoutCampoVerboIntroducidoA, layoutCampoVerboIntroducidoB;


    private RelativeLayout layoutPrincipal;

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


    private boolean finPartida=false;
    private boolean restauracion=false;





    //Intent intentShowerLevels;
    //Intent intentShowerLevels;


    // Fin variables globales de partida.



    private TextView timer;
    private int time;


    //private final long startTime=25000;
    //private final long interval= 1;
    //private MalibuCountDownTimer countDownTimer;
    private CountDownTimer countDownTimer;
    //private long timeElapsed;


    SharedPreferences prefe;

    //Constructor:
    public Juego(){


      //  showerLevels=new juego_show_level();
        //intentShowerLevels = new Intent(Juego.this, juego_show_level.class);
//        showerLevels.setVisible(true);
        //startActivity(intent);

        time=30;

        if(!restauracion) {
            System.out.println("Constructor de JUEGO");
            numVerbosListaSoft = numVerbosListaMedium = numVerbosListaHard = 0;
            formaMisteriosa = "";
            //La partida comienza con la puntación a 0
            puntuacionPartida = 0;
            //La partida comienza con tres vidas
            nVidas = 3;

            //La partida comienza en el nivel 1
            nivel = 1;

            int defecto = 0;

            //        nivel=Integer.parseInt(getIntent().getStringExtra("nivel"));
            jugadaEnNivel = 1;
            //Inicializamos  el objeto de tipo Random().
            rnd = new Random();
            numVerbo=0;
            numFormaA=0;
            numFormaB=0;
        }
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


    private void referenciaObjetosDeLaVista(){
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


        layoutPrincipal = (RelativeLayout) findViewById(R.id.layoutActivity);



        infinitivo=(TextView)findViewById(R.id.infinitivo);
        pasado=(TextView)findViewById(R.id.pasado);
        participio=(TextView)findViewById(R.id.participio);






        //Al campo de los puntos
        puntos=(TextView)findViewById(R.id.viewPuntos);

        //El texto del nivel:
        textNivel=(TextView)findViewById(R.id.textLevel);


        //Setemos el textView de nivel para que aparezca el nivel en el que estamos jugando:
        textNivel.setText(R.string.nivel+" "+nivel);

        // puntos.setText(Integer.toString(0));

        //A las imágenes de las vidas:
        vida1=(ImageView)findViewById(R.id.vida1);
        vida2=(ImageView)findViewById(R.id.vida2);
        vida3=(ImageView)findViewById(R.id.vida3);


        timer=(TextView)findViewById(R.id.timer);

        /* Fin de las referencias*/
    }


    /**
     * Para ajustar los elementos de la vista.
     */
    private void ajustarVista(){


        //Ajustamos los elementos de la vista (útil cuando giramos la pantalla)

        //Ajustamos el número de vidas
        this.ajustarVidas(nVidas);

        this.textNivel.setText("Nivel "+Integer.toString(this.nivel));
        this.puntos.setText(Integer.toString(this.puntuacionPartida));


        //Ajuste de fondo:

        try {
            Field field = R.drawable.class.getField("juegofondo"+nivel);
            try {
                layoutPrincipal.setBackgroundResource(field.getInt(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        //Si pasamos del nivel

        if(nivel!=4 && nivel!=5 && nivel!=9 && nivel!=10 && nivel!=14 && nivel!=15) {
            campoVerboIntroducidoB.setVisibility(TextView.INVISIBLE);
            layoutCampoVerboIntroducidoB.setVisibility(LinearLayout.INVISIBLE);
            layoutCampoVerboIntroducidoA.setBackgroundColor(Color.TRANSPARENT);

        }else{

            System.out.println("Ajustando vista");

            //Si estamos en los verbos, 4, 5, 9, 10. 14 o 15:

            layoutCampoVerboIntroducidoA.setBackgroundColor(Color.rgb(49,193,255));

            campoVerboIntroducidoB.setVisibility(TextView.VISIBLE);
            layoutCampoVerboIntroducidoB.setVisibility(LinearLayout.VISIBLE);
        }
    }


    private boolean comprueba(String introducido, String original){

        /*Esta función comprueba que el verbo introducido corresponda con el original.
        Construimos esta función poque el verbo original puede ser que esté compuesto por dos partes,
        la forma para el singular y para el plural por eso si se da este caso tenemos que separar el contenido
        y comprobar que coincida con uno de los dos.
         */

        String descompuestoA="", descompuestoB="";


        if(original.contains("/")){ //Se trata de un verbo compuesto.

            //1º. Se descompone el verbo.

            int posBarra = original.indexOf("/");
            descompuestoA=original.substring(0, posBarra);
            System.out.println("PrimeraSeccion: "+descompuestoA);
            descompuestoB=original.substring(posBarra+1, original.length());
            System.out.println("SegundaSeccion: "+descompuestoB);


            //2º. Se comprueba con cada una de las formas

            if(introducido.equals(descompuestoA) || introducido.equals(descompuestoB))
                return true;
            else
                return false;

        }else{//No se trata de un verbo compuest
            if(introducido.equals(original))
                return true;
            else
                return false;
        }

    }

    @Override
    //Método llamada cuando se crea por primera vez la actividad.
    protected void onCreate(Bundle inState) {
        System.out.println("Llamando a onCreate");

        //Llamamos al constructor del padre:
            super.onCreate(inState);

        //Relacionamos esta actividad con el layout (vista) correspondiente:
            setContentView(R.layout.activity_juego);


        //Ajustamos la caracteristicas visuales de esta actividad
            //Para que no se muestre la ActionBar.
            getSupportActionBar().hide();
            //Para que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);





        if(inState!=null) {

            //Recuperamos el estado de la actividad en el caso de que este existiera y pasase el condicional

            this.puntuacionPartida = inState.getInt("puntos");
            this.nivel = inState.getInt("nivel");
            this.nVidas = inState.getInt("nVidas");
            this.numVerbo=inState.getInt("numVerbo");
            this.numFormaA=inState.getInt("numFormaA");
            this.numFormaB=inState.getInt("numFormaB");
            this.time=inState.getInt("time");


            System.out.println("onRestore: puntos: "+puntuacionPartida+" "+nVidas+" + nVidas");

            //Llamamos a ajustarVista para que coloque las cosas bien.
            //this.ajustarVista();

        }else{
            //Sólo cuando no se recupere ningún estado significará que empieza el juego desde el principio y que no se ha rotado la pantalla al
            // no haber recursos guardados en el Bundle
            runFragment(1);
        }

        //Recibimos los datos de la actividad que nos invoca
        /*
            intent = getIntent();
            //Vamos a jugar en el nivel que nos dice la actividad que nos llama
            nivel=intent.getIntExtra("nivel", 0);
            System.out.println("Nivel recibido en Juego : "+nivel);
        */

        //Referenciamos todos los objetos de la vista para poder controlarlos:
            referenciaObjetosDeLaVista();

            ajustarVista();

            //Publicidad:  AdSense

            /*
                //Instanciamos el objeto
                adView=new AdView(this);
                //Asociamos el identificador que nos dan en la web de AdSense para este anuncio.
                adView.setAdUnitId("ca-app-pub-5502625112104069/3950415634");
                //Especificamos el tamaño del banner
                adView.setAdSize(AdSize.BANNER);

                //AÑadimos el banner al layout:
              //  layoutPrincipal.addView(adView);

                //Iniciamos la solicitud:
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //Cualquier emulador
                        .addTestDevice("9011F9E7CEC921EF1BB8A17A36B24813") //El telefono de Juan
                        .build();

                //Cargamos la adView con la respuesta de la solicitud
                adView.loadAd(adRequest);
*/

                // Buscar AdView como recurso y cargar una solicitud.
                AdView adView = (AdView)this.findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //Cualquier emulador
                        .addTestDevice("9011F9E7CEC921EF1BB8A17A36B24813") //El telefono de Juan
                        .build();
                adView.loadAd(adRequest);


            //Fin publicidad




        //Inicializamos la vista, con 3 vidas y con la puntuación a cero.
            //ajustarVidas(nVidas);
        //Insteramos esta puntuación en TextViewPuntos:
       // puntos.setText(Integer.toString(puntuacionPartida));

        //Cargamos los verbos desde los ficheros csv

            cargarVerbos();


        /*
        Si no es la primera vez que se arranca esta actividad puede que se hayan guardado datos

            prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);

        System.out.println("Recibido del Prefe getInt: "+prefe.getInt("puntos",0));

        //Almacenamos en la variable global el valor de la puntuación que teníamos:
        puntuacionPartida=prefe.getInt("puntos",0); //Los puntos de la partida

        nVidas=prefe.getInt("vidas",3); //El número de vidas de la partida. Si no devuelve nada es que acaba de empezar y es 3.
        */

        //Cuando se llega al nivel 4 y 9 se gana una vida.
        if(nivel==4 || nivel==9 )
            ganarVida();


        /*
            //Insteramos esta puntuación en TextViewPuntos:
        puntos.setText(Integer.toString(puntuacionPartida));

        System.out.println("Recibido del Prefe getInt: "+puntuacionPartida+" puntos "+nVidas+" vidas");

        //Cuando se llega al nivel 4 y 9 se gana una vida.
        if(nivel==4 || nivel==9 )
            ganarVida();

        ajustarVidas(nVidas);
        */

        //Ajustamos los elmentos de la vista según el nivel:
           // this.ajustarVista();

        /*
        El ajustar vistas ya no se hace aquí sino al ocnfigurar el nivel.
         */


        // ### GESTIÓN DE COMPORTAMIENTO ### ///

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

        //ANimación del botón
        final Animation animacionBotonSiguiente = AnimationUtils.loadAnimation(this, R.anim.myanimation);

        //Implementamos el evento click del botón next:
        btnNext.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {


                            v.startAnimation(animacionBotonSiguiente);
                            //Lo primero que hacemos al pulsar sobre next es comprobar el/los verbos introducidos
                            comprobarVerbo();
                            /*
                            Ojo si el verbo falla y estamos en un punto crítico podemo salir del juego porque hayamos
                            perdido todas las vidas.


                            Quizás habría que añadir un condicionar para ver la salida de comprobarVerbo() y en caso
                            de que fuera positiva continuar y en caso de ser negativa no hacer nada más.

                             */



                            //Limpiamos el contenido de los editText aunque dependiendo del nivel no se verán los dos:
                            campoVerboIntroducidoA.setText("");
                            campoVerboIntroducidoB.setText("");

                            //Aumentamos el número de la jugada en el nivel (10 verbos por nivel)
                            jugadaEnNivel++;

                            numVerbo=0;


                            //Si hemos completado las quince jugadas por nivel pasamos de nivel.

                            if (jugadaEnNivel > 15) {
                                nivel++; //Pasamos de nivel
                                jugadaEnNivel = 1; //Reiniciamos.

                                System.out.println("Vamos a show_level pasando nivel  " + nivel);

                                if(!finPartida)
                                    runFragment(nivel);

                                jugar();

                            }

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
                                jugar(); //COmo llamar a crearJugada()

                        //Al hacer click se reinicia el countDownTimer
                        time=30;


                        countDownTimer.cancel();
                        countDownTimer = new CountDownTimer(time*1000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                timer.setText("" + millisUntilFinished / 1000);
                            }

                            public void onFinish() {
                                //Al colocar un 0 si el jugador agota el timepo pero acierta no suma puntos pero tampoco pierde vida.
                                timer.setText("0");
                            }
                        };
                        countDownTimer.start();
                        //*/

                        //countDownTimer.start();
                    }
                }



        ); //Fin del manejador del botón next.

        //this.cargarVerbos();


        //this.crearJugada(); //Después de cargar los datos comienza el juego:


       // runFragment(1);

        //Cuando el proceso se crear se llama a jugar y se empieza a jugar.
        this.jugar();
        //countDownTimer = new MalibuCountDownTimer(startTime, interval);

        //Al crear la actividad se crear el objeto de tipo CountDownTimer
        countDownTimer = new CountDownTimer(time*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timer.setText("0");
            }
        }.start();

    } //Fin de onCreate

    private void runFragment(int level) {


        System.out.println("LLAMADA A FRAGMENT");

        arguments.putString("id", Integer.toString(level));


        //Declaramos una nueva hebra
        new Thread() {
            //Le decimos lo que queremos que haga:
            public void run() {

                MyDialogFragment frag;

                FragmentTransaction ft = getFragmentManager().beginTransaction();




                //Añadimos animación
            //    ft.setCustomAnimations(R.anim.zoom_back_in, R.anim.zoom_back_out);



                frag = MyDialogFragment.newInstance(arguments);
                frag.show(ft, "txn_tag");


                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                frag.dismiss();

            }
        }.start();
    }

    private void jugar(){



        //Lanzamos el fragment que muestra el nivel:
        //runFragment(3);


        //Ajustamos nivel:
        this.textNivel.setText("Level "+Integer.toString(nivel));



        this.crearJugada(); //Después de cargar los datos comienza el juego:
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("La actividad ha sido resumida");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        System.out.println("La actividad ha sido restarted");
    }



    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //Salvamos la puntuación de la partida
        outState.putInt("puntos",this.puntuacionPartida);
        //Salvamos el nivel de la partida:
        outState.putInt("nivel",this.nivel);
        //Salvamos el número de vidas de la partida:
        outState.putInt("nVidas",this.nVidas);

        //Salvamos el verbo elegido en la jugada
        outState.putInt("numVerbo",this.numVerbo);

        //Salvamos las formas verbales también;
        outState.putInt("numFormaA",this.numFormaA);
        outState.putInt("numFormaB",this.numFormaB);

        //Salvamos el tiempo por si al jugador le da por cambiar la orientación de la pantalla mientras juega
        outState.putInt("time",Integer.parseInt(timer.getText().toString()));

    }

    /*
    @Override
    protected void onRestoreInstanceState(Bundle inState){
        super.onRestoreInstanceState(inState);
        //Recuperamos el estadosde las variables:

        System.out.println("onRestore con null bundle");

       if(inState!=null) {
           this.puntuacionPartida = inState.getInt("puntos");
           this.nivel = inState.getInt("nivel");
           this.nVidas = inState.getInt("nVidas");
           this.numVerbo=inState.getInt("numVerbo");


           System.out.println("onRestore: puntos: "+puntuacionPartida+" "+nVidas+" + nVidas");

           //Llamamos a ajustarVista para que coloque las cosas bien.
           this.ajustarVista();

       }

    }

    */


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos devuelve a la pantalla principal perdiendo todos los puntos.
        if(keyCode==KeyEvent.KEYCODE_BACK){
            System.out.println("pulsado boton back");
            //SharedPreferences.Editor editor=prefe.edit();
            //Escribimos 0 en puntos
            //editor.putInt("puntos",0);
            //editor.putInt("vidas",3); //El número de vidas
            //Realizamos la escritura
            //editor.commit();
            //finish();


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


    // ## LÓGICA DEL JUEGO ## //
    public void crearJugada(){

        boolean pasa;

        if(numVerbo==0)
            pasa=true;
        else
            pasa=false;

        System.out.println("#################### Vidas: "+nVidas+" puntuacion "+puntuacionPartida+"verboElegido: "+numVerbo);


        String verboInfinitivo="";
        String verboPasado="";
        String verboParticipio="";

        /*
        Para evitar que modificaciones del color de las cajas queden en posteriores jugadas resetamos las tres:
         */
        infinitivo.setBackgroundColor(Color.TRANSPARENT);
        pasado.setBackgroundColor(Color.TRANSPARENT);
        participio.setBackgroundColor(Color.TRANSPARENT);

        //Ajustamos la vista al tipo de nivel:
        this.ajustarVista();

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

            /*
            Si numVerbo==0 querrá decir que ha sido el valor proporcionado por el constructor y no por una restauración de un actividad anterior.
             */
            System.out.println("VERRRRRBO"+numVerbo);
            if(numVerbo==0)
                numVerbo = (int) (rnd.nextDouble() * numVerbosListaSoft + 0);
            //Si no es así querra'decir que se usa el num de verbo guardado.


            //numVerbo=1; //ELIMINAME


            //Cargamos el verbo en las variables locales:
            verboInfinitivo=verbosSoft[numVerbo][0];
            verboPasado=verbosSoft[numVerbo][1];
            verboParticipio=verbosSoft[numVerbo][2];

        }else if(nivel>=6 && nivel<=10) { //Si estamos en los niveles 6-10

                //Elegimos el verbo de forma aleatoria:
                if(numVerbo==0)
                    numVerbo = (int) (rnd.nextDouble() * numVerbosListaMedium + 0);
                //Cargamos el verbo en las variables locales:
                verboInfinitivo=verbosMedium[numVerbo][0];
                verboPasado=verbosMedium[numVerbo][1];
                verboParticipio=verbosMedium[numVerbo][2];

                System.out.println("Elegimos verbo de la lista MEDIUM");

        } else if(nivel>=11 && nivel<=15) { //Si estamos en los niveles 6-10

            //Elegimos el verbo de forma aleatoria:
            if(numVerbo==0)
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

        if(pasa){

            System.out.println("Eligiendo verbo");
        /*
        Sólo generaremos las formas cuando no vengan dadas por ninǵun Bundle estate (lo que significaría que ya se han dado antes de entrar
        en la actividad. Por eso comprobamos para formarlas nosotros que sean iguales a 0, el valor que les setea nuestro constructor.
         */

            if ((nivel >= 1 && nivel <= 3) || (nivel >= 6 && nivel <= 8) || (nivel >= 11 && nivel <= 13)) {
                //Elegimos una forma a preguntar.
                numFormaA = (int) (rnd.nextDouble() * 3 + 0);

            }else if ((nivel >= 4 && nivel <= 5) || (nivel >= 9 && nivel <= 10) || (nivel >= 14 && nivel <= 15)) {
                //Elegimos dos formas a preguntar que no podrán ser nunca la misma.

                numFormaA = (int) (rnd.nextDouble() * 3 + 0);
                if ((int) (rnd.nextDouble() * 2 + 0) == 0)
                    numFormaB = (numFormaA + 1) % 3;
                else {
                    numFormaB = (numFormaA - 1) % 3;
                    if (numFormaB < 0)
                        numFormaB += 3;
                }
            }

            if ((nivel >= 1 && nivel <= 3) || (nivel >= 6 && nivel <= 8) || (nivel >= 11 && nivel <= 13))
                System.out.println("Forma elegida: " + numFormaA);
            else if ((nivel >= 4 && nivel <= 5) || (nivel >= 9 && nivel <= 10) || (nivel >= 14 && nivel <= 15))
                System.out.println("Formas elegidas: " + numFormaA + " y " + numFormaB);


         }

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


        //numVerbo=1;
        } //Fin de crearJugada();


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


    public void toast(String mensaje){
        //Configuramos el mensaje de vida ganada:
        Context context = getApplicationContext();
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);


        toast.show();
    }

    public void perderVida(){

        /*
        //Configuramos el mensaje de vida ganada:
        Context context = getApplicationContext();
        CharSequence text = "Loosed one live!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);


        toast.show();
        */



        if(this.nVidas==3) {
            //Se rompe el corazón 3:
            vida3.setImageResource(R.drawable.corazonmuerto);
            //Se le resta una:
            nVidas--;
            //Llamamos a toast con el mensaje guardad eon strings.xml
            toast(this.getString(R.string.mensajeVidaPerdida));
        }
        //Si le quedan 2 vidas:
        else if(this.nVidas==2) {
            //Se rompe el corazón 2:
            vida2.setImageResource(R.drawable.corazonmuerto);
            //Se le resta una:
            nVidas--;
            toast(this.getString(R.string.mensajeVidaPerdida));
        }
        //Si le queda 1 vidas:
        else if(this.nVidas==1) {
            //Se rompe el corazón 1:
            vida1.setImageResource(R.drawable.corazonmuerto);
            //Se le resta una:
            nVidas--;
            toast(this.getString(R.string.mensajeVidaPerdida));
        }
        //Si no le queda ninguna vida:
        else {
            if (this.nVidas == 0) {


                System.out.println("Entrando en sección de fin de partida");


                finPartida=true;

                //Si no nos quedan vidas se acabó el juego y vamos a la sección de resultados.




                //Vamos a la clase Resultados.class
                Intent intent = new Intent(Juego.this, Resultado.class);

                //Enviamos la puntuación:
                intent.putExtra("puntos", this.puntuacionPartida);

                startActivity(intent);

                //  SystemClock.sleep(5000);

                //Mostramos un mensaje toast! YOU ARE DEAD!
                toast(this.getString(R.string.mensajeMuerte));

                //Finalizamos la hebra que ejecuta esta actividad:
                this.finish();

                Thread.interrupted();
                //super.onDestroy();

                //finish();
                //onDestroy();

                /*
                LinearLayout principal = (LinearLayout)findViewById(R.id.layoutPrincipal);
                principal.setVisibility(View.INVISIBLE);

                viewStub=(ViewStub)findViewById(R.id.viewStub);
                //ViewStub viewInflated = (ViewStub) (findViewById(R.id.viewStub));
                //viewInflated.inflate();
                View viewInflated = viewStub.inflate();
                SystemClock.sleep(5000);
                */

            }
        }


    }

    public void comprobarVerbo(){


        //INFO DE DEPURACIÓN:

        if(nivel!=4 && nivel!=5 && nivel!=9 && nivel!=10 && nivel!=14 && nivel!=15)
            System.out.println("Verbo introducido: "+campoVerboIntroducidoA.getText());
        else{

            //Estamos en los niveles 4, 5, 9, 10, 14 o 15

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




        int seg=Integer.parseInt(timer.getText().toString());

        //  Comprobación de verbos:

        if(nivel>=1 && nivel<=5) {

            if(nivel==1 || nivel==2 || nivel==3) {

              //  if (campoVerboIntroducidoA.getText().toString().equals(verbosSoft[numVerbo][numFormaA])) {
                //Nueva forma de cormprobar:
                if (comprueba(campoVerboIntroducidoA.getText().toString(),verbosSoft[numVerbo][numFormaA] )){
                    /*
                    En el nivel 1 cada acierto suma 1 en el nivel 2 suma 2 y así con el resto de niveles
                     */
                    puntuacionPartida=puntuacionPartida+(nivel*seg);

                } else {
                    //Pierde una vida: ohhh!!
                    perderVida();
                }
            }
            else{

                if(nivel==4 || nivel==5) {
                    if (comprueba(campoVerboIntroducidoA.getText().toString(),verbosSoft[numVerbo][numFormaA]))
                        puntuacionPartida=puntuacionPartida+(nivel*seg);
                    else
                        perderVida();
                    if (comprueba(campoVerboIntroducidoB.getText().toString(),verbosSoft[numVerbo][numFormaB]))
                        puntuacionPartida=puntuacionPartida+(nivel*seg);
                    else
                        perderVida();
                }

            }
        }
        else if(nivel>=6 && nivel<=10) { //Lista Medium

            if(nivel==6 || nivel==7 || nivel==8) {
                if (comprueba(campoVerboIntroducidoA.getText().toString(),verbosMedium[numVerbo][numFormaA])) {
                    puntuacionPartida=puntuacionPartida+(nivel*seg);

                } else {
                    //Pierde una vida: ohhh!!
                    perderVida();
                }
            }
            else{

                if(nivel==9 || nivel==10) {
                    if (comprueba(campoVerboIntroducidoA.getText().toString(),verbosMedium[numVerbo][numFormaA]))
                        puntuacionPartida=puntuacionPartida+(nivel*seg);
                    else
                        perderVida();
                    if (comprueba(campoVerboIntroducidoB.getText().toString(),verbosMedium[numVerbo][numFormaB]))
                        puntuacionPartida=puntuacionPartida+(nivel*seg);
                    else
                        perderVida();
                }

            }

        }else if(nivel>=11 && nivel<=15) {//Lista Hard


            if(nivel==11 || nivel==12 || nivel==13) {
                if (comprueba(campoVerboIntroducidoA.getText().toString(),verbosHard[numVerbo][numFormaA])) {
                    puntuacionPartida=puntuacionPartida+(nivel*seg);

                } else {
                    //Pierde una vida: ohhh!!
                    perderVida();
                }
            }
            else{

                if(nivel==14 || nivel==15) {
                    if (comprueba(campoVerboIntroducidoA.getText().toString(),verbosHard[numVerbo][numFormaA]))
                        puntuacionPartida=puntuacionPartida+(nivel*seg);
                    else
                        perderVida();
                    if (comprueba(campoVerboIntroducidoB.getText().toString(),verbosHard[numVerbo][numFormaB]))
                        puntuacionPartida=puntuacionPartida+(nivel*seg);
                    else
                        perderVida();
                }

            }


        }


        //Ajustamos la puntuación según el tiempo, multiplicandola por los segunods que quedan, así
        //cuando menmos tiempo gaste el usuario más segundos le quedarán y el resultado será mayor.




        System.out.println("Puntucion: " +puntuacionPartida);

        puntos.setText(Integer.toString(puntuacionPartida));
    } //fin comprobarVerbo()


/*
    // CountDownTimer class
    public class MalibuCountDownTimer extends CountDownTimer
    {

        public MalibuCountDownTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {
            timer.setText("Time's up!");
            //timeElapsedView.setText("Time Elapsed: " + String.valueOf(startTime));
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            //Pasamos el tiempo a segundos y lo mostramos.
            timer.setText(" "+millisUntilFinished/1000);
            //timeElapsed = startTime - millisUntilFinished;
           // timeElapsedView.setText("Time Elapsed: " + String.valueOf(timeElapsed));
        }
    }*/
}



