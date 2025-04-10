import http from 'k6/http';
import { check, sleep } from 'k6';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.1/index.js';
import { Counter } from 'k6/metrics';

// 각 응답 유형별 카운터 (모든 VU의 결과가 집계됨)
export let immediateLogins = new Counter('immediate_logins');
export let queueLogins = new Counter('queue_logins');
export let failures = new Counter('failures');

export let options = {
    stages: [
        { duration: '30s', target: 10 },   // 초기 낮은 부하
        { duration: '1m', target: 1000 },     // 부하 증가 (대기열 사용 기대)
        { duration: '30s', target: 10 },     // 부하 감소 (쿨다운)
    ],
};

export default function () {
    let userNumber = Math.floor(Math.random() * 100) + 1;
    let loginId = `qeqe${userNumber}@qeqe.com`;

    let payload = JSON.stringify({
        loginId: loginId,
        password: "aaaa1111₩"
    });

    let params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // 로그인 API 요청 전송
    let res = http.post('http://localhost:8080/auth/signin', payload, params);

    // 응답 상태에 따라 카운터 업데이트 및 로그 출력
    if (res.status === 200) {
        immediateLogins.add(1);
        console.log(`[VU: ${__VU}, ITER: ${__ITER}] ✅ [${res.status}] 즉시 로그인 성공  | user: ${loginId}`);
    } else if (res.status === 202) {
        queueLogins.add(1);
        console.log(`[VU: ${__VU}, ITER: ${__ITER}] 🚧 [${res.status}] 대기열 진입       | user: ${loginId}`);
    } else {
        failures.add(1);
        console.log(`[VU: ${__VU}, ITER: ${__ITER}] ❌ [${res.status}] 알 수 없는 응답     | user: ${loginId}`);
    }

    // 응답이 200 또는 202인지 검사
    check(res, {
        'status is 200 or 202': (r) => r.status === 200 || r.status === 202,
    });

    sleep(1);
}

// 테스트 종료 후 커스텀 요약 리포트를 생성
export function handleSummary(data) {
    // bracket 표기법으로 내장 메트릭 값을 안전하게 읽음
    let totalRequests = data.metrics["http_reqs"] ? data.metrics["http_reqs"].count : 0;
    let immediateLoginCount = data.metrics["immediate_logins"] ? data.metrics["immediate_logins"].count : 0;
    let queueLoginCount = data.metrics["queue_logins"] ? data.metrics["queue_logins"].count : 0;
    let failureCount = data.metrics["failures"] ? data.metrics["failures"].count : 0;
    let p50 = data.metrics["http_req_duration"] ? data.metrics["http_req_duration"]["p(50)"] : 'undefined';
    let p95 = data.metrics["http_req_duration"] ? data.metrics["http_req_duration"]["p(95)"] : 'undefined';
    let p99 = data.metrics["http_req_duration"] ? data.metrics["http_req_duration"]["p(99)"] : 'undefined';

    let defaultSummary = textSummary(data, { indent: ' ', enableColors: true });

    let custom = `
===========================
     💻 TEST SUMMARY
===========================
총 요청 수:    ${totalRequests}
 - 즉시 로그인(200): ${immediateLoginCount}
 - 대기열 진입(202):  ${queueLoginCount}
 - 기타 실패:       ${failureCount}

** 응답 시간 분포 **
 - p(50):  ${p50} ms
 - p(95):  ${p95} ms
 - p(99):  ${p99} ms

기본 K6 요약:
${defaultSummary}
`;

    return {
        stdout: custom,
        // 필요하다면 파일로도 저장 가능: 'result.json': JSON.stringify(data),
    };
}