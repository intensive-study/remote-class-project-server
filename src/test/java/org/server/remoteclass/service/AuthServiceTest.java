package org.server.remoteclass.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.server.remoteclass.dto.LoginDto;
import org.server.remoteclass.dto.RequestUserDto;
import org.server.remoteclass.dto.TokenRequestDto;
import org.server.remoteclass.dto.UserDto;
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
public class AuthServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    AuthService authService;

    @Test
    public void testA(){
        Assertions.assertThat(5).isEqualTo(5);
    }

    @Test
    @DisplayName("회원가입 : 정상적인 Post요청시, 서버에서 상태코드 201을 받는다.")
    public void 회원가입() throws Exception{
        UserDto userDto = new UserDto();
        userDto.setEmail("park12345@naver.com");
        userDto.setName("박현우1234");
        userDto.setPassword("helloworld");
        String json = mapper.writeValueAsString(userDto);
//        String json = mapper.writeValueAsString(new SubmittedUserSolutionDto(1L, 1L, "2", false));
        String URL = "/signup";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(json);
        mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 : 정상적인 Post요청시, 서버에서 상태코드 200을 받는다.")
    public void 로그인() throws Exception{
        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setEmail("gusdn3477@naver.com");
        requestUserDto.setName("박현우");
        requestUserDto.setPassword("12345678");
        authService.signup(requestUserDto);
        String json = mapper.writeValueAsString(new LoginDto("gusdn3477@naver.com", "12345678"));
        System.out.println(json);
        String URL = "/login";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(json);
        MvcResult requestResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(requestBuilder.toString());
        String token = JsonPath.read(requestResult.getResponse().getContentAsString(), "$.accessToken");
        String token2 = JsonPath.read(requestResult.getResponse().getContentAsString(), "$.refreshToken");
        System.out.println("access token : " + token);
        System.out.println("refresh token" + token2);
    }

    @Test
    @DisplayName("토큰 재발급 : 정상적인 Post요청시, 서버에서 상태코드 200을 받는다.")
    public void 토큰_재발급() throws Exception{
        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setEmail("gusdn3477@naver.com");
        requestUserDto.setName("박현우");
        requestUserDto.setPassword("12345678");
        authService.signup(requestUserDto);
        String json = mapper.writeValueAsString(new LoginDto("gusdn3477@naver.com", "12345678"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(json);

        MvcResult requestResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("실행됨");
        String accessToken = JsonPath.read(requestResult.getResponse().getContentAsString(), "$.accessToken");
        String refreshToken = JsonPath.read(requestResult.getResponse().getContentAsString(), "$.refreshToken");
        TokenRequestDto tokenRequestDto = new TokenRequestDto();
        tokenRequestDto.setAccessToken(accessToken);
        tokenRequestDto.setRefreshToken(refreshToken);
        String json2 = mapper.writeValueAsString(tokenRequestDto);
        System.out.println(accessToken);
        System.out.println(refreshToken);
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.post("/reissue")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // "Bearer "를 붙여 줘야 함
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2);

        MvcResult requestResult2 = mockMvc.perform(requestBuilder2)
                .andExpect(status().isOk())
                .andReturn();

        String token = JsonPath.read(requestResult2.getResponse().getContentAsString(), "$.accessToken");
        String token2 = JsonPath.read(requestResult2.getResponse().getContentAsString(), "$.refreshToken");
        System.out.println("access token : " + token);
        System.out.println("refresh token" + token2);
    }

}
