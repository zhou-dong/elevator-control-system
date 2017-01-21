# Elevator Control System 

Mesosphere Distributed Applications Engineer Challenge

## Whole Structure

At first, I don't want to let the "Central Control System" to control every step of the elevators. Each elevator should have it's own way to traversal among the floors.

Steps after controller received pickup request (int pickupFloor, int destFloor) :

1. Controller received a pickup request.
2. Controller create a `request` and store it in `pending-request-list`.
3. Controller broadcast the `request` to all elevators.
4. All elevators compute the distance to pickup floor, then response it to controller.
5. Controller choose the nearest elevator to handle the request.
6. The nearest elevator pickup the request
7. The elevator call the controller to remove the request from `pending-request-list`.

## Data Structures, Algorithms and Design Patterns

- Data Structure: TreeSet
- Algorithms: SCAN
- Design patterns:
	- Observer Design Pattern
		- Not typical Observer Pattern, Just part of it.
		- Elevator will send step() notification after every step. 
	- Strategy Design Pattern
		- Future work for easier change SCAN algorithm to others.
	
## Implement SCAN algorithm

#### Data structure: Two TreeSet(Objects are stored in a sorted and ascending order)

- up-stop-floors(TreeSet): All the floors which elevator will stopped during ascent.
- down-stop-floors(TreeSet): All the floors which elevator will stopped during descent.

#### Compute the distance from elevator to pickup floor:

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
 
#### Handle the pickup request: pickup(int pickupFloor, int destFloor)

- pickupFloor == destFloor: return
- pickupFloor - destFloor < 0 ? up : down

1. up: add both pickupFloor and destFloor to up-stop-floors.
2. down: add both pickupFloor and destFloor to down-stop-floors.

## Disadvantages And Future Work:

#### Disadvantages 1:

1. Some elevators may be always more near to passengers then others, which could make some elevators overload meanwhile the others are IDLE.
2. Never consider the capacity of the elevator.

#### Solution 1:

- Use a `score` indicate how much match of a elevator to passenger.

#### Disadvantage 2:

- In this project, all the elevators use the same SCAN algorithm to traverse among the floors. May be different elevators should choose different algorithm.

#### Solution 2:

- Implement 'Strategy Design Pattern' to change algorithm friendly.

#### Disadvantage 3:

- The interface I defined were terrible, I should only left the really necessary method. If I had time, should redefine the interface. 

## Build Instructions

I leave all the eclipse configuration in repository, so just download the repository and import into eclipse as a java project.

I use JUnit to test the cases include:

- distance of elevator to pickup floor (different scenarios).
- controller assign request to elevator.
- elevator notify every step to controller.