package se.travappar.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import se.travappar.api.dal.HelloRepository;
import se.travappar.api.model.HelloWorld;

@RestController
public class HelloWorldController {

    @Autowired
    HelloRepository helloRepository;

    @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
    public @ResponseBody
    HelloWorld helloWorld() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setField("Hello World!");
        return helloWorld;
    }
}
