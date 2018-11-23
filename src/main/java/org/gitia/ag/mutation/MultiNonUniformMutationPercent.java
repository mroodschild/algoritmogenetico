/*
 * Copyright 2018 Matías Roodschild <mroodschild@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gitia.ag.mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.gitia.ag.population.Individuo;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class MultiNonUniformMutationPercent {

    static double porcentaje = 0.05;

    static Random r = new Random();

    /**
     *
     * Esta funcion reemplaza en la lista de no cruzados a los idividuos
     * selecionados, por el mutado, ademas los devuelve en un listado
     *
     * @param population_nocruzada // poblacion que no participo en el
     * cruzamiento
     * @param mutacion_size //cantidad de individuos a mutar
     * @param min // valor minimo del dna
     * @param max // valor maximo del dna
     * @param genAct // epoca actual
     * @param getMax // epoca total
     * @return un listado con los idividuos mutados
     */
    public static List<Individuo> mutacion(List<Individuo> population_nocruzada,
            int mutacion_size, double min, double max, int genAct, int getMax) {

        //System.out.println("poblacion no cruzada:\t" + population_nocruzada.size());
        SimpleMatrix vdir = population_nocruzada.get(0).getDna().copy();
        SimpleMatrix vmax = population_nocruzada.get(0).getDna().copy();
        SimpleMatrix step = population_nocruzada.get(0).getDna().copy();
        SimpleMatrix vPorcentaje = population_nocruzada.get(0).getDna().copy();
        vdir.zero();
        vPorcentaje.zero();
        setPorcentaje(vPorcentaje, porcentaje);
        setDir(vdir);
        List<Individuo> aux = new ArrayList<>();
        for (int i = 0; i < mutacion_size; i++) {
            int idx = r.nextInt(population_nocruzada.size() - 1);
            Individuo ind = population_nocruzada.get(idx);
            setDir(vdir);
            setMaxChg(vmax, vdir, ind.getDna(), min, max);
            step(step, genAct, getMax);
            ind.setDna(sol(ind.getDna(), vdir, step, vmax, vPorcentaje));
            aux.add(ind);
            population_nocruzada.set(idx, ind);
        }
        return aux;
    }

    /**
     * determina el vector de direcciones de cambios
     *
     * @param dir
     */
    static public void setDir(SimpleMatrix dir) {
        for (int i = 0; i < dir.getNumElements(); i++) {
            dir.set(i, Math.pow(-1, Math.round(r.nextDouble())));
        }
    }

    /**
     * genera el vector de máximos cambios posibles
     *
     * @param vMaxChg aqui se guarda
     * @param dir direcciones generadas
     * @param ind individuo
     * @param max
     * @param min
     */
    static public void setMaxChg(SimpleMatrix vMaxChg, SimpleMatrix dir,
            SimpleMatrix ind, double min, double max) {
        for (int i = 0; i < vMaxChg.getNumElements(); i++) {
            if (dir.get(i) == 1) {
                vMaxChg.set(i, max - ind.get(i));
            } else {
                vMaxChg.set(i, ind.get(i) - min);
            }

        }
    }

    static public void step(SimpleMatrix step, int genAct, int genMax) {
        for (int i = 0; i < step.getNumElements(); i++) {
            step.set(i, r.nextDouble() * (1 - (double) genAct / (double) genMax));
        }
    }

    static public SimpleMatrix sol(SimpleMatrix ind, SimpleMatrix dir, SimpleMatrix step, SimpleMatrix maxChange, SimpleMatrix vPorcentaje) {
        SimpleMatrix sol = ind.copy();
        sol = sol.plus(dir.elementMult(vPorcentaje).elementMult(step).elementMult(maxChange));
        return sol;
    }

    private static void setPorcentaje(SimpleMatrix vPorcentaje, double d) {
        for (int i = 0; i < vPorcentaje.getNumElements(); i++) {
            if (r.nextDouble() < d) {
                vPorcentaje.set(i, 1);
            }
        }
    }

}
