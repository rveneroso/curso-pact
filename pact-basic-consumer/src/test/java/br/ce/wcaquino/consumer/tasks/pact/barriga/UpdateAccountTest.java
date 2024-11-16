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
                /*
                 * Aqui são testados 2 estados. Para que os testes funcionem, a ordem ter que ser a que está definida. Isso porque
                 * entre os 2 estados ocorre um reset. Se o estado da conta for testado primeiro, após o reset a conta obtida para
                 * update terá sido perdida. Com o token sendo executado primeiro não há problemas já que o token não expira. Assim,
                 * após o reset, o token continuará válido, permitindo que os testes funcionem.
                 */
                .given("I have a valid token")
                .given("I have an accountId")
                .uponReceiving("Update the existing account")
                    //.path("/contas/1000")
                    .pathFromProviderState("/contas/${accountId}", "/contas/1000")
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
