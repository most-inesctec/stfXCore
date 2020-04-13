package stfXCore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;

import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

public abstract class MockTemplate {

    protected ClientAndServer mockServer;

    @BeforeEach
    public void startServer() {
        mockServer = startClientAndServer(5000);
    }

    @AfterEach
    public void stopServer() {
        mockServer.stop();
    }

    protected void mockCPD() {
        new MockServerClient("127.0.0.1", 5000)
                .when(request()
                        .withMethod("POST")
                        .withPath("/cpd-all")
                        .withHeader("\"Content-type\", \"application/json\"")
                        .withBody(exact("[{\"x\":[[49.448746,49.44956],[53.430676,49.449554],[53.434605,53.427555],[51.43714,55.40298],[49.443207,53.429226],[49.448746,49.44956]],\"y\":[[50.0,50.0],[54.0,50.0],[54.0,54.0],[52.0,56.0],[50.0,54.0],[50.0,50.0]]}]")))
                .respond(response()
                        .withStatusCode(200)
                        .withHeaders(
                                new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(exact("{\"transformations\": [{\"rotation\": 0.00020695867664263134, \"scale\": 0.9943084085430711, \"translation\": [-0.26522648780481717, -0.2675864390243703]}]}"))
                        .withDelay(TimeUnit.MILLISECONDS, 500));
    }
}
