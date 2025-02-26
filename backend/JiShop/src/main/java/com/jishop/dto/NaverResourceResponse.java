package com.jishop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NaverResourceResponse {
    // 네이버 사용자 리소스 API의 응답을 매핑하는 DTO
    private String resultCode;
    private String message;
    private NaverResourceResponseBody responseBody;

    @JsonCreator
    public NaverResourceResponse(
            @JsonProperty("resultcode") String resultCode,
            @JsonProperty("message") String message,
            @JsonProperty("response") NaverResourceResponseBody responseBody
    ){
        this.resultCode = resultCode;
        this.message = message;
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "NaverResourceResponse{" +
                "resultCode='" + resultCode + '\'' +
                ", message='" + message + '\'' +
                ", responseBody=" + responseBody +
                '}';
    }

    @Getter
    public static class NaverResourceResponseBody {
        // 실제 사용자의 이메일, 프로필 메세지 등을 얻음 => 필드 추가로 더 다양한거 얻어올수 있을듯?
        private String email;
        private String profileImage;

        @JsonCreator
        public NaverResourceResponseBody(
                @JsonProperty("email") String email,
                @JsonProperty("profile_image") String profileImage
        ){
            this.email = email;
            this.profileImage = profileImage;
        }

        @Override
        public String toString() {
            return "NaverResourceResponseBody{" +
                    "email='" + email + '\'' +
                    ", profileImage='" + profileImage + '\'' +
                    '}';
        }
    }
}
