

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

/**
 * This class implements a description of a player, that is
 * used to encode.
 * 
 * @author eleal
 *
 */
public class ChromosomeState implements Serializable {
	
	// This maps a feature with its value in the chromosome.
	HashMap<String,String> featureValues;
	
	// All the names of all features.
	//String[] allFeatureNames;
	
	// Stores for each feature, the possible values it can assume.
	//HashMap<String, String[]> allValuesForFeatures;
	
	
	
	public ChromosomeState(){
		featureValues = new HashMap<String,String>();
	}
	
	/**
	 * A gene is composed by features. This returns an array with all 
	 * possible features.
	 * 
	 * @return
	 */
	public static String[] getAllPossibleFeatureNames(){
		String[] allFeatureNames = {
				"moneyNearestAsteroid",
				"timeLeft",
				"energyOnShip",
				"distanceToNearestBase",
				"distanceToNearestBeacon",
				"moneyOnShip",
				"distanceToNearestEnemy", 
				"distanceToNearestAlly"
		};
		return allFeatureNames;
	}
	
	/**
	 * This method returns the names of all possible actions.
	 * 
	 * @return
	 */
	public static String[] getAllPossibleActionNames(){
		String[] allActionNames = {
			"defendShipUnderAttack",
			"attackEnemyShip",
			"attackEnemyBase",
			"goToBeacon",
			"goToAsteroid",
			"goToBase"
		};
		
		return allActionNames;
	}
	
	/*
	private String[] getAllPossibleValuesForFeature(String feature){
		return allValuesForFeatures.get(feature);
	}
	*/
	
	/**
	 * Returns the values that a gene has for each feature.
	 * @return
	 */
	public HashMap<String,String> getFeatureValues(){
		return featureValues;
	}
	
	public void setFeatureValue(String featureName, String featureValue){
		getFeatureValues().put(featureName, featureValue);
	}
	
	public String getFeatureValue(String featureName){
		return getFeatureValues().get(featureName);
	}
	
	/**
	 * This method returns an array that has all possible spacewar states contemplated
	 * by a chromosome.
	 * 
	 * @return
	 */
	public static ChromosomeState[] getAllPossibleStates() {
		final int numPossibleStates = 36864;
		
		ChromosomeState[] allPossibleStates = new ChromosomeState[numPossibleStates];
		
		// Initialize all chromosomes
		for(int i=0; i < allPossibleStates.length; i++){
			allPossibleStates[i] = new ChromosomeState();
		}
		
		// NearestAsteroidMoney
		for(int i=0; i < allPossibleStates.length; i++) {
			if(i % 3 == 0) {
				allPossibleStates[i].setFeatureValue("moneyNearestAsteroid", "<100");			
			}
			else if (i % 3 == 1) {
				allPossibleStates[i].setFeatureValue("moneyNearestAsteroid", "100-149");			
			}
			else {
				allPossibleStates[i].setFeatureValue("moneyNearestAsteroid", "150-200");			
			}
		}
		
		// For the time remaining.
		for(int i=0; i < allPossibleStates.length; i++) {
			if(i % 3 == 0) {
				allPossibleStates[i].setFeatureValue("timeLeft", "<5%");			
			}
			else if (i % 3 == 1) {
				allPossibleStates[i].setFeatureValue("timeLeft", "5%-30%");			
			}
			else {
				allPossibleStates[i].setFeatureValue("timeLeft", "30%-100");			
			}
		}

		// Energy on ship
		for(int i=0; i < allPossibleStates.length; i++) {
			if(i % 4 == 0) {
				allPossibleStates[i].setFeatureValue("energyOnShip", "<50");			
			}
			else if ( i % 4 == 1) {
				allPossibleStates[i].setFeatureValue("energyOnShip", "50-99");			
			}
			else if ( i % 4 == 2) {
				allPossibleStates[i].setFeatureValue("energyOnShip", "100-500");			
			}
			else  {
				allPossibleStates[i].setFeatureValue("energyOnShip", ">500");			
			}
		}
		
		

		// DistanceToNearestBase
		for(int i=0; i < allPossibleStates.length; i++) {
			if(i % 4 == 0) {
				allPossibleStates[i].setFeatureValue("distanceToNearestBase", "<50");			
			}
			else if (i % 4 == 1)  {
				allPossibleStates[i].setFeatureValue("distanceToNearestBase", "50-99");			
			}
			else if (i % 4 == 2) {
				allPossibleStates[i].setFeatureValue("distanceToNearestBase", "100-500");			
			}
			else  {
				allPossibleStates[i].setFeatureValue("distanceToNearestBase", ">500");			
			}
		}
		
		// DistanceToNearestBeacon
		for(int i=0; i < allPossibleStates.length; i++) {
			if (i % 4 == 0) {
				allPossibleStates[i].setFeatureValue("distanceToNearestBeacon", "<50");			
			}
			else if (i % 4 == 1) {
				allPossibleStates[i].setFeatureValue("distanceToNearestBeacon", "50-99");			
			}
			else if (i % 4 == 2) {
				allPossibleStates[i].setFeatureValue("distanceToNearestBeacon", "100-500");			
			}
			else  {
				allPossibleStates[i].setFeatureValue("distanceToNearestBeacon", ">500");			
			}
		}

		
		
		// Money on ship
		for(int i=0; i < allPossibleStates.length; i++) {
			if (i % 4 == 0) {
				allPossibleStates[i].setFeatureValue("moneyOnShip", "<50");			
			}
			else if (i % 4 == 1)  {
				allPossibleStates[i].setFeatureValue("moneyOnShip", "50-99");			
			}
			else if (i % 4 == 2)  {
				allPossibleStates[i].setFeatureValue("moneyOnShip", "100-500");			
			}
			else  {
				allPossibleStates[i].setFeatureValue("moneyOnShip", ">500");			
			}
		}
		
		
		// DistanceToNearestEnemy
		for(int i=0; i < allPossibleStates.length; i++) {
			if (i % 4 == 0) {
				allPossibleStates[i].setFeatureValue("distanceToNearestEnemy", "<50");			
			}
			else if (i % 4 == 1) {
				allPossibleStates[i].setFeatureValue("distanceToNearestEnemy", "50-99");			
			}
			else if (i % 4 == 2) {
				allPossibleStates[i].setFeatureValue("distanceToNearestEnemy", "100-500");			
			}
			else  {
				allPossibleStates[i].setFeatureValue("distanceToNearestEnemy", ">500");			
			}
		}
		
	
		// DistanceToNearestAlly
		for(int i=0; i < allPossibleStates.length; i++) {
			if (i % 4 == 0) {
				allPossibleStates[i].setFeatureValue("distanceToNearestAlly", "<50");			
			}
			else if (i % 4 == 1) {
				allPossibleStates[i].setFeatureValue("distanceToNearestAlly", "50-99");			
			}
			else if (i % 4 == 2) {
				allPossibleStates[i].setFeatureValue("distanceToNearestAlly", "100-500");			
			}
			else  {
				allPossibleStates[i].setFeatureValue("distanceToNearestAlly", ">500");			
			}
		}
		
		return allPossibleStates;
	}
	
	/**
	 * We are implementing a chromosome as a map between
	 * spacewar states and actions. This returns a random action.
	 * 
	 * @return
	 */
	public static String getRandomAction(){
		String[] actions = getAllPossibleActionNames();
		
		Random randGen = new Random();
		
		return actions[randGen.nextInt(actions.length)];
	}
	
	/**
	 * This method is used to initialize values of a discrete state.
	 * @param moneyNearestAsteroid
	 * @param energyOnShip
	 * @param distanceToNearestBase
	 * @param distanceToNearestBeacon
	 * @param moneyOnShip
	 * @param distanceToNearestEnemy
	 * @param distanceToNearestAlly
	 * @param totalTime
	 * @param timeElapsed
	 * @param classification
	 */
	public void setChromosomeState(
			int moneyNearestAsteroid,
			double energyOnShip,
			double distanceToNearestBase,
			double distanceToNearestBeacon,
			double moneyOnShip,
			double distanceToNearestEnemy,
			double distanceToNearestAlly,
			
			int totalTime,
			int timeElapsed,
			
			String classification
			){
		
		// NearestAsteroidMoney
		if(moneyNearestAsteroid < 100) {
			featureValues.put("moneyNearestAsteroid", "<100");			
		}
		else if (moneyNearestAsteroid < 150) {
			featureValues.put("moneyNearestAsteroid", "100-149");			
		}
		else if (moneyNearestAsteroid <= 200) {
			featureValues.put("moneyNearestAsteroid", "150-200");			
		}
		
		
		// For the time remaining.
		double percentTimeRemaining = (totalTime-timeElapsed)/ (double)totalTime;
		
		if(percentTimeRemaining < 0.05 ) {
			featureValues.put("timeLeft", "<5%");			
		}
		else if (percentTimeRemaining < 0.3) {
			featureValues.put("timeLeft", "5%-30%");			
		}
		else {
			featureValues.put("timeLeft", "30%-100");			
		}
		
		
		// Energy on ship
		if(energyOnShip < 50) {
			featureValues.put("energyOnShip", "<50");			
		}
		else if (energyOnShip < 100) {
			featureValues.put("energyOnShip", "50-99");			
		}
		else if (energyOnShip < 500) {
			featureValues.put("energyOnShip", "100-500");			
		}
		else  {
			featureValues.put("energyOnShip", ">500");			
		}
		
		
		
		// DistanceToNearestBase
		if(distanceToNearestBase < 50) {
			featureValues.put("distanceToNearestBase", "<50");			
		}
		else if (distanceToNearestBase < 100) {
			featureValues.put("distanceToNearestBase", "50-99");			
		}
		else if (distanceToNearestBase < 500) {
			featureValues.put("distanceToNearestBase", "100-500");			
		}
		else  {
			featureValues.put("distanceToNearestBase", ">500");			
		}
		
		
		// DistanceToNearestBeacon
		if(distanceToNearestBeacon < 50) {
			featureValues.put("distanceToNearestBeacon", "<50");			
		}
		else if (distanceToNearestBeacon < 100) {
			featureValues.put("distanceToNearestBeacon", "50-99");			
		}
		else if (distanceToNearestBeacon < 500) {
			featureValues.put("distanceToNearestBeacon", "100-500");			
		}
		else  {
			featureValues.put("distanceToNearestBeacon", ">500");			
		}
		
		
		
		
		// Money on ship
		if(moneyOnShip < 50) {
			featureValues.put("moneyOnShip", "<50");			
		}
		else if (moneyOnShip < 100) {
			featureValues.put("moneyOnShip", "50-99");			
		}
		else if (moneyOnShip < 500) {
			featureValues.put("moneyOnShip", "100-500");			
		}
		else  {
			featureValues.put("moneyOnShip", ">500");			
		}
		
		
		
		// DistanceToNearestEnemy
		if(distanceToNearestEnemy < 50) {
			featureValues.put("distanceToNearestEnemy", "<50");			
		}
		else if (distanceToNearestEnemy < 100) {
			featureValues.put("distanceToNearestEnemy", "50-99");			
		}
		else if (distanceToNearestEnemy < 500) {
			featureValues.put("distanceToNearestEnemy", "100-500");			
		}
		else  {
			featureValues.put("distanceToNearestEnemy", ">500");			
		}
		
		
	
		// DistanceToNearestAlly
		if(distanceToNearestAlly < 50) {
			featureValues.put("distanceToNearestAlly", "<50");			
		}
		else if (distanceToNearestAlly < 100) {
			featureValues.put("distanceToNearestAlly", "50-99");			
		}
		else if (distanceToNearestAlly < 500) {
			featureValues.put("distanceToNearestAlly", "100-500");			
		}
		else  {
			featureValues.put("distanceToNearestAlly", ">500");			
		}
	}
}
