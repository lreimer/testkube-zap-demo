package testkube.zap.demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.zaproxy.clientapi.core.ClientApi;

@Disabled
public class ZapScanAttackTest {
    @Test 
    void apiImportAndScan() throws Exception {
        ClientApi api = new ClientApi("localhost", 9080, "1qay2wsx3edc", true);

        api.core.newSession("ZapScanAttackTest", "True");
        api.openapi.importUrl("http://microservice.default.svc.cluster.local:8080/openapi/",
                "http://microservice.default.svc.cluster.local:8080");
    }
}
