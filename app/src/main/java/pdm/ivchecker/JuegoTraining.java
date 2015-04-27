package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    ArrayList<Integer> fallos_juegos_anteriores = new ArrayList<>();

    //String de todos los verbos fallados (se le pasa como estadística a mostrar en los resultados)
    String lista_verbos_fallados="";

    //Botón de siguiente verbo
    private Button btnNext;
    private EditText txtVerbo;

    private TextView infinitivo, pasado, participio, etiqueta_progreso;

    private int puntuacionJugada=0, verbos_acertados=0;
    private int numPartida=0;

    private int numVerbo, numForma, numLetrasForma, total_verbos_lista;
    private String misterio="";
    private FileOutputStream flujo_fichero;


    //Variable Intent con los datos que TrainingAreaInicio ha pasado
    Intent intent;
    //Variables de control del entrenamiento
    private int nivel=0, lista_a_preguntar=0,numero_verbos=0;


    @Override
    //Método llamada cuando se crea por primera vez la actividad
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_training);

        //Para que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Para que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Primero, obtenemos el intent con los datos importantes, y configuramos el juego
        intent = getIntent();

        //Obtenemos la referencia a ese botón de la vista
        btnNext=(Button)findViewById(R.id.nextButtonTraining);
        txtVerbo=(EditText)findViewById(R.id.formaMisteriosaTraining);

        infinitivo=(TextView)findViewById(R.id.infinitivoTraining);
        pasado=(TextView)findViewById(R.id.pasadoTraining);
        participio=(TextView)findViewById(R.id.participioTraining);
        etiqueta_progreso=(TextView) findViewById(R.id.etiqueta_progreso);

        //Implementamos el evento click del botón next:
        btnNext.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {

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


        prepararJuego();
        leerVerbos();
        jugar();
        actualizarProgreso();


    }

    /*
    Funcion que prepara las variables configuradas por el usuario en el menú de Configuración del
    Activity anterior (TrainingAreaInicio). Recordamos que el valor 0 es aleatorio.
     */
    private void prepararJuego(){
        Random rnd = new Random();
        //Obtencion de valores
        this.nivel=intent.getIntExtra("nivel",0);
        this.lista_a_preguntar = intent.getIntExtra("lista",0);
        this.numero_verbos = intent.getIntExtra("numero_verbos",0);

        //Si los valores son 0, generamos los aleatorios:
        if(nivel ==0)
            nivel = rnd.nextInt(16);    //Genera unn numero entre 0 (inbluido) y 15 (16 excluido)
        if(lista_a_preguntar==0)
            lista_a_preguntar = rnd.nextInt(3) +1;
        if(numero_verbos==0)
            numero_verbos = 10 + rnd.nextInt(31); //Genera un numero entre 10 y 40.

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

        String fichero= "puntuaciones.csv";

        try {
            //Abrimos el fichero
            this.flujo_fichero = openFileOutput(fichero, MODE_APPEND); //MODE_APPEND abre el fichero en modo de "escritura a continuación", es decir, sin machacar los datos que ya se encuentren
            /*
            Formato de la linea a guardar:
            Puntuacion, Lista preguntada, Nivel,{Lista de verbos fallados}
             */
            String linea;
            linea = String.valueOf(this.puntuacionJugada)+","+String.valueOf(this.lista_a_preguntar)+","+String.valueOf(this.nivel);
            if(this.verbos_fallados!=null){
                for(int i=0; i<verbos_fallados.size();i++)
                    linea = linea+","+verbos_fallados.get(i);
            }

            flujo_fichero.write(linea.getBytes());
            flujo_fichero.close();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
            System.out.println("ERROR: No ha sido posible escribir en el fichero de puntuaciones");
        }
    }

    /*
    Función que lee el fichero csv de fallos del usuario, para preguntar los verbos que más falla
    dicho usuario.

    Lee las últimas líneas del fichero csv (hasta las últimas 5 líneas) y comprueba los verbos
    que más ha fallado el usuario.
     */
    private void topFallosVerbos(){
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
        }
        catch (IOException ioe){
            ioe.printStackTrace();
            System.out.println("ERROR: No ha sido posible abrir el fichero de puntuaciones");
        }


        //Fichero leído. Comprobando las últimas líneas (hasta 5)

        if(datos_puntuaciones.size() > 5){  //Hay mas de 5 líneas. Nos quedamos con las últimas 5
            for(int i=1; i<=5;i++){
                line = datos_puntuaciones.get(datos_puntuaciones.size()-i); //i-ésima última línea
                RowData = line.split(","); //Separamos por comas
                if(RowData.length >3){  //La línea leída tiene indices de verbos fallados
                    int numero_fallos = RowData.length -3;
                    for(int j=0; i<numero_fallos;j++){
                        indices_encontrados.add(Integer.parseInt(RowData[3+j]));

                    }
                }
            }

        }else {                              //No hay mas de 5 líneas. Se toman todos los datos leídos
            for (int i = 0; i < datos_puntuaciones.size(); i++) {
                line = datos_puntuaciones.get(i); //
                RowData = line.split(","); //Separamos por comas
                if (RowData.length > 3) {  //La línea leída tiene indices de verbos fallados
                    int numero_fallos = RowData.length - 3;
                    for (int j = 0; i < numero_fallos; j++) {
                        indices_encontrados.add(Integer.parseInt(RowData[3 + j]));
                    }
                }
            }
        }

        //Todos los indices (incluyendo repetidos) se encuentran en indices_encontrados
        //Ahora toca crear la lista sin repetidos y ordenarla

        //Creacion de la lista de indices fallados SIN repetidos
        indices_encontrados_no_repetidos_hash = new HashSet<Integer>(indices_encontrados);
        indices_encontrados_no_repetidos.addAll(indices_encontrados_no_repetidos_hash);

        //Conocer cuantas veces se ha fallado un verbo dado
        for(int i=0;i<indices_encontrados_no_repetidos.size();i++) {
            int veces_fallado=0;
            int verbo_dado= indices_encontrados_no_repetidos.get(i);
            for (int j=0; j<indices_encontrados.size();j++)
                if(indices_encontrados.get(j) == verbo_dado)
                    veces_fallado++;
            verbosfallados.add(new VerbosFallados(verbo_dado,veces_fallado));

        }

        //Ordenar la lista de verbos fallados por orden de mas fallos a menos fallos
        Collections.sort(verbosfallados, new Comparator<VerbosFallados>() {
            @Override
            public int compare(VerbosFallados vf1, VerbosFallados vf2) {
                return new Integer(vf2.veces_fallado).compareTo(new Integer(vf1.veces_fallado));
            }
        });

        //La lista ya está ordenada. Solo queda pasarlo al array de la clase JuegoTraining
        for(int i=0; i<verbosfallados.size();i++)
            this.fallos_juegos_anteriores.add(verbosfallados.get(i).indice_verbo);
    }


    public void jugar(){

        //Para la generación de números:
        Random rnd = new Random();

        //Se preguntaran los verbos más fallados mientras haya verbos en esa lista. Si se acaba la lista, se preguntará aleatorio.
        if(this.numPartida <= this.fallos_juegos_anteriores.size())
            numVerbo = fallos_juegos_anteriores.get(numPartida);
        else
            //Generamos el verbo a mostrar (en función del tamaño de lista)
            numVerbo = rnd.nextInt(this.total_verbos_lista);    //Genera un aleatorio desde 0 hasta el máximo de verbos almacenados

        System.out.println("Verbo elegido: "+numVerbo);

        //Generamos la forma que no aparecerá
        numForma= rnd.nextInt(3);

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
        System.out.println("Texto introducido: "+txtVerbo.getText());
        System.out.println("Verbo a comparar: "+verbos[numVerbo][numForma]);

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
            this.lista_verbos_fallados = this.lista_verbos_fallados +verbos[numVerbo][0] + ", ";
        }


        txtVerbo.setText("");
    }

    public void acabarPartida(){
        //Partida acabada. Salvamos la puntuación del jugador.
        this.salvar_puntuacion_local();

        //Creamos el intent:
        Intent intent = new Intent(JuegoTraining.this, ResultadosTraining.class);

        //Creamos la información a pasar entre actividades: puntuación obtenida, numero verbos_preguntados y acertados.

        intent.putExtra("PUNTOS", puntuacionJugada);
        intent.putExtra("NUMERO_VERBOS_PREGUNTADOS",numero_verbos);
        intent.putExtra("NUMERO_VERBOS_ACERTADOS", verbos_acertados);
        intent.putExtra("LISTA",lista_a_preguntar);
        intent.putExtra("LISTA_VERBOS_FALLADOS",this.lista_verbos_fallados);
        intent.putExtra("nivel",nivel);

        //Nos vamos al activity resultados:
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_juego_training, menu);
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
