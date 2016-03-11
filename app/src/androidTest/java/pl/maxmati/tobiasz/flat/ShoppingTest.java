package pl.maxmati.tobiasz.flat;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.runner.RunWith;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import pl.maxmati.tobiasz.flat.api.APIConnector;
import pl.maxmati.tobiasz.flat.api.APIRequest;
import pl.maxmati.tobiasz.flat.api.session.SessionException;
import pl.maxmati.tobiasz.flat.api.shopping.PendingOrder;
import pl.maxmati.tobiasz.flat.api.shopping.ShoppingManager;

import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import pl.maxmati.tobiasz.flat.test.R;

import static org.junit.Assert.assertArrayEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by mmos on 05.03.16.
 *
 * @author mmos
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ShoppingTest extends InstrumentationTestCase {
    @org.junit.Test
    public void testGetPendingOrders() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'",
                Locale.ENGLISH);
        Date dates[] = {
                dateFormat.parse("2016-03-10T13:31:45.000Z"),
                dateFormat.parse("2016-03-11T22:31:45.000Z"),
                dateFormat.parse("2001-09-11T6:28:22.000Z"),
        };
        assertArrayEquals("Deserialization inconsistency", new PendingOrder[]{
                new PendingOrder(1, dates[0], 1, "Example product number " +
                        "one", 0, "Brief description about product", 2.6, 3),
                new PendingOrder(2, dates[1], 3, "Just product", 1,
                        "Buy it or die", 0.01, 0.75),
                new PendingOrder(4, dates[2], 2, "The most important product ever", 2,
                        "BUY, BUY, BUY", 1.2, 0.5)
        }, ShoppingManager.get(new APIConnector("/", null) {
            @Override
            public <T> ResponseEntity<T> sendRequest(APIRequest request, Class<T> responseType)
                    throws SessionException {
                RestTemplate restTemplate = prepareRestTemplate(request);
                MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

                mockServer.expect(requestTo("/shopping")).andExpect(method(HttpMethod.GET))
                        .andRespond(withSuccess(new InputStreamResource(InstrumentationRegistry
                                        .getInstrumentation().getContext().getResources()
                                        .openRawResource(R.raw.get_pending_orders_response)),
                                MediaType.APPLICATION_JSON));

                ResponseEntity<T> response = exchange(getApiUri() + request.getRequestPath(),
                        restTemplate, request, new HttpHeaders(), responseType);

                mockServer.verify();

                return response;
            }
        }));
    }
}
