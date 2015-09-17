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
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;


public class ActividadPrincipal extends ActionBarActivity {

    //Necesitamos dos variables para tener referenciados a los botones.
    private Button botonJugar, botonAreaEntrenamiento, botonListaVerbos, botonRankingMundial, botonAyuda;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        //Si pulsamos el botón back nos saca de la pantalla.
        if(keyCode==KeyEvent.KEYCODE_BACK){

            finish();
        }

        return super.onKeyDown(keyCode, event);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_principal);
        //Con esta orden conseguimos hacer que no se muestre la ActionBar.
        getSupportActionBar().hide();
        //Con esta hacemos que la barra de estado del teléfono no se vea y la actividad sea a pantalla completa.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Obtenemos una referencia a los controles de la interfaz.
        botonJugar=(Button)findViewById(R.id.botonJugar);
        botonAreaEntrenamiento=(Button)findViewById(R.id.botonAreaEntrenamiento);
        botonListaVerbos=(Button)findViewById(R.id.botonListaVerbos);
        botonRankingMundial=(Button)findViewById(R.id.botonRankingMundial);
        botonAyuda=(Button)findViewById(R.id.botonAyuda);



        //Cargamos la animacion desde R.anim folder
        Animation loopParpadeante = AnimationUtils.loadAnimation(this, R.anim.animacionbotonplay);
        Animation animacionSalida = AnimationUtils.loadAnimation(this, R.anim.animacionbotonplay2);
        //Lanzamos la animacion:
        //animacion.setRepeatCount(Animation.INFINITE);

        //
       // animacionEntrada.setRepeatCount(Animation.INFINITE);
        botonJugar.startAnimation(loopParpadeante);


        final Animation animacionBotones = AnimationUtils.loadAnimation(this,R.anim.myanimation);



        // ### Implementación de los eventos de botones: ###

        //Implementamos el evento click del botón btnJugar:
        botonJugar.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, Juego.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );
        botonAreaEntrenamiento.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        v.startAnimation(animacionBotones);
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, TrainingAreaInicio.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );

        botonListaVerbos.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.startAnimation(animacionBotones);
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, ListaVerbos2.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );
        botonRankingMundial.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, wcr.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);
                    }
                }
        );
        botonAyuda.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Creamos el Intent
                        Intent intent = new Intent(ActividadPrincipal.this, About.class);
                        //Iniciamos la nueva actividad
                        startActivity(intent);

                        //Aplicacion de transicion animada entre activities:
                        overridePendingTransition(R.anim.entrada_abajo, R.anim.salida_abajo);

                    }
                }
        );

    }
}
