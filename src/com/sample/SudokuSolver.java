package com.sample;

/**
 * @author Nicolas Whaibe
 */
public class SudokuSolver {
    private int dim = 9;
    private CellData[][] cellData;

    public SudokuSolver() {
        cellData = new CellData[dim][dim];
    }

    //Sudoku solver level 1
    public int[][] sudoSolve(int[][] initTable){
        //Intializing the cellData 2 dim matrix elements with propre possibilities
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (initTable[i][j]!=0){
                    cellData[i][j] = new CellData(initTable[i][j]);
                } else
                    cellData[i][j] = new CellData();
            }
        }
        boolean stillSolving = true;
        while(stillSolving) {
            stillSolving = false;
            //Removing initial assigned number from dependent cells (line, column, submatrix)
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (initTable[i][j] != 0) {
                        removeNumFromLine(initTable[i][j], i, j);
                        removeNumFromColumn(initTable[i][j], i, j);
                        removeNumFromSubmatrix(initTable[i][j], i, j);
                    }
                }
            }
            //Assigning number to a cell when only one probability is left
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (cellData[i][j].getProbNums().size() == 1 && initTable[i][j] == 0) {
                        initTable[i][j] = cellData[i][j].getProbNums().get(0);
                        stillSolving = true;
                    }
                }
            }
        }
        return initTable;
    }

    //Sudoku Solver level 2
    public int[][] sudoSolve_Level2(int[][] initTable){
        int count, countInLine, countInColumn;
        for (int i = 0; i < dim; i++) {
            proceed:
            for (int j = 0; j < dim; j++) {
                if (initTable[i][j]==0){
                    for (int num :
                            cellData[i][j].getProbNums()) {
                        count =0;
                        for (int ii = 3 * (i / 3); ii < 3 * (i / 3) + 3; ii++) {
                            for (int jj = 3 * (j / 3); jj < 3 * (j / 3) + 3; jj++){
                                if (cellData[ii][jj].getProbNums().contains(num)){
                                    count++;
                                }
                            }
                        }
                        if(count==1){
                            initTable[i][j]=num;
                            this.cellData[i][j] = new CellData(num);
                            continue proceed;
                        }
                        countInColumn=0;
                        for (int jj=0; jj<dim; jj++){
                            if (cellData[i][jj].getProbNums().contains(num)){
                                countInColumn++;
                            }
                        }
                        if(countInColumn==1){
                            initTable[i][j]=num;
                            this.cellData[i][j] = new CellData(num);
                            continue proceed;
                        }
                        countInLine=0;
                        for(int ii=0; ii<dim;ii++){
                            if (cellData[ii][j].getProbNums().contains(num)){
                                countInLine++;
                            }
                        }
                        if (countInLine==1){
                            initTable[i][j]=num;
                            this.cellData[i][j] = new CellData(num);
                            continue proceed;
                        }
                    }
                }
            }
        }
        return initTable;
    }

    private void removeNumFromLine(int num, int i,int j){
        for (int jj = 0; jj < dim; jj++) {
            if (jj!=j){
                cellData[i][jj].cancelCase(num);
            }
        }
    }

    private void removeNumFromColumn(int num, int i, int j){
        for (int ii = 0; ii < dim; ii++) {
            if (ii!=i){
               cellData[ii][j].cancelCase(num);
            }
        }
    }

    private void removeNumFromSubmatrix(int num, int i, int j){
        for (int ii = 3 * (i / 3); ii < 3 * (i / 3) + 3; ii++) {
            for (int jj = 3 * (j / 3); jj < 3 * (j / 3) + 3; jj++) {
                if (ii==i && jj==j) {
                    continue;
                }else cellData[ii][jj].cancelCase(num);
            }
        }
    }



}
