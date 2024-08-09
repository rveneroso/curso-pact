package br.ce.wcaquino.consumer.tasks.pact;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.consumer.tasks.model.Task;
import br.ce.wcaquino.consumer.tasks.service.TasksConsumer;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TasksConsumerContractTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "BasicConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        DslPart body = new PactDslJsonBody()
                /*
                 * A sintaxe abaixo diz que o id a ser incluído no corpo da mensagem deve ser um número qualquer. Porém, o segundo parâmetro,
                 * 1L, diz que, nos casos em que for necessário devolver o body à quem estiver fazendo a chamada, o valor presente no body
                 * deve ser 1L.
                 */
                .numberType("id", 1L)
                /*
                 * As linhas abaixos farão com que o arquivo JSON gerado a partir da execução desse teste, indique que qualquer string
                 * informada no campo task seja considerada válida (ver a mudança feita no Assert: deixou de testar um valor específico
                 * e passou a testar se é não-nulo).
                 */
                .stringType("task")
                .stringType("dueDate");
        return builder
                .given("There is a task with id = 1")
                .uponReceiving("Retrieve Task #1")
                    .path("/todo/1")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                .body(body)
                //.body("{\"id\": 1,\"task\": \"Task from pact\",\"dueDate\": \"2020-01-01\"}")

                .toPact();
    }

    @Test
    @PactVerification
    public void test() throws IOException {
        // Arrange
        TasksConsumer consumer = new TasksConsumer(mockProvider.getUrl());
        System.out.println(mockProvider.getUrl());

        // Act
        Task task = consumer.getTask(1L);
        System.out.println(task);


        // Assert
        Assert.assertThat(task.getId(), is(1L));
        // A assertiva abaixo foi alterada na aula que implementa os testes do provedor
        //Assert.assertThat(task.getTask(), is("Task from pact"));
        Assert.assertThat(task.getTask(), is(notNullValue()));
    }
}


