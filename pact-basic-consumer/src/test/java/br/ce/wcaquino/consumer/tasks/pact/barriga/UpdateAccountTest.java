package br.ce.wcaquino.consumer.tasks.pact.barriga;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.ConsumerPactRunnerKt;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.model.MockProviderConfig;
import au.com.dius.pact.core.model.RequestResponsePact;
import br.ce.wcaquino.consumer.barriga.service.BarrigaConsumer;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class UpdateAccountTest {
    private final String TOKEN = "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NTY5NzF9.7x72Q8YcNk7arsO8kpNv7oVtuC2_mmy02ItpnqwhSxg";
    private final String ACCOUNT_NAME = "Updated account";
    @Test
    public void test() {
        /*
         * Toda a parte da resposta foi omitida nesse teste. O sucesso do teste foi baseado exclusivamente no não retorno de um erro.
         */
        PactDslJsonBody requestBody = new PactDslJsonBody()
                .stringValue("nome", ACCOUNT_NAME);

        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("BasicConsumer")
                .hasPactWith("Barriga")
                .given("There is the account #1000")
                .uponReceiving("Update the existing account")
                    .path("/contas/1000")
                    .method("PUT")
                    .matchHeader("Authorization", "JWT .*", TOKEN)
                    .body(requestBody)
                .willRespondWith()
                    .status(200)
                .toPact();

        MockProviderConfig config = MockProviderConfig.createDefault();
        PactVerificationResult result = ConsumerPactRunnerKt.runConsumerTest(pact, config, (mockServer, context) -> {
            BarrigaConsumer consumer = new BarrigaConsumer(mockServer.getUrl());
            consumer.updateAccount("1000",ACCOUNT_NAME, TOKEN);
            return null;
        });

        if (result instanceof  PactVerificationResult.Error) {
            throw new RuntimeException(((PactVerificationResult.Error) result).getError());
        }
        assertThat(result, is(instanceOf(PactVerificationResult.Ok.class)));

    }
}
