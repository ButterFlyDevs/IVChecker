package pdm.ivchecker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//Para las pantalla completa:
import android.view.WindowManager;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ListaVerbos extends ActionBarActivity {


    //Variable de texto donde se alamcenan los verbos para mostrarlos posteriormente:
    private TextView txtVerbos;
    //Flujo de entrada para la lectura de fichero CSV
    private InputStream inputStream;
    BufferedReader reader;


    private void mostrarVerbos(String lista){


        //Abrimos el flujo del fichero almacenado en la carpeta denro de res llamada raw con el nombre iv
        inputStream=getResources().openRawResource(R.raw.iv);
        //Abrimos el flujo con un buffer.
        reader = new BufferedReader(new InputStreamReader(inputStream));
        //Asociamos el textView del diseño con la variable aquí:
        txtVerbos=(TextView)findViewById(R.id.TxtVerbos);


        //Construimos la lista de verbos irregulares:

        String infinitivo="";
        String pasado="";
        String participio="";
        int numero=1;
        try {
            String line;
            txtVerbos.append(" ----- "+lista+" List ----- \n");
            while(true){
                line=reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                infinitivo = RowData[0];
                pasado = RowData[1];
                participio = RowData[2];
                txtVerbos.append(numero+"  "+infinitivo+" | "+pasado+" | "+participio+"\n");
                numero++;
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_verbos);
        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Bundle extras = getIntent().getExtras();
        if(extras==null)
            mostrarVerbos("soft");
        else
            mostrarVerbos(extras.getString("lista"));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_verbos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Declaramos el camino desde esta activity a la misma activity.
        Intent intent = new Intent(ListaVerbos.this, ListaVerbos.class);

        switch (item.getItemId()) {

            //Acción del menú para ver la lista de verbos soft.
            case R.id.soft:
                //Iniciamos la nueva actividad, que es la misma donde estamos.
                intent.putExtra("lista","soft");
                startActivity(intent);
                return true;

            //Lista medium
            case R.id.medium:

                intent.putExtra("lista","medium");
                startActivity(intent);
                return true;

            //Lista medium
            case R.id.hard:
                intent.putExtra("lista","hard");
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
