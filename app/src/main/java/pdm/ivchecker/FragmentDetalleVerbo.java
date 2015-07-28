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

/**
 * Created by juan on 23/07/15.
 */
public class FragmentDetalleVerbo extends DialogFragment{

    //ELementos que necesitamos referenciar desde el layout para su modificación.
    public Button botonCierre;

    public TextView textViewVerbo;

    public TextView textViewInfinitivo;
    public TextView textViewPasado;
    public TextView textViewParticipio;

    public TextView textViewTFInfinitivo;
    public TextView textViewTFPasado;
    public TextView textViewTFParticipio;

    public TextView textViewSignificado;
    public TextView textViewEjemplos;

    public String textoVerbo="";

    public String textoInfinitivo="";
    public String textoPasado="";
    public String textoParticipio="";

    public String textoTFInfinitivo="";
    public String textoTFPasado="";
    public String textoTFParticipio="";


    public String textoSignificado="";

    public String textoEjemplos="";


    /**
     * La función que realmente setea los datos en el fragment
     * @param verbo
     */
    public void setVerbo(Verbo verbo){

        textoVerbo="to "+verbo.getVerbo().infinitivo;
        textoInfinitivo=verbo.getVerbo().infinitivo;
        textoParticipio=verbo.getVerbo().participio;
        textoPasado=verbo.getVerbo().pasado;

        textoTFInfinitivo=verbo.getTranscripcionFonetica().tfInfinitivo;
        textoTFParticipio=verbo.getTranscripcionFonetica().tfParticipio;
        textoTFPasado=verbo.getTranscripcionFonetica().tfPasado;

        textoSignificado=verbo.getSignificado().significadoES;



        //Si la lista de ejemplos no está vacía:
        if (!verbo.getEjemplos().isEmpty()){
            for(Verbo.FraseEjemplo frase: verbo.getEjemplos())
                textoEjemplos+=frase.ejemplo+"\n"+frase.traduccionES+"\n";
        }

    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Asociamos el layout del diálogo.
        dialog.setContentView(R.layout.layout_detalle_verbo);
        //Hacemos que el fondo sea transparente.
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Mostramos el diálogo
        dialog.show();

        //Asociamos los elementos del layout:
            botonCierre = (Button) dialog.findViewById(R.id.botonCierre);

            textViewVerbo = (TextView) dialog.findViewById(R.id.Verbo);

            textViewInfinitivo = (TextView) dialog.findViewById(R.id.textInfinitivo);
            textViewPasado = (TextView) dialog.findViewById(R.id.textPasado);
            textViewParticipio = (TextView) dialog.findViewById(R.id.textParticipio);

            textViewTFInfinitivo = (TextView) dialog.findViewById(R.id.TFInfinitivo);
            textViewTFPasado = (TextView) dialog.findViewById(R.id.TFPasado);
            textViewTFParticipio = (TextView) dialog.findViewById(R.id.TFParticipio);

            textViewSignificado = (TextView) dialog.findViewById(R.id.significado);
            textViewEjemplos = (TextView) dialog.findViewById(R.id.ejemplos);


        textViewVerbo.setText(textoVerbo);

        textViewInfinitivo.setText(textoInfinitivo);
        textViewPasado.setText(textoPasado);
        textViewParticipio.setText(textoParticipio);

        textViewTFInfinitivo.setText(textoTFInfinitivo);
        textViewTFPasado.setText(textoTFPasado);
        textViewTFParticipio.setText(textoTFParticipio);


        textViewSignificado.setText(textoSignificado);

        textViewEjemplos.setText(textoEjemplos);

        botonCierre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Cerramos el DialogFragment
                dismiss();
            }
        });
        return dialog;
    }
}
