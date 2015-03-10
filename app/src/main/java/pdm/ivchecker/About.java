package pdm.ivchecker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class About extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
    }


    // ## CREACIÓN DEL MENÚ ##
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflamos el menú; esta orden crea el menú que está definido en /res/menu/menu_about.xml y lo asocia
        //con el botón menu del terminal en esa actividad.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    // ## MANEJO DE OPCIONES DEL MENÚ ##
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
