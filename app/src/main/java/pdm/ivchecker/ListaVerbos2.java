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
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ListaVerbos2 extends ActionBarActivity {

    private ArrayList<Verbo> listaVerbosPrueba;
    private Button buttonBack, buttonAtras, buttonSiguiente;
    private TextView textNombreLista;
    private Dificultad lista = Dificultad.FACIL;

    enum Dificultad {
        FACIL,
        MEDIA,
        DIFICIL
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lista_verbos2);

        //Asociamos los elementos de la vista:
        textNombreLista = (TextView) findViewById(R.id.textLista);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonAtras = (Button) findViewById(R.id.buttonAtras);
        buttonSiguiente = (Button) findViewById(R.id.buttonSiguiente);


        //Programación del botón atrás
        buttonBack.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ListaVerbos2.this, ActividadPrincipal.class);
                        startActivity(intent);
                    }
                }
        );

        //Programación del botón lista anterior.
        buttonAtras.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lista == Dificultad.FACIL) {
                            lista = Dificultad.DIFICIL;
                            textNombreLista.setText("LISTA DIFICIL");
                        } else if (lista == Dificultad.MEDIA) {
                            lista = Dificultad.FACIL;
                            textNombreLista.setText("LISTA FÁCIL");
                        } else if (lista == Dificultad.DIFICIL) {
                            lista = Dificultad.MEDIA;
                            textNombreLista.setText("LISTA MEDIA");
                        }
                        cargarLista();
                    }
                }
        );

        //Programación del botón lista siguiente
        buttonSiguiente.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lista == Dificultad.FACIL) {
                            lista = Dificultad.MEDIA;
                            textNombreLista.setText("LISTA MEDIA");
                        } else if (lista == Dificultad.MEDIA) {
                            lista = Dificultad.DIFICIL;
                            textNombreLista.setText("LISTA DIFICIL");
                        } else if (lista == Dificultad.DIFICIL) {
                            lista = Dificultad.FACIL;
                            textNombreLista.setText("LISTA FÁCIL");
                        }
                        cargarLista();
                    }
                }
        );


        //Ajuste de la zona de anuncios:

        // Buscamos el  AdView como recurso y cargamos la  solicitud.
        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) //Cualquier emulador
                        //.addTestDevice("xxx") //El telefono de desarrollo
                .build();
        adView.loadAd(adRequest);

        //Por defecto cargamos la lista facil.
        cargarLista();


    }


    public void cargarLista() {

        InputStream inputStr = null;

        if (lista == Dificultad.FACIL)
            inputStr = getResources().openRawResource(R.raw.ivsoft2);
        if (lista == Dificultad.MEDIA)
            inputStr = getResources().openRawResource(R.raw.ivmedium2);
        if (lista == Dificultad.DIFICIL)
            inputStr = getResources().openRawResource(R.raw.ivhard2);


        listaVerbosPrueba = ProcesadorCSVs.obtenerVerbos(inputStr);

                /* ELIMINAME
                System.out.println("VERBOS CARGADOS: "+listaVerbosPrueba.size());
                for(Verbo v: listaVerbosPrueba)
                    System.out.println(v.toString());
                */

        //1º Asociamos el listview de la vista:
        final ListView listview = (ListView) findViewById(R.id.listView);

        //Especificamos que la lista sólo permita selección única
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //Programación del evento pulsar sobre un elementod e la lista.
        listview.setOnItemClickListener(

                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int posicion, long arg3) {
                        System.out.println("PULSADO BOTÓN DE LISTA " + Integer.toString(posicion));

                        FragmentDetalleVerbo fragment1 = new FragmentDetalleVerbo();


                        //Le enviamos al fragment el verbo del que queremos mostrar la información.
                        fragment1.setVerbo(listaVerbosPrueba.get(posicion));

                        //Hacemos que se muestre el fragment
                        fragment1.show(getFragmentManager(), "");

                    }

                }


        );


        //3º Iniciamos el adaptador y le pasamos la lista
        AdaptadorListVerbos adapter = new AdaptadorListVerbos(this, listaVerbosPrueba);

        //4º Asociamos el adaptador definido a nuestro listView
        listview.setAdapter(adapter);

    }

}






