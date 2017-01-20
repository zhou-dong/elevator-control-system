package com.mesosphere.challenge.test;

import org.junit.Before;
import org.junit.Test;

import com.mesosphere.challenge.Controller;
import com.mesosphere.challenge.ControllerImpl;
import com.mesosphere.challenge.Elevator.Direction;
import com.mesosphere.challenge.ElevatorSCAN;

import junit.framework.Assert;

public class TestController {

	Controller controller;

	@Before
	public void init() {
		controller = new ControllerImpl();
	}

	@Test
	public void testDistanceDifferentDirection() {

		ElevatorSCAN scan = new ElevatorSCAN(1);
		scan.setCurrDirection(Direction.UP);
		scan.addPickupRequest(5, 10);
		int actual = scan.distanceToPickup(8, 4);
		Assert.assertEquals(12, actual);
	}
}
