package tcc;

import tcc.flight.FlightReservationDoc;
import tcc.hotel.HotelReservationDoc;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Simple non-transactional client. Can be used to populate the booking services
 * with some requests.
 */
public class TestClient {
	public static void main(String[] args) {
		int counter = 0;
		try {
			Boolean allIsBooked = false;
			Boolean flightReserved = false;
			Boolean hotelReserved = false;
			String flightId = "";
			String hotelId = "";
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(TestServer.BASE_URI);

//			GregorianCalendar tomorrow = new GregorianCalendar();
//			tomorrow.setTime(new Date());
//			// TODO change "amount" for rollback- szenario
//			tomorrow.add(GregorianCalendar.DAY_OF_YEAR, 0);
//			long ts = tomorrow.getTimeInMillis();
			long ts = 1610279472138L;
			// book flight

			WebTarget webTargetFlight = target.path("flight");

			FlightReservationDoc docFlight = new FlightReservationDoc();
			docFlight.setName("Pia");
			docFlight.setFrom("Karlsruhe");
			docFlight.setTo("Berlin");
			docFlight.setAirline("airberlin");
			docFlight.setDate(ts);
			//docFlight.setDate(tomorrow.getTimeInMillis());

			Response responseFlight = webTargetFlight.request().accept(MediaType.APPLICATION_XML)
					.post(Entity.xml(docFlight));
			FlightReservationDoc outputFlight = null;

			if (responseFlight.getStatus() != 200) {
				System.out.println("Failed flight reservation: HTTP error code : " + responseFlight.getStatus());
			}else {
				outputFlight = responseFlight.readEntity(FlightReservationDoc.class);
				System.out.println("Output from flight server: " + outputFlight);
				flightReserved = true;
				flightId = outputFlight.getUrl().split(webTargetFlight.getUri().toString())[1].replace("/", "");
			}

			// book hotel

			WebTarget webTargetHotel = target.path("hotel");

			HotelReservationDoc docHotel = new HotelReservationDoc();
			docHotel.setName("Pia");
			docHotel.setHotel("Interconti");
			docHotel.setDate(ts);

			Response responseHotel = webTargetHotel.request().accept(MediaType.APPLICATION_XML)
					.post(Entity.xml(docHotel));

			HotelReservationDoc outputHotel = null;

			if (responseHotel.getStatus() != 200) {
				System.out.println("Failed hotel reservation: HTTP error code : " + responseHotel.getStatus());
			}else {
				outputHotel = responseHotel.readEntity(HotelReservationDoc.class);
				System.out.println("Output from hotel server: " + outputHotel);

				hotelReserved = true;
				hotelId = outputHotel.getUrl().split(webTargetHotel.getUri().toString())[1].replace("/", "");
			}

			//Confirm

//			//Scenario 1: no more reservations possible.
			//As delete is idempotent, we can repeat this.
			if (!flightReserved | !hotelReserved){

				Response responseHotelCancel = webTargetHotel.path(hotelId).request().delete();
				System.out.println("No more flights to book. Reservation deleted with status " +
						responseHotelCancel.getStatus());


				Response responseFlightCancel = webTargetFlight.path(flightId).request().delete();
				System.out.println("No more hotels to book. Reservation deleted with status " +
						responseFlightCancel.getStatus());

				System.out.println("Transactions rolled back! Nothing booked.");
				return;
			}

			//Szenario 2:
			// As long as there are errors and whether the flight nor the hotel reservation
			// have expired, try confirming
			while (!allIsBooked && (outputFlight.getExpires() > ts)
					&& (outputHotel.getExpires() > ts)) {
				System.out.println("Not expired yet");
				counter++;
				Response responseFlightConfirm = webTargetFlight.path(flightId).request()
						.put(Entity.text(MediaType.TEXT_PLAIN));
				System.out.println("Output from flight server: " + responseFlightConfirm.getStatus());

				if (responseFlightConfirm.getStatus() != 200) {
					System.out.println(" Failed : HTTP error code : " + responseFlightConfirm.getStatus());
					continue;
				}

				Response responseHotelConfirm = webTargetHotel.path(hotelId).request()
						.put(Entity.text(MediaType.TEXT_PLAIN));
				System.out.println("Output from hotel server: " + responseHotelConfirm.getStatus());

				if (responseHotelConfirm.getStatus() != 200) {
					System.out.println("Failed : HTTP error code : " + responseHotelConfirm.getStatus());
					continue;
				}
				allIsBooked = true;
				System.out.println("OK! Flight and hotel are booked!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
