package hello.controler;

import hello.service.BringgRestClient;
import hello.model.Order;
import hello.service.BringgService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class AlexCoffeeController {
    BringgService bringgService;

    public AlexCoffeeController() {
        BringgRestClient bringgRestClient = new BringgRestClient("ZtWsDxzfTTkGnnsjp8yC", "V_-es-3JD82YyiNdzot7");
        bringgService = new BringgService(bringgRestClient, "https://api.bringg.com/partner_api");
    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from Alex Coffee!";
    }

    @RequestMapping(value = "/store/order",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.POST)
    ResponseEntity<Order> placeOrder(@Valid @RequestBody Order body) {
        JSONObject order = bringgService.placeOrder(body);
        if (order.getBoolean("success")) {
            return new ResponseEntity<>(body, HttpStatus.OK);
        } else return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/store/order/{phoneNum}",
            produces = {"application/json", "application/xml"},
            method = RequestMethod.GET)
    ResponseEntity<Object> getOrders(@PathVariable("phoneNum") String phoneNum) {
        List<JSONObject> orders = bringgService.getOrders(phoneNum);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
