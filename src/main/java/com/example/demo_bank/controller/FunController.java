package com.example.demo_bank.controller;

import com.example.demo_bank.dto.ChatGptRequest;
import com.example.demo_bank.dto.ChatGptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bot")
public class FunController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate template;

    @GetMapping("/")
    public String sayHello(){
        return "Hello World!";
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt){
        ChatGptRequest chatGptRequest = new ChatGptRequest(model,prompt);
        ChatGptResponse chatGptResponse = template.postForObject(apiUrl,chatGptRequest,ChatGptResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

}
