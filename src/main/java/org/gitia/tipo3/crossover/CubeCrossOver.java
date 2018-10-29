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
package org.gitia.tipo3.crossover;

import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.gitia.tipo3.population.Individuo;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class CubeCrossOver {

    static Random r = new Random();

    public static void crossover(List<Individuo> population, double elite) {
        int eliteSize = (int) Math.round(population.size() * elite);
        int size = (elite > 0) ? population.size() - eliteSize : population.size();
        for (int i = 0; i < size; i++) {
            int idx1 = r.nextInt(population.size());
            SimpleMatrix p1 = population.get(idx1).getDna();
            int idx2 = r.nextInt(population.size());
            while (idx1 == idx2) {
                idx2 = r.nextInt(population.size());
            }
            SimpleMatrix p2 = population.get(idx2).getDna();
            SimpleMatrix son = cross(p1, p2);
            population.get(i).setDna(son);//corregir cruzamos a los hijos
        }
    }

    static private SimpleMatrix cross(SimpleMatrix p1, SimpleMatrix p2) {
        SimpleMatrix son = new SimpleMatrix(1, p1.numCols());
        for (int i = 0; i < p1.numCols(); i++) {
            double v = (r.nextDouble() < 0.5) ? p1.get(i) : p2.get(i);
            son.set(i, v);
        }
        return son;
    }
}
