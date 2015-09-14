package pdm.ivchecker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;


public class HistorialPuntuacionTraining extends ActionBarActivity {


    Integer[] serie_puntuaciones;

    Integer[] puntuaciones;

    private Button buttonBack, buttonEliminarHistorial;

    private LinearLayout prueba, layoutPieChart1, layoutPieChart2, layoutPieChart3;

    private int aciertosLista1Acumulados=0, fallosLista1Acumulados=0, aciertosLista2Acumulados=0, fallosLista2Acumulados=0, aciertosLista3Acumulados=0, fallosLista3Acumulados =0;

    /**
     * Funcion que puede ser llamada desde el dialogo que borra todo el fichero de puntuaciones guardado.
     */
    public void eliminarHistorial() {
        //Elimina los datos del fichero de puntuaciones:
        String fichero = "puntuaciones.csv";
        FileOutputStream flujo_fichero;
        try {
            flujo_fichero = openFileOutput(fichero, MODE_PRIVATE);

            flujo_fichero.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Recargamos la actividad, para que los cambios hagan efecto.
        finish();
        startActivity(getIntent());
    }


    public void cargarGrafica() {

        prueba = (LinearLayout) findViewById(R.id.prueba);

        //Carga de elementos desde el fichero CSV
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


        //Creamos un elemento LineChartView
        LineChartView chart = new LineChartView(this);

        prueba.addView(chart);


        List<PointValue> aciertos = new ArrayList<PointValue>();
        List<PointValue> fallos = new ArrayList<PointValue>();

        //Por cada linea del fichero de puntuaciones vamos a cargar el numero de aciertos en el array anterior.


        System.out.println(lineasFicheroPuntuaciones.size() + " LINEAS CARGADAS");
        String datosSeccionados[];
        for (String linea : lineasFicheroPuntuaciones) {
            System.out.println(linea);

            //Seccionamos la linea.
            datosSeccionados = linea.split(",");
            //Sacamos los datos y los grabamos en los array que usa la grafica.
            int numVerbos = Integer.parseInt(datosSeccionados[1]);

            //Puede que en una partida no hayan ocurrido fallos y por eso no se pueda acceder a esas posiciones de la linea
            //Si hay verbos fallados
            int numVerbosFallados=0;
            if(datosSeccionados.length>2) {
                numVerbosFallados = Integer.parseInt(datosSeccionados[2]);
            }
            int numVerbosAcertados = numVerbos - numVerbosFallados;
            aciertos.add(new PointValue(lineasFicheroPuntuaciones.indexOf(linea), numVerbosAcertados));
            fallos.add(new PointValue(lineasFicheroPuntuaciones.indexOf(linea), numVerbosFallados));

            //Aprovechamos y guardamos las acumuladas:

            //Si la linea leida es de la lista 1 (fácil)
            if(Integer.parseInt(datosSeccionados[0])==1){
                fallosLista1Acumulados+=numVerbosFallados;
                aciertosLista1Acumulados+=numVerbosAcertados;
            }
            if(Integer.parseInt(datosSeccionados[0])==2){
                fallosLista2Acumulados+=numVerbosFallados;
                aciertosLista2Acumulados+=numVerbosAcertados;
            }
            if(Integer.parseInt(datosSeccionados[0])==3){
                fallosLista3Acumulados+=numVerbosFallados;
                aciertosLista3Acumulados+=numVerbosAcertados;
            }


        }


        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);

        axisX.setName("Jugadas");
        axisY.setName("Aciertos/Fallos");

        //Creamos una linea con los valors de aciertos
        Line lineaAciertos = new Line(aciertos).setColor(Color.rgb(160, 251, 62)).setCubic(true);
        lineaAciertos.setStrokeWidth(1);
        lineaAciertos.setPointRadius(3);
        lineaAciertos.setPointColor(Color.rgb(151, 253, 41));

        Line lineaFallos = new Line(fallos).setColor(Color.rgb(255, 92, 92)).setCubic(true);
        lineaFallos.setStrokeWidth(1);
        lineaFallos.setPointRadius(3);
        lineaFallos.setPointColor(Color.rgb(255, 92, 92));

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


        // ## PIE CHARTS ## //

        // ## PIE CHART 1 ## //

            //Asociamos el layout donde irá el gráfico
            layoutPieChart1 = (LinearLayout) findViewById(R.id.layoutPieChart1);
            //Creamos el gráfico
            PieChartView graficoCircular1 = new PieChartView(this);
            //Añadimos el gráfico al layout
            layoutPieChart1.addView(graficoCircular1);

            //Creamos una lista de valores que usará el gráfico circular
            List<SliceValue> valoresGraficoCircular1 = new ArrayList<SliceValue>();
            valoresGraficoCircular1.add(new SliceValue((float) aciertosLista1Acumulados, ChartUtils.COLOR_GREEN));
            valoresGraficoCircular1.add(new SliceValue((float) fallosLista1Acumulados, ChartUtils.COLOR_RED));

            //Papra poder pasar estos valores al grafico tenemos que crear un objeto de tipo PieChartData que almacena estos datos entre otros:
            PieChartData datosGraficoCircular1 = new PieChartData(valoresGraficoCircular1);
            datosGraficoCircular1.setHasLabels(true);
            datosGraficoCircular1.setHasLabelsOnlyForSelected(true);
            datosGraficoCircular1.setHasLabelsOutside(true);
            datosGraficoCircular1.setHasCenterCircle(true);

            //Por ultimo añadimos los datos al grafico.
            graficoCircular1.setPieChartData(datosGraficoCircular1);


        // ## PIE CHART 2 ## //  (Replicamos el caso 1, condistintos valores de relleno)


            layoutPieChart2=(LinearLayout)findViewById(R.id.layoutPieChart2);

            PieChartView graficoCircular2 = new PieChartView(this);
            layoutPieChart2.addView(graficoCircular2);

            List<SliceValue> valoresGraficoCircular2 = new ArrayList<SliceValue>();

            //Usamos distintos valores de relleno.
            valoresGraficoCircular2.add(new SliceValue((float) aciertosLista2Acumulados, ChartUtils.COLOR_GREEN));
            valoresGraficoCircular2.add(new SliceValue((float) fallosLista2Acumulados, ChartUtils.COLOR_RED));

            PieChartData datosGraficoCircular2 = new PieChartData(valoresGraficoCircular2);
            datosGraficoCircular2.setHasLabels(true);
            datosGraficoCircular2.setHasLabelsOnlyForSelected(true);
            datosGraficoCircular2.setHasLabelsOutside(true);
            datosGraficoCircular2.setHasCenterCircle(true);

            graficoCircular2.setPieChartData(datosGraficoCircular2);


        // ## PIE CHART 2 ## //  (Replicamos el caso 1, condistintos valores de relleno)


            layoutPieChart3=(LinearLayout)findViewById(R.id.layoutPieChart3);

            PieChartView graficoCircular3 = new PieChartView(this);

            //Añadimos el gráfico al layout
            layoutPieChart3.addView(graficoCircular3);

            List<SliceValue> valoresGraficoCircular3= new ArrayList<SliceValue>();

            valoresGraficoCircular3.add(new SliceValue((float) aciertosLista3Acumulados, ChartUtils.COLOR_GREEN));
            valoresGraficoCircular3.add(new SliceValue((float) fallosLista3Acumulados, ChartUtils.COLOR_RED));

            PieChartData datosGraficoCircular3 = new PieChartData(valoresGraficoCircular3);
            datosGraficoCircular3.setHasLabels(true);
            datosGraficoCircular3.setHasLabelsOnlyForSelected(true);
            datosGraficoCircular3.setHasLabelsOutside(true);
            datosGraficoCircular3.setHasCenterCircle(true);

            graficoCircular3.setPieChartData(datosGraficoCircular3);





    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_puntuacion_training);


        //Para que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Para que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Asociamos los elementos
        buttonBack = (Button) findViewById(R.id.buttonAtras);
        buttonEliminarHistorial = (Button) findViewById(R.id.buttonEliminarEstadisticas);


        //El botón back nos lleva de nuevo al area de entrenamiento.
        buttonBack.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(HistorialPuntuacionTraining.this, TrainingAreaInicio.class);
                        startActivity(intent);
                    }
                }
        );

        //Declaramos el emento fuera de clickListener para poder llamar al método getParent pasandonos nosotros mismos.
        //Además de eso debemos hacer la variable final.
        final DialogoEliminacionDatos dialogo = new DialogoEliminacionDatos();
        //Nos pasamos nostros mismos como padre de este fragment (podría no ser así)
        dialogo.setPadre(this);
        //El botón back nos abre el dialogo de confirmación de borrado.
        buttonEliminarHistorial.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogo.show(getFragmentManager(), "");
                        // DialogoInfoEntrenamiento dialogoInfo = new DialogoInfoEntrenamiento();
                        // dialogoInfo.show(getFragmentManager(), "");
                    }
                }
        );


        //Cargamos la grafica

        cargarGrafica();

    }
}


