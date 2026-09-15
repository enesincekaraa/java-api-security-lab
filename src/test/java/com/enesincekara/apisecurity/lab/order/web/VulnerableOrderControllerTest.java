package com.enesincekara.apisecurity.lab.order.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class VulnerableOrderControllerTest {

    private static final String ALICE_USER_ID =
            "11111111-1111-1111-1111-111111111111";

    private static final String BOB_USER_ID =
            "22222222-2222-2222-2222-222222222222";

    private static final String BOB_ORDER_ID =
            "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb";


    private final MockMvc mockMvc;


    @Autowired
    VulnerableOrderControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }


    @Test
    void shouldDemonstrateBolaVulnerability() throws Exception {
        mockMvc.perform(
                get(
                        "/api/v1/lab/vulnerable/orders/{orderId}",
                        BOB_ORDER_ID
                ).header(
                        "X-Lab-User-Id",
                        ALICE_USER_ID
                )
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOB_ORDER_ID))
                .andExpect(
                jsonPath("$.ownerId")
                        .value(BOB_USER_ID)
        ).andExpect(
                        jsonPath("$.productName")
                                .value("Oyuncu Monitörü")
                );



    }

}