# Booking service for hiking trails

### Minimum requirements:
* _docker_ installed

### Assumptions
* The price is always in the same currency (f.e. Euro), so we don't have to deal with currency converter/exchange.
* All dates and times are in local DateTime
* The booking price is a simple calculation of `Trail.unitPrice` * `Booking.hikers.size` (so no discounts for children, students, old people, etc)
* There's no limit of people for booking same trail at the same date
* The booking id is just a UUID, which is not user-friendly. In the real world it should be introduced a natural id (similar to flight booking id)
* The booking process is ultra simplified: no trails are overlapped, the system doesn't check if a hiker would like to participate in two overlapped trails at the same day(f.e. T1 [10:00-12:00] & T2 [11:00-12:00]). 

### Content

* Run the tests: `./gradlew clean test -i` or run the script `./run-tests`

* The following commands are provided to run the applications with`docker-compose`:

Operation | Command
--------- | ----------
Start:    | `./start`
Stop:     | `./stop`
Restart:  | `./restart`

- [actuator](http://localhost:8080/actuator)
- [health check](http://localhost:8080/actuator/health)
- [API docs](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

After starting the service, you can start testing it using OpenAPI docs.

The following endpoints are exposed to simulate booking use cases:

No. | Operation            | Endpoint
--- | -------------------- | -------------------------------------------------
1)  | Get all trails       | `GET`    http://localhost:8080/api/trails
2)  | Get trail by ID      | `GET`    http://localhost:8080/api/trails/{trailId}
3)  | Create hiker         | `POST`   http://localhost:8080/api/hikers
4)  | Get hiker by ID      | `GET`    http://localhost:8080/api/hikers/{hikerId}
5)  | Create booking       | `POST`   http://localhost:8080/api//api/booking
6)  | Get booking by ID    | `GET`    http://localhost:8080/api/booking/{id}
7)  | Delete booking by ID | `DELETE` http://localhost:8080/api/booking/{id}

### Final note
Please feel free to contact me sending an [email](mailto:led.spaho@gmail.com) for any discussion/doubt/clarification.
