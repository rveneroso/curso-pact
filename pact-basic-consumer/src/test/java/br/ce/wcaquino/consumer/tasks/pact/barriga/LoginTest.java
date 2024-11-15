package br.ce.wcaquino.consumer.tasks.pact.barriga;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.consumer.barriga.service.BarrigaConsumer;
import org.junit.Rule;

import au.com.dius.pact.consumer.junit.PactProviderRule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class LoginTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Barriga", this);

    @Pact(consumer = "BasicConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        PactDslJsonBody requestBody = new PactDslJsonBody()
                .stringType("email", "rvenerosostudy@gmail.com")
                .stringType("senha", "15112024");

        PactDslJsonBody responseBody = new PactDslJsonBody()
                .stringType("token");

        return builder
                .given("Your user is created")
                .uponReceiving("Signin with a valid user")
                    .path("/signin")
                    .method("POST")
                    .body(requestBody)
                .willRespondWith()
                    .status(200)
                    .body(responseBody)
            .toPact();
    }

    @Test
    @PactVerification
    public void shouldSignin() throws IOException {
        BarrigaConsumer consumer = new BarrigaConsumer(mockProvider.getUrl());
        String token = consumer.login("rvenerosostudy@gmail.com","15112024");
        assertThat(token, is(notNullValue()));
    }
}