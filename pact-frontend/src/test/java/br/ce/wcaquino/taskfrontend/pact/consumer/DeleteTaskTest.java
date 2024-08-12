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
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;

public class DeleteTaskTest {

    @Rule
    public PactProviderRule mockProvider = new PactProviderRule("Tasks", this);

    @Pact(consumer = "TasksFront")
    public RequestResponsePact createPact(PactDslWithProvider builder) {

//        DslPart requestBody = new PactDslJsonBody()
//            .numberType("id", 1)
//                .stringType("task", "Task Updated")
//                .array("dueDate")
//                    .numberType(LocalDate.now().getYear())
//                    .numberType(LocalDate.now().getMonthValue())
//                    .numberType(LocalDate.now().getDayOfMonth())
//                .closeArray();
        return builder
                .given("There is a task with id = 1")
                .uponReceiving("Remove a task")
                    .path("/todo/1")
                    .method("DELETE")
                .willRespondWith()
                    .status(204)
                .toPact();
    }

    @Test
    @PactVerification
    public void shouldUpdateTask() {
        TasksRepository repo = new TasksRepository(mockProvider.getUrl());
        repo.delete(1L);
    }
}
