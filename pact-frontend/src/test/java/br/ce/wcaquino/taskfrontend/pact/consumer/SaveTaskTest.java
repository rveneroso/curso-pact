package br.ce.wcaquino.taskfrontend.pact.consumer;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import br.ce.wcaquino.tasksfrontend.model.Todo;
import br.ce.wcaquino.tasksfrontend.repositories.TasksRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class SaveTaskTest {
    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "TasksFront")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        DslPart requestBody = new PactDslJsonBody()
                .nullValue("id")
                .stringType("task", "New task")
                .array("dueDate")
                    .numberType(LocalDate.now().getYear())
                    .numberType(LocalDate.now().getMonthValue())
                    .numberType(LocalDate.now().getDayOfMonth())
                .closeArray();

        DslPart responseBody = new PactDslJsonBody()
                .numberType("id")
                .stringType("task", "New task")
                .date("dueDate","yyy-MM-dd", new Date());
        return builder
                .uponReceiving("Save a task")
                    .path("/todo")
                    .method("POST")
                    .matchHeader("Content-type","application/json.*", "application/json")
                    .body(requestBody)
                .willRespondWith()
                    .status(201)
                    .body(responseBody)
                .toPact();
    }

    @Test
    @PactVerification
    public void test() throws IOException {
        // Arrange
        TasksRepository consumer = new TasksRepository(mockProvider.getUrl());
        System.out.println(mockProvider.getUrl());

        // Act
        Todo task = consumer.save(new Todo(null, "New Task", LocalDate.now()));
        System.out.println(task);


        // Assert
        Assert.assertThat(task.getId(), is(notNullValue()));
        // A assertiva abaixo foi alterada na aula que implementa os testes do provedor
        //Assert.assertThat(task.getTask(), is("Task from pact"));
        Assert.assertThat(task.getTask(), is("New task"));
        Assert.assertThat(task.getDueDate(), is(LocalDate.now()));
    }
}
