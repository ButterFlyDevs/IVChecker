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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Random;


public class Juego extends ActionBarActivity {

    //Matriz donde se almacenan los verbos:
    private String [][] verbos;
    //Flujo de entrada para la lectura de fichero CSV:
    private InputStream inputStream;

    //Botón de siguiente verbo
    private Button btnNext;
    private EditText txtVerbo;
    private TextView infinitivo, pasado, participio;

    private int puntuacionJugada;
    private int numPartida=0, numPartidas=5;

    private int numVerbo, numForma, numLetrasForma;
    private String misterio="";

    @Override
    //Método llamada cuando se crea por primera vez la actividad
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        //Obtenemos la referencia a ese botón de la vista
        btnNext=(Button)findViewById(R.id.nextButton);
        txtVerbo=(EditText)findViewById(R.id.formaMisteriosa);

        infinitivo=(TextView)findViewById(R.id.infinitivo);
        pasado=(TextView)findViewById(R.id.pasado);
        participio=(TextView)findViewById(R.id.participio);

        //Implementamos el evento click del botón next:
        btnNext.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    //Implementamos la acción del click sobre el botón next.
                    public void onClick(View v) {

                        comprobarVerbo();
                        numPartida++;
                        if(numPartida==numPartidas) {
                            //Creamos el intent:
                            Intent intent = new Intent(Juego.this, Resultados.class);

                            //Creamos la información a pasar entre actividades:
                            Bundle b = new Bundle();
                            b.putString("PUNTOS", String.valueOf(puntuacionJugada));

                            //Añadimos la información al intent:
                            intent.putExtras(b);

                            //Nos vamos al activity resultados:
                            startActivity(intent);
                        }
                        jugar();


                    }
                }
        );


        //Abrimos el flujo del fichero almacenado en la carpeta denro de res llamada raw con el nombre iv
        inputStream=getResources().openRawResource(R.raw.ivsoft);

        //Abrimos el flujo con un buffer.
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        verbos = new String [16][3];

        //Cargamos los verbos en una matriz para manejarlos mejor.
        try {
            String line;
            int fila=0;
            while(true){
                line=reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                verbos[fila][0] = RowData[0];
                verbos[fila][1] = RowData[1];
                verbos[fila][2] = RowData[2];
                fila++;
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Después de cargar los datos comienza el juego:
        this.jugar();


    }

    public void jugar(){
        //Sección de la lógica del juego

        //Asociamos los objetos de la lógica a los de la vista.


        //Para la generación de números:
        Random rnd = new Random();


        //Generamos el verbo a mostrar:
        numVerbo=(int)(rnd.nextDouble() * 16 + 0);

        System.out.println("Verbo elegido: "+numVerbo);

        //Generamos la forma que no aparecerá
        numForma=(int)(rnd.nextDouble() * 3 + 0);

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


    public void comprobarVerbo(){
        System.out.println("Texto introducido: "+txtVerbo.getText());
        System.out.println("Verbo a comparar: "+verbos[numVerbo][numForma]);

        if(txtVerbo.getText().toString().equals(verbos[numVerbo][numForma])){
            puntuacionJugada++;
        }


        txtVerbo.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_juego, menu);
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
