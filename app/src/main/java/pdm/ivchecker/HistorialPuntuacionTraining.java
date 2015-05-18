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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


public class HistorialPuntuacionTraining extends ActionBarActivity {

    private XYPlot grafico;
    Integer [] serie_puntuaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_puntuacion_training);

        // Inicializamos el objeto XYPlot búscandolo desde el layout:
        grafico = (XYPlot) findViewById(R.id.grafico);

        //Leemos los datos y rellenamos el vector con los datos leidos
        leerPuntuaciones();

        for(int i=0; i<serie_puntuaciones.length;i++)
            System.out.println("Numero leido: "+serie_puntuaciones[i] + ", iteración: "+i);

        //Añadimos la linea número 1
        /*
            El constructor de la clase de series (SimpleXYSeries tiene 3 argumentos:
            1º array de datos, 2º especificacion de los valores, 3º nombre de la serie*/
        XYSeries serie_linea = new SimpleXYSeries(
                Arrays.asList(serie_puntuaciones),  //Array de datos que pasamos al gráfico (los números)
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, //Los datos son valores verticales solamente
                "Nombre serie 1");  //Nombre de la serie

        //Modificamos colores de las series
        /*
            El constructor de la clase que da formato a la linea del gráfico tiene 3 argumentos:
            1º color de la linea
            2º color del punto
            3º relleno
            4º Formateo del punto*/
        try {
            LineAndPointFormatter serie_formato = new LineAndPointFormatter(
                    Color.rgb(0,100,0),
                    Color.rgb(0, 100, 100),
                    Color.rgb(150,190,0),
                    PointLabelFormatter.class.newInstance());
            //Una vez definida la serie (datos y estilo) la añadimos al panel:
            grafico.addSeries(serie_linea,serie_formato);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /*
    Funcion que lee el historial de puntuaciones del usuario.
    Consulta el fichero puntuaciones.csv en busca de todas las líneas.
     */

    private void leerPuntuaciones(){
        ArrayList<Integer> puntuaciones_leidas = new ArrayList<>();
        String line;
        String [] RowData;

        try {
            //Apertura del fichero
            String fichero = "puntuaciones.csv";
            InputStream inputStream = openFileInput(fichero);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while (true) {
                line = reader.readLine();
                if (line == null) break;
                System.out.println("LINEA LEIDA!");
                RowData = line.split(",");
                puntuaciones_leidas.add(Integer.parseInt(RowData[0]));

            }
            inputStream.close();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
            System.out.println("ERROR: No ha sido posible abrir el fichero de puntuaciones");
        }
        serie_puntuaciones = new Integer[puntuaciones_leidas.size()];
        for(int i=0; i<puntuaciones_leidas.size();i++)
            serie_puntuaciones[i] = (Integer) puntuaciones_leidas.get(i);
    }

}
