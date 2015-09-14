package pdm.ivchecker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;


public class JuegoTraining extends ActionBarActivity {

    //Matriz donde se almacenan los verbos:
    private String [][] verbos;
    //Flujo de entrada para la lectura de fichero CSV:
    private InputStream inputStream;

    //Lista de indices de los verbos fallados durante el juego actual
    ArrayList<Integer> verbos_fallados = null;
    //Lista de indices de los verbos que el usuario ha fallado en entrenamientos pasados (leídos del csv)
    ArrayList<Integer> fallos_juegos_anteriores = null;

    //String de todos los verbos fallados (se le pasa como estadística a mostrar en los resultados)
    String lista_verbos_fallados="\n";

    //Botón de siguiente verbo
    private Button btnNext, buttonBack;


    /**
     * Campo de texto donde el jugador introduce su respuresta.
     * Existe solamente un campo de texto editText que se mueve de layout dependiendo de cual sea la forma
     * por la que se le pregunta. En lugar de tener tres se tiene solo uno.
     *
     */
    private EditText txtVerbo;

    private TextView  etiqueta_progreso;

    //Contenedores LAYOUT que pueden tener un EditText o un TextView
    private LinearLayout layoutInfinitivo, layoutPasado, layoutParticipio;


    private int puntuacionJugada=0, verbos_acertados=0;
    private int numPartida=0;

    private int numVerbo, numForma, numLetrasForma, total_verbos_lista;
    private String misterio="";
    private FileOutputStream flujo_fichero;

    private int tamTexto=40;


    //Variable Intent con los datos que TrainingAreaInicio ha pasado
    Intent intent;
    //Variables de control del entrenamiento
    private int  lista_a_preguntar=0,numero_verbos=0;
    boolean smartVerb=true;
    //Variable booleana para controlar si los datos de la partida actual han sido cargados de una instancia anterior
    //(se ha rotado la pantalla)
    boolean ocurridaRotacion = false;



    @Override
    //Método llamada cuando se crea por primera vez la actividad
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_training);

        //Para que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Para que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*
        ############ RECUPERAR LA INSTANCIA DEL JUEGOTRAINING ANTERIOR, EN CASO DE EXISTIR
        Esto ocurrirá cuando se gire la pantalla mientras se juega (se creará una nueva
        instancia de JuegoTraining, la cual debe cargar el estado de partida tal cual
        estaba antes de girar la pantalla.

        Null si es la primera ejecucion. Distinto a null si viene de una ejecución previa
         */
        if(savedInstanceState !=null) {

            this.verbos_fallados = savedInstanceState.getIntegerArrayList("LISTA_FALLADOS_TRAINING");
            this.lista_verbos_fallados = savedInstanceState.getString("STRING_FALLADOS_TRAINING");
            this.puntuacionJugada = savedInstanceState.getInt("PUNTUACION_TRAINING");
            this.verbos_acertados = savedInstanceState.getInt("VERBOS_ACERTADOS_TRAINING");
            this.numPartida = savedInstanceState.getInt("NUM_PARTIDA_TRAINING");
            this.numVerbo = savedInstanceState.getInt("NUM_VERBO_TRAINING");
            this.numForma = savedInstanceState.getInt("NUM_FORMA_TRAINING");
            this.lista_a_preguntar = savedInstanceState.getInt("LISTA_A_PREGUNTAR_TRAINING");
            this.numero_verbos = savedInstanceState.getInt("NUMERO_VERBOS_TRAINING");
            this.smartVerb = savedInstanceState.getBoolean("STMARTVERB_TRAINING");
            this.ocurridaRotacion=true;
        }
        //Primero, obtenemos el intent con los datos importantes, y configuramos el juego
        intent = getIntent();

        //Obtenemos la referencia a ese botón de la vista
        btnNext=(Button)findViewById(R.id.nextButtonTraining);
        buttonBack = (Button)findViewById(R.id.buttonBAck);

         //Ajustes del campo de texto editText que se movera por los tres layouts.

            //Inicialización y ajuste del campo introducción de texto
            txtVerbo = new EditText(this);
            //Ajustamos el tamaño del texto:
            txtVerbo.setTextSize(tamTexto);
            txtVerbo.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
            txtVerbo.setBackgroundColor(Color.TRANSPARENT);
            txtVerbo.setTextColor(Color.WHITE);

        /**
         * Añadimos este código que enlaza el editText txtVerbo al cusor definido en R.drawable.cursor
         */
            try {
                // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(txtVerbo, R.drawable.cursor);
            } catch (Exception ignored) {}


            /**
             * Programacion de la respuesta al evento "hacer click" dentro del campo de texto.
             */
            txtVerbo.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        //Implementamos la acción del click sobre el botón next.
                        public void onClick(View v) {

                      //Borramos el contenido escribiendo "" = nada.
                      txtVerbo.setText("");

                        }
                    }
            );


            //Programación del evento que escucha los cambios de focos sobre el EditText
            txtVerbo.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        /*Codigo que ejecutamos cuando el editText pierde el foco, también se puede
                        programar que hacer para cuando lo tiene con if(hasFocus){...}.
                        Entonces lo que hacemos cuando lo pierde es simplemente devolvérselo, para evitar
                        que el cursor no aparezca y que el usuario tenga que clicar dos veces sobre el
                        campo de texto para escribir.
                        */
                        txtVerbo.requestFocus();
                    }
                }
            });

            /**
             * Programacion del evento "hacer click sobre el boton back" de la parte superior.
             */
            buttonBack.setOnClickListener(

                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent  = new Intent(JuegoTraining.this, TrainingAreaInicio.class);
                            //Iniciamos la nueva actividad
                            startActivity(intent);
                        }
                    }
            );



            // Para cerrar el teclado al pulsar intro
            txtVerbo.setOnKeyListener(new View.OnKeyListener() {
            /**
             * This listens for the user to press the enter button on
             * the keyboard and then hides the virtual keyboard
             */
            public boolean onKey(View arg0, int arg1, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (arg1 == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(txtVerbo.getWindowToken(), 0);
                            return true;
                }
                return false;
            }
        });

        //Asociación e los layout contenedores:
        layoutInfinitivo = (LinearLayout) findViewById(R.id.layoutInfinitivo);
        layoutPasado = (LinearLayout) findViewById(R.id.layoutPasado);
        layoutParticipio = (LinearLayout) findViewById(R.id.layoutParticipio);


        etiqueta_progreso=(TextView) findViewById(R.id.etiqueta_progreso);

        ;



        final Animation animacionBotonSiguiente = AnimationUtils.loadAnimation(this, R.anim.myanimation);

        //Implementamos el evento click del botón next:
        btnNext.setOnClickListener(



                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {

                        v.startAnimation(animacionBotonSiguiente);

                        comprobarVerbo();
                        numPartida++;
                        if(numPartida==numero_verbos) {
                            acabarPartida();
                        }
                        else{
                            jugar();
                            actualizarProgreso();
                        }


                    }
                }


        );







        if(savedInstanceState==null)
            prepararJuego();
        leerVerbos();
        topFallosVerbos();
        jugar();
        actualizarProgreso();
        //Ponemos de nuevo ocurridaRotacion a false para que pueda seguir modificando los datos cuando sea necesario
        this.ocurridaRotacion=false;
    }

    /*
    Funcion que prepara las variables configuradas por el usuario en el menú de Configuración del
    Activity anterior (TrainingAreaInicio). Recordamos que el valor 0 es aleatorio.
     */
    private void prepararJuego(){

        Random rnd = new Random();
        int respuesta_smartVerb;
        //Obtencion de valores
        respuesta_smartVerb=intent.getIntExtra("smartVerb",0);
        if(respuesta_smartVerb==0)
            this.smartVerb=true;
        else
            this.smartVerb=false;

        this.lista_a_preguntar = intent.getIntExtra("lista",0);
        this.numero_verbos = intent.getIntExtra("numero_verbos",0);

        //Si los valores son 0, generamos los aleatorios:
        if(lista_a_preguntar==0)
            lista_a_preguntar = rnd.nextInt(3) +1;
        if(numero_verbos==0)
            numero_verbos = 3 + rnd.nextInt(22); //Genera un numero entre 10 y 24.


        DialogoInfoEntrenamiento dialogoInfo = new DialogoInfoEntrenamiento();

        //Alertas al usuario sobre el modo escogido
        if(intent.getIntExtra("lista",0)==0 && intent.getIntExtra("numero_verbos",0)==0)
            dialogoInfo.setRestoTitulo("Aleatorio");
        else
            dialogoInfo.setRestoTitulo("Personalizado");

        if(smartVerb)
            dialogoInfo.setEntrenamientoInteligente("Activado");
        else
            dialogoInfo.setEntrenamientoInteligente("Desactivado");

        if(lista_a_preguntar==1)
            dialogoInfo.setDificultadVerbos("Fácil");
        if(lista_a_preguntar==2)
            dialogoInfo.setDificultadVerbos("Media");
        if(lista_a_preguntar==3)
            dialogoInfo.setDificultadVerbos("Difícil");


        dialogoInfo.setNumVerbos(Integer.toString(numero_verbos));

        dialogoInfo.show(getFragmentManager(), "");

    }
    private void leerVerbos(){

        switch(lista_a_preguntar){
            case 1: //Lista soft
                inputStream=getResources().openRawResource(R.raw.ivsoft);
                break;
            case 2: //Lista medium
                inputStream=getResources().openRawResource(R.raw.ivmedium);
                break;
            default:    //Lista Hard
                inputStream=getResources().openRawResource(R.raw.ivhard);
                break;
        }

        //Abrimos el flujo con un buffer.
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            //Leemos la primera linea del CSV para crear la matriz de verbos
            String line;
            line = reader.readLine();
            //Creamos la matriz de verbos
            total_verbos_lista = Integer.parseInt(line);
            verbos = new String [total_verbos_lista][3];
            //Leemos los verbos
            for(int i=0;i<total_verbos_lista;i++){
                line=reader.readLine();
                String[] RowData = line.split(",");
                verbos[i][0] = RowData[0];
                verbos[i][1] = RowData[1];
                verbos[i][2] = RowData[2];
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo utilizado para guardar la puntuacion en un fichero local
    private void salvar_puntuacion_local(){

        //Fichero donde se guardan las puntuaciones de las partidas.
        String fichero= "puntuaciones.csv";

        try {
            //Abrimos el fichero
            this.flujo_fichero = openFileOutput(fichero, MODE_APPEND); //MODE_APPEND abre el fichero en modo de "escritura a continuación", es decir, sin machacar los datos que ya se encuentren
            /*
            Formato de la linea a guardar:
            Puntuacion, Lista preguntada, Nivel,{Lista de verbos fallados}

            Nuevo formato:
            Lista,numVerbos, nºfallos, {0,1 ... n verbos fallados}
             */
            String linea;

            linea=String.valueOf(lista_a_preguntar)+","+String.valueOf(numero_verbos);
            if(this.verbos_fallados!=null){
                linea+=","+String.valueOf(verbos_fallados.size());
                for(int i=0; i<verbos_fallados.size();i++)
                    linea = linea+","+verbos_fallados.get(i);
            }

            System.out.println("Linea guardada en puntuaciones.csv"+linea);

            /*
            linea = String.valueOf(this.puntuacionJugada)+","+String.valueOf(this.lista_a_preguntar)+","+String.valueOf(0);
            if(this.verbos_fallados!=null){
                for(int i=0; i<verbos_fallados.size();i++)
                    linea = linea+","+verbos_fallados.get(i);
            }
            */
            linea=linea+"\n";
            flujo_fichero.write(linea.getBytes());
            flujo_fichero.close();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
            Toast.makeText(getApplicationContext(), getString(R.string.ErrorLecturaCSV), Toast.LENGTH_LONG).show();
        }
    }

    /*
    Función que lee el fichero csv de fallos del usuario, para preguntar los verbos que más falla
    dicho usuario.

    Lee las últimas líneas del fichero csv (hasta las últimas 5 líneas) y comprueba los verbos
    que más ha fallado el usuario.
     */
    private void topFallosVerbos(){
        if(this.smartVerb) {
            //Variable temporal para almacenar el contenido del fichero
            ArrayList<String> datos_puntuaciones = new ArrayList<>();
            String line;    //Linea leída
            String[] RowData;   //Datos de línea separados en columnas
            ArrayList<Integer> indices_encontrados = new ArrayList<>(); //Vector con todos los indices de verbos encontrados (es temporal)
            //Variables usadas para crear un ArrayList SIN repetidos
            HashSet<Integer> indices_encontrados_no_repetidos_hash; //Copia el ArrayList de indices_encontrados sin repetidos
            ArrayList<Integer> indices_encontrados_no_repetidos = new ArrayList<>();
            //Variable temporal para ordenar los verbos
            ArrayList<VerbosFallados> verbosfallados = new ArrayList<>();

            //Leer el fichero puntuaciones.csv en busca de las últimas líneas
            try {
                //Apertura del fichero
                String fichero = "puntuaciones.csv";
                InputStream inputStream = openFileInput(fichero);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                while (true) {
                    line = reader.readLine();
                    if (line == null) break;
                    datos_puntuaciones.add(line);
                }
                inputStream.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                Toast.makeText(getApplicationContext(), getString(R.string.ErrorLecturaCSV), Toast.LENGTH_LONG).show();
            }


            //Fichero leído. Comprobando las últimas líneas (hasta 5)
            if (datos_puntuaciones.size() > 0) {    //Ha leido al menos una línea....
                if (datos_puntuaciones.size() > 5) {  //Hay mas de 5 líneas. Nos quedamos con las últimas 5
                    for (int i = 1; i <= 5; i++) {
                        line = datos_puntuaciones.get(datos_puntuaciones.size() - i); //i-ésima última línea
                        RowData = line.split(","); //Separamos por comas
                        if (RowData.length > 3) {  //La línea leída tiene indices de verbos fallados
                            if (Integer.parseInt(RowData[1]) == this.lista_a_preguntar) {       //Si la linea contiene fallos de la misma lista...
                                int numero_fallos = RowData.length - 3;
                                for (int j = 0; j < numero_fallos; j++)
                                    indices_encontrados.add(Integer.parseInt(RowData[3 + j]));
                            }

                        }
                    }

                } else {                              //No hay mas de 5 líneas. Se toman todos los datos leídos
                    for (int i = 0; i < datos_puntuaciones.size(); i++) {
                        line = datos_puntuaciones.get(i); //
                        RowData = line.split(","); //Separamos por comas
                        if (RowData.length > 3) {  //La línea leída tiene indices de verbos fallados
                            if (Integer.parseInt(RowData[1]) == this.lista_a_preguntar) {       //Si la linea contiene fallos de la misma lista...
                                int numero_fallos = RowData.length - 3;
                                for (int j = 0; j < numero_fallos; j++)
                                    indices_encontrados.add(Integer.parseInt(RowData[3 + j]));
                            }

                        }
                    }
                }

                if (indices_encontrados.size() > 0) {  //HAY AL MENOS 1 VERBO QUE SE DEBE AÑADIR A LA LISTA DE FALLADOS

                    //Todos los indices (incluyendo repetidos) se encuentran en indices_encontrados
                    //Ahora toca crear la lista sin repetidos y ordenarla

                    //Creacion de la lista de indices fallados SIN repetidos
                    indices_encontrados_no_repetidos_hash = new HashSet<Integer>(indices_encontrados);
                    indices_encontrados_no_repetidos.addAll(indices_encontrados_no_repetidos_hash);

                    //Conocer cuantas veces se ha fallado un verbo dado
                    for (int i = 0; i < indices_encontrados_no_repetidos.size(); i++) {
                        int veces_fallado = 0;
                        int verbo_dado = indices_encontrados_no_repetidos.get(i);
                        for (int j = 0; j < indices_encontrados.size(); j++)
                            if (indices_encontrados.get(j) == verbo_dado)
                                veces_fallado++;
                        verbosfallados.add(new VerbosFallados(verbo_dado, veces_fallado));

                    }

                    //Ordenar la lista de verbos fallados por orden de mas fallos a menos fallos
                    Collections.sort(verbosfallados, new Comparator<VerbosFallados>() {
                        @Override
                        public int compare(VerbosFallados vf1, VerbosFallados vf2) {
                            return new Integer(vf2.veces_fallado).compareTo(new Integer(vf1.veces_fallado));
                        }
                    });

                    //La lista ya está ordenada. Solo queda pasarlo al array de la clase JuegoTraining

                    fallos_juegos_anteriores = new ArrayList<>();
                    for (int i = 0; i < verbosfallados.size(); i++)
                        this.fallos_juegos_anteriores.add(verbosfallados.get(i).indice_verbo);
                }
            }
        }
    }


    public void jugar(){

        //Para la generación de números:
        Random rnd = new Random();

        //Se preguntaran los verbos más fallados mientras haya verbos en esa lista. Si se acaba la lista, se preguntará aleatorio.
        if(!this.ocurridaRotacion) {    //Si ha ocurrido rotacion, no se debe elegir de nuevo el verbo
            if (this.fallos_juegos_anteriores != null) {
                if (this.numPartida < this.fallos_juegos_anteriores.size())
                    numVerbo = fallos_juegos_anteriores.get(numPartida);
                else
                    numVerbo = rnd.nextInt(this.total_verbos_lista);
            } else
                //Generamos el verbo a mostrar (en función del tamaño de lista)
                numVerbo = rnd.nextInt(this.total_verbos_lista);    //Genera un aleatorio desde 0 hasta el máximo de verbos almacenados
        }

        //Generamos la forma que no aparecerá
        if(!this.ocurridaRotacion)
            numForma= rnd.nextInt(3);

        //Obtenermos el verbo que falta en forma de rallitas:
        numLetrasForma=verbos[numVerbo][numForma].length();
        for(int i=0; i<numLetrasForma; i++){
            misterio+=" _ ";
        }

        //Limpiamos el txtVerbo esté donde esté
        if(txtVerbo.getParent()!=null)
            ((ViewGroup)txtVerbo.getParent()).removeView(txtVerbo);

        //Limpiamos los layouts por si hubiese algún elemento de alguna ejecución anterior.
        layoutPasado.removeAllViewsInLayout();
        layoutParticipio.removeAllViewsInLayout();
        layoutInfinitivo.removeAllViewsInLayout();


        //Escribimos en la pantalla:

        //INFINITIVO

            //Si ha sido elegido el como forma a preguntar
            if(numForma==0) {
                //Escribimos la forma en forma de misterio
                txtVerbo.setText(misterio);
                layoutInfinitivo.addView(txtVerbo);

            //Si no ha sido elegido se ecribe el infinitivo del verbo en ese lugar
            }else {

                //Creamos un textView para imprimir el texto en la pantalla:
                TextView texto = new TextView(this);
                //Ajustamos el tamaño del texto:
                texto.setTextSize(tamTexto);

                //Colocamos en texto en el centro del layout


                //Añadimos el infinitivo en el texto:
                texto.setText(verbos[numVerbo][0]);
                //Seteamos el color blanco al texto:
                texto.setTextColor(Color.WHITE);
                //Añadimos el texto al layout:
                layoutInfinitivo.addView(texto);

            }

        if(numForma==1) {
            txtVerbo.setText(misterio);
            layoutPasado.addView(txtVerbo);
        }else {

            TextView texto = new TextView(this);
            //Ajustamos el tamaño del texto:
            texto.setTextSize(tamTexto);
            texto.setText(verbos[numVerbo][1]);
            texto.setTextColor(Color.WHITE);
            layoutPasado.addView(texto);
         //   pasado.setText(verbos[numVerbo][1]);
        }

        if(numForma==2) {
            txtVerbo.setText(misterio);
            layoutParticipio.addView(txtVerbo);
        }else {
            TextView texto = new TextView(this);
            //Ajustamos el tamaño del texto:
            texto.setTextSize(tamTexto);
            texto.setText(verbos[numVerbo][2]);
            texto.setTextColor(Color.WHITE);
            layoutParticipio.addView(texto);
            //   pasado.setText(verbos[numVerbo][1]);
        }

        //Después misterio vuelve a estar vacía
        misterio="";


    }

    /*
    Función utilizada para actualizar la etiqueta que se muestra en la pantalla
    sobre el número de verbos totales que se deben preguntar, y los que se han preguntado ya
    La sintaxis es de la forma:
    Verb: verbos_Contestados / verbos_a_contestar
     */
    private void actualizarProgreso(){
        etiqueta_progreso.setText("Verb: "+(numPartida+1)+"/"+(numero_verbos));
    }
    /*
    Funcion de comprobar verbo introducido.
    En esta variante de Training, la App debe guardar los verbos en los que se falla
     */
    public void comprobarVerbo(){
        if(txtVerbo.getText().toString().equals(verbos[numVerbo][numForma])){
            //Usuario ha acertado
            puntuacionJugada = puntuacionJugada + lista_a_preguntar;
            this.verbos_acertados++;
        }
        else{
            //Usuario ha fallado
            if(this.verbos_fallados==null)  //Si es el primer fallo, creamos la lista de fallos
                this.verbos_fallados = new ArrayList();

            //Añadimos el indice del verbo fallado
            this.verbos_fallados.add(this.numVerbo);
            this.lista_verbos_fallados = this.lista_verbos_fallados +verbos[numVerbo][0] + ", " + verbos[numVerbo][1] +
                    ", " + verbos[numVerbo][2] + "\n";
        }


        txtVerbo.setText("");
    }

    /**
     * Función que termina la partida de entrenamiento guardando los resultados obtenidos en el fichero de puntuaciones
     * y llevándonos después a ResultadosTraining para mostrarle al usuario los resultados de la partida de entrenamiento.
     */
    public void acabarPartida(){
        //Partida acabada. Salvamos la puntuación del jugador en el fichero puntuaciones.csv
        this.salvar_puntuacion_local();

        //Creamos el intent:
        Intent intent = new Intent(JuegoTraining.this, ResultadosTraining.class);
        //Nos vamos al activity:
        startActivity(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos devuelve a la pantalla principal del area de entranamiento..
        if(keyCode==KeyEvent.KEYCODE_BACK){

            Intent intent = new Intent(JuegoTraining.this, TrainingAreaInicio.class);
            startActivity(intent);
            //Aplicacion de transicion animada entre activities:
            overridePendingTransition(R.anim.zoom_back_in2, R.anim.zoom_back_out2);

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //Salvamos los indices de verbos fallados
      /*  if(verbos_fallados!=null) {
            outState.putInt("TOTAL_FALLADOS_TRAINING",verbos_fallados.size());
            for (int i = 0; i < this.verbos_fallados.size(); i++) {
                outState.putInt("VerboFallado" + i, (int) this.verbos_fallados.get(i));
            }
        }
        else
            outState.putInt("TOTAL_FALLADOS_TRAINING",0);*/
        outState.putIntegerArrayList("LISTA_FALLADOS_TRAINING",this.verbos_fallados);

        //Salvamos la cadena de verbos fallados
        outState.putString("STRING_FALLADOS_TRAINING",this.lista_verbos_fallados);

        //Salvamos la puntuacion
        outState.putInt("PUNTUACION_TRAINING",this.puntuacionJugada);

        //Salvamos los verbos acertados hasta el momento
        outState.putInt("VERBOS_ACERTADOS_TRAINING",this.verbos_acertados);

        //Salvamos el indice de verbos contestados hasta el momento
        outState.putInt("NUM_PARTIDA_TRAINING",this.numPartida);

        //Salvamos el verbo elegido en la jugada
        outState.putInt("NUM_VERBO_TRAINING",this.numVerbo);

        //Salvamos la forma elegida para el verbo a preguntar
        outState.putInt("NUM_FORMA_TRAINING",this.numForma);

        //Salvamos la lista a preguntar
        outState.putInt("LISTA_A_PREGUNTAR_TRAINING",this.lista_a_preguntar);

        //Salvamos el numero de verbos a preguntar
        outState.putInt("NUMERO_VERBOS_TRAINING",this.numero_verbos);

        //Salvamos si está activada la característica smartVerb
        outState.putBoolean("STMARTVERB_TRAINING",this.smartVerb);
    }
}
