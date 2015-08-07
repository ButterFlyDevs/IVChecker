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

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class DialogoInfoEntrenamiento extends DialogFragment {

    private Button botonEntendido;
    private TextView textoTitulo, textoFrase1, textoFrase2, textoFrase3;

    private String textoParaTitulo="Entrenamiento ";
    private String textoParaFrase1="E.Inteligente: ";
    private String textoParaFrase2="Dificultad verbos: ";
    private String textoParaFrase3="Nº verbos: ";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialogo_area_entrenamiento);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        dialog.show();


        //Asociamos los elementos de la vista
        botonEntendido = (Button) dialog.findViewById(R.id.buttonEntendido);
        textoTitulo = (TextView) dialog.findViewById(R.id.textViewTitulo);
        textoFrase1 = (TextView) dialog.findViewById(R.id.textViewFrase1);
        textoFrase2 = (TextView) dialog.findViewById(R.id.textViewFrase2);
        textoFrase3 = (TextView) dialog.findViewById(R.id.textViewFrase3);

        textoTitulo.setText(textoParaTitulo);
        textoFrase1.setText(textoParaFrase1);
        textoFrase2.setText(textoParaFrase2);
        textoFrase3.setText(textoParaFrase3);



        botonEntendido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return dialog;
    }

    public void setRestoTitulo(String restanteDelTitulo){
        textoParaTitulo+=restanteDelTitulo;
    }
    public void setEntrenamientoInteligente(String restanteFrase1){
        textoParaFrase1+=restanteFrase1;
    }
    public void setDificultadVerbos(String restanteFrase2){
        textoParaFrase2+=restanteFrase2;
    }
    public void setNumVerbos(String restanteFrase3){
        textoParaFrase3+=restanteFrase3;
    }


}
