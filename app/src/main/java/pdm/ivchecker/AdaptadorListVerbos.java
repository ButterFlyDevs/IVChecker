package pdm.ivchecker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by juan on 23/07/15.
 */
public class AdaptadorListVerbos extends ArrayAdapter<Verbo> {

    /**
     * Contexto donde se mostrará la lista.
     */
    private final Context context;
    /**
     *     Lista de objetos que serán los que se muestren en la lista.
     */
    private final ArrayList<Verbo> itemsArrayList;


    /**
     * Constructor del adaptador. Inicializa los datos del adaptador, contexto y lista de elementos
     * con los datos pasados. Si fuera necesario podría ser el momento de realizar algún cambio o mod.
     * en los datos.
     * @param context Contexto donde se usará la lista.
     * @param itemsArrayList Datos de la lista.
     */
    public AdaptadorListVerbos(Context context, ArrayList<Verbo> itemsArrayList){
        /**
         * Se llama al constructor del padre para inicializar el objeto de tipo ArrayAdapter del
         * que este hereda y el padre neceista conocer el contexto, el layout con el que se define
         * como se verá cada elemento en la lista y la propia lista de elementos.
         */
        super(context, R.layout.verbo, itemsArrayList);

        this.context = context;
        this.itemsArrayList=itemsArrayList;
    }

    /**
     * Configurador de cada uno de los elementos de la lista.
     * Se coge cada un de los objetos de la lista y se configura al layout como se quiera.
     * @param position Posicon en la lista que se pasa por argumento al constructor del adaptador.
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.verbo, parent, false);

        // 3. Asociamos los elementos.
        TextView infinitivo  = (TextView) rowView.findViewById(R.id.infinitivo);
        TextView pasado      = (TextView) rowView.findViewById(R.id.pasado);
        TextView participio  = (TextView) rowView.findViewById(R.id.participio);
        TextView Pos         = (TextView) rowView.findViewById(R.id.textPos);

        // 4. Seteamos los elementos con información
        infinitivo.setText(itemsArrayList.get(position).getVerbo().infinitivo);
        pasado.setText(itemsArrayList.get(position).getVerbo().pasado);
        participio.setText(itemsArrayList.get(position).getVerbo().participio);
        Pos.setText(Integer.toString(position+1));

        // 5. Devolvemos un el objeto fila.
        return rowView;
    }
}
