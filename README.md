## APIs List

### Parking Order APIs
- GET /orders
- POST /orders Input: {"carNumber":String}

### Parking Boy APIs
- GET /parkingclerks
- POST /parkingclerks Input: {"name":String,"email":String,"phoneNumber":String,"status": Not yet decide, always to be "OnDuty"}
- GET /parkingclerks/{id}/parkinglots
- POST /parkingclerks/{id}/parkinglots
- GET /parkingclerks/{id}/orders
- POST /parkingclerks/{id}/orders Input: {"parkingOrderId": Integer}


### Parking Lot APIs
- GET /parkinglots
- POST /parkinglots Input: {"name": String, "capacity":Integer}
- POST /parkinglots/{id}/orders Input: {"parkingOrderId": Integer, "vaild": true}
- GET /parkinglots/{id}/orders 


## Milestones

### Iteration 1
#### APIs
- [x] Customer: Make car parking order
- [x] Parking Boy: Can View all orders, park a car (Accept an order, decide which parking lot for this order)
- [x] Manager: view all parking lots
- [x] System Admin: Create parkinglots, create parkingboys,view all parking lots 

#### UI
- [ ]  Parking Boy UI View - Need to refactor
- [x]  Customers Car Parking Request Page

#### Authentication
- [x] Sign Up
- [x] Sign In Phrase 1 - Get Token
- [x] Sign In Phrase 2 - Use Token for API authorisation
- [ ] Sign In Phrase 3 - Reuse token throughout web surfing



