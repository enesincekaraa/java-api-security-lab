package com.enesincekara.apisecurity.lab.order.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecureOrderControllerTest {
    private static final String ALICE_USER_ID =
            "11111111-1111-1111-1111-111111111111";

    private static final String ALICE_ORDER_ID =
            "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa";

    private static final String BOB_ORDER_ID =
            "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb";



    private final MockMvc mockMvc;

    @Autowired
    SecureOrderControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void shouldAllowAliceToReadHerOwnOrder()  throws Exception {
        mockMvc.perform(
                get(
                        "/api/v1/lab/secure/orders/{orderId}",
                        ALICE_ORDER_ID
                ).header(
                        "X-Lab-User-Id",
                        ALICE_USER_ID
                )

        )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(ALICE_ORDER_ID)
                )
                .andExpect(
                        jsonPath("$.ownerId")
                                .value(ALICE_USER_ID)
                )
                .andExpect(
                        jsonPath("$.productName")
                                .value("Mekanik Klavye")
                );

    }



    @Test
    void shouldHideBobsOrderFromAlice() throws Exception {
        mockMvc.perform(
                        get(
                                "/api/v1/lab/secure/orders/{orderId}",
                                BOB_ORDER_ID
                        )
                                .header(
                                        "X-Lab-User-Id",
                                        ALICE_USER_ID
                                )
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }





}