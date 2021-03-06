/*
        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.
        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
        GNU General Public License for more details.
        You should have received a copy of the GNU General Public License
        along with this program. If not, see <http://www.gnu.org/licenses/>.
        Copyright 2015 Jose A. Gonzalez Cervera
        Copyright 2015 Juan A. Fernández Sánchez
*/
package pdm.ivchecker;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import com.google.android.gms.ads.*;

public class Juego extends ActionBarActivity {

    //Variable del fragment:
    String id = "IdQueNecesitaMyFragment";

    //Variable necesaria para el paso de datos al fragment (necesario que sea global)
    Bundle arguments = new Bundle();

    //ArrayList donde se almacenan los verbos:
    private ArrayList<Verbo> verbosSoft;
    private ArrayList<Verbo> verbosMedium;
    private ArrayList<Verbo> verbosHard;

    //Elementos de la vista:
    private Button btnNext;
    //private EditText campoVerboIntroducidoA, campoVerboIntroducidoB;

    private TextView puntos, textNivel;


    //ELEMENTOS CON LOS QUE SE JUGARÁ DURANTE TODO EL JUEGO.

        //Layouts:
        private LinearLayout layoutInfinitivo, layoutPasado, layoutParticipio;



    //    Vamos a probar a ir creándolos dinámicamente conforme los vayamos necesitando.

        //textViews:
        private TextView textViewInfinitivo, textViewPasado, textViewParticipio;

        //editTexts:
        private EditText editTextInfinitivo, editTextPasado, editTextParticipio;



    private ImageView vida1, vida2, vida3; // vida4, vida5;


    private RelativeLayout layoutPrincipal;

    //Para la aleatoreidad de las preguntas:
    private Random rnd;

    //Número de verbos de las listas
    private int numVerbosListaSoft, numVerbosListaMedium, numVerbosListaHard;

    //Respecto a los datos del jugador:
    private int nVidas; //Número de vidas del jugador.
    private int puntuacionPartida; //Puntuación del jugador.

    //Respecto a la lógica del juego:
    private int numVerbo, numFormaA, numFormaB, numLetrasForma; //Variables para crear la jugada
    private String formaMisteriosa; //Forma que el usuario debe introducir.
    private int nivel; //Nivel por el que el usuario va.
    private int jugadaEnNivel; //Jugada (de 15) por la que va dentro del nivel.

    private boolean finPartida=false;
    private boolean restauracion=false;

    //Variables Utiles para el temporizador
    private TextView timer;
    private int time;
    private CountDownTimer countDownTimer;


    private int lugar;
    private int  formaVisible;

    private boolean nivelesConDosEditText=false;

    //Constructor:
    public Juego(){

        time=30;

        if(!restauracion) {
            numVerbosListaSoft = numVerbosListaMedium = numVerbosListaHard = 0;
            formaMisteriosa = "";
            //La partida comienza con la puntación a 0
            puntuacionPartida = 0;
            //La partida comienza con tres vidas
            nVidas = 3;

            //La partida comienza en el nivel 1
            nivel = 1;

            int defecto = 0;

            //El número de jugada por nivel empieza en 1
            jugadaEnNivel = 1;


            //Inicializamos  el objeto de tipo Random().
            rnd = new Random();
            numVerbo=0;
            numFormaA=0;
            numFormaB=0;
            lugar=0;
        }
    }


    /**
     * Función que carga en los arrays los verbos desde los .csv
     */
    private void cargarVerbos(){

        InputStream inputStream;

        //Obtenemos el flujo del fichero donde se almacenan los verbos de la lista SOFT
        inputStream=getResources().openRawResource(R.raw.ivsoft2);
        //Extreamos todos los verbos del fichero con una función extrena y los almacenamos en nuestro array.
        verbosSoft = ProcesadorCSVs.obtenerVerbos(inputStream);

        //Idénticamente con la lista de dificultad media
        inputStream=getResources().openRawResource(R.raw.ivmedium2);
        verbosMedium = ProcesadorCSVs.obtenerVerbos(inputStream);

        //Idénticamente con la lista de dificultad alta
        inputStream=getResources().openRawResource(R.raw.ivhard2);
        verbosHard = ProcesadorCSVs.obtenerVerbos(inputStream);


    }


    private void referenciaObjetosDeLaVista(){
        /* ## Referencias a los elementos del layout (vista) ## */

        //Obtenemos la referencia al botón de la vista "Next"
        btnNext=(Button)findViewById(R.id.nextButton);


        /*Los tres elementos principales con los que se interactua durante toda la partida son
         los tres layout donde irán insertados editText o textView.*/

        layoutInfinitivo=(LinearLayout)findViewById(R.id.layoutInfinitivo);
        layoutPasado=(LinearLayout)findViewById(R.id.layoutPasado);
        layoutParticipio=(LinearLayout)findViewById(R.id.layoutParticipio);


        //Referencias a los layouts de las cajas de texto EditText:
        //layoutCampoVerboIntroducidoA=(LinearLayout)findViewById(R.id.LinearLayoutA);
        //layoutCampoVerboIntroducidoB=(LinearLayout)findViewById(R.id.LinearLayoutB);


        layoutPrincipal = (RelativeLayout) findViewById(R.id.layoutActivity);



      //  infinitivo=(TextView)findViewById(R.id.infinitivo);
      //  pasado=(TextView)findViewById(R.id.pasado);
      //  participio=(TextView)findViewById(R.id.participio);






        //Al campo de los puntos
        puntos=(TextView)findViewById(R.id.viewPuntos);

        //El texto del nivel:
        textNivel=(TextView)findViewById(R.id.textLevel);


        //Setemos el textView de nivel para que aparezca el nivel en el que estamos jugando:
        textNivel.setText(getString(R.string.nivel)+" "+nivel);

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

        //Ajustamos los elementos de la vista (útil también cuando giramos la pantalla)

        //Ajustamos el número de vidas
        this.ajustarVidas(nVidas);

        this.textNivel.setText(getString(R.string.nivel)+" "+Integer.toString(this.nivel));
        this.puntos.setText(Integer.toString(this.puntuacionPartida));


        //Ajuste de fondo:
        String juegoFondo="juegofondo";
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Cuando la pantalla se gira cargamos un backgroud que se ajuste más:
            juegoFondo+="landscape";
        }
        try {
            Field field = R.drawable.class.getField(juegoFondo+nivel);
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
       //     campoVerboIntroducidoB.setVisibility(TextView.INVISIBLE);
            //layoutCampoVerboIntroducidoB.setVisibility(LinearLayout.INVISIBLE);
            //layoutCampoVerboIntroducidoA.setBackgroundColor(Color.TRANSPARENT);
        }else{
            //Si estamos en los verbos, 4, 5, 9, 10. 14 o 15:
            //layoutCampoVerboIntroducidoA.setBackgroundColor(Color.rgb(49,193,255));
            //campoVerboIntroducidoB.setVisibility(TextView.VISIBLE);
            //layoutCampoVerboIntroducidoB.setVisibility(LinearLayout.VISIBLE);
        }
    }

    /**
     * Comprobación del texto introducido comprobando coincidencias entre el verbo correspondiente,
     * útil sobretodo cuando el verbo es compuesto, pues se descompone el verbo y se comprueba con los dos.
     * @param introducido Verbo introducido por el usuario.
     * @param original Verbo original que puede ser compuesto (dividido entre "/") o no.
     * @return
     */
    private boolean comprueba(String introducido, String original){

        /*Esta función comprueba que el verbo introducido corresponda con el original.
        Construimos esta función poque el verbo original puede ser que esté compuesto por dos partes,
        la forma para el singular y para el plural por eso si se da este caso tenemos que separar el contenido
        y comprobar que coincida con uno de los dos.
         */

        System.out.println("A: "+introducido+" B: "+original);

        String descompuestoA="", descompuestoB="";


        if(original.contains("/")){ //Se trata de un verbo compuesto.

            //1º. Se descompone el verbo.
            int posBarra = original.indexOf("/");
            descompuestoA=original.substring(0, posBarra);
            descompuestoB=original.substring(posBarra+1, original.length());

            //2º. Se comprueba con cada una de las formas
            if(introducido.equals(descompuestoA) || introducido.equals(descompuestoB))
                return true;
            else
                return false;

        }else{//No se trata de un verbo compuesto
            if(introducido.equals(original))
                return true;
            else
                return false;
        }

    }

    @Override
    //Método llamada cuando se crea por primera vez la actividad.
    protected void onCreate(Bundle inState) {
        //Llamamos al constructor del padre:
            super.onCreate(inState);

        //Relacionamos esta actividad con el layout (vista) correspondiente:
            setContentView(R.layout.activity_juego);


        //Ajustamos la caracteristicas visuales de esta actividad
            //Para que no se muestre la ActionBar.
            getSupportActionBar().hide();
            //Para que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);




        //Ajustes generales de los elementos que se moverán por los layout de la vista.

        int tamTexto=50;



        editTextInfinitivo = new EditText(this);
        editTextInfinitivo.setTextSize(tamTexto);
        editTextInfinitivo.setTextColor(Color.WHITE);

        editTextInfinitivo.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        editTextInfinitivo.setBackgroundColor(Color.TRANSPARENT);

        /**
         * Programacion de la respuesta al evento "hacer click" dentro del campo de texto.
         */
        editTextInfinitivo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {

                        //Borramos el contenido escribiendo "" = nada.
                        editTextInfinitivo.setText("");

                    }
                }
        );
        // Para cerrar el teclado al pulsar intro
        editTextInfinitivo.setOnKeyListener(new View.OnKeyListener() {
            /**
             * This listens for the user to press the enter button on
             * the keyboard and then hides the virtual keyboard
             */
            public boolean onKey(View arg0, int arg1, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (arg1 == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextInfinitivo.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        //Programación del evento que escucha los cambios de focos sobre el EditText
        editTextInfinitivo.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                        /*Codigo que ejecutamos cuando el editText pierde el foco, también se puede
                        programar que hacer para cuando lo tiene con if(hasFocus){...}.
                        Entonces lo que hacemos cuando lo pierde es simplemente devolvérselo, para evitar
                        que el cursor no aparezca y que el usuario tenga que clicar dos veces sobre el
                        campo de texto para escribir.
                        */

                    if(!nivelesConDosEditText)
                        editTextInfinitivo.requestFocus();
                }
            }
        });




        editTextPasado = new EditText(this);
        editTextPasado.setTextSize(tamTexto);
        editTextPasado.setTextColor(Color.WHITE);
        editTextPasado.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        editTextPasado.setBackgroundColor(Color.TRANSPARENT);
        /**
         * Programacion de la respuesta al evento "hacer click" dentro del campo de texto.
         */
        editTextPasado.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {

                        //Borramos el contenido escribiendo "" = nada.
                        editTextPasado.setText("");

                    }
                }
        );
        //Programación del evento que escucha los cambios de focos sobre el EditText
        editTextPasado.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!nivelesConDosEditText)
                        editTextPasado.requestFocus();
                }
            }
        });
        // Para cerrar el teclado al pulsar intro
        editTextPasado.setOnKeyListener(new View.OnKeyListener() {
            /**
             * This listens for the user to press the enter button on
             * the keyboard and then hides the virtual keyboard
             */
            public boolean onKey(View arg0, int arg1, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (arg1 == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextPasado.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });



        editTextParticipio = new EditText(this);
        editTextParticipio.setTextSize(tamTexto);
        editTextParticipio.setTextColor(Color.WHITE);
        editTextParticipio.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        editTextParticipio.setBackgroundColor(Color.TRANSPARENT);

        /**
         * Programacion de la respuesta al evento "hacer click" dentro del campo de texto.
         */
        editTextParticipio.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {

                        //Borramos el contenido escribiendo "" = nada.
                        editTextParticipio.setText("");

                    }
                }
        );
        //Programación del evento que escucha los cambios de focos sobre el EditText
        editTextParticipio.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!nivelesConDosEditText)
                        editTextParticipio.requestFocus();
                }
            }
        });
        // Para cerrar el teclado al pulsar intro
        editTextParticipio.setOnKeyListener(new View.OnKeyListener() {
            /**
             * This listens for the user to press the enter button on
             * the keyboard and then hides the virtual keyboard
             */
            public boolean onKey(View arg0, int arg1, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (arg1 == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextParticipio.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        //Modificamos el cursor para los tres editText
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editTextParticipio, R.drawable.cursor);
            f.set(editTextPasado, R.drawable.cursor);
            f.set(editTextInfinitivo, R.drawable.cursor);
        } catch (Exception ignored) {}






        textViewInfinitivo = new TextView(this);
        textViewInfinitivo.setTextSize(tamTexto);
        textViewInfinitivo.setTextColor(Color.WHITE);

        textViewPasado = new TextView(this);
        textViewPasado.setTextSize(tamTexto);
        textViewPasado.setTextColor(Color.WHITE);

        textViewParticipio = new TextView(this);
        textViewParticipio.setTextSize(tamTexto);
        textViewParticipio.setTextColor(Color.WHITE);




        if(inState!=null) {

            //Recuperamos el estado de la actividad en el caso de que este existiera y pasase el condicional

            this.puntuacionPartida = inState.getInt("puntos");
            this.nivel = inState.getInt("nivel");
            this.nVidas = inState.getInt("nVidas");
            this.numVerbo=inState.getInt("numVerbo");
            this.numFormaA=inState.getInt("numFormaA");
            this.numFormaB=inState.getInt("numFormaB");
            this.time=inState.getInt("time");

        }else{
            //Sólo cuando no se recupere ningún estado significará que empieza el juego desde el principio y que no se ha rotado la pantalla al
            // no haber recursos guardados en el Bundle
            runFragment(1);
        }

        //Referenciamos todos los objetos de la vista para poder controlarlos:
            referenciaObjetosDeLaVista();

            ajustarVista();

            //Publicidad:  AdSense

                // Buscamos el  AdView como recurso y cargamos la  solicitud.
                AdView adView = (AdView)this.findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //Cualquier emulador
                        .addTestDevice("9011F9E7CEC921EF1BB8A17A36B24813") //El telefono de desarrollo
                        .build();
                adView.loadAd(adRequest);

            //Fin publicidad

        //Cargamos los verbos desde los ficheros csv

            cargarVerbos();

        //Cuando se llega al nivel 4 y 9 se gana una vida.
        if(nivel==4 || nivel==9 )
            ganarVida();


        /*

        // ### GESTIÓN DE COMPORTAMIENTO ### ///

        // Para cerrar el teclado al pulsar intro
        campoVerboIntroducidoA.setOnKeyListener(new View.OnKeyListener() {

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
        */

        /*
        // Para cerrar el teclado al pulsar intro
        campoVerboIntroducidoB.setOnKeyListener(new View.OnKeyListener() {
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
        */

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
                            //campoVerboIntroducidoA.setText("");
                            //campoVerboIntroducidoB.setText("");

                            //Aumentamos el número de la jugada en el nivel (10 verbos por nivel)
                            jugadaEnNivel++;

                            numVerbo=0;


                            //Si hemos completado las quince jugadas por nivel pasamos de nivel.

                            if (jugadaEnNivel > 15) {
                                nivel++; //Pasamos de nivel
                                jugadaEnNivel = 1; //Reiniciamos.

                                //if(!finPartida)
                                if(nivel<=15)
                                    runFragment(nivel);

                                System.out.println("NIVEL: "+nivel);

                                //jugar();

                            }

                            //Si se han completado todos los niveles (se ha acabado el juego) vamos a una pantalla de resultados:.
                            if (nivel > 15) {

                                //Ejecutar este fragmente hace que se muestre y después se finalice la partida.
                                if(!finPartida) {
                                    runFragmentSucess();


                                }

                                //Después llamamos a mostrar la puntuación.





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

                    }
                }



        ); //Fin del manejador del botón next.


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


    /**
     * Lanza un fragmen en una hebra concurrente que muestra el nivel por el que el usuario va.
     * @param level Nivel que necesita el fragment para configurar los tres mensajes mostrados.
     */
    private void runFragment(int level) {

        //Enviamos los datos al fragment a trabés de un objeto Bundle
        arguments.putString("id", Integer.toString(level));

        //Declaramos una nueva hebra
        new Thread() {
            //Le decimos lo que queremos que haga:
            public void run() {

                MyDialogFragment frag;

                FragmentTransaction ft = getFragmentManager().beginTransaction();

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


    private void runFragmentSucess() {




        //Declaramos una nueva hebra
        new Thread() {
            //Le decimos lo que queremos que haga:
            public void run() {

                MyDialogFragmentSucess frag = new MyDialogFragmentSucess();

                FragmentTransaction ft = getFragmentManager().beginTransaction();


                frag.show(ft, "txn_tag");

                try {
                    Thread.sleep(5000);

                    //Después creamos el intent que nos llevará a la activity resultado.

                    //Creamos el intent:
                    Intent intent = new Intent(Juego.this, Resultado.class);
                    intent.putExtra("puntos", puntuacionPartida);

                    //Nos vamos al activity resultados:
                    startActivity(intent);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                frag.dismiss();




            }
        }.start();


    }

    /**
     * Función que crea la jugada
     */
    private void jugar(){

        //Ajustamos nivel:
        this.textNivel.setText(getString(R.string.nivel)+Integer.toString(nivel));

        this.crearJugada(); //Después de cargar los datos comienza el juego:
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos devuelve a la pantalla principal perdiendo todos los puntos.
        if(keyCode==KeyEvent.KEYCODE_BACK){

            //Al pulsar el botón back en el juego volvemos a la actividad principal (pantalla principal de la app)
            Intent intent = new Intent(Juego.this, ActividadPrincipal.class);
            startActivity(intent);

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }

    /**
     * Lógica del juego: función que crea la jugada al completo, inclusive la modificación del aspecto.
     */
    public void crearJugada(){

        boolean pasa;

        if(numVerbo==0)
            pasa=true;
        else
            pasa=false;

        String verboInfinitivo="";
        String verboPasado="";
        String verboParticipio="";

        //Limpiamos todos los elementos que conforman el diseño:
        layoutInfinitivo.removeAllViews();
        layoutPasado.removeAllViews();
        layoutParticipio.removeAllViews();





        /*
        Para evitar que modificaciones del color de las cajas queden en posteriores jugadas resetamos las tres:
         */
      //  infinitivo.setBackgroundColor(Color.TRANSPARENT);
      //  pasado.setBackgroundColor(Color.TRANSPARENT);
      //  participio.setBackgroundColor(Color.TRANSPARENT);

        //Ajustamos la vista al tipo de nivel:
        this.ajustarVista();

        /* 1º
           GENERAMOS EL VERBO A PREGUNTAR según lo que mida la lista de verbos que dependerá del nivel en el que estemos.:
           el número "numVerbosLista" se coge desde el propio fichero que se carga.
           **Mejora: que no pueda volver a preguntar por el mismo verbo.
        */

        //Ademas lo cargarmos:


        if(nivel>=1 && nivel<=5) { //Si estamos en los niveles 1-5

            //Elegimos el verbo de forma aleatoria:

            /*
            Si numVerbo==0 querrá decir que ha sido el valor proporcionado por el constructor y no por una restauración de un actividad anterior.
             */
            if(numVerbo==0)
                //numVerbo = (int) (rnd.nextDouble() * numVerbosListaSoft + 0);
                numVerbo = (int) (rnd.nextDouble() * verbosSoft.size() + 0);
            //Si no es así querra'decir que se usa el num de verbo guardado.


            //Cargamos el verbo en las variables locales:
            verboInfinitivo=verbosSoft.get(numVerbo).getVerbo().infinitivo;
            //verboInfinitivo=verbosSoft[numVerbo][0];
            verboPasado=verbosSoft.get(numVerbo).getVerbo().pasado;
            //verboPasado=verbosSoft[numVerbo][1];
            verboParticipio=verbosSoft.get(numVerbo).getVerbo().participio;
            //verboParticipio=verbosSoft[numVerbo][2];

        }else if(nivel>=6 && nivel<=10) { //Si estamos en los niveles 6-10

                //Elegimos el verbo de forma aleatoria:
                if(numVerbo==0)
                    //numVerbo = (int) (rnd.nextDouble() * numVerbosListaMedium + 0);
                    numVerbo = (int) (rnd.nextDouble() * verbosMedium.size() + 0);
                //Cargamos el verbo en las variables locales:
                verboInfinitivo=verbosMedium.get(numVerbo).getVerbo().infinitivo;
                //verboInfinitivo=verbosMedium[numVerbo][0];
                verboPasado=verbosMedium.get(numVerbo).getVerbo().pasado;
                //verboPasado=verbosMedium[numVerbo][1];
                verboParticipio=verbosMedium.get(numVerbo).getVerbo().participio;
                //verboParticipio=verbosMedium[numVerbo][2];

        } else if(nivel>=11 && nivel<=15) { //Si estamos en los niveles 6-10

            //Elegimos el verbo de forma aleatoria:
            if(numVerbo==0)
                numVerbo = (int) (rnd.nextDouble() * verbosHard.size() + 0);
                //numVerbo = (int) (rnd.nextDouble() * numVerbosListaHard + 0);
            //Cargamos el verbo en las variables locales:
            verboInfinitivo=verbosHard.get(numVerbo).getVerbo().infinitivo;
           // verboInfinitivo=verbosHard[numVerbo][0];
            verboPasado=verbosHard.get(numVerbo).getVerbo().pasado;
            //verboPasado=verbosHard[numVerbo][1];
            verboParticipio=verbosHard.get(numVerbo).getVerbo().participio;
            //verboParticipio=verbosHard[numVerbo][2];


        }


        //Grabamos los verbos seleccionados en un vector para su mejor manejo
        String verbos[]={verboInfinitivo,verboPasado,verboParticipio};


        //Vamos ajustar un poco el tamaño de letra para cuando sean verbos largos o compuestos
        if(verboInfinitivo.length()>10)
            textViewInfinitivo.setTextSize(30);
        if(verboPasado.length()>10)
            textViewPasado.setTextSize(30);
        if(verboParticipio.length()>10)
            textViewParticipio.setTextSize(30);

        //2º GENERAMOS LA FORMA O FORMAS dependiendo del nivel que no aparecerán (por la que preguntaremos)

        if(pasa){

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
            /*
            if ((nivel >= 1 && nivel <= 3) || (nivel >= 6 && nivel <= 8) || (nivel >= 11 && nivel <= 13))
                System.out.println("Forma elegida: " + numFormaA);
            else if ((nivel >= 4 && nivel <= 5) || (nivel >= 9 && nivel <= 10) || (nivel >= 14 && nivel <= 15))
                System.out.println("Formas elegidas: " + numFormaA + " y " + numFormaB);
            */

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
                formaMisteriosa="?";



        //4º Seteamos la información en los textView de pantalla.


            //Si se trata del nivel 1,2, 6,7, 11 o 12 la forma misteriosa se pasa directamente:
            if(nivel==1 || nivel==2 || nivel==6 || nivel==7 || nivel==11 || nivel==12) {

                //Si la forma a preguntar al usuario es el INFINITIVO;
                if (numFormaA == 0) {

                    //Metemos la forma misteriosa dentro del editText que se colocará en el layout del infinitivo.

                    editTextInfinitivo.setText(formaMisteriosa); //formaMisteriosa es la forma en  _ _ _
                    layoutInfinitivo.addView(editTextInfinitivo);

                    //infinitivo.setText(formaMisteriosa);

                //Si la forma no es el INFINITIVO, entonces este hay que mostrarlo en el layout para INFINITIVO:
                }else{

                    //Modificamos el textView del infinitivo y lo añadimos al layout.
                    textViewInfinitivo.setText(verboInfinitivo);
                    layoutInfinitivo.addView(textViewInfinitivo);

                    //Da igual en el nivel que estemos porque se ha cargado el verbo correspondiente al nivel antes.
                    //infinitivo.setText(verboInfinitivo);
                }

                //Si la forma a preguntar al usuario es el PASADO
                if (numFormaA == 1){
                    editTextPasado.setText(formaMisteriosa); //formaMisteriosa es la forma en  _ _ _
                    layoutPasado.addView(editTextPasado);
                    //pasado.setText(formaMisteriosa);
                }else{
                        textViewPasado.setText(verboPasado);
                        layoutPasado.addView(textViewPasado);
                        //pasado.setText(verboPasado);
                }

                //Si la forma a preguntar al usuario es el PARTICIPIO
                if (numFormaA == 2) {
                    editTextParticipio.setText(formaMisteriosa); //formaMisteriosa es la forma en  _ _ _
                    layoutParticipio.addView(editTextParticipio);
                    //participio.setText(formaMisteriosa);
                }else{
                    textViewParticipio.setText(verboParticipio);
                    layoutParticipio.addView(textViewParticipio);
                    //participio.setText(verboParticipio);
                }

                //Después la forma misteriosa vuelve a estar vacía
                formaMisteriosa = "";


            }else

            //Entonces se colocan las tres formas verbales de forma no ordenada donde sigue habiendo una sin pista
            if(nivel==3 || nivel==8 || nivel==13) {



                //El lugar donde poner la forma misteriosa
                lugar = (int) (rnd.nextDouble() * 3 + 0); //(0 - 1- 2)

                /*
                Si el lugar decidido para poner la forma misteriosa es 0 ahí se coloca la forma misteriosa.
                 */
                if(lugar==0) { //Lugar = 0 -> en el infinitivo se pone la forma misteriosa.


                    editTextInfinitivo.setText(formaMisteriosa); //formaMisteriosa es la forma en  _ _ _
                    layoutInfinitivo.addView(editTextInfinitivo);
                    //Colocamos la forma misteriosa:
                 //   infinitivo.setText(formaMisteriosa);

                    //Quedan dos huecos:

                    if( (int)(rnd.nextDouble()*2+0) == 0  ) { //Cara o cruz:
                        //Cara
                         textViewPasado.setText(verbos[(numFormaA+1)%3]);
                         layoutPasado.addView(textViewPasado);

                         textViewParticipio.setText(verbos[(numFormaA+2)%3]);
                         layoutParticipio.addView(textViewParticipio);

                   //     pasado.setText(verbos[(numFormaA+1)%3]);
                     //   participio.setText(verbos[(numFormaA+2)%3]);
                    }else {


                        textViewParticipio.setText(verbos[(numFormaA+1)%3]);
                        layoutParticipio.addView(textViewParticipio);

                        textViewPasado.setText(verbos[(numFormaA+2)%3]);
                        layoutPasado.addView(textViewPasado);

                 //       participio.setText(verbos[(numFormaA+1)%3]);
                   //     pasado.setText(verbos[(numFormaA+2)%3]);
                    }


                }
                if(lugar==1){

                    editTextPasado.setText(formaMisteriosa); //formaMisteriosa es la forma en  ?
                    layoutPasado.addView(editTextPasado);

                 //   pasado.setText(formaMisteriosa);


                    //Quedan dos huecos;
                    if( (int)(rnd.nextDouble()*2+0) == 0  ) {


                        textViewInfinitivo.setText(verbos[(numFormaA+1)%3]);
                        layoutInfinitivo.addView(textViewInfinitivo);

                        textViewParticipio.setText(verbos[(numFormaA+2)%3]);
                        layoutParticipio.addView(textViewParticipio);



                        //Cara
                  //      infinitivo.setText(verbos[(numFormaA+1)%3]);
                  //      participio.setText(verbos[(numFormaA+2)%3]);
                    }else {


                        textViewParticipio.setText(verbos[(numFormaA+1)%3]);
                        layoutParticipio.addView(textViewParticipio);

                        textViewInfinitivo.setText(verbos[(numFormaA+2)%3]);
                        layoutInfinitivo.addView(textViewInfinitivo);

                  //      participio.setText(verbos[(numFormaA+1)%3]);
                  //      infinitivo.setText(verbos[(numFormaA+2)%3]);
                    }

                }
                if(lugar==2){

                    editTextParticipio.setText(formaMisteriosa); //formaMisteriosa es la forma en  _ _ _
                    layoutParticipio.addView(editTextParticipio);
                 //   participio.setText(formaMisteriosa);

                    //Quedan dos huecos;
                    if( (int)(rnd.nextDouble()*2+0) == 0  ) {
                        textViewInfinitivo.setText(verbos[(numFormaA+1)%3]);
                        layoutInfinitivo.addView(textViewInfinitivo);

                        textViewPasado.setText(verbos[(numFormaA+2)%3]);
                        layoutPasado.addView(textViewPasado);

                        //Cara
                   //     infinitivo.setText(verbos[(numFormaA+1)%3]);
                   //     pasado.setText(verbos[(numFormaA + 2) % 3]);

                    }else {
                        textViewPasado.setText(verbos[(numFormaA+1)%3]);
                        layoutPasado.addView(textViewPasado);

                        textViewInfinitivo.setText(verbos[(numFormaA+2)%3]);
                        layoutInfinitivo.addView(textViewInfinitivo);
                        //Cruz
                    //    pasado.setText(verbos[(numFormaA+1)%3]);
                    //    infinitivo.setText(verbos[(numFormaA+2)%3]);
                    }

                }
                formaMisteriosa = "";
            }else{
                /*
                Si se trata del nivel 4 se marcar de colores los cuadros donde irán las formas y no se introduce nada:
                 */
                if(nivel==4 || nivel==9 || nivel==14){

                    /*
                    En el nivel 4 se trata de colocar visible sólo una de las formas del verbo para que el usuario
                    tenga que introducir las otras dos. Entonces ponemos dos editText según hayan salida numFormaA y
                    numFormaB y en el que quede es donde ponemos el textView para que se vea la forma que se puede ver.
                    En estos niveles no hay lineas que indiquen la longitud del verbo, sólo el signo de interrogación.
                     */

                    formaMisteriosa="?";
                    switch(numFormaA){

                        case 0:
                            editTextInfinitivo.setText(formaMisteriosa);
                            layoutInfinitivo.addView(editTextInfinitivo);
                        break;

                        case 1:
                            editTextPasado.setText(formaMisteriosa);
                            layoutPasado.addView(editTextPasado);
                        break;

                        case 2:
                            editTextParticipio.setText(formaMisteriosa);
                            layoutParticipio.addView(editTextParticipio);
                        break;

                    }

                    switch(numFormaB){

                        case 0:
                            editTextInfinitivo.setText(formaMisteriosa);
                            layoutInfinitivo.addView(editTextInfinitivo);
                            break;

                        case 1:
                            editTextPasado.setText(formaMisteriosa);
                            layoutPasado.addView(editTextPasado);
                            break;

                        case 2:
                            editTextParticipio.setText(formaMisteriosa);
                            layoutParticipio.addView(editTextParticipio);
                            break;

                    }

                    //Colocamos el textView para ver la forma que el jugador tiene de referencia en el lugar que haya
                    //quedado


                    formaVisible=3-(numFormaA+numFormaB);
                    //System.out.println("numFormaA: "+numFormaA);
                    //System.out.println("numFormaB: "+numFormaB);
                    //System.out.println("Forma visible"+formaVisible);
                    switch(formaVisible){

                        case 0:
                            textViewInfinitivo.setText(verboInfinitivo);
                            layoutInfinitivo.addView(textViewInfinitivo);
                        break;

                        case 1:
                            textViewPasado.setText(verboPasado);
                            layoutPasado.addView(textViewPasado);
                        break;

                        case 2:
                            textViewParticipio.setText(verboParticipio);
                            layoutParticipio.addView(textViewParticipio);
                        break;

                    }




                }else if(nivel==5 || nivel==10 || nivel==15){

                    /*
                    Intento de solucionar que cada EditText quiera el foco de uso.
                    Todo esto se solucionaría con un teclado propio.
                     */
                    nivelesConDosEditText=true;

                    //La forma verbal de las tres que será la visible.
                    formaVisible=3-(numFormaA+numFormaB);

                    //El lugar donde poner la forma visible
                    lugar = (int) (rnd.nextDouble() * 3 + 0); //(0 - 1- 2)

                    System.out.println("CREADA lugar"+lugar+" forma visible"+formaVisible);

                    if(lugar==0){ //Si la forma visible se coloca en hueco del INFINITIVO

                        //Dependiendo de la forma verbal que se vaya a mostrar se mete un texto u otro en ##ESE HUECO##.
                        if(formaVisible==0) {
                            textViewInfinitivo.setText(verboInfinitivo);
                            layoutInfinitivo.addView(textViewInfinitivo);
                            //infinitivo.setText(verboInfinitivo);
                        }
                        if(formaVisible==1){
                            textViewInfinitivo.setText(verboPasado);
                            layoutInfinitivo.addView(textViewInfinitivo);
                            //infinitivo.setText(verboPasado);
                        }

                        if(formaVisible==2){
                            textViewInfinitivo.setText(verboParticipio);
                            layoutInfinitivo.addView(textViewInfinitivo);
                            //infinitivo.setText(verboParticipio);
                        }

                    //seguimos en lugar==0

                        //Quedan dos huecos, en los que hay que poner dos INTERROGACIONES, son editTEXT
                        editTextPasado.setText("?");
                        layoutPasado.addView(editTextPasado);

                        editTextParticipio.setText("?");
                        layoutParticipio.addView(editTextParticipio);


                    }


                    if(lugar==1){//Si la forma visible se coloca en el hueco del pasado

                        if(formaVisible==0){
                            textViewPasado.setText(verboInfinitivo);
                            layoutPasado.addView(textViewPasado);
                        }
                            //pasado.setText(verboInfinitivo);
                        if(formaVisible==1) {
                            textViewPasado.setText(verboPasado);
                            layoutPasado.addView(textViewPasado);
                           // pasado.setText(verboPasado);
                        }
                        if(formaVisible==2){
                            textViewPasado.setText(verboParticipio);
                            layoutPasado.addView(textViewPasado);
                        }
                           // pasado.setText(verboParticipio);


                        //Quedan dos huecos:

                        //Quedan dos huecos, en los que hay que poner dos INTERROGACIONES!!
                        editTextInfinitivo.setText("?");
                        layoutInfinitivo.addView(editTextInfinitivo);

                        editTextParticipio.setText("?");
                        layoutParticipio.addView(editTextParticipio);



                    }

                    if(lugar==2){

                        if(formaVisible==0){
                            textViewParticipio.setText(verboInfinitivo);
                            layoutParticipio.addView(textViewParticipio);
                        }
                            //participio.setText(verboInfinitivo);
                        if(formaVisible==1){
                            textViewParticipio.setText(verboPasado);
                            layoutParticipio.addView(textViewParticipio);
                        }
                            //participio.setText(verboPasado);
                        if(formaVisible==2){
                            textViewParticipio.setText(verboParticipio);
                            layoutParticipio.addView(textViewParticipio);
                        }
                            //participio.setText(verboParticipio);



                        //Quedan dos huecos, en los que hay que poner dos INTERROGACIONES!!
                        editTextInfinitivo.setText("?");
                        layoutInfinitivo.addView(editTextInfinitivo);

                        editTextPasado.setText("?");
                        layoutPasado.addView(editTextPasado);


                    }



                }//Fin nivel 5,10,15


            }


        //numVerbo=1;
        } //Fin de crearJugada();

    /**
     * Implementa el aumento de una vida para el jugador.
     */
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

    /**
     * Ajusta los icónos de los corazones en la vista según el número de vidas
     * @param numero Número de vidas que el usuario tiene y que se usa para ajustar los iconos.
     */
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

    /**
     * Para simplificar el mostrado de mensajes de tipo toast.
     * @param mensaje String a mosrar en el toast.
     */
    public void toast(String mensaje){
        //Configuramos el mensaje de vida ganada:
        Context context = getApplicationContext();
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);


        toast.show();
    }

    /**
     * Implementa la pérdida de una vida por parte del usuario
     */
    public void perderVida(){

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

            }
        }


    }

    /**
     * Para realizar la comprobación del verbo
     */
    public void comprobarVerbo(){


        //INFO DE DEPURACIÓN: no eliminar, crítica en el caso de depuración posterior.

        /*
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
        */
        //Fin de depuración


        //Agrupamos las listas en un vector para poder manejarlas mejor dinamicamente.
        ArrayList<Verbo> [] grupo = new ArrayList[3];
        grupo[0]=verbosSoft;
        grupo[1]=verbosMedium;
        grupo[2]=verbosHard;

        int eleccion=0;



        int seg=Integer.parseInt(timer.getText().toString());


        //  Comprobación de verbos:

        if(nivel>=1 && nivel<=5) //Se usa la lista simple.
            eleccion=0;
        if(nivel>=6 && nivel<=10)//Se usa la lista media.
            eleccion=1;
        if(nivel>=10 && nivel <=15)//Se usa la lista hard.
            eleccion=2;


            if(nivel==1 || nivel==2 || nivel==6 || nivel==7 || nivel==11 || nivel==12) {

              //  if (campoVerboIntroducidoA.getText().toString().equals(verbosSoft[numVerbo][numFormaA])) {
                //Nueva forma de cormprobar:

                //Usamos numFormaA porque sólo vamos a comprobar una de las tres formas del verbo almacenada ahí.
                switch (numFormaA) {

                    case 0:
                        if (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextInfinitivo.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA));
                    break;

                    case 1:
                        if (comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextPasado.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA));
                    break;

                    case 2:
                        if (comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextParticipio.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA));
                    break;

                }

            }


            if(nivel==3 || nivel==8 || nivel==13){

                //Así comprobamos en el lugar donde se ha puesto el editText a rellenar por el suario con la forma elegida, que sigue pudiendo ser cualquiera.
                switch (lugar) {

                    case 0:
                        if (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextInfinitivo.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA));
                        break;

                    case 1:
                        if (comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextPasado.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA));
                        break;

                    case 2:
                        if (comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextParticipio.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA));
                        break;

                }


            }
            if(nivel==4 || nivel==9 || nivel==14){

                //Tenemos que comprobar que los dos verbos introducidos por el usuario estén bien.

                //Comprobamos la forma A
                switch (numFormaA) {

                    case 0:
                        if (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextInfinitivo.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA));
                        break;

                    case 1:
                        if (comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextPasado.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA));
                        break;

                    case 2:
                        if (comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextParticipio.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA));
                        break;

                }

                //Comprobamos la forma B
                switch (numFormaB) {

                    case 0:
                        if (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaB)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextInfinitivo.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaB));
                        break;

                    case 1:
                        if (comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaB)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextPasado.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaB));
                        break;

                    case 2:
                        if (comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaB)))
                            puntuacionPartida=puntuacionPartida+(nivel*seg);
                        else
                            perderVida();
                        System.out.println("introducido: "+editTextParticipio.getText().toString()+" solucion: "+grupo[eleccion].get(numVerbo).getVerbo().getForma(numFormaA));
                        break;

                }


            }

            if(nivel==5 || nivel==10 || nivel==15){

                //Tenemos que comprobar que los dos verbos introducidos por el usuario estén bien, aunque da igual donde lo haya puesto:

                int infinitivo=0;
                int pasado=1;
                int participio=2;
                //lugar es donde se coloca la forma visible.
                System.out.println("Lugar al comprobar 5: "+lugar);
                switch(lugar){

                    //La forma visible está en el layout del Infinitivo, comprobaremos que en los otros
                    //dos estén las otras dos formas visible.
                    case 0:

                        System.out.println("lugar"+lugar+" forma visible"+formaVisible);
                        switch(formaVisible){

                            /*Si la forma que está en el lugar 0 es la 0, comprobaremos que en en los lugares 1 y 2
                            estén las formas 1 y 2 aunque de igual cual en cada lugar.
                             */
                            case 0:

                                if ( (comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                      &&
                                      comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                     )
                                     ||
                                     (comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                       &&
                                      comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                     )

                                   )puntuacionPartida=puntuacionPartida+(nivel*seg);
                                else{
                                    System.out.println("LEVEL5");

                                    perderVida();
                                }



                            break;

                            case 1:

                                if ( (comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                        &&
                                        comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                     )
                                     ||
                                     (comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                        &&
                                        comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                     )
                                   )puntuacionPartida=puntuacionPartida+(nivel*seg);
                                else
                                    perderVida();
                            break;
                            case 2:

                                if ( (comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                        &&
                                        comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                )
                                        ||
                                        (comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                                &&
                                                comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                        )
                                        )puntuacionPartida=puntuacionPartida+(nivel*seg);
                                else
                                    perderVida();
                            break;


                        }


                    break;


                    case 1:

                        switch(formaVisible){

                            /*Si la forma que está en el lugar 0 es la 0, comprobaremos que en en los lugares 1 y 2
                            estén las formas 1 y 2 aunque de igual cual en cada lugar.
                             */
                            case 0:

                                if ( (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                        &&
                                        comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                )
                                        ||
                                        (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                                &&
                                                comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                        )

                                        )puntuacionPartida=puntuacionPartida+(nivel*seg);
                                else
                                    perderVida();


                                    break;

                            case 1:

                                if ( (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                        &&
                                        comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                )
                                        ||
                                        (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                                &&
                                                comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                        )
                                        )puntuacionPartida=puntuacionPartida+(nivel*seg);
                                else
                                    perderVida();
                                    break;
                            case 2:

                                if ( (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                        &&
                                        comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                )
                                        ||
                                        (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                                &&
                                                comprueba(editTextParticipio.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                        )
                                        )puntuacionPartida=puntuacionPartida+(nivel*seg);
                                else
                                    perderVida();
                                    break;


                        }

                    break;


                    case 2:

                        switch(formaVisible){

                            /*Si la forma que está en el lugar 0 es la 0, comprobaremos que en en los lugares 1 y 2
                            estén las formas 1 y 2 aunque de igual cual en cada lugar.
                             */
                            case 0:

                                if ( (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                        &&
                                        comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                )
                                        ||
                                        (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                                &&
                                                comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                        )

                                        )puntuacionPartida=puntuacionPartida+(nivel*seg);
                                else
                                    perderVida();


                                    break;

                            case 1:

                                if ( (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                        &&
                                        comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                )
                                        ||
                                        (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(participio))
                                                &&
                                                comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                        )
                                        )puntuacionPartida=puntuacionPartida+(nivel*seg);
                                else
                                    perderVida();
                                    break;
                            case 2:

                                if ( (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                        &&
                                        comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                )
                                        ||
                                        (comprueba(editTextInfinitivo.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(infinitivo))
                                                &&
                                                comprueba(editTextPasado.getText().toString(), grupo[eleccion].get(numVerbo).getVerbo().getForma(pasado))
                                        )
                                        )puntuacionPartida=puntuacionPartida+(nivel*seg);
                                else
                                    perderVida();
                                    break;


                        }

                    break;


                }




            }




        //Ajustamos la puntuación según el tiempo, multiplicandola por los segunods que quedan, así
        //cuando menmos tiempo gaste el usuario más segundos le quedarán y el resultado será mayor.


        puntos.setText(Integer.toString(puntuacionPartida));
    } //fin comprobarVerbo()

}