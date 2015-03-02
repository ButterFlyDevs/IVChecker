package pdm.ivchecker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ListaVerbos extends ActionBarActivity {


    //Variable de texto donde se alamcenan los verbos para mostrarlos posteriormente:
    private TextView txtVerbos;
    //Flujo de entrada para la lectura de fichero CSV
    private InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_verbos);

        //Abrimos el flujo del fichero almacenado en la carpeta denro de res llamada raw con el nombre iv
        inputStream=getResources().openRawResource(R.raw.iv);

        //Abrimos el flujo con un buffer.
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        txtVerbos=(TextView)findViewById(R.id.TxtVerbos);

        //Construimos la lista de verbos irregulares:

        String infinitivo="";
        String pasado="";
        String participio="";
        try {
            String line;
            while(true){
                line=reader.readLine();
                if (line == null) break;
                String[] RowData = line.split(",");
                infinitivo = RowData[0];
                pasado = RowData[1];
                participio = RowData[2];
                txtVerbos.append(infinitivo+" | "+pasado+" | "+participio+"\n");
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_verbos, menu);
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
