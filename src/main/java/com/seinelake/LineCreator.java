package com.seinelake;

import java.util.ArrayList;
import java.util.Random;

public class LineCreator {

    private ArrayList<Integer> squaresGoingThrough = new ArrayList<Integer>();
    private int currentSquare;
    private int currentDirection;
    private int recursionAmount;

    public LineCreator(int startingSquare){

    createLine(startingSquare);

    }

    public void createLine (int startingSquare){

        this.currentSquare = startingSquare;
        this.squaresGoingThrough.add(startingSquare);
        recursionAmount = 0;

        Random rnd = new Random();
        currentDirection = rnd.nextInt(4);

        randomizeNextSquare(rnd);

    }


    public int getSquare(int a){

        return squaresGoingThrough.get(a);

    }

    public int getSize(){

        return squaresGoingThrough.size();
    }

    public void randomizeNextSquare(Random rnd){

        //TRY TO CLOSE THE RING

        boolean finish = false;

        //up 0

        int startingSquare = squaresGoingThrough.get(0);

        if(recursionAmount!=1){

            if(currentSquare > 4){

                if( startingSquare == currentSquare - 5){
                    finish = true;
                }

            }else{

                if( startingSquare == currentSquare + 10){
                    finish = true;
                }

            }



            //right 1



            if(currentSquare == 4 || currentSquare == 9 || currentSquare == 14){

                if( startingSquare == currentSquare - 4){
                    finish = true;
                }

            }else{

                if( startingSquare == currentSquare + 1){
                    finish = true;
                }

            }



            //down 2



            if(currentSquare <= 9){

                if( startingSquare == currentSquare + 5){
                    finish = true;
                }

            }else{

                if( startingSquare  == currentSquare - 10){
                    finish = true;
                }

            }



            //left 3



            if(currentSquare == 0 || currentSquare == 5 || currentSquare == 10){

                if( startingSquare == currentSquare + 4){
                    finish = true;
                }

            }else{

                if( startingSquare == currentSquare - 1){
                    finish = true;
                }

            }
        }

        if( finish == false){


        boolean canMoveUp = true;
        boolean canMoveLeft = true;
        boolean canMoveRight = true;
        boolean canMoveDown = true;

        //Check illegal squares, squares that are already used by the line

        int a;
        for(a = 0; a < squaresGoingThrough.size(); a++ ){

        //up

            if(currentSquare > 4){

                if( squaresGoingThrough.get(a) == currentSquare - 5){
                    canMoveUp = false;
                }

            }else{

                if( squaresGoingThrough.get(a) == currentSquare + 10){
                    canMoveUp = false;
                }

            }


        //right


            if(currentSquare == 4 || currentSquare == 9 || currentSquare == 14){

                if( squaresGoingThrough.get(a) == currentSquare - 4){
                    canMoveRight = false;
                }

            }else{

                if( squaresGoingThrough.get(a) == currentSquare + 1){
                    canMoveRight = false;
                }
            }


        //down

            if(currentSquare <= 9){

                if( squaresGoingThrough.get(a) == currentSquare + 5){
                    canMoveDown = false;
                }

            }else{

                if( squaresGoingThrough.get(a) == currentSquare - 10){
                    canMoveDown = false;
                }
            }

        //left

            if(currentSquare == 0 || currentSquare == 5 || currentSquare == 10){

                if( squaresGoingThrough.get(a) == currentSquare + 4){
                    canMoveLeft = false;
                }

            }else{

                if( squaresGoingThrough.get(a) == currentSquare - 1){
                    canMoveLeft = false;
                }
            }
        }

        //count next direction

        int directionsYouCanMove = 0;
        ArrayList<Integer> directionValue = new ArrayList<Integer>();

        if(canMoveUp == true){
            directionsYouCanMove++;
            directionValue.add(0);
        }
        if(canMoveRight == true){
            directionsYouCanMove++;
            directionValue.add(1);
        }
        if(canMoveDown == true){
            directionsYouCanMove++;
            directionValue.add(2);
        }
        if(canMoveLeft == true){
            directionsYouCanMove++;
            directionValue.add(3);
        }

        if(directionsYouCanMove == 0){
            squaresGoingThrough.clear();
            createLine(startingSquare);
        }else{


        int newSquare = 0;
        int nextRandom = rnd.nextInt(directionsYouCanMove);

        int nextDirection = directionValue.get(nextRandom);

        //currentDirection

        //up 0

        if(nextDirection == 0){

        if(currentSquare > 4){

            newSquare = currentSquare - 5;

        }else{

            newSquare = currentSquare + 10;
            Achievements.difficultyBonusOverEdge++;

        }
        }

        //right 1

        if(nextDirection == 1){

        if(currentSquare == 4 || currentSquare == 9 || currentSquare == 14){

            newSquare = currentSquare - 4;
            Achievements.difficultyBonusOverEdge++;

        }else{

            newSquare = currentSquare + 1;

        }
        }

        //down 2

        if(nextDirection == 2){

        if(currentSquare <= 9){

            newSquare = currentSquare + 5;

        }else{

            newSquare = currentSquare - 10;
            Achievements.difficultyBonusOverEdge++;

        }
        }

        //left 3


        if(nextDirection == 3){


        if(currentSquare == 0 || currentSquare == 5 || currentSquare == 10){

            newSquare = currentSquare + 4;
            Achievements.difficultyBonusOverEdge++;

        }else{

            newSquare = currentSquare - 1;

        }
        }

        squaresGoingThrough.add(newSquare);


        currentSquare = newSquare;
        currentDirection = nextDirection;

        recursionAmount++;

        randomizeNextSquare(rnd);

        }
        }
    }
}
