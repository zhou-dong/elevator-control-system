package com.mesosphere.challenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mesosphere.challenge.Elevator.State;

public class ControllerImpl implements Controller {

	private Map<Integer, Elevator> elevators;

	public ControllerImpl() {
		this.elevators = new HashMap<>();
	}

	@Override
	public List<State> status() {
		List<State> states = new ArrayList<>();
		for (Elevator elevator : elevators.values())
			states.add(elevator.getState());
		return states;
	}

	@Override
	public void update(State state) {
		if (state == null || !elevators.containsKey(state.elevatorId))
			return;
		elevators.get(state.elevatorId).setCurrFloor(state.currentFloor);
		elevators.get(state.elevatorId).setDestFloor(state.destFloor);
	}

	@Override
	public void pickup(int pickupFloor, int destFloor) {
		if (pickupFloor == destFloor)
			return;
		if (elevators.size() == 0)
			throw new RuntimeException("no elevator available");
		nearestElevator(pickupFloor, destFloor).addPickupRequest(pickupFloor, destFloor);
	}

	private Elevator nearestElevator(int pickupFloor, int destFloor) {
		Elevator result = null;
		int min = Integer.MAX_VALUE;
		for (Elevator elevator : elevators.values()) {
			int distance = elevator.distanceToPickup(pickupFloor, destFloor);
			if (distance < min) {
				min = distance;
				result = elevator;
			}
		}
		return result;
	}

	@Override
	public void step(State state) {
		StringBuilder sb = new StringBuilder();
		sb.append("Elevator: " + state.elevatorId);
		sb.append(" arrived: " + state.currentFloor);
		sb.append(" dest: " + state.destFloor);
		System.out.println(sb.toString());
	}

	@Override
	public void register(Elevator elevator) {
		elevators.put(elevator.getId(), elevator);
	}

	@Override
	public void unregister(Elevator elevator) {
		elevators.remove(elevator.getId());
	}

}
