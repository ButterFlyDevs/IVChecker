/*
        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.
        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
        GNU General Public License for more details.
        You should have received a copy of the GNU General Public License
        along with this program. If not, see <http://www.gnu.org/licenses/>.
        Copyright 2015 Jose A. Gonzalez Cervera
        Copyright 2015 Juan A. Fernández Sánchez
*/
package pdm.ivchecker;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



/*
¡¡¡¡¡¡
Cuando añadimos anidado el horizontalScrollView ya no podemos centrar los datos en la pantalla
como cuando no lo teníamos esto tiene que poder resolverse de alguna manera. !!!
 */



public class ListaVerbos extends ActionBarActivity {


    //Variable de texto donde se alamcenan los verbos para mostrarlos posteriormente:
    private TextView textNombreLista, textNumero, textInfinitivo, textPasado, textParticipio;
    private ScrollView scrollView;
    private Button botonSoft, botonMedium, botonHard;


    //Flujo de entrada para la lectura de fichero CSV
    private InputStream inputStream;
    BufferedReader reader;


    private void mostrarVerbos(String lista){


        //Reseteamos los campos (para no tener los valores usados de muestra en la vista del diseño):
            textNombreLista.setText("");
            textNumero.setText("");
            textInfinitivo.setText("");
            textPasado.setText("");
            textParticipio.setText("");

        //Dependiendo de la elección se abre un fichero u otro y cambiamos el color del fondo:
        if(lista.equals("soft")) {
            //Abrimos el flujo del fichero almacenado en la carpeta denro de res llamada raw con el nombre ivsoft
            inputStream = getResources().openRawResource(R.raw.ivsoft);
            textNombreLista.append(getString(R.string.listaSimple));
           // scrollView.setBackgroundColor(Color.rgb(255,228,169));
        }
        if(lista.equals("medium")){
            inputStream=getResources().openRawResource(R.raw.ivmedium);
            textNombreLista.append(getString(R.string.listaMedia));
            //scrollView.setBackgroundColor(Color.rgb(255,197,125));
        }
        if(lista.equals("hard")){
            inputStream=getResources().openRawResource(R.raw.ivhard);
            textNombreLista.append(getString(R.string.listaDificil));
            //scrollView.setBackgroundColor(Color.rgb(255,183,93));
        }

        //Abrimos el flujo con un buffer.
        reader = new BufferedReader(new InputStreamReader(inputStream));

        //Construimos la lista de verbos irregulares:




        int numero=1;

        try {
            String line;
            //Sacamos el número
            line=reader.readLine();

            while(true){

                line=reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                textNumero.append(numero+"\n\n\n\n");
                textInfinitivo.append(RowData[0]+"\n"+RowData[1]+"\n"+RowData[2]+"\n\n");


                // textPasado.append(RowData[1]+"\n");
                //textParticipio.append(RowData[2]+"\n");



                /*
                infinitivo = RowData[0];
                pasado = RowData[1];
                participio = RowData[2];
                //System.out.println(numero+"  "+infinitivo+" | "+pasado+" | "+participio+"\n");
                txtVerbos.append(numero+"  "+infinitivo+" | "+pasado+" | "+participio+"\n");
                */
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


        //Asociamos el textView del diseño con la variable aquí:
        textNombreLista=(TextView)findViewById(R.id.textNombreLista);
        textNumero=(TextView)findViewById(R.id.textNumero);
        textInfinitivo=(TextView)findViewById(R.id.textInfinitivo);
        textPasado=(TextView)findViewById(R.id.textPasado);
        textParticipio=(TextView)findViewById(R.id.textParticipio);

        scrollView=(ScrollView)findViewById(R.id.scrollView);

        botonSoft=(Button)findViewById(R.id.botonSoft);
        botonMedium=(Button)findViewById(R.id.botonMedium);
        botonHard=(Button)findViewById(R.id.botonHard);


        Animation animacionEntrada = AnimationUtils.loadAnimation(this, R.anim.animacionbotonplay);
        botonSoft.startAnimation(animacionEntrada);
        botonMedium.startAnimation(animacionEntrada);
        botonHard.startAnimation(animacionEntrada);


        botonSoft.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarVerbos("soft");
                    }
                }
        );

        botonMedium.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarVerbos("medium");
                    }
                }
        );

        botonHard.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarVerbos("hard");
                    }
                }
        );

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
}
