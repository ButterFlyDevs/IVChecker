package pdm.ivchecker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;


public class ResultadosTraining extends ActionBarActivity {

    //Variable Intent para obtener los datos de la actividad anterior
    Intent intent;

    //Variables utilizadas para el gráfico
    private PieChart grafico;
    private Segment segmento_aciertos;
    private Segment segmento_fallos;

    //Variables String para controlar los textos de la pantalla
    TextView total_verbos;
    TextView total_acertados;
    TextView total_fallados;
    TextView tipo_lista;
    TextView verbos_fallados_infinitivo, verbos_fallados_pasado, verbos_fallados_participio;

    private LinearLayout layout;
    private ScrollView scroll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_training);

        layout = (LinearLayout) findViewById(R.id.layoutPrincipalResultadosTraining);
        scroll = (ScrollView) findViewById(R.id.scrollLayout);
        //Para que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Para que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Primero, obtenemos el intent con los datos importantes, y configuramos el juego
        intent = getIntent();

        //Obtencion de las referencias de la vista XML de la actividad
        grafico = (PieChart) findViewById(R.id.graficoQueso);
        total_verbos = (TextView) findViewById(R.id.etiqueta_total_verbos);
        total_fallados = (TextView) findViewById(R.id.etiqueta_verbos_fallados);
        total_acertados = (TextView) findViewById(R.id.etiqueta_verbos_acertados);
        tipo_lista = (TextView) findViewById(R.id.tipo_lista);
        verbos_fallados_infinitivo = (TextView) findViewById(R.id.infinitivoResultado);
        verbos_fallados_pasado = (TextView) findViewById(R.id.pasadoResultado);
        verbos_fallados_participio = (TextView) findViewById(R.id.participioResultado);

        //Texto lista
        switch(intent.getIntExtra("LISTA",0)){
            case 1: tipo_lista.setText("List: Soft List"); break;
            case 2: tipo_lista.setText("List: Medium List"); break;
            default: tipo_lista.setText("List: Hard List"); break;
        }

        //Texto verbos
        total_verbos.setText("Total verbs: "+intent.getIntExtra("NUMERO_VERBOS_PREGUNTADOS",0));
        total_fallados.setText("Misses: "+(intent.getIntExtra("NUMERO_VERBOS_PREGUNTADOS",0)
                - intent.getIntExtra("NUMERO_VERBOS_ACERTADOS",0)));
        total_acertados.setText("Hits: "+intent.getIntExtra("NUMERO_VERBOS_ACERTADOS",0));

        //Lista verbos fallados
        String string_verbos_fallados = intent.getStringExtra("LISTA_VERBOS_FALLADOS");
        String lineas_verbos_fallados[] = string_verbos_fallados.split("\n");
        if(lineas_verbos_fallados.length>1) {   //Ha de ser mayor que 1 y el for empieza en 1 por que el primer String guardado es la cadena de inicialización de la variable en la otra activity, es decir: ""
            for (int i = 1; i < lineas_verbos_fallados.length; i++) {
                String[] RowData = lineas_verbos_fallados[i].split(",");
                verbos_fallados_infinitivo.append(RowData[0] + "\n");
                verbos_fallados_pasado.append(RowData[1] + "\n");
                verbos_fallados_participio.append(RowData[2] + "\n");
            }
        }
        //verbos_fallados.setText("Failed verbs: " + intent.getStringExtra("LISTA_VERBOS_FALLADOS"));
        //Grafico

        segmento_aciertos = new Segment("hits", intent.getIntExtra("NUMERO_VERBOS_ACERTADOS",0));
        segmento_fallos = new Segment("misses", intent.getIntExtra("NUMERO_VERBOS_PREGUNTADOS",0) -
                intent.getIntExtra("NUMERO_VERBOS_ACERTADOS",0));

        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);

                /*
        Creamos los formateadores de los trozos del queso. Para ello:

            1. Creamos la carpeta xml dentro de res (si no existiera), y los formateadores dentro de esa carpeta (son archivos xml)
            2. Copiamos el contenido del formateador. Básicamente tiene dos lineas: en la primera se indica el color, y en la segunda, el tamaño del texto.
            3. En values / dimensions.xml hay que modificar el fichero diimens.xml (de bajo dpi) para incluir la variable pie_segment_label_font_size y title_font_size

            Fuente: https://bitbucket.org/androidplot/androidplot/src
         */

        SegmentFormatter sf1 = new SegmentFormatter();
        sf1.configure(getApplicationContext(),R.xml.formato_aciertos);
        sf1.getFillPaint().setMaskFilter(emf);

        SegmentFormatter sf2 = new SegmentFormatter();
        sf2.configure(getApplicationContext(), R.xml.formato_fallos);
        sf2.getFillPaint().setMaskFilter(emf);

        //Añadimos los segmentos y los formatos al gráfico
        grafico.addSeries(segmento_aciertos, sf1);
        grafico.addSeries(segmento_fallos, sf2);

        grafico.getBorderPaint().setColor(Color.TRANSPARENT);
        grafico.getBackgroundPaint().setColor(Color.TRANSPARENT);

        layout.invalidate();
        scroll.invalidate();
        layout.requestLayout();
        scroll.requestLayout();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultados_training, menu);
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
