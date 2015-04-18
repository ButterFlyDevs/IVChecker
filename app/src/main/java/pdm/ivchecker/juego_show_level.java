package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;


public class juego_show_level extends ActionBarActivity {

    int nivel;

    private TextView texto, texto2;

    public juego_show_level(){
        nivel=0;
    }



    public void configuradorMensajePantalla(int nivel){

        String tS=""; //textoSecundario

        if(nivel==0)
            this.nivel=1;
        else
            this.nivel=nivel;


        texto.setText("Level " + this.nivel);

        switch (this.nivel) {

            case 1:
                tS="Are you ready?";
                break;
            case 2:
                tS="It's good";
                break;
            case 3:
                tS="You are a master!";
                break;
            case 4:
                tS="You are a hero!";
                break;


            default:
                tS="Default";
                break;

        }
        texto2.setText(tS);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("COnstructor de juego_show_level");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_show_level);

        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //Depuración de los intent.
        System.out.println("onCreate getIntent() :> "+getIntent().getStringExtra("nivel"));




        texto=(TextView)findViewById(R.id.text);
        texto2=(TextView)findViewById(R.id.text2);
        /*
        //Si es la primera vez qu ese entra nos e recibirán datos desde juego y por tanto será el nivel 1.
        if(getIntent().getIntExtra("nivel",0)==0) {
            nivel = 1;
            texto.setText("Level "+nivel);
            texto2.setText("Ready to learn?");

        }else{
            nivel=getIntent().getIntExtra("nivel",0);
            texto.setText("Level "+nivel);
            texto2.setText("You are a master!");
        }
        */
        configuradorMensajePantalla(getIntent().getIntExtra("nivel",0));


        Handler handler = new Handler();
        boolean nivel1 = handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(juego_show_level.this, Juego.class);

                //Vamos a la activity juego con el nivel 1
                intent.putExtra("nivel", nivel);

                startActivity(intent);
            }
        }, 1500);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_juego_show_level, menu);
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
