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
    String [][] verbos;
    //Flujo de entrada para la lectura de fichero CSV:
    private InputStream inputStream;

    //Botón de siguiente verbo
    private Button btnNext;
    private EditText txtVerbo;

    @Override
    //Método llamada cuando se crea por primera vez la actividad
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        //Obtenemos la referencia a ese botón de la vista
        btnNext=(Button)findViewById(R.id.nextButton);
        txtVerbo=(EditText)findViewById(R.id.formaMisteriosa);

        //Implementamos el evento click del botón btnJugar:
        btnNext.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(Juego.this, Juego.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );


        //Abrimos el flujo del fichero almacenado en la carpeta denro de res llamada raw con el nombre iv
        inputStream=getResources().openRawResource(R.raw.iv);

        //Abrimos el flujo con un buffer.
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        verbos = new String [5][3];

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
        TextView infinitivo=(TextView)findViewById(R.id.infinitivo);
        TextView pasado=(TextView)findViewById(R.id.pasado);
        TextView participio=(TextView)findViewById(R.id.participio);

        //Para la generación de números:
        Random rnd = new Random();
        int numVerbo, numForma, numLetrasForma;
        String misterio="";

        //Generamos el verbo a mostrar:
        numVerbo=(int)(rnd.nextDouble() * 4 + 0);

        //Generamos la forma que no aparecerá
        numForma=(int)(rnd.nextDouble() * 2 + 0);

        //Obtenermos el verbo que falta en forma de rallitas:
        numLetrasForma=verbos[numVerbo][numForma].length();
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
            infinitivo.setText(verbos[numVerbo][1]);

        if(numForma==2)
            participio.setText(misterio);
        else
            participio.setText(verbos[numVerbo][2]);

        //Después misterio vuelve a estar vacía
        misterio="";
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
