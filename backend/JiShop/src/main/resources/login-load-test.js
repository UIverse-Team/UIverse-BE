import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Rate } from 'k6/metrics';

// Custom metrics
const successfulLogins = new Counter('successful_logins');
const queuedLogins = new Counter('queued_logins');
const failedLogins = new Counter('failed_logins');
const loginResponseTime = new Rate('login_response_time');

// Test options
export const options = {
    scenarios: {
        login_load_test: {
            executor: 'ramping-vus',
            startVUs: 1,
            stages: [
                { duration: '30s', target: 50 },   // Ramp up to 50 users over 30s
                { duration: '1m', target: 100 },   // Ramp up to 100 users over 1m
                { duration: '3m', target: 100 },   // Stay at 100 users for 3m
                { duration: '30s', target: 0 },    // Ramp down to 0 users
            ],
            gracefulRampDown: '30s',
        },
        login_spike_test: {
            executor: 'ramping-arrival-rate',
            startRate: 5,
            timeUnit: '1s',
            preAllocatedVUs: 200,
            maxVUs: 500,
            stages: [
                { duration: '10s', target: 5 },    // Start with 5 requests per second
                { duration: '30s', target: 50 },   // Ramp up to 50 requests per second
                { duration: '1m', target: 50 },    // Stay at 50 requests per second
                { duration: '10s', target: 5 },    // Ramp down to 5 requests per second
            ],
        }
    },
    thresholds: {
        http_req_failed: ['rate<0.1'],         // http errors should be less than 10%
        http_req_duration: ['p(95)<5000'],     // 95% of requests should be below 5s
    },
};

// Test credentials
const users = Array(100)
    .fill()
    .map((_, i) => ({
        loginId: `qeqe${i}@qeqe.com`,
        password: `aaaa1111₩`
    }));

export default function() {
    // 각 VU별 cookie jar 생성 (init 컨텍스트에서는 생성하지 않음)
    const jar = http.cookieJar();

    // 로그인 시 나머지 쿠키 작업이 필요하다면 jar.set(url, cookieName, cookieValue) 등을 이용할 수 있습니다.

    const userData = users[Math.floor(Math.random() * users.length)];

    // Attempt to login
    const loginStart = new Date();
    const loginResponse = http.post(
        'http://localhost:8080/auth/signin',
        JSON.stringify({
            loginId: userData.loginId,
            password: userData.password
        }),
        {
            headers: { 'Content-Type': 'application/json' },
            tags: { name: 'LoginRequest' }
        }
    );
    const loginTime = new Date() - loginStart;
    loginResponseTime.add(loginTime / 1000.0);

    // Check login response (200: 바로 로그인, 202: 로그인 요청 대기열 등록)
    check(loginResponse, {
        'status is 200 or 202': (r) => r.status === 200 || r.status === 202,
    });

    if (loginResponse.status === 200) {
        // Immediate login success
        successfulLogins.add(1);
        sleep(1);

        // Test the checkLogin endpoint
        const checkResponse = http.get('http://localhost:8080/auth', {
            tags: { name: 'CheckLoginRequest' }
        });
        check(checkResponse, {
            'logged in successfully': (r) =>
                r.status === 200 && r.body.includes('로그인 중')
        });

        sleep(1);
        // Logout to complete the flow
        const logoutResponse = http.get('http://localhost:8080/auth/logout', {
            tags: { name: 'LogoutRequest' }
        });
        check(logoutResponse, {
            'logout successful': (r) => r.status === 200
        });
    } else if (loginResponse.status === 202) {
        // Login queued
        queuedLogins.add(1);
        let taskId;
        try {
            const responseBody = JSON.parse(loginResponse.body);
            taskId = responseBody.taskId;
            let queueComplete = false;
            let attempts = 0;
            const maxAttempts = 10;

            while (!queueComplete && attempts < maxAttempts) {
                sleep(2);
                attempts++;
                // 실제 큐 상태 확인 엔드포인트 구현 필요
                const queueStatusResponse = http.get(
                    `http://localhost:8080/queue/status?taskId=${taskId}`,
                    { tags: { name: 'QueueStatusCheck' } }
                );
                if (queueStatusResponse.status === 200) {
                    const status = JSON.parse(queueStatusResponse.body);
                    if (status.completed) {
                        queueComplete = true;
                        // Once queued login is complete, check login again
                        const checkResponse = http.get('http://localhost:8080/auth', {
                            tags: { name: 'CheckLoginAfterQueue' }
                        });
                        check(checkResponse, {
                            'queued login eventually successful': (r) =>
                                r.status === 200 && r.body.includes('로그인 중')
                        });
                    }
                }
            }
        } catch (e) {
            console.error('Error checking queue status:', e);
            failedLogins.add(1);
        }
    } else {
        // Login failed
        failedLogins.add(1);
    }

    sleep(2);
}

// Handle test summary and export to file
export function handleSummary(data) {
    // 간단한 JSON 파일 출력으로 대체
    return {
        './login-load-test-summary.json': JSON.stringify(data),
    };
}