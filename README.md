# Elevator Control System (Mesosphere Distributed Applications Engineer Challenge)


At first, I don't want to let the "Control center" to control every step of elevator.

I want to let elevator 

After a pickup request sent to control system, the control system will ask every elevator who is the nearest one to the pickup floor.

How to compute the distance from elevator to pickup floor:

1. Elevator is IDLE : Math.abs(currentFloor - pickupFloor) ;
2. Elevator and pickup-request have different direction: Math.abs(currentFloor - destFloor) + Math.abs(destFloor - pickupFloor) ;
3. Elevator and pickup-request have same direction and elevator not passed: Math.abs(currentFloor - pickupFloor);
4. Elevator and pickup-request have same direction but elevator passed: Math.abs(currentFloor - destFloor) + Math.abs(destFloor - firstFloor/lastFloor) + Math.abs(firstFloot/lastFloor - pickupFloor);
 


Design Architecture


Distance Algorithm:


Implement of SCAN algorithm:

Use to TreeSet to implement scan.


Design pattern:

- Wanna to use: 
- Observer


Future Work:

In this solution, Elevator-Control-System will choose the 'nearest' Elevator to handle the pickup request.

In this scenario, some elevator will working overload and the others may be idle.

Should have a better solution, which will consider 'comprehensive environments'. 

Never consider of numbers of Passsiger in elevator and number of quest pansigner.