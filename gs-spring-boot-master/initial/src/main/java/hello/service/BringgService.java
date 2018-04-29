package hello.service;

import hello.model.Order;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BringgService {
    private final String customersUrlPath;
    private final String tasksUrlPath;
    private BringgRestClient bringgRestClient;

    public BringgService(BringgRestClient bringgRestClient, String urlPath) {
        this.bringgRestClient = bringgRestClient;
        customersUrlPath = String.format("%s/customers", urlPath);
        tasksUrlPath = String.format("%s/tasks", urlPath);
    }

    public JSONObject placeOrder
            (Order order) {
        Integer customerId = createCustomer(order.getName(), order.getCellNumber(), order.getAddress());
        if (customerId != null) {
            return createOrder(order.getName(), order.getCellNumber(), order.getAddress(), order.getOrderDetails(), customerId );
        }
        return null;
    }

    private Integer createCustomer(String name, String cellNumber, String address) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("address", address);
        parameters.put("phone", cellNumber);
        try {
            JSONObject customer = bringgRestClient.sendPostRequest(parameters, customersUrlPath);
            Integer customerId = customer.getJSONObject("customer").getInt("id");
            return customerId;
        } catch (IOException e) {
            System.out.print("Error creating customer");
        }
        return null;
    }

    private JSONObject createOrder(String name, String cellNumber, String address, String orderDetails, Integer customerId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("address", address);
        parameters.put("phone", cellNumber);
        parameters.put("customer_id", customerId.toString());
        try {
            JSONObject order = bringgRestClient.sendPostRequest(parameters, tasksUrlPath);
            return order;
        } catch (IOException e) {
            System.out.print("Error creating customer");
        }
        return null;
    }

    public List<JSONObject> getOrders(String phoneNum) {
        List<JSONObject> results = new ArrayList<>();
        try {
            JSONObject customer = bringgRestClient.sendGetRequest(String.format("https://developer-api.bringg.com/customers/%s", phoneNum));
            Integer customerId = customer.getJSONObject("customer").getInt("id");
            JSONObject customersTaskCount = bringgRestClient.sendGetRequest(String.format("https://admin-api.bringg.com/customers/%s/tasks/count", String.valueOf(customerId)));
            int numOfTasks = customersTaskCount.getInt("count");

            if (numOfTasks != 0){
            for (int pageNum = 0; pageNum <= numOfTasks/50;pageNum++){
                JSONObject tasks = bringgRestClient.sendGetRequest(String.format("https://admin-api.bringg.com/customers/%s/tasks?page=%s",
                        String.valueOf(customerId), String.valueOf(pageNum)));
                results.add(tasks);
            }}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
}
