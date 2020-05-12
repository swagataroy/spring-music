package org.cloudfoundry.samples.music.web;

import io.pivotal.cfenv.core.CfEnv;
import io.pivotal.cfenv.core.CfService;
import org.cloudfoundry.samples.music.domain.ApplicationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

//import org.springframework.http.*;

@RestController
public class InfoController {
    private final CfEnv cfEnv;

    private Environment springEnvironment;

    @Autowired
    public InfoController(Environment springEnvironment) {
        this.springEnvironment = springEnvironment;
        this.cfEnv = new CfEnv();
    }

    @RequestMapping(value = "/appinfo")
    public ApplicationInfo info() {
        return new ApplicationInfo(springEnvironment.getActiveProfiles(), getServiceNames());
    }

    @RequestMapping(value = "/service")
    public List<CfService> showServiceInfo() {
        return cfEnv.findAllServices();
    }


    private String[] getServiceNames() {
        List<CfService> services = cfEnv.findAllServices();

        List<String> names = new ArrayList<>();
        for (CfService service : services) {
            names.add(service.getName());
        }
        return names.toArray(new String[0]);
    }

    @RequestMapping(value = "/swagata", method = RequestMethod.GET)
    public void redirectToTwitter(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("https://twitter.com");
    }

    @RequestMapping (value = "/swagatatest", method = RequestMethod.GET)
    public void redirectToExternalUrl(HttpServletResponse httpServletResponse) throws  IOException, URISyntaxException {
        URI uri = new URI("https://www.google.com");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        HttpStatus statusCode = response.getStatusCode();
        System.out.println("statusCode  is "+statusCode);
        PrintWriter out = httpServletResponse.getWriter();
        out.println("Testing again with https");
        out.println("statusCode  is "+statusCode);
    }


    @RequestMapping (value = "/swagatatesttrust", method = RequestMethod.GET)
    public void testTrusts(HttpServletResponse httpServletResponse) throws  IOException, URISyntaxException {
        //URI uri = new URI("https://www.google.com");
        //URI uri = new URI("https://httpbin.org/user-agent");
        URI uri = new URI("https://self-signed.badssl.com");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        HttpStatus statusCode = response.getStatusCode();
        System.out.println("statusCode  is "+statusCode);
        PrintWriter out = httpServletResponse.getWriter();
        out.println("Testing again with https");
        out.println("StatusCode  is "+statusCode);
        out.println("Response Body is "+response.getBody());
    }

}