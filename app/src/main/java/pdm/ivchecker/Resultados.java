package pdm.ivchecker;

import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;


import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;


public class Resultados extends ActionBarActivity {

    //private XYPlot grafico;

    private PieChart grafico;
    private TextView donutSizeTextView;
    private SeekBar donutSizeSeekBar;

    private Segment s1;
    private Segment s2;
    private Segment s3;
    private Segment s4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);



      /*        ######################## COMENTADO TODO LO NECESARIO PARA HACER GRAFICO DE LINEAS ############

        // Inicializamos el objeto XYPlot búscandolo desde el layout:
        grafico = (XYPlot) findViewById(R.id.grafico);

        // Creamos dos arrays de prueba. En el caso real debemos reemplazar
        // estos datos por los que realmente queremos mostrar

        Number[] serie1 = {1, 8, 5, 2, 7, 4};
        Number[] serie2 = {4, 6, 3, 8, 2, 10};

        //Añadimos la linea número 1
        /*
            El constructor de la clase de series (SimpleXYSeries tiene 3 argumentos: 1º array de datos, 2º especificacion de los valores, 3º nombre de la serie


        XYSeries serie1_linea = new SimpleXYSeries(
                Arrays.asList(serie1),  //Array de datos que pasamos al gráfico (los números)
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, //Los datos son valores verticales solamente
                "Nombre serie 1");  //Nombre de la serie

        //Añadimos la línea número 2
        XYSeries serie2_linea = new SimpleXYSeries(
                Arrays.asList(serie2),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                "Nombre serie 2");

        //Modificamos colores de las series

        /*
            El constructor de la clase que da formato a la linea del gráfico tiene 3 argumentos:
            1º color de la linea
            2º color del punto
            3º relleno
            4º Formateo del punto

        try {
            LineAndPointFormatter serie1_formato = new LineAndPointFormatter(
                    Color.rgb(0,100,0),
                    Color.rgb(0,100,100),
                    Color.rgb(150,190,0),
                    PointLabelFormatter.class.newInstance());


            //Una vez definida la serie (datos y estilo) la añadimos al panel:
            //grafico.addSeries(serie1_linea,serie1_formato);

            //Repetimos para la segunda serie:

            LineAndPointFormatter serie2_formato = new LineAndPointFormatter(
                    Color.rgb(0,100,0),
                    Color.rgb(0,0,200),
                    Color.rgb(0,190,0),
                    PointLabelFormatter.class.newInstance());

            //Una vez definida la serie (datos y estilo) la añadimos al panel:
            grafico.addSeries(serie2_linea,serie2_formato);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        */

        //inicializamos la referencia del objeto xml del gráfico:
        grafico = (PieChart) findViewById(R.id.graficoQueso);

        //grafico.getRenderer(PieRenderer.class).setDonutSize(30 / 100f, PieRenderer.DonutMode.PERCENT);
        /*              #### TODO: MANEJADOR DEL SEEKBAR BORRADO #####
        donutSizeSeekBar = (SeekBar) findViewById(R.id.donutSizeSeekBar);
        donutSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                grafico.getRenderer(PieRenderer.class).setDonutSize(seekBar.getProgress() / 100f,
                        PieRenderer.DonutMode.PERCENT);
                grafico.redraw();
                //updateDonutText();
            }
        });*/

        s1 = new Segment("s1", 10);
        s2 = new Segment("s2", 10);
        s3 = new Segment("s3", 10);
        s4 = new Segment("s4", 10);

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
        sf1.configure(getApplicationContext(),R.xml.formato_queso1);


        sf1.getFillPaint().setMaskFilter(emf);

        SegmentFormatter sf2 = new SegmentFormatter();
        sf2.configure(getApplicationContext(), R.xml.formato_queso2);

        sf2.getFillPaint().setMaskFilter(emf);

        SegmentFormatter sf3 = new SegmentFormatter();
        sf3.configure(getApplicationContext(), R.xml.formato_queso3);

        sf3.getFillPaint().setMaskFilter(emf);

        SegmentFormatter sf4 = new SegmentFormatter();
        sf4.configure(getApplicationContext(), R.xml.formatoqueso5);

        sf4.getFillPaint().setMaskFilter(emf);

        grafico.addSeries(s1, sf1);
        grafico.addSeries(s2, sf2);
        grafico.addSeries(s3, sf3);
        grafico.addSeries(s4, sf4);

        grafico.getBorderPaint().setColor(Color.TRANSPARENT);
        grafico.getBackgroundPaint().setColor(Color.TRANSPARENT);
    }
/*
    protected void updateDonutText() {
        donutSizeTextView.setText(donutSizeSeekBar.getProgress() + "%");
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resultados, menu);
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
