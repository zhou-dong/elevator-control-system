package com.mesosphere.challenge;

import java.util.TreeSet;

public class ElevatorSCAN implements Elevator, Runnable {

	private int id;
	private int currFloor;
	private Direction currDirection;
	private boolean isAlive;
	private Controller controller;
	private Thread thread;
	private boolean isProductEnv;

	private TreeSet<Integer> upStops;
	private TreeSet<Integer> downStops;

	public ElevatorSCAN(int id) {
		this.id = id;
		this.isAlive = false;
		this.upStops = new TreeSet<>();
		this.downStops = new TreeSet<>();
	}

	@Override
	public int distanceToPickup(Request request) {
		// case 0: this elevator is idle
		if (this.getDirection() == Direction.IDLE)
			return Math.abs(this.getCurrFloor() - request.pickupFloor);

		// case 1: request direction and current direction is different
		if (getDirection() != request.direction)
			return differentDirectionDistance(request.pickupFloor);

		// case 2: all in same direction
		if (getDirection() == Direction.getDirection(getCurrFloor(), request.pickupFloor))
			return Math.abs(getCurrFloor() - request.pickupFloor);

		// case 3: currDirection and request is same but passed pickup floor
		return sameDirectionButPassedDistance(request.pickupFloor);
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
	public void addPickupRequest(Request request) {
		if (request.direction == Direction.UP) {
			addStopFloors(upStops, request);
		} else {
			addStopFloors(downStops, request);
		}
		if (controller != null) {
			controller.removePendingRequest(request);
		}
		if (isProductEnv) {
			this.start();
		}
	}

	private void addStopFloors(TreeSet<Integer> stops, Request request) {
		stops.add(request.pickupFloor);
		stops.add(request.destFloor);
	}

	@Override
	public int getId() {
		return this.id;
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
		return new State(getId(), getCurrFloor(), getNextFloor());
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

	private void step() {
		if (controller != null)
			controller.step(getState());
	}

	@Override
	public void register(Controller controller) {
		controller.register(this);
		this.controller = controller;
		this.isAlive = true;
	}

	private void start() {
		if (this.isEmpty())
			return;
		if (thread == null)
			thread = new Thread(this);
		if (thread.isAlive())
			return;
		thread.start();
	}

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

	public boolean isProductEnv() {
		return isProductEnv;
	}

	public void setProductEnv(boolean isProductEnv) {
		this.isProductEnv = isProductEnv;
	}

	public Direction getDirection() {
		return this.currDirection;
	}

	public void setCurrDirection(Direction direction) {
		this.currDirection = direction;
	}

	public boolean isEmpty() {
		return upStops.isEmpty() && downStops.isEmpty();
	}

	public int getCurrFloor() {
		return this.currFloor;
	}

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
		} else {
			return downStops.isEmpty() ? getCurrFloor() : downStops.first();
		}
	}

}
