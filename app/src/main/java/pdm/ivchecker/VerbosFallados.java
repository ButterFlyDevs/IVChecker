package pdm.ivchecker;

/**
 * Created by JOSE ANTONIO on 27/04/2015.
 *
 * Estructura temporal creada para poder ordenar los verbos fallados
 * por orden de más fallado a menos
 */
public class VerbosFallados {
    public Integer indice_verbo;
    public Integer veces_fallado;

    public VerbosFallados(int ind_ver, int vec_fallado){
        indice_verbo=ind_ver;
        veces_fallado=vec_fallado;
    }
    public boolean esMayor(VerbosFallados vf1, VerbosFallados vf2){
        if(vf1.veces_fallado > vf2.veces_fallado)
            return true;
        else
            return false;
    }

}
