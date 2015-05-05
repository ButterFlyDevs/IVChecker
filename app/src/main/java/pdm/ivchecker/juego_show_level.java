package pdm.ivchecker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;


public class juego_show_level extends ActionBarActivity {

    int nivel;

    private TextView texto, texto2, texto3;

    public juego_show_level(){
        nivel=0;
    }



    public void configuradorMensajePantalla(int nivel){

        String tS=""; //textoSecundario
        String tD=""; //textoTerciario: Descripción de nivel.

        //Si es la primera vez qu ese entra nos e recibirán datos desde juego y por tanto será el nivel 1.
        if(nivel==0)
            this.nivel=1;
        else
            this.nivel=nivel;

        //Seteamos el texto de nivel:
        texto.setText("Level " + this.nivel);



        //Aparece por la izquierda:
        //texto.startAnimation(AnimationUtils.loadAnimation(juego_show_level.this, android.R.anim.slide_in_left));
        //texto.startAnimation(AnimationUtils.loadAnimation(juego_show_level.this, android.R.anim.fade_in));

        //Animacion creada en res/anim/ con nombre myanimation.
        final Animation myAnimation=AnimationUtils.loadAnimation(this,R.anim.myanimation);
        texto.startAnimation(myAnimation);


        //Seteamos el mensaje secundario que acompaña al nivel:
        switch (this.nivel) {
            case 1:
                tS="Are you ready?";
                tD="Uso de la lista simple con ayuda";
                break;
            case 2:
                tS="It's good";
                tD="Uso de la lista simple sin ayuda";
                break;
            case 3:
                tS="You are a master!";
                tD="Uso de la lista simple sin ayuda";
                break;
            case 4:
                tS="You are a hero!";
                break;
            case 5:
                tS="Oh my good #/?";
                break;
            case 6:
                tS="";
                break;
            case 7:
                tS="";
                break;
            case 8:
                tS="";
                break;
            case 9:
                tS="";
                break;
            case 10:
                tS="";
                break;
            case 11:
                tS="";
                break;
            case 12:
                tS="";
                break;
            case 13:
                tS="";
                break;
            case 14:
                tS="";
                break;
            case 15:
                tS="It's the final countdown!";
                break;

            default:
                tS="Oups!";
                break;
        }
        texto2.setText(tS);
        texto2.startAnimation(myAnimation);
        texto3.setText(tD);
        texto3.startAnimation(myAnimation);
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
        texto3=(TextView)findViewById(R.id.text3);

        //Configuramos el texto en pantalla según los datos (recibidos por la actividad que nos lanza) en el parámetro nivel:
        configuradorMensajePantalla(getIntent().getIntExtra("nivel",0));


        Handler handler = new Handler();
        boolean nivel1 = handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(juego_show_level.this, Juego.class);

                //Vamos a la activity juego con el nivel 1
                intent.putExtra("nivel", nivel);

                startActivity(intent);

                //Aplicacion de transicion animada entre activities:
                overridePendingTransition(R.anim.zoom_back_out, R.anim.zoom_back_in);
            }
        }, 1500);


    }



}
