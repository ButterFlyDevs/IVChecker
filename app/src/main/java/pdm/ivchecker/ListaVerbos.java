package pdm.ivchecker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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


        //Dependiendo de la elección se abre un fichero u otro:
        if(lista.equals("soft")) {
            //Abrimos el flujo del fichero almacenado en la carpeta denro de res llamada raw con el nombre ivsoft
            inputStream = getResources().openRawResource(R.raw.ivsoft);
        }
        if(lista.equals("medium")){
            inputStream=getResources().openRawResource(R.raw.ivmedium);
        }
        if(lista.equals("hard")){
            inputStream=getResources().openRawResource(R.raw.ivhard);
        }


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
            txtVerbos.append(" ----- "+lista+" list ----- \n");
            while(true){
                line=reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                infinitivo = RowData[0];
                pasado = RowData[1];
                participio = RowData[2];
                //System.out.println(numero+"  "+infinitivo+" | "+pasado+" | "+participio+"\n");
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

        //Lo primero que se hace es recoger los datos enviados a esta actividad.
        Bundle extras = getIntent().getExtras();

        /*
        Si no se han recibido datos porque es la primera vez que se abre el activity se muestra la lista
         soft.
        */
        if(extras==null)
            mostrarVerbos("soft");
        //Si se han enviado datos se llama a mostrarVerbos con la lista específica.
        else
            mostrarVerbos(extras.getString("lista"));

    }

    /*
    Sobrescribimos el método onKeyDown para programar los pocos
    botones físicos que dispone el terminal.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        Intent intent = new Intent(ListaVerbos.this, ActividadPrincipal.class);

        switch(keyCode){
            //Programación del botón atrás del terminal.
            case KeyEvent.KEYCODE_BACK:
                System.out.println("Pulsado botón atrás");
                startActivity(intent);

        }
        return super.onKeyDown(keyCode, event);
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

                //Enviamos a la actividad el string "soft" en la variable "lista" para que este mismo intent lo coja.
                intent.putExtra("lista","soft");
                //Iniciamos la nueva actividad, que es la misma donde estamos.
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
