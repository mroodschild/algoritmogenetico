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
package org.gitia.ag.crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.gitia.ag.population.Individuo;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class CubeCrossOver {

    static Random r = new Random();

    /**
     * enviamos la lista de individuos a cruzar, por cada par de padres se generan 2 hijos
     *
     * @param population lista completa de individuos
     * @param paring indices de individuos a cruzar p1 con p2
     * @return
     */
    public static List<Individuo> crossover(List<Individuo> population, int[][] paring) {
        List<Individuo> crossPop = new ArrayList<>();
        for (int[] paring1 : paring) {
            SimpleMatrix p1 = population.get(paring1[0]).getDna();
            SimpleMatrix p2 = population.get(paring1[1]).getDna();
            SimpleMatrix son1 = cross(p1, p2);
            SimpleMatrix son2 = cross(p2, p1);
            crossPop.add(new Individuo(son1, 0));
            crossPop.add(new Individuo(son2, 0));
        }
        return crossPop;
    }

    /**
     * random entre -1.25 a 1.25
     * @param p1
     * @param p2
     * @return R .* (p2 - p1) + p1
     */
    static private SimpleMatrix cross(SimpleMatrix p1, SimpleMatrix p2) {
        SimpleMatrix rand = SimpleMatrix.random_DDRM(1, p1.getNumElements(), -0.5, 0.5, r);
        SimpleMatrix son = p2.minus(p1).elementMult(rand).plus(p1);
        return son;
    }
}
