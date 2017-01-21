package com.mesosphere.challenge.test;

import org.junit.Before;
import org.junit.Test;

import com.mesosphere.challenge.Elevator.Direction;
import com.mesosphere.challenge.Elevator.Request;
import com.mesosphere.challenge.ElevatorSCAN;

import junit.framework.Assert;

public class TestElevatorSCAN {

	ElevatorSCAN scan = null;

	@Before
	public void init() {
		scan = new ElevatorSCAN(1);
		scan.setProductEnv(false);
	}

	@Test
	public void testDistanceSameDirection() {
		scan.setCurrDirection(Direction.UP);
		scan.addPickupRequest(new Request(5, 10));
		int actual = scan.distanceToPickup(new Request(6, 7));
		Assert.assertEquals(6, actual);
	}

	@Test
	public void testDistanceDifferentDirection() {
		scan.setCurrDirection(Direction.UP);
		scan.addPickupRequest(new Request(5, 10));
		int actual = scan.distanceToPickup(new Request(8, 4));
		Assert.assertEquals(12, actual);
	}

	@Test
	public void testDistanceSameDirectionButPassed() {
		scan.setCurrDirection(Direction.UP);
		scan.addPickupRequest(new Request(8, 10));
		scan.addPickupRequest(new Request(9, -9));
		int actual = scan.distanceToPickup(new Request(-5, -1));
		Assert.assertEquals(33, actual);
	}

	@Test
	public void testNextFloor() {
		scan.setCurrDirection(Direction.UP);
		scan.addPickupRequest(new Request(8, 10));
		scan.addPickupRequest(new Request(9, -9));
		Assert.assertEquals(8, scan.getNextFloor());
		scan.setCurrDirection(Direction.DOWN);
		Assert.assertEquals(-9, scan.getNextFloor());
	}

	@Test
	public void testDestFloor() {
		scan.setCurrDirection(Direction.UP);
		scan.addPickupRequest(new Request(8, 10));
		scan.addPickupRequest(new Request(9, -9));
		Assert.assertEquals(10, scan.getDestFloor());
		scan.setCurrDirection(Direction.DOWN);
		Assert.assertEquals(-9, scan.getNextFloor());
	}
}
