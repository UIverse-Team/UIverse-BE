#  💫 UIverse BE Convention
## 🕊️ 1. 프로젝트 구조
```text

```
## 🕊️ 2. 코드 스타일 가이드
#### (1) 어노테이션이 붙은 파라미터는 두 줄로 작성
```java
    @GetMapping
    public PagedModel<PostResponse> getAllPosts(@RequestParam int page,
                                                @RequestParam int size) {
         ...     
    }
```
#### (3) 어노테이션 순서
- 애노테이션은 피라미드 형식으로 정리합니다.
- 글자수가 적은 애노테이션이 위에 위치하며, 아래로 갈수록 길이가 길어집니다.
```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/uiverse")
```

**(3) 코드 라인 길이 :** 코드 한 줄의 길이는 최대 120자를 초과하지 않습니다.

**(4) Setter 사용 지양 :** `Setter` 메서드 사용을 지양하고, 생성자나 빌더 패턴을 활용합니다.

**(5) Record 사용 권장 :** DTO 역할을 하는 클래스에서는 `record`를 필수적으로 사용합니다.

**(6) 생성자와 빌더 패턴 :** `@Builder`는 클래스가 아닌 생성자에 사용하며, 생성자는 `private`으로 선언합니다.

**(7) DTO 네이밍 규칙 :** DTO 클래스는 `Request`, `Response` 접두사를 사용합니다.

**(8) 테이블 네이밍 규칙 :** 테이블명은 복수형을 사용합니다.

#### (9) 클래스, 인터페이스, Enum 선언 후 개행
- 클래스, 인터페이스, `Enum`을 선언한 후에는 한 줄 개행을 추가합니다.
- ⚠️ 단, 메서드 바디`{ }` 내에서 불필요한 개행을 하지 않습니다.

**(11) 메서드 체이닝 시 개행 규칙 :** 메서드 체이닝 시 `.`을 기준으로 개행합니다.
```java
return response().status("OK")
    .data(data)
    .build();
```

**(12) `return`문 개행 규칙 :** `return` 문은 위에 한 줄 개행 후 작성합니다.
```java
...

return result;
```

---
## 🕊️ 3. 프로젝트 운영
### (1) Milestone 관리
- 프로젝트 주요 목표를 `Milestone`으로 정의합니다.
- 해당 `Milestone`에서 처리해야하는 작업을 `Issue`로 발행합니다.

### (2) Issue 관리
- GitHub `Issue`를 통해 작업과 문제를 관리합니다.
- `Issue`와 `Label`을 사용하여 작업 유형과 상태를 구분합니다.

### (3) Brach 생성 규칙
- 작업은 이슈 번호를 기반으로 브랜치를 생성하여 진행합니다.
- 브랜치는 `be/feat/issueNumber` 네이밍 규칙을 따릅니다.

### (4) Branch 전략
- Branch는 `main` - `develop` - `feature` 구조를 따릅니다.
- `main` : 운영 환경에서 사용되는 안정적인 코드만 포함합니다.
- `develop` : 새로운 기능이 병합되기 전 테스트 및 통합이 이루어지는 브랜치입니다.
- `feature` : 각 기능 개발을 위한 브랜치로, 작업이 완료되면 `develop` 브랜치로 병합됩니다.

```text
*   main
|   
|   *   develop
|   |   
|   |   *   be/feat/1
|   |   *   be/feat/2
|   |   *   be/feat/3
|   |  /
|   | /
|   *
|  /
| /
*   
```
---
## 🕊️ 4. ERD







