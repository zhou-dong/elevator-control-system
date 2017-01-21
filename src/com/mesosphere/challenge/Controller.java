package com.mesosphere.challenge;

import java.util.List;

import com.mesosphere.challenge.Elevator.Request;
import com.mesosphere.challenge.Elevator.State;

public interface Controller {

	public List<State> getStatus();

	public void update(State state);

	public void pickup(int pickupFloor, int destFloor);

	public void step(State state);

	public void register(Elevator elevator);

	public void unregister(Elevator elevator);

	public void removePendingRequest(Request request);

}
