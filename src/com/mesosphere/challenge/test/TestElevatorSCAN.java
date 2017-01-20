package com.mesosphere.challenge.test;

import org.junit.Before;
import org.junit.Test;

import com.mesosphere.challenge.Elevator.Direction;

import junit.framework.Assert;

import com.mesosphere.challenge.ElevatorSCAN;

public class TestElevatorSCAN {

	ElevatorSCAN scan = null;

	@Before
	public void init() {
		scan = new ElevatorSCAN(1);
	}

	@Test
	public void testDistanceSameDirection() {
		scan.setCurrDirection(Direction.UP);
		scan.addPickupRequest(5, 10);
		int actual = scan.distanceToPickup(6, 7);
		Assert.assertEquals(6, actual);
	}

	@Test
	public void testDistanceDifferentDirection() {
		scan.setCurrDirection(Direction.UP);
		scan.addPickupRequest(5, 10);
		int actual = scan.distanceToPickup(8, 4);
		Assert.assertEquals(12, actual);
	}

	@Test
	public void testDistanceSameDirectionButPassed() {
		scan.setCurrDirection(Direction.UP);
		scan.addPickupRequest(8, 10);
		scan.addPickupRequest(9, -9);
		int actual = scan.distanceToPickup(-5, -1);
		Assert.assertEquals(33, actual);
	}

}
