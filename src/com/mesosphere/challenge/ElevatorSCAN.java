package com.mesosphere.challenge;

import java.util.TreeSet;

public class ElevatorSCAN implements Elevator {

	private int id;
	private int currFloor;
	private Direction currDirection;

	TreeSet<Integer> upStops;
	TreeSet<Integer> downStops;

	public ElevatorSCAN(int id) {
		this.id = id;
		this.upStops = new TreeSet<>();
		this.downStops = new TreeSet<>();
	}

	@Override
	public int distanceToPickup(int pickupFloor, int destFloor) {
		if (pickupFloor == destFloor)
			return -1;
		// case 0: this elevator is idle
		if (this.getDirection() == Direction.IDLE)
			return Math.abs(this.getCurrFloor() - pickupFloor);

		// case 1: request direction and current direction is different
		if (getDirection() != Direction.getDirection(pickupFloor, destFloor))
			return differentDirectionDistance(pickupFloor);

		// case 2: all in same direction
		if (getDirection() == Direction.getDirection(this.getCurrFloor(), pickupFloor))
			return Math.abs(getCurrFloor() - pickupFloor);

		// case 3: currDirection and request is same but passed pickup floor
		return sameDirectionButPassedDistance(pickupFloor);
	}

	private int differentDirectionDistance(int pickupFloor) {
		return Math.abs(getCurrFloor() - getDestFloor()) + Math.abs(getDestFloor() - pickupFloor);
	}

	private int sameDirectionButPassedDistance(int pickupFloor) {
		int distance = Math.abs(getCurrFloor() - getDestFloor());
		distance += Math.abs(upStops.last() - downStops.first());
		if (getDirection() == Direction.UP) {
			return distance + Math.abs(pickupFloor - downStops.first());
		} else {
			return distance + Math.abs(upStops.last() - pickupFloor);
		}
	}

	@Override
	public void addPickupRequest(int pickupFloor, int destFloor) {
		if (pickupFloor == destFloor) {
			return;
		}
		Direction direction = Direction.getDirection(pickupFloor, destFloor);
		if (direction == Direction.UP) {
			addStopFloors(upStops, pickupFloor, destFloor);
		} else {
			addStopFloors(downStops, pickupFloor, destFloor);
		}
	}

	private void addStopFloors(TreeSet<Integer> stops, int... floors) {
		for (int floor : floors) {
			stops.add(floor);
		}
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public int getCurrFloor() {
		return this.currFloor;
	}

	@Override
	public int getNextFloor() {
		if (getDirection() == Direction.IDLE)
			return -1;
		return getDirection() == Direction.UP ? upStops.higher(getCurrFloor()) : downStops.lower(getCurrFloor());
	}

	public int getDestFloor() {
		return getDirection() == Direction.IDLE ? -1
				: getDirection() == Direction.UP ? upStops.last() : downStops.first();
	}

	public Direction getDirection() {
		return this.currDirection;
	}

	public void setCurrDirection(Direction currDirection) {
		this.currDirection = currDirection;
	}

}
