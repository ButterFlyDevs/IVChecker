package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TrainingAreaInicio extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_area_inicio);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_training_area_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch(item.getItemId()){
            //Usamos los identificadores de menu_training_area_inicio.xml (@+id) para definirles una acción.

            //Para ir a la configuracion
            case R.id.Menu_Tr_Opc1:
                intent  = new Intent(TrainingAreaInicio.this, Configuracion.class);
                //Iniciamos la nueva actividad
                startActivity(intent);
                return true;

            //Para ir a las estadisticas
            case R.id.Menu_Tr_Opc2:
                intent = new Intent(TrainingAreaInicio.this, Resultados.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
