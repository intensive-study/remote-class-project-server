package org.server.remoteclass.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.server.remoteclass.dto.auth.LoginDto;
import org.server.remoteclass.dto.auth.RequestTokenDto;
import org.server.remoteclass.dto.fixDiscountCoupon.RequestFixDiscountCouponDto;
import org.server.remoteclass.dto.user.RequestUserDto;
import org.server.remoteclass.service.auth.AuthService;
import org.server.remoteclass.service.coupon.CouponService;
import org.server.remoteclass.service.fixDiscountCoupon.FixDiscountCouponService;
import org.server.remoteclass.service.rateDiscountCoupon.RateDiscountCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    AuthService authService;

    @Autowired
    CouponService couponService;

    @Autowired
    FixDiscountCouponService fixDiscountCouponService;

    @Autowired
    RateDiscountCouponService rateDiscountCouponService;

    @Test
    public void testA(){
        Assertions.assertThat(5).isEqualTo(5);
    }

    @Test
    @DisplayName("전체 쿠폰 조회 : 정상적인 Post요청시, 서버에서 상태코드 200을 받는다.")
    public void 전체쿠폰조회() throws Exception{
        // 일단 유저가 만드는 걸로(나중에 관리자로 해야 함)
        authService.signup(new RequestUserDto("gusdn3477@naver.com", "박현우", "12345678"));
        String json = mapper.writeValueAsString(new LoginDto("gusdn3477@naver.com", "12345678"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(json);

        MvcResult requestResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = JsonPath.read(requestResult.getResponse().getContentAsString(), "$.accessToken");
        RequestTokenDto requestTokenDto = new RequestTokenDto();
        requestTokenDto.setAccessToken(accessToken);
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/coupons")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // "Bearer "를 붙여 줘야 함
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder2)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("쿠폰생성 : 정상적인 Post요청시, 서버에서 상태코드 200을 받는다.")
    public void 쿠폰생성() throws Exception{
        // 일단 유저가 만드는 걸로(나중에 관리자로 해야 함)
        authService.signup(new RequestUserDto("gusdn3477@naver.com", "박현우", "12345678"));
        String json = mapper.writeValueAsString(new LoginDto("gusdn3477@naver.com", "12345678"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(json);

        MvcResult requestResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = JsonPath.read(requestResult.getResponse().getContentAsString(), "$.accessToken");
        RequestTokenDto requestTokenDto = new RequestTokenDto();
        requestTokenDto.setAccessToken(accessToken);
        String json2 = mapper.writeValueAsString(new RequestFixDiscountCouponDto("정률할인 연습1", 5 , 1000) {
        });
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.post("/coupons")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // "Bearer "를 붙여 줘야 함
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2);

        mockMvc.perform(requestBuilder2)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("쿠폰비활성화 : 쿠폰 valid 상태를 false로 한다.")
    public void 쿠폰비활성화() throws Exception{
        // 일단 유저가 만드는 걸로(나중에 관리자로 해야 함)
        authService.signup(new RequestUserDto("gusdn3477@naver.com", "박현우", "12345678"));
        String json = mapper.writeValueAsString(new LoginDto("gusdn3477@naver.com", "12345678"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(json);

        MvcResult requestResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = JsonPath.read(requestResult.getResponse().getContentAsString(), "$.accessToken");
        RequestTokenDto requestTokenDto = new RequestTokenDto();
        requestTokenDto.setAccessToken(accessToken);
        String json2 = mapper.writeValueAsString(new RequestFixDiscountCouponDto("정률할인 연습1", 5 , 1000));
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.post("/coupons")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // "Bearer "를 붙여 줘야 함
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2);

        mockMvc.perform(requestBuilder2)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        RequestBuilder requestBuilder3 = MockMvcRequestBuilders.put("/coupons/deactivate/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // "Bearer "를 붙여 줘야 함
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder3)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("쿠폰 삭제")
    public void 쿠폰삭제() throws Exception{
        // 일단 유저가 만드는 걸로(나중에 관리자로 해야 함)
        authService.signup(new RequestUserDto("gusdn3477@naver.com", "박현우", "12345678"));
        String json = mapper.writeValueAsString(new LoginDto("gusdn3477@naver.com", "12345678"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(json);

        MvcResult requestResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = JsonPath.read(requestResult.getResponse().getContentAsString(), "$.accessToken");
        RequestTokenDto requestTokenDto = new RequestTokenDto();
        requestTokenDto.setAccessToken(accessToken);
        String json2 = mapper.writeValueAsString(new RequestFixDiscountCouponDto("정률할인 연습1", 5 , 1000));
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.post("/coupons")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // "Bearer "를 붙여 줘야 함
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2);

        mockMvc.perform(requestBuilder2)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        RequestBuilder requestBuilder3 = MockMvcRequestBuilders.delete("/coupons/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // "Bearer "를 붙여 줘야 함
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder3)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
