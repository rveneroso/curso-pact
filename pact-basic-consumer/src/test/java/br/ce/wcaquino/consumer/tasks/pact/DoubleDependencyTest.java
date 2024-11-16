package br.ce.wcaquino.consumer.tasks.pact;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.ConsumerPactRunnerKt;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.model.MockProviderConfig;
import au.com.dius.pact.core.model.RequestResponsePact;
import br.ce.wcaquino.consumer.barriga.service.BarrigaConsumer;
import br.ce.wcaquino.consumer.tasks.service.DoubleDependency;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DoubleDependencyTest {
    private final String TOKEN = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NTY5NzF9.7x72Q8YcNk7arsO8kpNv7oVtuC2_mmy02ItpnqwhSxg";
    private final String ACCOUNT_NAME = "Updated account";
    @Test
    public void test() {

        PactDslJsonBody requestBody = new PactDslJsonBody()
                .stringType("email", "rvenerosostudy@gmail.com")
                .stringType("senha", "15112024");

        PactDslJsonBody responseBody = new PactDslJsonBody()
                .stringType("token");

        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("BasicConsumer")
                .hasPactWith("Barriga")
                .given("Your user is created")
                .uponReceiving("Signin with a valid user")
                    .path("/signin")
                    .method("POST")
                    .body(requestBody)
                .willRespondWith()
                    .status(200)
                    .body(responseBody)
                .toPact();

        DslPart requestTBody = new PactDslJsonBody()
                .stringType("task", "Task With String")
                .date("dueDate", "yyyy-MM-dd", new Date());

        DslPart responseTBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("task")
                .date("dueDate", "yyyy-MM-dd", new Date());

        RequestResponsePact tPact = ConsumerPactBuilder
                    .consumer("BasicConsumer")
                    .hasPactWith("Tasks")
                .uponReceiving("Save a task with string")
                    .path("/todo")
                    .method("POST")
                    .body(requestTBody)
                .willRespondWith()
                    .status(201)
                    .body(responseTBody)
                .toPact();
        MockProviderConfig config = MockProviderConfig.createDefault();
        PactVerificationResult bResult = ConsumerPactRunnerKt.runConsumerTest(pact, config, (mockServer, context) -> {
            PactVerificationResult tResult = ConsumerPactRunnerKt.runConsumerTest(tPact, config, (tMockServer, tContext) -> {
                DoubleDependency dd = new DoubleDependency(mockServer.getUrl(), tMockServer.getUrl());
                String task = dd.getToken("rvenerosostudy@gmail.com", "15112024");
                assertNotNull(task);
                //assertNull(task);
                return null;
            });
            if (tResult instanceof  PactVerificationResult.Error) {
                throw new RuntimeException(((PactVerificationResult.Error) tResult).getError());
            }
            assertThat(tResult, is(instanceOf(PactVerificationResult.Ok.class)));
            return null;
        });

        if (bResult instanceof  PactVerificationResult.Error) {
            throw new RuntimeException(((PactVerificationResult.Error) bResult).getError());
        }
        //assertThat(result, is(instanceOf(PactVerificationResult.Ok.class)));

    }
}
