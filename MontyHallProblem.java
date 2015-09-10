import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;

public class MontyHallProblem {

/*

Michael Geiser
Personal Project
8 November 2007
Copyright Michael J. Geiser - all rights reserved

This test harness will demonstrate the results of the classic "Monty Hall Problem"
see 
http://montyhallproblem.com/
http://en.wikipedia.org/wiki/Monty_Hall_problem

You can pick the:
  1) Number of Trials (numberOfTrials)  A very large number proves the point
  2) Number of Doors (numberOfDoors)  3 is the most common, but any number will work 

This program does the following

1) Sets up a loop to run through the specified number of trials

2) Creates a memory structure in a LinkedHashMap with each key representing a door and the value holding either Goat or Car (One car only)

3) A random door is selected as the Contestant's pick.  That door is removed from LinkedHashMap - it can neither be opened by Monty nor can the contestant switch to the door (they already selected the door) in the run

4) A random door that is a goat is revealed and removed from the LinkedHashMap (The contestant cannot pick this door - it is a loser)

5) A random door is picked from the remaining doors and this is door to which the contestant switched

6) A running total of Trials and winning switches is maintained

7) Summary of Trials are output

*/


  // These two values should be changed for the number of trials and doors desired  
  private static final long numberOfTrials = 1000000;
  private static final long numberOfDoors = 3;
  // TODO: exit if numberOfDoors < 3, the simulation is not meaningful in this case
  // These two values should be changed for the number of trials and doors desired

  
  // These values should not be changed
  private static final String CHOICE_GOAT = "Goat";
  private static final String CHOICE_CAR = "Car";
  private static long numberofTrailsCounter = 0;
  private static long numberofWinsCounter = 0;
  private static LinkedHashMap doorsAndPrizes = null;
  // These values should not be changed
  
  
  
  //The main execution path of the program
  public static void main(String[] args) {

      // Get the start time to use to determine the elapsed time 
      long startTime = System.currentTimeMillis();
	  //You could use System.nanoTime() for a very precise measurement, but System.currentTimeMillis() is good enough for this usage
	  
    
      // 1) Sets up a loop to run through the specified number of trials
      for (long counter=1; counter <= numberOfTrials; counter++){
        
        //we need to know the number of doors that are available to select from 
        long currentNumberOfDoorsToChooseFrom = numberOfDoors;
        
        // 2) Creates a memory structure in a LinkedHashMap with each key representing a door and the value holding either Goat or Car (One car only)
        setupDoorsAndPrizes();
		//this is a private method that could be used in other programs.  It would have been better to pass in numberOfDoors

        // 3) A random door is selected as the Contestants pick.  That door is removed from LinkedHashMap - 
        long contestantsInitialPick = getRandomNumberofaDoor(numberOfDoors);    
        //need a Long Object
		Long mapKeyForCcontestantsInitialPick = new Long(contestantsInitialPick);
        
        /*  determine what the contestant's initial pick was and record it.
         *  The pick is then removed
         */
        if (doorsAndPrizes.containsKey(mapKeyForCcontestantsInitialPick)) {
            String contestantsInitialPrize = (String)doorsAndPrizes.get(mapKeyForCcontestantsInitialPick); 
//            System.out.println("Originally choosen prize: " + contestantsInitialPrize);
            // 3) That door is removed from LinkedHashMap
            doorsAndPrizes.remove(mapKeyForCcontestantsInitialPick);
        }
        
          /* There is one less door to choose from now, so we decrement the
           * number of doors that can be picked from
           */ 
          currentNumberOfDoorsToChooseFrom--;

          
          // 4) A random door that is a goat is revealed and removed from the LinkedHashMap (The contestant cannot pick this door - it is a loser)
          long revealedDoor = getRandomNumberofaDoor(currentNumberOfDoorsToChooseFrom);
          /*
          *  if revealDoor() returns false, the door "Monty" picked to reveal was the car
          *  If the randomly selected door was the last door, we pick the next to last door
          *  otherwise we pick the next door.
          *            
          *  Since there is only one car, once we pick a different door,   
          *  revealDoor() will return TRUE in all cases 
          */                    
          if (revealDoor(revealedDoor) == false) {
            if (currentNumberOfDoorsToChooseFrom == revealedDoor) {
              revealedDoor--;
            } else {
              revealedDoor++;
            }
            revealDoor(revealedDoor);
          }
          /* There is one less door to choose from now, so we decrement the
           * number of doors that can be picked from
           */ 
          currentNumberOfDoorsToChooseFrom--;

          // 5) A random door is picked from the remaining doors and this is door to which the contestant switched
          long contestantsSwitchedPick = getRandomNumberofaDoor(currentNumberOfDoorsToChooseFrom);
          String finalPrize = whatDidContestantWin(contestantsSwitchedPick);
//          System.out.println("Contestant switches to door: " + contestantsSwitchedPick + " of " + currentNumberOfDoorsToChooseFrom + " doors");
          
          //6) A running total of Trials and winning switches is maintained
          numberofTrailsCounter++;
          if (finalPrize.equalsIgnoreCase(CHOICE_CAR)) {
              numberofWinsCounter++;            
          }
      }
      
	  //the iterations (looping) has ended, time to summarize the results
	  
	  // Get the end time to use to determine the elapsed time
      long endTime = (System.currentTimeMillis());
      
      // 7) Summary of Trials are output
      System.out.println("Results: " + numberofTrailsCounter + " trials and " + numberofWinsCounter + " winners that switched");
      System.out.println("Elapsed time: " + (endTime - startTime) + " milliseconds");
      // or in seconds if you prefer...
      // System.out.println("Elapsed time: " + (((double)endTime - (double)startTime)/1000) + " seconds");
	  //if you used nanoTime you'd divide by 1,000,000 instead
	  

    }


  /*
  *
  * The whatDidContestantWin() method takes the index of the randomly select "Switched to" door and finds
  * what is in the value for the door's key in the LinkedHashMap    
  *
  */
  private static String whatDidContestantWin(long doorContestantSwitchedTo) {
    // Get the value.  We know the Map has the key; it has to contain the key.
    // We'll skimp on Exception handling and bounds checking here  
    
    // I like explicitly creating a holder for the return
    String returnValue = null;  
    
    long doorCounter = 1;
    Long key = null;
    String value = null; 
    
    Iterator keyValuePairs = doorsAndPrizes.entrySet().iterator();
        while (keyValuePairs.hasNext()) {
          Map.Entry entry = (Map.Entry) keyValuePairs.next();
          if (doorContestantSwitchedTo == doorCounter) {
            key = (Long)entry.getKey();
            value = (String)entry.getValue();
            returnValue = value;
          break; //stop iterating if we can         
      }
          doorCounter++;          
        }
        //System.out.println("What did contestant win? " + key + ":" + value);        
        return returnValue;
  }
    
  
  /*
  *
  * The revealDoor() method takes the index of the revealed door and 
  * returns true if the door hid a goat and false if it was the car 
  *
  */
  private static boolean revealDoor(long aRevealedDoor) {
    //get the value.  We know this Map the key
    boolean returnValue = false;
    long numberOfDoors = doorsAndPrizes.size();
    long revealCounter=1;
    Long key = null;
    String value = null; 
    
    Iterator keyValuePairs = doorsAndPrizes.entrySet().iterator();
        while (keyValuePairs.hasNext()) {
          Map.Entry entry = (Map.Entry) keyValuePairs.next();
      
          // Loop until we get to the index for the door and then get the key and value
          if (aRevealedDoor == revealCounter) {
            key = (Long)entry.getKey();  //  key is not used
            value = (String)entry.getValue();
          
          // if the door hid a goat, the door is removed
          //if it was the car, another choice will be made 
          if (value.equalsIgnoreCase(CHOICE_GOAT)) {
            returnValue = true;
                doorsAndPrizes.remove(key);           
          } else {
            returnValue = false;
          }
          break;          
      }
          revealCounter++;          
        }
    return returnValue;
  }
  

  /*
  *
  * The getRandomNumberofaDoor() method returns a random (random enough for this 
  * exercise anyway) long in teh range from 1 to the specified parameter
  *
  */
  private static long getRandomNumberofaDoor(long upperRangeofRandomNumber) {
    long randomIntofaDoor = 1 + (int)(Math.random() * upperRangeofRandomNumber);
    return randomIntofaDoor;
  }
  
  /*
  * The method setupDoorsAndPrizes() creates and populates a LinkedHashMap
  * equal to numberOfDoors.  Behind one - and only one - of the doors is a car, 
  * behind the others is a goat
  * 
  */
  private static void setupDoorsAndPrizes() {

    doorsAndPrizes = new LinkedHashMap();

    //Randomly pick the door that has the car 
    long randomInt = getRandomNumberofaDoor(numberOfDoors);

    //Load the HashMap
    for (int counter=1; counter <= numberOfDoors ;counter++){
        if (randomInt == counter) {
            doorsAndPrizes.put(new Long(counter), CHOICE_CAR);        
        } else {
            doorsAndPrizes.put(new Long(counter), CHOICE_GOAT);
        }
    }
   }
   
   
}
