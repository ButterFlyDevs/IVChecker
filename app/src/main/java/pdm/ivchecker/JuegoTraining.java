package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


public class JuegoTraining extends ActionBarActivity {

    //Matriz donde se almacenan los verbos:
    private String [][] verbos;
    //Flujo de entrada para la lectura de fichero CSV:
    private InputStream inputStream;

    //Lista de indices de los verbos fallados
    ArrayList<Integer> verbos_fallados = null;

    //Botón de siguiente verbo
    private Button btnNext;
    private EditText txtVerbo;

    private TextView infinitivo, pasado, participio;

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

        //Primero, obtenemos el intent con los datos importantes, y configuramos el juego
        intent = getIntent();

        //Obtenemos la referencia a ese botón de la vista
        btnNext=(Button)findViewById(R.id.nextButtonTraining);
        txtVerbo=(EditText)findViewById(R.id.formaMisteriosaTraining);

        infinitivo=(TextView)findViewById(R.id.infinitivoTraining);
        pasado=(TextView)findViewById(R.id.pasadoTraining);
        participio=(TextView)findViewById(R.id.participioTraining);

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
                        jugar();


                    }
                }
        );


        prepararJuego();
        leerVerbos();
        jugar();


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
            this.flujo_fichero = openFileOutput(fichero, MODE_APPEND);
            /*
            Formato de la linea a guardar:
            Puntuacion, Lista preguntada, Nivel,{Lista de verbos fallados}
             */
            String linea;
            linea = String.valueOf(this.puntuacionJugada)+","+String.valueOf(this.lista_a_preguntar)+","String.valueOf(this.nivel);
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
    public void jugar(){

        //Para la generación de números:
        Random rnd = new Random();

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
            if(this.verbos_fallados==null)  //Si es el primer fallo, creamos la lista
                this.verbos_fallados = new ArrayList();

            //Añadimos el indice del verbo fallado
            this.verbos_fallados.add(this.numVerbo);
        }


        txtVerbo.setText("");
    }

    public void acabarPartida(){
        //Partida acabada. Salvamos la puntuación del jugador.
        this.salvar_puntuacion_local();

        //Creamos el intent:
        Intent intent = new Intent(JuegoTraining.this, Resultados.class);

        //Creamos la información a pasar entre actividades: puntuación obtenida, numero verbos_preguntados y acertados.
        Bundle b = new Bundle();
        b.putString("PUNTOS", String.valueOf(puntuacionJugada));
        b.putString("NUMERO_VERBOS_PREGUNTADOS",String.valueOf(this.numero_verbos));
        b.putString("NUMERO_VERBOS_ACERTADOS", String.valueOf(this.verbos_acertados));


        //Añadimos la información al intent:
        intent.putExtras(b);

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
