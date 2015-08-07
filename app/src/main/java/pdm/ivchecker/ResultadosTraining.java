package pdm.ivchecker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;


public class ResultadosTraining extends ActionBarActivity {

    //Variable Intent para obtener los datos de la actividad anterior
    Intent intent;


    //Botones:
    private Button buttonBack, buttonListaVerbos, buttonEstadisticas;


    //Variables String para controlar los textos de la pantalla
    TextView textViewNumVerbos, textViewVerbosAcertados, textViewVerbosFallados, textViewTipoLista, textViewErrores;

    int numVerbosFallados;
    int numVerbos;

    private LinearLayout layoutGrafico;


    ArrayList<Verbo> listaVerbosFallados = new ArrayList();

    ArrayList<Verbo> listaVerbosCargadaDesdeCSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_training);


        //Para que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Para que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Primero, obtenemos el intent con los datos importantes, y configuramos el juego
       // intent = getIntent();

        //Obtencion de las referencias de la vista XML de la actividad
        //grafico = (PieChart) findViewById(R.id.graficoQueso);

        textViewNumVerbos = (TextView) findViewById(R.id.textViewNumVerbos);
        textViewVerbosFallados = (TextView) findViewById(R.id.textViewFallos);
        textViewVerbosAcertados = (TextView) findViewById(R.id.textViewAciertos);
        textViewErrores = (TextView) findViewById(R.id.textViewErrores);
        textViewTipoLista = (TextView) findViewById(R.id.textViewDificultadLista);

      //  verbos_fallados_infinitivo = (TextView) findViewById(R.id.infinitivoResultado);
      //  verbos_fallados_pasado = (TextView) findViewById(R.id.pasadoResultado);
      //  verbos_fallados_participio = (TextView) findViewById(R.id.participioResultado);

        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonListaVerbos = (Button) findViewById(R.id.buttonVerbos);
        buttonEstadisticas = (Button) findViewById(R.id.buttonEstadisticas);


        buttonBack.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ResultadosTraining.this, TrainingAreaInicio.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );

        buttonListaVerbos.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ResultadosTraining.this, ListaVerbos2.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );
        buttonEstadisticas.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ResultadosTraining.this, HistorialPuntuacionTraining.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );




        /* ## Recuperamos todos los elementos necesarios de la última linea del CSV puntuaciones.csv ##
        * Para eso primero cargamos el fichero y leemos todas sus lineas almacenandolas en un array.
        * Después nos quedamos sólo con la útlima linea.
        * Por último seccionamos la partes de la linea para extraer la información necesaria.
        * */

        //Array donde almacenar las lineas del fichero:
        ArrayList<String> lineasFicheroPuntuaciones = new ArrayList();
        //Variable temporal de lectura de linea individual:
        String tmpLine;

        try {
            String fichero = "puntuaciones.csv";
            InputStream inputStream = null;

            inputStream = openFileInput(fichero);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while (true) {
                tmpLine = reader.readLine();
                if (tmpLine == null) break;
                lineasFicheroPuntuaciones.add(tmpLine);
            }
            inputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        //Si se han cargado lineas:
        if(!lineasFicheroPuntuaciones.isEmpty()) {
            //Extraemos la última linea y seguimos usando la variable temporal tmpLine
            tmpLine = lineasFicheroPuntuaciones.get(lineasFicheroPuntuaciones.size() - 1);


            //Extraemos la información de la linea y la cargamos en un vector de String.
            String[] datosSeccionados;
            datosSeccionados = tmpLine.split(",");

            //Sacamos los datos del vector de string.

            //Primer elemento: Tipo de lista (además cargamos los vermos en un array)
            String tipoLista = "Lista ";
            switch (Integer.parseInt(datosSeccionados[0])) {
                case 1:
                    tipoLista+="Fácil";
                    listaVerbosCargadaDesdeCSV = ProcesadorCSVs.obtenerVerbos(getResources().openRawResource(R.raw.ivsoft2));
                    break;
                case 2:
                    tipoLista+="Media";
                    listaVerbosCargadaDesdeCSV = ProcesadorCSVs.obtenerVerbos(getResources().openRawResource(R.raw.ivmedium2));
                    break;
                case 3:
                    tipoLista+="Difícil";
                    listaVerbosCargadaDesdeCSV = ProcesadorCSVs.obtenerVerbos(getResources().openRawResource(R.raw.ivhard2));
                    break;
            }
            textViewTipoLista.setText(tipoLista);

            //Segundo elemento: Número de verbos
            textViewNumVerbos.setText(datosSeccionados[1]+" verbos");
            numVerbos=Integer.parseInt(datosSeccionados[1]);

            //Tercer elemento: Número de verbos fallados
            textViewVerbosFallados.setText(datosSeccionados[2]+" fallos");
            //Almacenamos el número de verbos fallados.
            numVerbosFallados=Integer.parseInt(datosSeccionados[2]);

            //Con este dato seteamos el otro textView
            textViewVerbosAcertados.setText( Integer.toString(Integer.parseInt(datosSeccionados[1])-Integer.parseInt(datosSeccionados[2]))+" aciertos");

            //Últimos elementos, lista de verbos fallados

            //Extraemos de toda la lista los que hemos fallado usando los índices guardados en la última sección de la fila del csv

            for(int i=3; i<datosSeccionados.length; i++){
                listaVerbosFallados.add(listaVerbosCargadaDesdeCSV.get(Integer.parseInt(datosSeccionados[i])));
            }
        }

        //Gráfico pieChart


        layoutGrafico = (LinearLayout) findViewById(R.id.layoutGrafico);

        PieChartView chart = new PieChartView(this);



        //Añadimos el gráfico al layout
        layoutGrafico.addView(chart);

        List<SliceValue> values = new ArrayList<SliceValue>();

        //SliceValue sliceValue = new SliceValue((float) 20, ChartUtils.pickColor());
        values.add(new SliceValue((float) numVerbos-numVerbosFallados, ChartUtils.COLOR_GREEN));
        values.add(new SliceValue((float) numVerbosFallados, ChartUtils.COLOR_RED));



        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
        data.setHasLabelsOnlyForSelected(true);
        data.setHasLabelsOutside(true);
        data.setHasCenterCircle(true);

        chart.setPieChartData(data);



        // ## CARGA DE VERBOS FALLADOS ## //

        //1º Asociamos el listview de la vista:
        final ListView listview = (ListView) findViewById(R.id.listViewVerbos);

        //2º Especificamos que la lista sólo permita selección única
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        //3º Iniciamos el adaptador y le pasamos la lista
        AdaptadorListVerbos adapter = new AdaptadorListVerbos(this, listaVerbosFallados);

        //4º Asociamos el adaptador definido a nuestro listView
        listview.setAdapter(adapter);

        //5º Programamos la acción al pulsar sobre un elmento:
        //Programación del evento pulsar sobre un elementod e la lista.
        listview.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int posicion, long arg3) {
                        System.out.println("PULSADO BOTÓN DE LISTA " + Integer.toString(posicion));

                        FragmentDetalleVerbo fragment1 = new FragmentDetalleVerbo();


                        //Le enviamos al fragment el verbo del que queremos mostrar la información.
                        fragment1.setVerbo(listaVerbosFallados.get(posicion));

                        //Hacemos que se muestre el fragment
                        fragment1.show(getFragmentManager(), "");

                    }

                }


        );

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos devuelve a la pantalla principal!.
        if(keyCode== KeyEvent.KEYCODE_BACK){


            Intent intent = new Intent(ResultadosTraining.this, TrainingAreaInicio.class);
            startActivity(intent);

            return true;
        }

        return super.onKeyDown(keyCode, event);

    }
}
