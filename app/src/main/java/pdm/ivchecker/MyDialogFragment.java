package pdm.ivchecker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by juan on 7/05/15.
 */
public class MyDialogFragment extends DialogFragment {


    private TextView textNivel, textMotivacion, textDescripcion;

    public static MyDialogFragment newInstance(Bundle arguments){
        MyDialogFragment f = new MyDialogFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);



    }

    @Override
    public void onResume(){
        super.onResume();

    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Cargamos el layout del fragment
        View v = inflater.inflate(R.layout.my_fragment, container, false);



        if(v!=null){
            textNivel=(TextView)v.findViewById(R.id.textNivel);
            textMotivacion=(TextView)v.findViewById(R.id.textMotivacion);
            textDescripcion=(TextView)v.findViewById(R.id.textDescripcion);

            ajustaTextos();
        }

        return v;
    }


    public void ajustaTextos(){

        int nivel=Integer.parseInt((this.getArguments()).getString("id"));

        //Mostramos el nivel:

            textNivel.setText(getString(R.string.nivel)+Integer.toString(nivel));

        //La motivación y la descripción depende del nivel:

            String tM=""; //Texto Motivación
            String tD=""; //Texto Descripción


          //  tM=getString(R.string.tM<nivel>);

            /*
            switch(nivel){
                case 1:
                    tM=getString(R.string.tM1);
                    tD=getString(R.string.tD1);
                    break;
                case 2:
                    tM=getString(R.string.tM2);
                    tD=getString(R.string.tD2);
                    break;
                tM=getString(R.string.tM2);
                tD=getString(R.string.tD2);
                break;
            }
            */

        try {
            Field fieldtM = R.string.class.getField("tM"+nivel);
            tM = getString(fieldtM.getInt(null));

            Field fieldtD = R.string.class.getField("tD"+nivel);
            tD = getString(fieldtD.getInt(null));

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        textMotivacion.setText(tM);
        textDescripcion.setText(tD);

    }


}