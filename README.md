# Elevator Control System (Mesosphere Distributed Applications Engineer Challenge)


At first, I don't want to let the "Control center" to control every step of elevator.

I want to let elevator 

After a pickup request sent to control system, the control system will ask every elevator who is the nearest one to the pickup floor.

## Implement SCAN algorithm:

### Data structure: Two TreeSet(Objects are stored in a sorted and ascending order)

- up-stop-floors(TreeSet): All the floors which elevator will stopped during ascent.
- down-stop-floors(TreeSet): All the floors which elevator will stopped during descent.

### Compute the distance from elevator to pickup floor:

1. Elevator is IDLE : 
	- Math.abs(currentFloor - pickupFloor) ;
2. Elevator and pickup-request have different direction:
	- Math.abs(currentFloor - destFloor) + 
	- Math.abs(destFloor - pickupFloor) ;
3. Elevator and pickup-request have same direction and elevator not passed:
	- Math.abs(currentFloor - pickupFloor);
4. Elevator and pickup-request have same direction but elevator passed:
	- Math.abs(currentFloor - destFloor) + 
	- Math.abs(destFloor - firstFloor/lastFloor) + 
	- Math.abs(firstFloot/lastFloor - pickupFloor);
 
### Handle the pickup request: pickup(int pickupFloor, int destFloor)

- pickupFloor == destFloor: return
- pickupFloor - destFloor < 0 ? up : down

1. up: add both pickupFloor and destFloor to up-stop-floors.
2. down: add both pickupFloor and destFloor to down-stop-floors.

Design pattern:

- Wanna to use: 
- Observer

## Disadvantages And Future Work:

### First

#### Disadvantages:

1. Some elevators may be always more near to passengers then others, which could make some elevators overload meanwhile the others are IDLE.
2. Never consider the capacity of the elevator.

#### Solution:

- Use a 'score' indicate how much match of a elevator to passenger.

### Second

#### Disadvantage:

- In this project, all the elevators use the same SCAN algorithm to traverse among the floors. May be different elevators could choose different algorithm.

#### Solution:

- Implement 'Strategy Design Pattern' to change algorithm friendly.