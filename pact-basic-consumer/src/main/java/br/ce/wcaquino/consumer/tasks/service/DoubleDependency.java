package br.ce.wcaquino.consumer.tasks.service;

import br.ce.wcaquino.consumer.barriga.service.BarrigaConsumer;
import br.ce.wcaquino.consumer.tasks.model.Task;

import java.io.IOException;

public class DoubleDependency {

    private String barrigaURL;
    private String tasksURL;

    public DoubleDependency(String barrigaURL, String tasksURL) {
        this.barrigaURL = barrigaURL;
        this.tasksURL = tasksURL;
    }

    public String getToken(String user, String password) throws IOException {
        BarrigaConsumer bConsumer = new BarrigaConsumer(barrigaURL);
        TasksConsumer tConsumer = new TasksConsumer(tasksURL);

        String token = bConsumer.login(user, password);
        Task saveTask = tConsumer.saveTask("Expire token: " + token, "2050-10-10");
        return saveTask.getTask();
    }

    public static void main(String[] args) throws IOException {
        DoubleDependency dd = new DoubleDependency("https://barrigarest.wcaquino.me", "http://localhost:8000");
        String task = dd.getToken("rvenerosostudy@gmail.com", "15112024");
    }
}
