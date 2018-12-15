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
public class MultiNonUniformMutationPercentNeurona {

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
     * @param val_min // valor minimo del dna
     * @param val_max // valor maximo del dna
     * @param genAct // epoca actual
     * @param getMax // epoca total
     * @param neuronasCapas cantidad de neuronas que tiene determinada capa
     * @param pesosNeuronaCapa cantidad de pesos que tiene una neurona en
     * determinada capa
     * @return un listado con los idividuos mutados
     */
    public static List<Individuo> mutacion(List<Individuo> population_nocruzada,
            int mutacion_size, double val_min, double val_max, int genAct, int getMax,
            int[] neuronasCapas, int[] pesosNeuronaCapa, double porcentajeMutacion
    ) {
        porcentaje = porcentajeMutacion;
        //System.out.println("poblacion no cruzada:\t" + population_nocruzada.size());
        SimpleMatrix vdir = population_nocruzada.get(0).getDna().copy();
        SimpleMatrix vmax = population_nocruzada.get(0).getDna().copy();
        SimpleMatrix step = population_nocruzada.get(0).getDna().copy();
        SimpleMatrix vPorcentaje = population_nocruzada.get(0).getDna().copy();
        vdir.zero();
        vPorcentaje.zero();
        setPorcentaje(vPorcentaje, porcentaje, neuronasCapas, pesosNeuronaCapa);
        setDir(vdir);
        List<Individuo> aux = new ArrayList<>();
        for (int i = 0; i < mutacion_size; i++) {
            int idx = r.nextInt(population_nocruzada.size() - 1);
            Individuo ind = population_nocruzada.get(idx);
            setDir(vdir);
            setMaxChg(vmax, vdir, ind.getDna(), val_min, val_max);
            step(step, genAct, getMax);
            ind.setDna(sol(ind.getDna(), vdir, step, vmax, vPorcentaje));
            aux.add(ind);
            population_nocruzada.set(idx, ind);
        }
        return aux;
    }

    /**
     *
     * @param vPorcentaje vector mascara para guardar los genes a mutar
     * @param porcentajeMutacion
     * @param first_gen posición inicial de la mutación
     * @param last_gen posición final de la mutación
     */
    private static void setPorcentaje(
            SimpleMatrix vPorcentaje, double porcentajeMutacion,
            int[] neuronasCapas, int[] pesosNeuronaCapa) {
        int neuronas = neuronasCapas[0];// solo capa oculta
        int pesos = pesosNeuronaCapa[0];
        int neuronaSeleccionada = r.nextInt(neuronas);
        int first_gen = neuronaSeleccionada * pesos;
        int last_gen = first_gen + pesos;
        for (int i = first_gen; i < last_gen; i++) {
            if (r.nextDouble() < porcentajeMutacion) {
                vPorcentaje.set(i, 1);
            }
        }
    }

    private static void setPorcentajeLastLayer(
            SimpleMatrix vPorcentaje, double porcentajeMutacion,
            int[] neuronasCapas, int[] pesosNeuronaCapa, double desviacion) {

        int L = neuronasCapas.length;//capas
        //int cantidadNeuronasCapasAnteriores = 0;
        int cantidadPesosCapasAnteriores = 0;
        //neuronas antes de la capa de salida
        for (int i = 0; i < L - 1; i++) {
            //cantidadNeuronasCapasAnteriores += neuronasCapas[i];
            cantidadPesosCapasAnteriores += neuronasCapas[i]*pesosNeuronaCapa[i];
        }
        int neuronas = neuronasCapas[L - 1];// neuronas capa de salida
        int pesos = pesosNeuronaCapa[L - 1];// Pesos por neurona capa salida
        int neuronaSeleccionada = r.nextInt(neuronas);//seleccionada de la ultima capa
        int first_gen = neuronaSeleccionada * pesos + cantidadPesosCapasAnteriores;//posiciones para la ultima capa
        int last_gen = first_gen + pesos;//ultimo peso
        for (int i = first_gen; i < last_gen; i++) {
            if (r.nextDouble() < porcentajeMutacion) {
                vPorcentaje.set(i, 1);
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

}
