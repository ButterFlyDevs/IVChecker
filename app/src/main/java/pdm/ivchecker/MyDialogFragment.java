package pdm.ivchecker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by juan on 7/05/15.
 */
public class MyDialogFragment extends DialogFragment {


    private TextView label;

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
            label=(TextView)v.findViewById(R.id.label);
            label.setText(((Bundle)this.getArguments()).getString("id"));
        }

        return v;
    }

}