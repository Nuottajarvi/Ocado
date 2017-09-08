package com.seinelake;

import java.util.Comparator;


public class SortDifficulties  implements  Comparator<ChooseDifficulties>{

    @Override
    public int compare(ChooseDifficulties a, ChooseDifficulties b){

        if( a.getIntersections() > b.getIntersections()){
            return 1;
        }else if( b.getIntersections() > a.getIntersections()){
            return -1;
        }else{

            if(a.getLength() > b.getLength()){
                return 1;
            }else if(b.getLength() > a.getLength()){
                return -1;
            }else{

                if(a.getOvers() > b.getOvers()){
                    return 1;
                }else{
                    return -1;
                }
            }
        }
    }

}