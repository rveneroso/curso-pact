package br.ce.wcaquino.consumer.tasks.service;
import br.ce.wcaquino.consumer.tasks.model.Task;
import br.ce.wcaquino.consumer.utils.RequestHelper;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class TasksConsumerTest {

    private static final String INVALID_URL = "http://invalidURL.com";
    @InjectMocks
    private TasksConsumer consumer = new TasksConsumer(INVALID_URL);
    @Mock
    private RequestHelper helper;

    @Test
    public void shouldGetAnExistingTask() throws IOException {
        // Arrange
        Map<String, String> expectedTask = new HashMap<String, String>();
        expectedTask.put("id", "1");
        expectedTask.put("task", "Mocked task");
        expectedTask.put("dueDate", "2001-01-01");

        Mockito
                .when(helper.get(INVALID_URL + "/todo/1"))
                .thenReturn(expectedTask);
        // Act
        Task task = consumer.getTask(1L);

        // Assert
        Assert.assertNotNull(task);
        Assert.assertThat(task.getId(), CoreMatchers.is(1L));
    }
}
