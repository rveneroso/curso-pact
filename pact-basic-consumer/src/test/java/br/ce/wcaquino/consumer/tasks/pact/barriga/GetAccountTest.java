package br.ce.wcaquino.consumer.tasks.pact.barriga;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.PactTestExecutionContext;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.ConsumerPactTest;
import au.com.dius.pact.core.model.RequestResponsePact;
import br.ce.wcaquino.consumer.barriga.service.BarrigaConsumer;
import io.pactfoundation.consumer.dsl.LambdaDsl;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class GetAccountTest extends ConsumerPactTest {
    private final String TOKEN = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NTY5NzF9.7x72Q8YcNk7arsO8kpNv7oVtuC2_mmy02ItpnqwhSxg";
    @Override
    protected RequestResponsePact createPact(PactDslWithProvider builder) {

        DslPart body = LambdaDsl.newJsonArrayMinLike(1, (arr) -> {
            arr.object((obj) -> {
                obj.numberType("id");
                obj.stringType("nome");
            });
        }).build();

        return builder
                .given("There is at least one account")
                .uponReceiving("Retrieve user's accounts")
                    .path("/contas")
                    .method("GET")
                    .headers("Authorization", TOKEN)
                .willRespondWith()
                    .status(200)
                    .body(body)
                .toPact();
    }

    @Override
    protected String providerName() {
        return "Barriga";
    }

    @Override
    protected String consumerName() {
        return "BasicConsumer";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected void runTest(MockServer mockServer, PactTestExecutionContext context) throws IOException {
        BarrigaConsumer consumer = new BarrigaConsumer(mockServer.getUrl());
        List accounts = consumer.getAccounts(TOKEN);
        assertTrue(accounts.size() >= 1);

    }
}
