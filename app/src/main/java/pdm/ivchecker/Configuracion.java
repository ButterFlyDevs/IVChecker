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

import java.util.ArrayList;
import java.util.List;


public class Configuracion extends ActionBarActivity {

    private Spinner spinner_num_verbos;
    private Switch smartVerb_switch=null;
    private Button botonOK;
    private RadioGroup tipoLista;
    private RadioButton boton_soft, boton_medium, boton_hard;

    private int smartVerb=0, lista_a_preguntar=0,numero_verbos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        botonOK = (Button) findViewById(R.id.botonConfirmarCconfiguracion);
        tipoLista = (RadioGroup) findViewById(R.id.tipoLista);
        boton_soft = (RadioButton) findViewById(R.id.radioSoft);
        boton_medium = (RadioButton) findViewById(R.id.radioMedium);
        boton_hard = (RadioButton) findViewById(R.id.radioHard);


        //Rellenamos el spinner y preparamos el Switch
        rellenar_spinner_NumVerbos();
        smartVerb_switch = (Switch) findViewById(R.id.switch_smartVerb);

        //Comprobamos los datos introducidos
        botonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Comprobamos la lista pedida comparando con cada uno de los botones
                int ID_lista = tipoLista.getCheckedRadioButtonId();
                if (ID_lista == boton_soft.getId())
                    lista_a_preguntar = 1;
                if (ID_lista == boton_medium.getId())
                    lista_a_preguntar = 2;
                if (ID_lista == boton_hard.getId())
                    lista_a_preguntar = 3;

                //Comprobamos SmartVerb
                if (smartVerb_switch.isChecked())
                    smartVerb = 0;
                else
                    smartVerb = 1;

                //Comprobamos el numero de verbos
                numero_verbos = Integer.parseInt(String.valueOf(spinner_num_verbos.getSelectedItem()));
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

    //Funcion para rellenar el spinner de numero de verbos a preguntar

    private void rellenar_spinner_NumVerbos() {

        spinner_num_verbos= (Spinner) findViewById(R.id.spinnerVerbos);
        List<String> list = new ArrayList<String>();
        list.add("3");
        list.add("6");
        list.add("9");
        list.add("12");
        list.add("15");
        list.add("18");
        list.add("21");
        list.add("24");
        list.add("27");
        list.add("30");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_num_verbos.setAdapter(dataAdapter);
    }

}
