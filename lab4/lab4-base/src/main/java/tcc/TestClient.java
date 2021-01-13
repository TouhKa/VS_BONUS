package tcc;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tcc.flight.FlightReservationDoc;
import tcc.hotel.HotelReservationDoc;

/**
 * Simple non-transactional client. Can be used to populate the booking services
 * with some requests.
 */
public class TestClient {
	public static void main(String[] args) {
		int counter = 0;
		try {
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(TestServer.BASE_URI);

			GregorianCalendar tomorrow = new GregorianCalendar();
			tomorrow.setTime(new Date());
			// TODO change "amount" for rollback- szenario
			tomorrow.add(GregorianCalendar.DAY_OF_YEAR, 0);

			// book flight

			WebTarget webTargetFlight = target.path("flight");

			FlightReservationDoc docFlight = new FlightReservationDoc();
			docFlight.setName("Pia");
			docFlight.setFrom("Karlsruhe");
			docFlight.setTo("Berlin");
			docFlight.setAirline("airberlin");
			docFlight.setDate(tomorrow.getTimeInMillis());

			Response responseFlight = webTargetFlight.request().accept(MediaType.APPLICATION_XML)
					.post(Entity.xml(docFlight));

			if (responseFlight.getStatus() != 200) {
				System.out.println("Failed : HTTP error code : " + responseFlight.getStatus());
			}

			FlightReservationDoc outputFlight = responseFlight.readEntity(FlightReservationDoc.class);
			System.out.println("Output from Server: " + outputFlight);

			// book hotel

			WebTarget webTargetHotel = target.path("hotel");

			HotelReservationDoc docHotel = new HotelReservationDoc();
			docHotel.setName("Pia");
			docHotel.setHotel("Interconti");
			docHotel.setDate(tomorrow.getTimeInMillis());

			Response responseHotel = webTargetHotel.request().accept(MediaType.APPLICATION_XML)
					.post(Entity.xml(docHotel));

			if (responseHotel.getStatus() != 200) {
				System.out.println("Failed : HTTP error code : " + responseHotel.getStatus());
			}

			HotelReservationDoc outputHotel = responseHotel.readEntity(HotelReservationDoc.class);
			System.out.println("Output from Server: " + outputHotel);

			//Confirm

			Boolean everythingIsBooked = false;
			String flightId = "";
			String hotelId = "";

			try {
				//remove "/" in URI
				flightId = outputFlight.getUrl().split(webTargetFlight.getUri().toString())[1].replace("/", "");
				hotelId = outputHotel.getUrl().split(webTargetHotel.getUri().toString())[1].replace("/", "");
				System.out.println(flightId + " " + hotelId);
				System.out.println(outputFlight.getExpires() < tomorrow.getTimeInMillis());
				System.out.println(outputHotel.getExpires() < tomorrow.getTimeInMillis());
				System.out.println("Timestemp Send: " + tomorrow.getTimeInMillis());
			} catch (NullPointerException e) {
				System.out.println("No more hotels to book");
				Response responseFlightCancel = webTargetFlight.path(flightId).request().delete();

				if (responseFlightCancel.getStatus() != 200) {
					System.out.println("Failed : HTTP error code : " + responseFlightCancel.getStatus());
				}
				return;
			}

			// As long as there are errors and whether the flight nor the hotel reservation
			// have expired, try confirming
			while (!everythingIsBooked && (outputFlight.getExpires() > tomorrow.getTimeInMillis())
					&& (outputHotel.getExpires() > tomorrow.getTimeInMillis())) {
				System.out.println("Not expired");
				counter++;
				Response responseFlightConfirm = webTargetFlight.path(flightId).request()
						.put(Entity.text(MediaType.TEXT_PLAIN));

				if (responseFlightConfirm.getStatus() != 200) {
					System.out.println("Failed : HTTP error code : " + responseFlightConfirm.getStatus());
					continue;
				}

				Response responseHotelConfirm = webTargetHotel.path(hotelId).request()
						.put(Entity.text(MediaType.TEXT_PLAIN));
				if (responseHotelConfirm.getStatus() != 200) {
					System.out.println("Failed : HTTP error code : " + responseHotelConfirm.getStatus());
					continue;
				}

				everythingIsBooked = true;
			}

			// Hotel or Flight reservation expired - so we cancel the remaining reservation
			if (!everythingIsBooked) {
				Response responseFlightCancel = webTargetFlight.path(flightId).request().delete();

				if (responseFlightCancel.getStatus() != 200) {
					System.out.println("Failed : HTTP error code : " + responseFlightCancel.getStatus());
				}

				Response responseHotelCancel = webTargetHotel.path(hotelId).request().delete();

				if (responseHotelCancel.getStatus() != 200) {
					System.out.println("Failed : HTTP error code : " + responseHotelCancel.getStatus());
				}

				System.out.println("Transactions rolled back! Nothing booked.");
			} else {
				System.out.println("OK! Flight and hotel are booked!");
			}
			System.out.println("Counter: " + counter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
