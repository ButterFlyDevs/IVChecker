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
