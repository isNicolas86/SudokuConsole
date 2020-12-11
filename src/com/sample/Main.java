package com.sample;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        int[][] table = {{0,0,0, 0,6,0, 0,0,0}, {0,0,0, 0,0,0, 2,0,7},
                {1,0,0,0,0,4,0,6,3}, {6,8,0,0,0,0,0,0,0},
                {2,0,0,0,0,0,6,0,0}, {0,0,0,0,1,2,9,5,0},
                {4,0,0,0,8,1,0,0,0}, {0,0,1,0,0,0,0,3,0},
                {0,0,0,0,3,0,7,0,1}};

        SudokuSolver sudokuSolver = new SudokuSolver();
        table=sudokuSolver.sudoSolve(table);
        table=sudokuSolver.sudoSolve_Level2(table);
        table=sudokuSolver.sudoSolve_Level3(table);


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(table[i][j]);
            }
            System.out.println("");
        }


    }
}
