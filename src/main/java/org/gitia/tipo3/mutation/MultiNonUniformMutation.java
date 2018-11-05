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
package org.gitia.tipo3.mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.gitia.tipo3.population.Individuo;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class MultiNonUniformMutation {

    static Random r = new Random();

    //public static void mutation(List<Individuo> population, SimpleMatrix bounds, double mutation, double elite, double min, double max) {
    public static void mutacion(List<Individuo> population, 
            double mutation, double elite, double max, double min, int genAct, int getMax) {

        SimpleMatrix vdir = population.get(0).getDna().copy();
        SimpleMatrix vmax = population.get(0).getDna().copy();
        SimpleMatrix step = population.get(0).getDna().copy();
        vdir.zero();
        setDir(vdir);
        int eliteSize = (int) Math.round(population.size() * elite);
        int size = (mutation > 0) ? population.size() - eliteSize : population.size();
        List<Individuo> aux = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (r.nextDouble() < mutation) {
                Individuo ind = new Individuo();
                setDir(vdir);
                setMaxChg(vmax, vdir, population.get(i).getDna(), max, min);
                step(step, genAct, getMax);
                ind.setDna(sol(ind.getDna(), vdir, step, vmax));
                aux.add(ind);
            }else{
                aux.add(population.get(i));
            }
        }
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
    static public void setMaxChg(SimpleMatrix vMaxChg, SimpleMatrix dir, SimpleMatrix ind, double max, double min) {
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

    static public SimpleMatrix sol(SimpleMatrix ind, SimpleMatrix dir, SimpleMatrix step, SimpleMatrix maxChange) {
        SimpleMatrix sol = ind.copy();
        sol = sol.plus(dir.elementMult(step).elementMult(maxChange));
        return sol;
    }
}
