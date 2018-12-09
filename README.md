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
- POST /parkinglots/{parkingLotId}/orders Input: {"parkingOrderId": Integer, "vaild": true}
- POST /parkinglots Input: {"name": String, "capacity":Integer}


## Milestones

### Iteration 1
#### APIs
- [x] Customer: Make car parking order
- [x] Parking Boy: Can View all orders, park a car (Accept an order, decide which parking lot for this order)
- [x] Manager: view all parking lots
- [x] System Admin: Create parkinglots, create parkingboys,view all parking lots 

#### UI
- [ ]  Parking Boy UI View - Need to refactor
- [ ]  Customers Car Parking Request Page

#### Authentication
- [x] Sign Up
- [x] Sign In Phrase 1 - Get Token
- [x] Sign In Phrase 2 - Use Token for API authorisation
- [ ] Sign In Phrase 3 - Reuse token throughout web surfing



