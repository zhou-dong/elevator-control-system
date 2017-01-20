package com.mesosphere.challenge;

public interface Elevator {

	public enum Direction {
		UP, DOWN, IDLE;
		public static Direction getDirection(int from, int to) {
			return from == to ? null : from > to ? DOWN : UP;
		}
	}

	public class State {
		int elevatorId;
		int currentFloor;
		int destFloor;
	}

	public int distanceToPickup(int pickupFloor, int destFloor);

	public void addPickupRequest(int pickupFloor, int destFloor);

	public void register(Controller controller);

	public int getId();

	public int getCurrFloor();

	public void setCurrFloor(int floor);

	public int getDestFloor();

	public void setDestFloor(int floor);

	public int getNextFloor();

	public State getState();

	public boolean isEmpty();

	public void step();

	public void start();

	public void stop();
}
