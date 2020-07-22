package com.manu.test.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author emmanuel.mura
 *
 */
@Controller
@RequestMapping("/test")
public class TestWebController {

    public static final Logger logger = LoggerFactory.getLogger(TestWebController.class);

    @GetMapping(value = {"/home"})
    public String index(Model model) {
        logger.debug("Welcome !");
//        model.addAttribute("myObject", new MyObject("helloWorld"));
        return "index.html";
    }

    @GetMapping("/player")
    public String player() {
        logger.debug("player");
        return "index.html";
    }
    
    @GetMapping("/wowza-cloud")
    public String wowza() {
        logger.debug("wowza");
        return "wowza.html";
    }
    
    @GetMapping("/videojs-stream")
    public String stream() {
        logger.debug("stream");
        return "stream.html";
    }
    
    @GetMapping("/send-request")
    public String sendRequest() throws ClientProtocolException, IOException {
        logger.debug("sendRequest");

        HttpClient httpClient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet("http://localhost:8090/city");

            // HttpPost httpPost = new HttpPost("http://www.a-domain.com/foo/");
            //
            // // Request parameters and other properties.
            // List<NameValuePair> params = new ArrayList<>();
            // params.add(new BasicNameValuePair("param-1", "12345"));
            // params.add(new BasicNameValuePair("param-2", "Hello!"));
            //
            // httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            // Execute and get the response.
            // HttpResponse response = httpClient.execute(httpPost);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                // Print out the response message
                logger.debug(EntityUtils.toString(response.getEntity()));

//                InputStream instream = entity.getContent();
//                try {
//                    // do something useful
//                } finally {
//                    instream.close();
//                }
            }

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }

        return "stream.html";
    }
}
