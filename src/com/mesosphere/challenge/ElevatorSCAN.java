package com.mesosphere.challenge;

import java.util.TreeSet;

public class ElevatorSCAN implements Elevator, Runnable {

	private int id;
	private int currFloor;
	private Direction currDirection;
	private boolean isAlive;
	private Controller controller;
	private Thread thread;

	TreeSet<Integer> upStops;
	TreeSet<Integer> downStops;

	public ElevatorSCAN(int id) {
		this.id = id;
		this.isAlive = true;
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
		if (getDirection() == Direction.getDirection(getCurrFloor(), pickupFloor))
			return Math.abs(getCurrFloor() - pickupFloor);

		// case 3: currDirection and request is same but passed pickup floor
		return sameDirectionButPassedDistance(pickupFloor);
	}

	private int differentDirectionDistance(int pickupFloor) {
		return Math.abs(getCurrFloor() - getDestFloor()) + Math.abs(getDestFloor() - pickupFloor);
	}

	private int sameDirectionButPassedDistance(int pickupFloor) {
		int first = downStops.isEmpty() ? getCurrFloor() : downStops.first();
		int last = upStops.isEmpty() ? getCurrFloor() : upStops.last();
		int distance = Math.abs(getCurrFloor() - getDestFloor());
		distance += Math.abs(last - first);
		if (getDirection() == Direction.UP) {
			return distance + Math.abs(pickupFloor - first);
		} else {
			return distance + Math.abs(last - pickupFloor);
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
		this.start();
	}

	private void addStopFloors(TreeSet<Integer> stops, int... floors) {
		for (int floor : floors) {
			stops.add(floor);
		}
	}

	@Override
	public int getNextFloor() {
		if (getDirection() == Direction.IDLE)
			return -1;
		if (getDirection() == Direction.UP) {
			if (upStops.isEmpty() || upStops.last() == getCurrFloor())
				return getCurrFloor();
			return upStops.higher(getCurrFloor());
		} else {
			if (downStops.isEmpty() || downStops.first() == getCurrFloor())
				return getCurrFloor();
			return downStops.lower(getCurrFloor());
		}
	}

	public int getDestFloor() {
		if (getDirection() == Direction.IDLE)
			return -1;
		if (getDirection() == Direction.UP) {
			return upStops.isEmpty() ? getCurrFloor() : upStops.last();
		} else
			return downStops.isEmpty() ? getCurrFloor() : downStops.first();
	}

	public synchronized Direction getDirection() {
		return this.currDirection;
	}

	public synchronized void setCurrDirection(Direction direction) {
		this.currDirection = direction;
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
	public void setCurrFloor(int floor) {
		this.currFloor = floor;
	}

	@Override
	public void setDestFloor(int floor) {
		if (this.getDirection() == Direction.UP)
			upStops.add(floor);
		else if (this.getDirection() == Direction.DOWN)
			downStops.add(floor);
		else if (Direction.getDirection(currFloor, floor) == Direction.UP)
			upStops.add(floor);
		else
			downStops.add(floor);
	}

	@Override
	public State getState() {
		State state = new State();
		state.currentFloor = getCurrFloor();
		state.destFloor = getNextFloor();
		state.elevatorId = getId();
		return state;
	}

	@Override
	public boolean isEmpty() {
		return upStops.isEmpty() && downStops.isEmpty();
	}

	@Override
	public void run() {
		while (isAlive) {
			while (!this.isEmpty()) {
				if (getDirection() == Direction.IDLE)
					setCurrDirection(Direction.UP);
				goUp();
				setCurrDirection(Direction.DOWN);
				goDown();
				setCurrDirection(Direction.UP);
			}
			setCurrDirection(Direction.IDLE);
		}
	}

	private void goUp() {
		if (upStops.isEmpty() || getDirection() != Direction.UP)
			return;
		while (!upStops.isEmpty() && currFloor <= upStops.last()) {
			step();
			upStops.remove(currFloor);
			Integer next = upStops.higher(currFloor);
			currFloor = next != null ? next : currFloor;
		}
	}

	private void goDown() {
		if (downStops.isEmpty() || getDirection() != Direction.DOWN)
			return;
		while (!downStops.isEmpty() && currFloor >= downStops.first()) {
			step();
			downStops.remove(currFloor);
			Integer next = downStops.lower(currFloor);
			currFloor = next != null ? next : currFloor;
		}
	}

	@Override
	public void step() {
		controller.step(getState());
	}

	@Override
	public void register(Controller controller) {
		controller.register(this);
		this.controller = controller;
	}

	@Override
	public void start() {
		if (this.isEmpty())
			return;
		if (thread == null)
			thread = new Thread(this);
		if (thread.isAlive())
			return;
		thread.start();
	}

	@Override
	public void stop() {
		this.isAlive = false;
		if (thread == null)
			return;
		if (!thread.isAlive())
			return;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
