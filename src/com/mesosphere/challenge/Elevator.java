package com.mesosphere.challenge;

public interface Elevator {

	public enum Direction {
		UP, DOWN, IDLE;
		public static Direction getDirection(int from, int to) {
			return from == to ? null : from > to ? DOWN : UP;
		}
	}

	public class State {
		final int elevatorId;
		final int currentFloor;
		final int destFloor;

		public State(int elevatorId, int currentFloor, int destFloor) {
			this.elevatorId = elevatorId;
			this.currentFloor = currentFloor;
			this.destFloor = destFloor;
		}
	}

	public class Request {
		final int pickupFloor;
		final int destFloor;
		final Direction direction;

		public Request(int pickupFloor, int destFloor) {
			if (pickupFloor == destFloor)
				throw new IllegalArgumentException("same pickup and dest floor");
			this.pickupFloor = pickupFloor;
			this.destFloor = destFloor;
			this.direction = Direction.getDirection(pickupFloor, destFloor);
		}
	}

	public int getId();

	public int distanceToPickup(Request request);

	public void addPickupRequest(Request request);

	public void register(Controller controller);

	public void setCurrFloor(int floor);

	public void setDestFloor(int floor);

	public State getState();

}
