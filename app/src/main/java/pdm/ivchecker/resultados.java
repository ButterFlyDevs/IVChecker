package pdm.ivchecker;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.Arrays;


public class Resultados extends ActionBarActivity {

    private XYPlot grafico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        // Inicializamos el objeto XYPlot búscandolo desde el layout:
        grafico = (XYPlot) findViewById(R.id.grafico);

        // Creamos dos arrays de prueba. En el caso real debemos reemplazar
        // estos datos por los que realmente queremos mostrar

        Number[] serie1 = {1, 8, 5, 2, 7, 4};
        Number[] serie2 = {4, 6, 3, 8, 2, 10};

        //Añadimos la linea número 1
        /*
            El constructor de la clase de series (SimpleXYSeries tiene 3 argumentos: 1º array de datos, 2º especificacion de los valores, 3º nombre de la serie
        */

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
         */
        try {
            LineAndPointFormatter serie1_formato = new LineAndPointFormatter(
                    Color.rgb(0,100,0),
                    Color.rgb(0,100,100),
                    Color.rgb(150,190,0),
                    PointLabelFormatter.class.newInstance());


            //Una vez definida la serie (datos y estilo) la añadimos al panel:
            grafico.addSeries(serie1_linea,serie1_formato);

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







    }


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
