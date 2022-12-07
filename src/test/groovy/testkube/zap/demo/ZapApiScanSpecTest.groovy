package testkube.zap.demo

import org.zaproxy.clientapi.core.ApiResponseList
import org.zaproxy.clientapi.core.ClientApi
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class ZapScanAttackSpecTest extends Specification {

    @Shared
    ClientApi api = new ClientApi("localhost", 9080, "1qay2wsx3edc")

    def "Scan OpenAPI definition"() {
        given:
        api.core.newSession("ZapScanAttackSpecTest", "True")
        api.openapi.importUrl("http://microservice.default.svc.cluster.local:8080/openapi/",
                "http://microservice.default.svc.cluster.local:8080")

        when:
        ApiResponseList urls = api.core.urls()

        then:
        urls.items.size() == 5
    }

    def "Attack OpenAPI endpoints"() {
        given:
        api.ascan.enableAllScanners("API-Minimal")
        api.ascan.scan(urls.toString(), "False", "False", "", "", "")

        when:
        def response = api.core.numberOfAlerts("http://microservice.default.svc.cluster.local:8080")

        then:
        response.toString() == "0"

        where:
        urls << ((ApiResponseList) api.core.urls()).items
    }
    
}
