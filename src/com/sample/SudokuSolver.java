package com.sample;

import java.util.ArrayList;

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
    /*
     * Sudoku solver level 3
     */
    public int[][] sudoSolve_Level3(int[][] initTable){
        boolean stillSolving = true;
        while(stillSolving) {
            int countAllNumsBefore = 0;
            int countAllNumsAfter = 0;
            countAllNumsBefore = countAllNums();
            //Scanning all 4 combinations of cells through lines and columns searching for locked 4 numbers
            for (int s = 0; s < dim; s++) {
                for (int x = 0; x < dim - 3; x++) {
                    for (int xx = x + 1; xx < dim - 2; xx++) {
                        for (int xxx = xx + 1; xxx < dim - 1; xxx++) {
                            for (int xxxx = xxx + 1; xxxx < dim; xxxx++) {
                                if (linkedProbaLine(s, x, xx, xxx, xxxx).size() == 8) {
                                    cancelOutLocatedNumLine(s, linkedProbaLine(s, x, xx, xxx, xxxx));
                                }
                                if (linkedProbaColumn(s, x, xx, xxx, xxxx).size() == 8) {
                                    cancelOutLocatedNumColumn(s, linkedProbaColumn(s, x, xx, xxx, xxxx));
                                }
                            }
                        }
                    }
                }
            }
            //Scanning all 3 combinations of cells through lines and columns searching for locked 3 numbers
            for (int s = 0; s < dim; s++) {
                for (int x = 0; x < dim - 2; x++) {
                    for (int xx = x + 1; xx < dim - 1; xx++) {
                        for (int xxx = xx + 1; xxx < dim; xxx++) {
                            if (linkedProbaLine(s, x, xx, xxx).size() == 6) {
                                cancelOutLocatedNumLine(s, linkedProbaLine(s, x, xx, xxx));
                            }
                            if (linkedProbaColumn(s, x, xx, xxx).size() == 6) {
                                cancelOutLocatedNumColumn(s, linkedProbaColumn(s, x, xx, xxx));
                            }
                        }
                    }
                }
            }
            //Scanning all 4 combinations of cells inside each 3x3 matrix
            for (int h = 0; h < 3; h++) {
                for (int v = 0; v < 3; v++) {
                    a:
                    for (int x = 3 * h; x < 3 * h + 3; x++) {
                        for (int y = 3 * v; y < 3 * v + 3; y++) {
                            b:
                            for (int xx = 3 * h; xx < 3 * h + 3; xx++) {
                                bypassXY:
                                for (int yy = 3 * v; yy < 3 * v + 3; yy++) {
                                    c:
                                    for (int xxx = 3 * h; xxx < 3 * h + 3; xxx++) {
                                        bypassXXYY:
                                        for (int yyy = 3 * v; yyy < 3 * v + 3; yyy++) {
                                            d:
                                            for (int xxxx = 3 * h; xxxx < 3 * h + 3; xxxx++) {
                                                bypassXXXYYY:
                                                for (int yyyy = 3 * v; yyyy < 3 * v + 3; yyyy++) {
                                                    if (yy <= y && xx == x || xx < x) continue bypassXY;
                                                    if (yyy <= yy && xxx == xx || xxx < xx) continue bypassXXYY;
                                                    if (yyyy <= yyy && xxxx == xxx || xxxx < xxx) continue bypassXXXYYY;
                                                    // Write here
                                                    if (linkedProbaSubMat(x, y, xx, yy, xxx, yyy, xxxx, yyyy).size() == 12) {
                                                        cancelOutLocatedNum(linkedProbaSubMat(x, y, xx, yy, xxx, yyy, xxxx, yyyy));
                                                    }
                                                    if (xxxx == 3 * h + 3 - 1 && yyyy == 3 * v + 2) break d;
                                                    if (xxx == 3 * h + 3 - 1 && yyy == 3 * v + 1) break c;
                                                    if (xx == (3 * h + 3) - 1) break b;
                                                    if (x == 3 * h + 3 - 1 && y == 3 * v) break a;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (cellData[i][j].getProbNums().size() == 1 && initTable[i][j] == 0) {
                        initTable[i][j] = cellData[i][j].getProbNums().get(0);
                    }
                }
            }
            countAllNumsAfter = countAllNums();
            if (countAllNumsBefore!=countAllNumsAfter){
                stillSolving=true;
            } else stillSolving=false;
        }
        return initTable;
    }

    //Count all the assigned or possible numbers in the table
    private int countAllNums(){
        int c = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                c=c+cellData[i][j].getProbNums().size();
            }
        }
        return c;
    }

    private ArrayList<Integer> linkedProbaSubMat(int x, int y, int xx, int yy, int xxx, int yyy, int xxxx, int yyyy){
        ArrayList<Integer> localLinkedProba = new ArrayList<>();
        for (int num :
                cellData[x][y].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[xx][yy].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[xxx][yyy].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[xxxx][yyyy].getProbNums()) {
            localLinkedProba.add(num);
        }
        //Removing repetetive numbers
        boolean removing=true;
        while (removing) {
            removing = false;
            for (int h1 = 0; h1 < localLinkedProba.size() - 1; h1++) {
                for (int h2 = h1 + 1; h2 < localLinkedProba.size(); h2++) {
                    if (localLinkedProba.get(h2) == localLinkedProba.get(h1)) {
                        localLinkedProba.remove(h2);
                        removing = true;
                    }
                }
            }
        }
        localLinkedProba.add(x);
        localLinkedProba.add(y);
        localLinkedProba.add(xx);
        localLinkedProba.add(yy);
        localLinkedProba.add(xxx);
        localLinkedProba.add(yyy);
        localLinkedProba.add(xxxx);
        localLinkedProba.add(yyyy);
        return localLinkedProba;
    }

    private ArrayList<Integer> linkedProbaColumn(int j, int x, int xx, int xxx, int xxxx){
        ArrayList<Integer> localLinkedProba = new ArrayList<>();
        for (int num :
                cellData[x][j].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[xx][j].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[xxx][j].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[xxxx][j].getProbNums()) {
            localLinkedProba.add(num);
        }
        //Removing repetetive numbers
        boolean removing=true;
        while (removing) {
            removing = false;
            for (int h1 = 0; h1 < localLinkedProba.size() - 1; h1++) {
                for (int h2 = h1 + 1; h2 < localLinkedProba.size(); h2++) {
                    if (localLinkedProba.get(h2) == localLinkedProba.get(h1)) {
                        localLinkedProba.remove(h2);
                        removing = true;
                    }
                }
            }
        }
        localLinkedProba.add(x);
        localLinkedProba.add(xx);
        localLinkedProba.add(xxx);
        localLinkedProba.add(xxxx);
        return localLinkedProba;
    }

    private ArrayList<Integer> linkedProbaColumn(int j, int x, int xx, int xxx){
        ArrayList<Integer> localLinkedProba = new ArrayList<>();
        for (int num :
                cellData[x][j].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[xx][j].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[xxx][j].getProbNums()) {
            localLinkedProba.add(num);
        }
        //Removing repetetive numbers
        boolean removing=true;
        while (removing) {
            removing = false;
            for (int h1 = 0; h1 < localLinkedProba.size() - 1; h1++) {
                for (int h2 = h1 + 1; h2 < localLinkedProba.size(); h2++) {
                    if (localLinkedProba.get(h2) == localLinkedProba.get(h1)) {
                        localLinkedProba.remove(h2);
                        removing = true;
                    }
                }
            }
        }
        localLinkedProba.add(x);
        localLinkedProba.add(xx);
        localLinkedProba.add(xxx);
        return localLinkedProba;
    }

    private ArrayList<Integer> linkedProbaLine(int i, int x, int xx, int xxx, int xxxx){
        ArrayList<Integer> localLinkedProba = new ArrayList<>();
        for (int num :
                cellData[i][x].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[i][xx].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[i][xxx].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[i][xxxx].getProbNums()) {
            localLinkedProba.add(num);
        }
        //Removing repetetive numbers
        boolean removing=true;
        while (removing) {
            removing = false;
            for (int h1 = 0; h1 < localLinkedProba.size() - 1; h1++) {
                for (int h2 = h1 + 1; h2 < localLinkedProba.size(); h2++) {
                    if (localLinkedProba.get(h2) == localLinkedProba.get(h1)) {
                        localLinkedProba.remove(h2);
                        removing = true;
                    }
                }
            }
        }
        localLinkedProba.add(x);
        localLinkedProba.add(xx);
        localLinkedProba.add(xxx);
        localLinkedProba.add(xxxx);
        return localLinkedProba;
    }

    private ArrayList<Integer> linkedProbaLine(int i, int x, int xx, int xxx){
        ArrayList<Integer> localLinkedProba = new ArrayList<>();
        for (int num :
                cellData[i][x].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[i][xx].getProbNums()) {
            localLinkedProba.add(num);
        }
        for (int num :
                cellData[i][xxx].getProbNums()) {
            localLinkedProba.add(num);
        }
        //Removing repetetive numbers
        boolean removing=true;
        while (removing) {
            removing = false;
            for (int h1 = 0; h1 < localLinkedProba.size() - 1; h1++) {
                for (int h2 = h1 + 1; h2 < localLinkedProba.size(); h2++) {
                    if (localLinkedProba.get(h2) == localLinkedProba.get(h1)) {
                        localLinkedProba.remove(h2);
                        removing = true;
                    }
                }
            }
        }
        localLinkedProba.add(x);
        localLinkedProba.add(xx);
        localLinkedProba.add(xxx);
        return localLinkedProba;
    }


    /*
    Cancelling number outside of group cells having 100% chance of that a number
     */
    private void cancelOutLocatedNumLine(int i, ArrayList<Integer> linkedProba){
        for (int s = 0; s < dim; s++) {
            if (linkedProba.size()== 8 && s!=linkedProba.get(4) && s!=linkedProba.get(5) && s!=linkedProba.get(6) && s!=linkedProba.get(7)){
                cellData[i][s].cancelCase(linkedProba.get(0));
                cellData[i][s].cancelCase(linkedProba.get(1));
                cellData[i][s].cancelCase(linkedProba.get(2));
                cellData[i][s].cancelCase(linkedProba.get(3));
            } else if (linkedProba.size()==6 && s!=linkedProba.get(3) && s!=linkedProba.get(4) & s!=linkedProba.get(5)){
                cellData[i][s].cancelCase(linkedProba.get(0));
                cellData[i][s].cancelCase(linkedProba.get(1));
                cellData[i][s].cancelCase(linkedProba.get(2));
            }
        }
    }
    private void cancelOutLocatedNumColumn(int j, ArrayList<Integer> linkedProba){
        for (int s = 0; s < dim; s++) {
            if (linkedProba.size()==8 && s!=linkedProba.get(4) && s!=linkedProba.get(5) && s!=linkedProba.get(6) && s!=linkedProba.get(7)){
                cellData[s][j].cancelCase(linkedProba.get(0));
                cellData[s][j].cancelCase(linkedProba.get(1));
                cellData[s][j].cancelCase(linkedProba.get(2));
                cellData[s][j].cancelCase(linkedProba.get(3));
            } else if (linkedProba.size()==6 && s!=linkedProba.get(3) && s!=linkedProba.get(4) & s!=linkedProba.get(5)){
                cellData[s][j].cancelCase(linkedProba.get(0));
                cellData[s][j].cancelCase(linkedProba.get(1));
                cellData[s][j].cancelCase(linkedProba.get(2));
            }
        }
    }

    private void cancelOutLocatedNum(ArrayList<Integer> arrayList){
        for (int i = 3*(arrayList.get(4)/3); i < 3 * (arrayList.get(4) / 3) + 3; i++) {
            for (int j = 3*(arrayList.get(5)/3); j < 3 * (arrayList.get(5)/3) + 3; j++) {
                if (i==arrayList.get(4)&& j==arrayList.get(5) || i ==arrayList.get(6) && j==arrayList.get(7)
                        || i==arrayList.get(8) && j==arrayList.get(9) || i==arrayList.get(10) && j==arrayList.get(11)){
                    continue;
                } else{
                    cellData[i][j].cancelCase(arrayList.get(0));
                    cellData[i][j].cancelCase(arrayList.get(1));
                    cellData[i][j].cancelCase(arrayList.get(2));
                    cellData[i][j].cancelCase(arrayList.get(3));
                }
            }
        }
    }

    //private void
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
