package com.xquadro.sugardrop;

import com.badlogic.gdx.utils.Array;

public class Challenge {
	public enum ChallengeType {QUICK_PLAY, SUGAR_RUSH, SUGARDRIVE, HYPERACTIVE};
	private String name;
	private int rounds;	
	private float candySize;
	private int colors;
	private int candyCount;
	private ChallengeType challengeType;

	private Array<Candy> candies;
	

	public Challenge() {
		candies = new Array<Candy>();
	}
	
	public void addCandy(Candy candy){
		candies.add(candy);
	}

	public String getName() {
		return name;
	}

	public Challenge setName(String name) {
		this.name = name;
		return this;
	}

	public int getRounds() {
		return rounds;
	}

	public Challenge setRounds(int rounds) {
		this.rounds = rounds;
		return this;
	}

	public float getCandySize() {
		return candySize;
	}

	public Challenge setCandySize(float candySize) {
		this.candySize = candySize;
		return this;
	}

	public int getColors() {
		return colors;
	}

	public Challenge setColors(int colors) {
		this.colors = colors;
		return this;
	}

	public Array<Candy> getCandies() {
		return candies;
	}

	public int getCandyCount() {
		return candyCount;
	}

	public Challenge setCandyCount(int candyCount) {
		this.candyCount = candyCount;
		return this;
	}

	public ChallengeType getChallengeType() {
		return challengeType;
	}

	public Challenge setChallengeType(ChallengeType challengeType) {
		this.challengeType = challengeType;
		return this;
	}
	
	
	
}
