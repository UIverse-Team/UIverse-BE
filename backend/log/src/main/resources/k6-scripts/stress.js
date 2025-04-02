import http from "k6/http";
import { check, sleep } from "k6";
import { randomItem } from "https://jslib.k6.io/k6-utils/1.4.0/index.js";

// 테스트 옵션 설정 (동시 사용자 50명, 30초 동안 테스트)
export let options = {
  vus: 30, // 동시 사용자 수 (Virtual Users)
  duration: "30s", // 테스트 지속 시간
};

// Enum 값 설정 (UserActionType)
const userActions = [
  "PAGE_VIEW",
  "BUTTON_CLICK",
  "LINK_CLICK",
  "SCROLL",
  "HOVER",
  "FORM_SUBMIT",
  "VIDEO_PLAY",
  "VIDEO_PAUSE",
  "VIDEO_COMPLETE",
  "TEXT_INPUT",
];

export default function () {
  let url = "http://host.docker.internal/logs"; // Spring Boot API 엔드포인트

  let payload = JSON.stringify({
    userId: Math.floor(Math.random() * 1000) + 1, // 1~1000 랜덤 유저 ID
    message: "User performed an action",
    userActionType: randomItem(userActions), // Enum 값 중 랜덤 선택
  });

  let params = {
    headers: {
      "Content-Type": "application/json",
    },
  };

  let res = http.post(url, payload, params);

  // 응답 검사 (200이면 성공)
  check(res, {
    "is status 200": (r) => r.status === 200,
    "response time < 500ms": (r) => r.timings.duration < 500,
  });

  sleep(1); // 요청 간격을 1초로 설정
}
