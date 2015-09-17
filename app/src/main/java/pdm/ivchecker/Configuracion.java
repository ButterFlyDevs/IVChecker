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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

public class Configuracion extends ActionBarActivity {

    private Switch smartVerb_switch=null;
    private Button botonOK, botonBack;
    private SegmentedGroup tipoLista;
    private SegmentedGroup smart;
    private RadioButton botonListaFacil, botonListaMedia, botonListaDificil;
    private RadioButton smartOn, smartOf;
    private DiscreteSeekBar barNumVerbos;


    private int smartVerb=0, lista_a_preguntar=0,numero_verbos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        botonOK = (Button) findViewById(R.id.buttonOk);
        botonBack = (Button) findViewById(R.id.buttonBack);
        tipoLista = (SegmentedGroup) findViewById(R.id.grupoLista);
        smart = (SegmentedGroup) findViewById(R.id.smartGroup);

        botonListaFacil = (RadioButton) findViewById(R.id.RadioButtonFacil);
        botonListaMedia = (RadioButton) findViewById(R.id.RadioButtonMedio);
        botonListaDificil = (RadioButton) findViewById(R.id.RadioButtonDificil);

        smartOn=(RadioButton)findViewById(R.id.smartON);
        smartOf=(RadioButton)findViewById(R.id.smartOF);

        barNumVerbos = (DiscreteSeekBar) findViewById(R.id.bar);


        botonBack.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent  = new Intent(Configuracion.this, TrainingAreaInicio.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );


        //Comprobamos los datos introducidos
        botonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Comprobamos la lista pedida comparando con cada uno de los botones
                int ID_lista1 = tipoLista.getCheckedRadioButtonId();
                if (ID_lista1 == botonListaFacil.getId())
                    lista_a_preguntar = 1;
                if (ID_lista1 == botonListaMedia.getId())
                    lista_a_preguntar = 2;
                if (ID_lista1 == botonListaDificil.getId())
                    lista_a_preguntar = 3;


                int ID_lista2 = smart.getCheckedRadioButtonId();
                if (ID_lista2 == smartOn.getId())
                    smartVerb = 0;
                if (ID_lista2 == smartOf.getId())
                    smartVerb = 1;


                //Comprobamos el numero de verbos
                numero_verbos = barNumVerbos.getProgress();

                //Devolvemos los datos a la Actividad TrainigAreaInicio (que llamó a esta actividad Configuracion)
                Intent intent = new Intent();
                intent.putExtra("lista", lista_a_preguntar);
                intent.putExtra("smartVerb", smartVerb);
                intent.putExtra("numero_verbos", numero_verbos);
                setResult(RESULT_OK, intent);
                /*
                    Si quisieramos cancelar los datos y ponerlos a valores por defecto (programar otro boton Cancelar):
                    Intent returnIntent = new Intent();
                    setResult(RESULT_CANCELED, returnIntent);
                 */
                //Acabamos esta actividad
                finish();
            }
        });

    }

}
