package com.example
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.services.lambda.runtime.Context
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import spock.lang.Specification
import spock.lang.Shared
import spock.lang.AutoCleanup
import io.micronaut.function.aws.proxy.MicronautLambdaHandler

class TestControllerSpec extends Specification {

    @Shared
    @AutoCleanup
    MicronautLambdaHandler handler = new MicronautLambdaHandler()

    @Shared
    Context lambdaContext = new MockLambdaContext()

    @Shared
    ObjectMapper objectMapper = handler.applicationContext.getBean(ObjectMapper)

    void "test string params"() {
        given:
        String json = objectMapper.writeValueAsString(["a":"A","b":"B","c":"C"])

        when:
        AwsProxyRequest request = new AwsProxyRequestBuilder("/stringParams", HttpMethod.POST.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(json).build()
        AwsProxyResponse response = handler.handleRequest(request, lambdaContext)

        then:
        HttpStatus.OK.code == response.statusCode
        println response.body
        Map map = objectMapper.readValue(response.body, Map)
        map == ["a":"A", "b":"B"]
    }
}
