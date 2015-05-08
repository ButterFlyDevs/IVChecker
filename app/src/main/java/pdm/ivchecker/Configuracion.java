package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class Configuracion extends ActionBarActivity {

    private Spinner spinner_smartVerb, spinner_num_verbos;
    private Button botonOK;
    private RadioGroup tipoLista;
    private RadioButton boton_soft, boton_medium, boton_hard;

    private int smartVerb=0, lista_a_preguntar=0,numero_verbos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        botonOK = (Button) findViewById(R.id.botonConfirmarCconfiguracion);
        tipoLista = (RadioGroup) findViewById(R.id.tipoLista);
        boton_soft = (RadioButton) findViewById(R.id.radioSoft);
        boton_medium = (RadioButton) findViewById(R.id.radioMedium);
        boton_hard = (RadioButton) findViewById(R.id.radioHard);

        //Rellenamos los spinner
        rellenar_spinner_NumVerbos();
        rellenar_smartVerb();
        //Comprobamos los datos introducidos
        botonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Comprobamos la lista pedida comparando con cada uno de los botones
                int ID_lista = tipoLista.getCheckedRadioButtonId();
                if(ID_lista == boton_soft.getId())
                    lista_a_preguntar = 1;
                if(ID_lista == boton_medium.getId())
                    lista_a_preguntar = 2;
                if(ID_lista == boton_hard.getId())
                    lista_a_preguntar = 3;

                //Comprobamos SmartVerb
                String respuesta_smartVerb = String.valueOf(spinner_smartVerb.getSelectedItem() );
                System.out.println(respuesta_smartVerb);
                if(respuesta_smartVerb == "Yes")
                    smartVerb=0;
                else
                    smartVerb=1;

                //Comprobamos el numero de verbos
                numero_verbos = Integer.parseInt( String.valueOf (spinner_num_verbos.getSelectedItem() ) );
                //Devolvemos los datos a la Actividad TrainigAreaInicio (que llamó a esta actividad Configuracion)
                Intent intent = new Intent();
                intent.putExtra("lista",lista_a_preguntar);
                intent.putExtra("smartVerb",smartVerb);
                intent.putExtra("numero_verbos",numero_verbos);
                setResult(RESULT_OK,intent);
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
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_num_verbos.setAdapter(dataAdapter);
    }

    private void rellenar_smartVerb(){
        spinner_smartVerb= (Spinner) findViewById(R.id.spinnerSmartVerb);
        List<String> list = new ArrayList<String>();
        list.add("Yes");
        list.add("No");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_smartVerb.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracion, menu);
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
