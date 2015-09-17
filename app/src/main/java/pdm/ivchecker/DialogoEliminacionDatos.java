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
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
public class DialogoEliminacionDatos extends DialogFragment {

    private Button botonBorrar, botonCancelar;

    HistorialPuntuacionTraining padre;

    public void setPadre(HistorialPuntuacionTraining padre){
        this.padre=padre;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity());

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialogo_eliminacion_datos);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        dialog.show();


        botonBorrar=(Button)dialog.findViewById(R.id.buttonBorrar);
        botonCancelar=(Button)dialog.findViewById(R.id.buttonCancelar);

        botonBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Llamamos a una función de HistorialuntuacionTraining
                        padre.eliminarHistorial();
                        //Cerramos después.
                        dismiss();
                    }
                }
        );

        botonCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Sólo cerramos el dialogo.
                dismiss();
            }
        });


        return dialog;
    }



}
