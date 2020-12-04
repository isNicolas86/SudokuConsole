package com.sample;

import java.util.ArrayList;

/**
 * @author Nicolas Whaibe
 */
public class CellData {
    ArrayList<Integer> probNums;

    //Constructor assigning all numbers when the sudoku cell is empty/zero
    public CellData() {
        this.probNums = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            this.probNums.add(i+1);
        }
    }
    //Constructor assigning passed number
    public CellData(int n) {
        this.probNums = new ArrayList<>();
        probNums.add(n);
    }

    //Sending request to eliminate a number in the cell
    public void cancelCase(int numToCancel){
        if (probNums.indexOf(numToCancel)>=0) {
            probNums.remove(probNums.indexOf(numToCancel));
        }
    }

    public ArrayList<Integer> getProbNums() {
        return probNums;
    }
}
