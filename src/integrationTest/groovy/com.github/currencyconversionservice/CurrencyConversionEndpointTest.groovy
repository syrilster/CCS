package com.github.currencyconversionservice

import com.github.currencyconversionservice.endpoints.CurrencyConversionController
import com.github.currencyconversionservice.model.CurrencyConverter
import com.github.currencyconversionservice.proxy.CurrencyExchangeServiceProxy
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.cloud.contract.wiremock.WireMockRestServiceServer
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import javax.ws.rs.core.MultivaluedMap

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Ignore
class CurrencyConversionEndpointTest extends Specification {
    @Autowired
    private TestRestTemplate testRestTemplate
    //@Autowired
    //private RestTemplate restTemplate
    CurrencyExchangeServiceProxy currencyExchangeServiceProxy = Mock(CurrencyExchangeServiceProxy)
    CurrencyConversionController controller = new CurrencyConversionController("proxy": currencyExchangeServiceProxy)

    def setup() {

    }

    def cleanup() {

    }

    @Test
    def "Sample test case"() {
        given:
        CurrencyConverter currencyConverter = new CurrencyConverter(null, null, null, new BigDecimal(51.56), null, new BigDecimal(5100), 8080)
        currencyExchangeServiceProxy.retrieveExchangeValue("AUD", "INR") >> currencyConverter
        when:
        def entity = controller.retrieveExchangeValueFeign("AUD", "INR", new BigDecimal(100))
        then:
        assert entity != null
    }

    @Test
    def "Sample test case programatic"() {
        given:
        MockRestServiceServer server = MockRestServiceServer.createServer(this.restTemplate)
        server.expect(requestTo("/url"))
                .andExpect(method(HttpMethod.GET))
                .andRespond({ response -> throw new SocketException() })

        def request = new HttpEntity<MultivaluedMap<String, String>>("body", new HttpHeaders())
        when:
        def entity = testRestTemplate.postForEntity("url", request, CurrencyConverter.class)
        then:
        entity.statusCode == HttpStatus.GATEWAY_TIMEOUT
    }

    @Test
    def "Sample test case json stub"() {
        given:
        MockRestServiceServer server = WireMockRestServiceServer
                .with(this.restTemplate)
                .stubs("classpath:/stubs/testResponse.json")
                .baseUrl("basepath")
                .build()
        def request = new HttpEntity<MultivaluedMap<String, String>>("body", new HttpHeaders())
        when:
        def entity = testRestTemplate.postForEntity("url", request, CurrencyConverter.class)
        then:
        entity.statusCode == HttpStatus.OK
    }
}
