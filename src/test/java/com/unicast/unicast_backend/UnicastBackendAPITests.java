package com.unicast.unicast_backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import com.unicast.unicast_backend.persistance.repository.UserRepository;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UnicastBackendAPITests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepo;

    @Test
    public void testUserRegisterAndLogin() throws Exception {
        // TODO: arreglar, de momento no funciona

        // final String username = "testusername123543";
        // final String password = "1234";

        // HttpEntity entity = MultipartEntityBuilder.create().addTextBody("user",
        //         "{\"username\": \"" + username + "\",\"password\": \"" + password + "\"}", ContentType.APPLICATION_JSON)
        //         .build();

        // mvc.perform(post("/api/public/register").header(entity.getContentType().getName(), entity.getContentType().getValue())
        //         .content(EntityUtils.toString(entity)))
        //         .andExpect(status().is2xxSuccessful());

        // mvc.perform(get("/api/authenticate").param("username", username).param("password", password))
        //         .andExpect(status().is2xxSuccessful());

        // // TODO: gestionar si ha saltado una excepcion antes, no llega aqui y el usuario se queda colgando
        // userRepo.delete(userRepo.findByUsername(username));
    }
}
