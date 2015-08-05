package pdm.ivchecker;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;


public class HistorialPuntuacionTraining extends ActionBarActivity {


    Integer [] serie_puntuaciones;

    Integer [] puntuaciones;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_puntuacion_training);


        //Para que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Para que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);




        LinearLayout prueba = (LinearLayout) findViewById(R.id.prueba);



        //Asociamos el chart al elemento del xml
        LineChartView chart = new LineChartView(this);

        prueba.addView(chart);


        List<PointValue> aciertos = new ArrayList<PointValue>();
        aciertos.add(new PointValue(0, 2));
        aciertos.add(new PointValue(1, 3));
        aciertos.add(new PointValue(2, 7));
        aciertos.add(new PointValue(3, 1));
        aciertos.add(new PointValue(4, 6));

        List<PointValue> fallos = new ArrayList<PointValue>();
        fallos.add(new PointValue(0, 1));
        fallos.add(new PointValue(1, 2));
        fallos.add(new PointValue(2, 3));
        fallos.add(new PointValue(3, 4));
        fallos.add(new PointValue(4, 5));

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);

        axisX.setName("Jugadas");
        axisY.setName("Aciertos/Fallos");

        //Creamos una linea con los valors de aciertos
        Line lineaAciertos = new Line(aciertos).setColor(Color.rgb(160,251,62)).setCubic(true);
        lineaAciertos.setStrokeWidth(1);
        lineaAciertos.setPointRadius(3);
        lineaAciertos.setPointColor(Color.rgb(151,253,41));

        Line lineaFallos = new Line(fallos).setColor(Color.rgb(255,92,92)).setCubic(true);
        lineaFallos.setStrokeWidth(1);
        lineaFallos.setPointRadius(3);
        lineaFallos.setPointColor(Color.rgb(255,92,92));

        List<Line> lines = new ArrayList<Line>();
        lines.add(lineaAciertos);
        lines.add(lineaFallos);

        LineChartData data = new LineChartData();

        //Añadimos las lineas a los datos del gráfico
        data.setLines(lines);
        //Añadimos los eje a los datos del gráfico
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chart.setLineChartData(data);


        //Leemos los datos y rellenamos el vector con los datos leidos
        leerPuntuaciones();

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
//                puntuaciones.add(Integer.parseInt(RowData[0]));

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
