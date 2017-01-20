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
		ElevatorSCAN scan1 = new ElevatorSCAN(1);
		ElevatorSCAN scan2 = new ElevatorSCAN(2);
		scan1.register(controller);
		scan2.register(controller);
		scan1.setCurrDirection(Direction.UP);
		scan2.setCurrDirection(Direction.DOWN);
		scan1.addPickupRequest(5, 10);
		int actual = scan1.distanceToPickup(8, 4);
		Assert.assertEquals(12, actual);
		scan1.addPickupRequest(8, 4);
		scan2.addPickupRequest(4, 5);
		scan2.addPickupRequest(7, 8);
		scan1.addPickupRequest(10, 1);

		controller.pickup(15, 10);
		controller.pickup(51, 10);
		controller.pickup(5, 100);
		controller.pickup(125, 10);
		controller.pickup(5, 10);
		controller.pickup(12, 5);
		controller.pickup(21, 3);
		controller.pickup(8, 17);
		controller.pickup(4, 79);
		controller.pickup(66, 5);
		controller.pickup(1, 22);
		controller.pickup(33, 0);
		controller.pickup(8, 99);
	}

}
