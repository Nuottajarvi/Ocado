package com.seinelake;


public class ChooseDifficulties{

    private LineCreator line1;
    private LineCreator line2;
    int difficultyBonusIntersections = 0;
    int difficultyBonusLength = 0;
    int difficultyBonusOverEdge = 0;


    public ChooseDifficulties(LineCreator line1, LineCreator line2){

        this.line1 = line1;
        this.line2 = line2;

        this.difficultyBonusLength = line1.getSize() + line2.getSize();
        countOvers();

    }

    public void countOvers(){

        int a;

        for(a = 0; a < line1.getSize(); a++){

            int subtraction = 0;

            if(a == line1.getSize() - 1){
                subtraction = Math.abs(line1.getSquare(a) - line1.getSquare(0));
            }else{
                subtraction = Math.abs(line1.getSquare(a) - line1.getSquare(a + 1));
            }

            if(subtraction == 4 || subtraction == 10){
                difficultyBonusOverEdge++;
            }


        }

        for(a = 0; a < line2.getSize(); a++){

            int subtraction = 0;

            if(a == line2.getSize() - 1){
                subtraction = Math.abs(line2.getSquare(a) - line2.getSquare(0));
            }else{
                subtraction = Math.abs(line2.getSquare(a) - line2.getSquare(a + 1));
            }

            if(subtraction == 4 || subtraction == 10){
                difficultyBonusOverEdge++;
            }

        }


    }

    public LineCreator getLine1(){
        return line1;
    }

    public LineCreator getLine2(){
        return line2;
    }

    public void setIntersections(int intersections){
        this.difficultyBonusIntersections = intersections;
    }

    public int getIntersections(){
        return difficultyBonusIntersections;
    }

    public int getLength(){
        return difficultyBonusLength;
    }

    public int getOvers(){
        return difficultyBonusOverEdge;
    }
}


