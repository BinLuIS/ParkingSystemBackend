## APIs List

### Parking Order APIs
- GET /orders
- POST /orders Input: {"carNumber":String}

### Parking Boy APIs
- GET /parkingclerks
- POST /parkingclerks Input: {"name":String,"email":String,"phoneNumber":String,"status": Not yet decide, always to be "OnDuty"}
- POST /parkingclerks/{id}/parkingorders Input: {"parkingOrderId": Integer}

### Parking Lot APIs
- GET /parkinglots
- POST /parkinglots/{parkingLotId}/orders Input: {"parkingOrderId": Integer}
- POST /parkinglots Input: {"name": String, "capacity":Integer}


## Milestones

### Iteration 1
- [x] Customer: Make car parking order
- [x] Parking Boy: Can View all orders, park a car (Accept an order, decide which parking lot for this order)
- [x] Manager: view all parking lots
- [x] System Admin: Create parkinglots, create parkingboys,view all parking lots 
- [ ]  Parking Boy UI View
- [ ]  Customers Car Parking Request Page

