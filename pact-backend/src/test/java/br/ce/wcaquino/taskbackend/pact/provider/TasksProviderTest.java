package br.ce.wcaquino.taskbackend.pact.provider;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactUrl;
import au.com.dius.pact.provider.junitsupport.target.Target;
import au.com.dius.pact.provider.junitsupport.target.TestTarget;
import org.junit.runner.RunWith;

@RunWith(PactRunner.class)
@Provider("Tasks")
@PactUrl(urls = {"file:////home/rveneroso/wso2/curso-pact/pact-backend/src/test/resources"})
public class TasksProviderTest {

//    @TestTarget
//    public final Target target = new HttpTarget("localhost",8000);
//
//    @State("There is a task with id = 1")
//    public void saveTask1() {
//        // SQL script to insert the task
//    }
}
