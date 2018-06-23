/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gitia.algoritmogenetico;

import java.util.Comparator;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class FitComparatorMaxMin implements Comparator<Fit> {

    @Override
    public int compare(Fit o1, Fit o2) {
        return (int) (o2.getVal() - o1.getVal());
    }

}
