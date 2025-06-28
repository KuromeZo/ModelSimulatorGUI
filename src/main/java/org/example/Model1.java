package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Model1 {
    @Bind
    private int LL;

    @Bind
    private double[] LATA;

    @Bind
    private double[] twKI;

    @Bind
    private double[] twKS;

    @Bind
    private double[] twINW;

    @Bind
    private double[] twEKS;

    @Bind
    private double[] twIMP;

    @Bind
    private double[] KI;

    @Bind
    private double[] KS;

    @Bind
    private double[] INW;

    @Bind
    private double[] EKS;

    @Bind
    private double[] IMP;

    @Bind
    private double[] PKB;

    private Map<String, double[]> dynamicVariables = new HashMap<>();

    public Model1() {}

    public void setYears(String[] parts) {
        LL = parts.length - 1; // We determine the number of years
    }

    // Method for setting a dynamic variable
    public void setDynamicVariable(String name, double[] values) {
        dynamicVariables.put(name, values);
    }

    // Method to get all dynamic variables
    public Map<String, double[]> getDynamicVariables() {
        return dynamicVariables;
    }

    public int getLL() {
        return LL;
    }

    public double[] getLATA() {
        return LATA;
    }

    public double[] getTwKI() {
        return twKI;
    }

    public double[] getTwKS() {
        return twKS;
    }

    public double[] getTwINW() {
        return twINW;
    }

    public double[] getTwEKS() {
        return twEKS;
    }

    public double[] getTwIMP() {
        return twIMP;
    }

    public double[] getKI() {
        return KI;
    }

    public double[] getKS() {
        return KS;
    }

    public double[] getINW() {
        return INW;
    }

    public double[] getEKS() {
        return EKS;
    }

    public double[] getIMP() {
        return IMP;
    }

    public double[] getPKB() {
        return PKB;
    }

    public void run() {
        PKB = new double[LL];
        PKB [ 0 ] = KI [ 0 ] + KS [ 0 ] + INW [ 0 ] + EKS [ 0 ] - IMP [ 0 ] ;
        for (int t = 1; t < LL; t++) {
            KI[t] = twKI[t] * KI[t - 1];
            KS[t] = twKS[t] * KS[t - 1];
            INW[t] = twINW[t] * INW[t - 1];
            EKS[t] = twEKS[t] * EKS[t - 1];
            IMP[t] = twIMP[t] * IMP[t - 1];
            PKB[t] = KI[t] + KS[t] + INW[t] + EKS[t] - IMP[t];
        }
    }
}
