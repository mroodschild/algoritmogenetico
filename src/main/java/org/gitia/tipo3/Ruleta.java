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
package org.gitia.tipo3;

import java.util.List;
import java.util.Random;
import org.ejml.simple.SimpleMatrix;
import org.gitia.tipo3.population.Individuo;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
class Ruleta {

    static Random r = new Random();

    /**
     *
     * @param offspring para saber cuantos cruzamientos se requieren
     * @param population para poder seleccionar la elite en el cruzamiento
     * @return los indices de p1 y p2
     */
    static int[][] getParing(int offspring, List<Individuo> population) {
        SimpleMatrix tabla_tickets = getTickets(population);
        int cant = (int) (offspring / 2); // porque salen dos hijos cada dos padres
        int[][] indices = new int[cant][2];
        int p1, p2;
        for (int i = 0; i < cant; i++) {
            p1 = ruleta(tabla_tickets);
            p2 = ruleta(tabla_tickets);
            while (p1 == p2) {
                p2 = ruleta(tabla_tickets);
            }
            indices[i][0] = p1;
            indices[i][1] = p2;
        }
        return indices;
    }

    /**
     * genera los tickets para el sorteo de la ruleta
     *
     * @param population
     * @return
     */
    private static SimpleMatrix getTickets(List<Individuo> population) {
        SimpleMatrix tabla_tickets = new SimpleMatrix(population.size(), 2);
        double sum = 0;
        for (int i = 0; i < population.size(); i++) {
            Individuo ind = population.get(i);
            tabla_tickets.set(i, 0, sum);
            sum += (ind.getFitness() == 0) ? 0.1 : ind.getFitness();
            tabla_tickets.set(i, 1, sum);
        }
        return tabla_tickets.divide(sum);
    }

    /**
     * Hace un sorteo y devuelve el indice del ganador
     *
     * @param tabla tickets acorde al fitness
     * @return
     */
    private static int ruleta(SimpleMatrix tabla) {
        double sorteo = r.nextDouble();
        int idx = 0;
        for (int i = 0; i < tabla.numRows(); i++) {
            double min = tabla.get(i, 0);
            double max = tabla.get(i, 1);
            if (sorteo <= max && sorteo >= min) {
                idx = i;
                break;
            }
        }
        return idx;
    }

}
