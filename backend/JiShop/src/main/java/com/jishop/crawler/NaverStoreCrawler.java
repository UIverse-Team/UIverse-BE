package com.jishop.crawler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.jishop.category.domain.Category;
import com.jishop.category.repository.CategoryRepository;

import com.jishop.crawler.dto.ProductCrawlerData;
import com.jishop.product.domain.DiscountStatus;
import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;
import com.jishop.product.domain.SaleStatus;
import com.jishop.product.repository.ProductRepository;
import com.jishop.store.domain.Store;
import com.jishop.store.repository.StoreRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class NaverStoreCrawler {

    private static final Logger log = LoggerFactory.getLogger(NaverStoreCrawler.class);
    private static final String[] USER_AGENTS = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
    };

    private static final Random RANDOM = new Random();

    private String getRandomUserAgent() {
        int index = RANDOM.nextInt(USER_AGENTS.length);
        return USER_AGENTS[index];
    }
    private static final int TIMEOUT = 10000; // 10 seconds
    private static final String OUTPUT_DIR = "src/main/resources/static/dbData";


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 전체 크롤링 프로세스를 실행합니다.
     * 1. 카테고리별 상품 데이터를 크롤링
     * 2. JSON 파일로 저장
     * 3. JSON 파일에서 데이터를 읽어 DB에 저장
     */
    @Transactional
    public void crawlAndSaveProducts(boolean skipCrawling) {
        try {
            // 출력 디렉토리 생성
            createOutputDirectory();

            if (!skipCrawling) {
                // 카테고리 리스트 가져오기
                List<String> categoryIds = getCategoryIds();

                // API 응답 구조 테스트 (첫 번째 카테고리로만 테스트)
                log.info("네이버 API 응답 구조 테스트 시작...");
                testNaverApiResponse(categoryIds.get(0));

                // 모든 카테고리 크롤링
                List<ProductCrawlerData> allProducts = new ArrayList<>();

                for (String categoryId : categoryIds) {
                    log.info("카테고리 {} 크롤링 시작", categoryId);
                    List<ProductCrawlerData> products = crawlCategoryProducts(categoryId, 1); // 테스트를 위해 1페이지만 가져옴
                    allProducts.addAll(products);

                    // 카테고리별 JSON 파일 저장
                    saveToJsonFile(products, OUTPUT_DIR + "/products_" + categoryId + ".json");
                    log.info("카테고리 {} 크롤링 완료: {}개의 상품을 저장했습니다.", categoryId, products.size());
                }

                // 모든 상품을 하나의 JSON 파일로 저장
                saveToJsonFile(allProducts, OUTPUT_DIR + "/all_products.json");
                log.info("전체 크롤링 완료: 총 {}개의 상품을 저장했습니다.", allProducts.size());
            }

            // JSON 파일에서 상품 데이터를 읽어 DB에 저장
            saveProductsToDatabase();

        } catch (Exception e) {
            log.error("크롤링 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    /**
     * 출력 디렉토리를 생성합니다.
     */
    private void createOutputDirectory() {
        File directory = new File(OUTPUT_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * 크롤링할 카테고리 ID 목록을 반환합니다.
     */
    private List<String> getCategoryIds() {
        List<String> categoryIds = new ArrayList<>();

        // 남성 의류
        categoryIds.add("50000830"); // 티셔츠
//        categoryIds.add("50000831"); // 니트/스웨터
//        categoryIds.add("50000832"); // 카디건
//        categoryIds.add("50000833"); // 셔츠/남방
//        categoryIds.add("50000834"); // 조끼
//        categoryIds.add("50000835"); // 청바지
//        categoryIds.add("50000836"); // 바지
//        categoryIds.add("50000837"); // 점퍼
//        categoryIds.add("50000838"); // 재킷
//        categoryIds.add("50000839"); // 코트

//        // 남성 언더웨어/잠옷
//        categoryIds.add("50000845"); // 팬티
//        categoryIds.add("50000846"); // 러닝
//        categoryIds.add("50000847"); // 러닝팬티세트
//        categoryIds.add("50000848"); // 잠옷/홈웨어
//
//        // 여성 의류
//        categoryIds.add("50000803"); // 티셔츠
//        categoryIds.add("50000804"); // 블라우스/셔츠
//        categoryIds.add("50000805"); // 니트/스웨터
//        categoryIds.add("50000806"); // 카디건
//        categoryIds.add("50000807"); // 원피스
//        categoryIds.add("50000808"); // 스커트
//        categoryIds.add("50000809"); // 청바지
//        categoryIds.add("50000810"); // 바지
//        categoryIds.add("50000811"); // 점프슈트
//        categoryIds.add("50000812"); // 레깅스
//        categoryIds.add("50000813"); // 코트
//        categoryIds.add("50000814"); // 점퍼
//        categoryIds.add("50000815"); // 재킷
//
//        // 여성 언더웨어/잠옷
//        categoryIds.add("50000823"); // 브라
//        categoryIds.add("50000824"); // 팬티
//        categoryIds.add("50000825"); // 브라팬티세트
//        categoryIds.add("50000826"); // 잠옷/홈웨어
//
//        // 여성 신발
//        categoryIds.add("50000779"); // 부티
//        categoryIds.add("50000780"); // 슬리퍼
//        categoryIds.add("50000781"); // 실내화
//
//        // 남성 신발
//        categoryIds.add("50000787"); // 구두
//        categoryIds.add("50000788"); // 스니커즈
//        categoryIds.add("50000789"); // 샌들
//        categoryIds.add("50000790"); // 슬리퍼
//        categoryIds.add("50000791"); // 워커
//
//        // 여성 가방
//        categoryIds.add("50000639"); // 숄더백
//        categoryIds.add("50000640"); // 토트백
//        categoryIds.add("50000641"); // 크로스백
//        categoryIds.add("50000642"); // 클러치백
//        categoryIds.add("50000643"); // 파우치
//        categoryIds.add("50000644"); // 백팩
//
//        // 남성 가방
//        categoryIds.add("50000646"); // 숄더백
//        categoryIds.add("50000647"); // 토트백
//        categoryIds.add("50000648"); // 크로스백
//        categoryIds.add("50000649"); // 클러치백
//        categoryIds.add("50000650"); // 브리프케이스
//        categoryIds.add("50000651"); // 백팩
//
//        // 여행용 가방/소품
//        categoryIds.add("50000653"); // 보스턴가방
//        categoryIds.add("50000654"); // 복대
//        categoryIds.add("50000656"); // 슈트케이스
//        categoryIds.add("50000657"); // 여권지갑/케이스
//        categoryIds.add("50000658"); // 여행소품케이스
//
//        // 벨트
//        categoryIds.add("50000539"); // 여성벨트
//        categoryIds.add("50000540"); // 멜빵
//        categoryIds.add("50001473"); // 남성벨트
//
//        // 모자
//        categoryIds.add("50001474"); // 야구모자
//        categoryIds.add("50001475"); // 스냅백
//
//        // 장갑
//        categoryIds.add("50000551"); // 여성장갑
//        categoryIds.add("50000552"); // 남성장갑
//        categoryIds.add("50000553"); // 암워머/토시

        // 운영 환경에서는 더 많은 카테고리를 추가할 수 있음

        return categoryIds;
    }

    /**
     * 네이버 API의 응답 형식을 확인하기 위한 테스트 메서드
     */
    private void testNaverApiResponse(String categoryId) {
        try {
            // API URL 생성
            String baseUrl = "https://search.shopping.naver.com";
            String path = "/ns/v1/search/paged-composite-cards";
            String queryParams = String.format("query=%s&pageSize=%d&cursor=%d&searchMethod=displayCategory.basic&isFreshCategory=false&listPage=0&hiddenNonProductCard=true&hasMoreAd=false",
                    categoryId, 20, 0);
            String url = baseUrl + path + "?" + queryParams;

            // API 호출
            Document doc = Jsoup.connect(url)
                    .userAgent(getRandomUserAgent())
                    .header("Accept", "application/json")
                    .timeout(TIMEOUT)
                    .ignoreContentType(true)
                    .get();

            // 응답 파싱
            String jsonResponse = doc.body().text();
            JsonObject responseJson = JsonParser.parseString(jsonResponse).getAsJsonObject();

            // 원시 응답 데이터 저장
            saveRawJsonToFile(responseJson, OUTPUT_DIR + "/raw_response_" + categoryId + ".json");

            log.info("네이버 API 응답 구조 테스트 완료: {}", OUTPUT_DIR + "/raw_response_" + categoryId + ".json");
        } catch (Exception e) {
            log.error("API 테스트 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    /**
     * 파일에 JSON 객체 저장하는 유틸리티 메서드
     */
    private static void saveRawJsonToFile(JsonObject jsonObject, String fileName) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(jsonObject, writer);
        }
    }

    /**
     * 특정 카테고리의 상품 목록을 크롤링합니다.
     */
    public List<ProductCrawlerData> crawlCategoryProducts(String categoryId, int pageCount) throws IOException {
        List<ProductCrawlerData> products = new ArrayList<>();
        int cursor = 0;
        int pageSize = 40;

        for (int page = 1; page <= pageCount; page++) {
            // 네이버 쇼핑 API URL
            String baseUrl = "https://search.shopping.naver.com";
            String path = "/ns/v1/search/paged-composite-cards";
            String queryParams = String.format("query=%s&pageSize=%d&cursor=%d&searchMethod=displayCategory.basic&isFreshCategory=false&listPage=0&hiddenNonProductCard=true&hasMoreAd=false",
                    categoryId, pageSize, cursor);
            String url = baseUrl + path + "?" + queryParams;

            log.debug("API 호출: {}", url);

            Document doc = Jsoup.connect(url)
                    .userAgent(getRandomUserAgent())
                    .header("Accept", "application/json")
                    .timeout(TIMEOUT)
                    .ignoreContentType(true) // JSON 응답을 가져오기 위해 필요
                    .get();

            // JSON 응답 파싱
            String jsonResponse = doc.body().text();
            JsonObject responseJson = JsonParser.parseString(jsonResponse).getAsJsonObject();

            // JSON 응답 구조 로깅 (디버깅용)
            log.debug("API 응답 구조 확인: {}", responseJson.keySet());

            // 파이썬 코드 응답 구조에 맞게 수정
            if (responseJson.has("data") && responseJson.getAsJsonObject("data").has("products")) {
                JsonObject dataObj = responseJson.getAsJsonObject("data");
                JsonObject productsObj = dataObj.getAsJsonObject("products");

                // 다음 페이지를 위한 커서 값 업데이트
                if (productsObj.has("cursor")) {
                    cursor = productsObj.get("cursor").getAsInt();
                }

                // 상품 목록 추출
                if (productsObj.has("list")) {
                    JsonArray productList = productsObj.getAsJsonArray("list");

                    for (JsonElement productElement : productList) {
                        try {
                            JsonObject productJson = productElement.getAsJsonObject();

                            // 상품 데이터가 card > product 경로에 있는 경우 처리
                            if (productJson.has("card") && productJson.getAsJsonObject("card").has("product")) {
                                productJson = productJson.getAsJsonObject("card").getAsJsonObject("product");
                            }

                            // JSON 구조 확인을 위한 로깅
                            if (page == 1 && products.size() == 0) {
                                log.debug("첫 번째 상품 JSON 구조: {}", productJson.keySet());
                            }

                            ProductCrawlerData product = convertToProductData(productJson, categoryId);
                            products.add(product);
                        } catch (Exception e) {
                            log.error("상품 파싱 중 오류 발생: {}", e.getMessage(), e);
                        }
                    }
                }
            }

            log.info("카테고리 {} - 페이지 {} 크롤링 완료", categoryId, page);

            // 과도한 요청 방지를 위한 대기
            try {
                Thread.sleep(2000 + new Random().nextInt(3000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return products;
    }

    /**
     * JSON 객체를 ProductCrawlerData 객체로 변환합니다.
     */
    private ProductCrawlerData convertToProductData(JsonObject productJson, String categoryId) {
        ProductCrawlerData product = new ProductCrawlerData();

        // 카테고리 ID 설정
        product.setCategoryId(categoryId);

        // 카테고리 계층 정보 설정
        if (productJson.has("lCatId")) {
            product.setLcatId(getJsonString(productJson, "lCatId", ""));
        }
        if (productJson.has("mCatId")) {
            product.setMcatId(getJsonString(productJson, "mCatId", ""));
        }
        if (productJson.has("sCatId")) {
            product.setScatId(getJsonString(productJson, "sCatId", ""));
        }

        // 스토어 정보 설정 (mallSeq를 storeSeq로 사용)
        product.setStoreSeq(getJsonString(productJson, "mallSeq", ""));

        // 상품명
        product.setName(getJsonString(productJson, "productName", ""));

        // 설명 (상품명 재사용)
        product.setDescription(product.getName());

        // 가격 정보
        product.setOriginPrice(getJsonInt(productJson, "salePrice", 0));

        // 할인 가격
        if (productJson.has("discountedSalePrice")) {
            product.setDiscountPrice(getJsonInt(productJson, "discountedSalePrice", product.getOriginPrice()));
            product.setIsDiscount(true);
        } else {
            product.setDiscountPrice(product.getOriginPrice());
            product.setIsDiscount(false);
        }

        // 제조일자 (임의 설정)
        product.setManufactureDate(LocalDateTime.now().minusMonths(new Random().nextInt(6)));

        // 비밀 상품 여부 (기본값)
        product.setSecret(false);

        // 판매 상태 (기본값)
        product.setSaleStatus("ON_SALE");

        // 할인 상태
        product.setDiscountStatus(product.getIsDiscount() ? "ON" : "OFF");

        // 브랜드 (판매자명 사용)
        product.setBrand(getJsonString(productJson, "mallName", ""));

        // 평점
        product.setRate(getJsonDouble(productJson, "averageReviewScore", 0.0));

        // 리뷰 수
        product.setReviewCount(getJsonInt(productJson, "totalReviewCount", 0));

        // 좋아요 수 (임의 설정)
        product.setLikeCount(new Random().nextInt(100));

        // 라벨 (기본값)
        product.setLabels("NONE");

        // 삭제 여부
        product.setDeleteFlag(false);

        // 이미지 URL 설정
        // 이미지 필드 접근 시도 (여러 가능한 경로를 시도)
        if (productJson.has("images") && productJson.getAsJsonArray("images").size() > 0) {
            JsonObject imageObj = productJson.getAsJsonArray("images").get(0).getAsJsonObject();
            String imageUrl = getJsonString(imageObj, "imageUrl", "");

            product.setMainImage(imageUrl);
            product.setDetailImage(imageUrl);

            // 추가 이미지가 있는 경우 설정
            if (productJson.getAsJsonArray("images").size() > 1) {
                for (int i = 1; i < Math.min(productJson.getAsJsonArray("images").size(), 5); i++) {
                    JsonObject additionalImageObj = productJson.getAsJsonArray("images").get(i).getAsJsonObject();
                    String additionalImageUrl = getJsonString(additionalImageObj, "imageUrl", "");

                    switch (i) {
                        case 1: product.setImage1(additionalImageUrl); break;
                        case 2: product.setImage2(additionalImageUrl); break;
                        case 3: product.setImage3(additionalImageUrl); break;
                        case 4: product.setImage4(additionalImageUrl); break;
                    }
                }
            }
        }

        return product;
    }

    // JsonObject에서 안전하게 문자열 값 가져오기
    private static String getJsonString(JsonObject json, String key, String defaultValue) {
        return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsString() : defaultValue;
    }

    // JsonObject에서 안전하게 정수 값 가져오기
    private static int getJsonInt(JsonObject json, String key, int defaultValue) {
        return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsInt() : defaultValue;
    }

    // JsonObject에서 안전하게 실수 값 가져오기
    private static double getJsonDouble(JsonObject json, String key, double defaultValue) {
        return json.has(key) && !json.get(key).isJsonNull() ? json.get(key).getAsDouble() : defaultValue;
    }

    /**
     * 상품 데이터 리스트를 JSON 파일로 저장합니다.
     */
    private void saveToJsonFile(List<ProductCrawlerData> products, String fileName) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonArray = new JsonArray();

        for (ProductCrawlerData product : products) {
            JsonObject jsonObject = new JsonObject();

            // ProductCrawlerData 객체의 모든 필드를 JSON으로 변환
            jsonObject.addProperty("categoryId", product.getCategoryId());
            if (product.getLcatId() != null) jsonObject.addProperty("lcatId", product.getLcatId());
            if (product.getMcatId() != null) jsonObject.addProperty("mcatId", product.getMcatId());
            if (product.getScatId() != null) jsonObject.addProperty("scatId", product.getScatId());
            jsonObject.addProperty("storeSeq", product.getStoreSeq());
            jsonObject.addProperty("name", product.getName());
            jsonObject.addProperty("description", product.getDescription());
            jsonObject.addProperty("originPrice", product.getOriginPrice());
            jsonObject.addProperty("discountPrice", product.getDiscountPrice());
            jsonObject.addProperty("manufactureDate", product.getManufactureDate());
            jsonObject.addProperty("secret", product.getSecret());
            jsonObject.addProperty("saleStatus", product.getSaleStatus());
            jsonObject.addProperty("discountStatus", product.getDiscountStatus());
            jsonObject.addProperty("isDiscount", product.getIsDiscount());
            jsonObject.addProperty("brand", product.getBrand());
            jsonObject.addProperty("rate", product.getRate());
            jsonObject.addProperty("reviewCount", product.getReviewCount());
            jsonObject.addProperty("likeCount", product.getLikeCount());
            jsonObject.addProperty("labels", product.getLabels());
            jsonObject.addProperty("deleteFlag", product.getDeleteFlag());

            // 이미지 URL
            if (product.getMainImage() != null) jsonObject.addProperty("mainImage", product.getMainImage());
            if (product.getImage1() != null) jsonObject.addProperty("image1", product.getImage1());
            if (product.getImage2() != null) jsonObject.addProperty("image2", product.getImage2());
            if (product.getImage3() != null) jsonObject.addProperty("image3", product.getImage3());
            if (product.getImage4() != null) jsonObject.addProperty("image4", product.getImage4());
            if (product.getDetailImage() != null) jsonObject.addProperty("detailImage", product.getDetailImage());

            jsonArray.add(jsonObject);
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(jsonArray, writer);
        }
    }

    /**
     * JSON 파일에서 상품 데이터를 읽어 DB에 저장합니다.
     */
    @Transactional
    public void saveProductsToDatabase() {
        try {
            log.info("JSON 파일에서 데이터 로드 및 DB 저장 시작");

            // all_products.json 파일 읽기
            String filePath = OUTPUT_DIR + "/all_products.json";
            Path path = Paths.get(filePath);

            if (!Files.exists(path)) {
                log.warn("파일이 존재하지 않습니다: {}", filePath);
                return;
            }

            // JSON 파일 읽기
            Gson gson = new Gson();
            try (Reader reader = new FileReader(filePath)) {
                ProductCrawlerData[] productDataArray = gson.fromJson(reader, ProductCrawlerData[].class);
                log.info("{}개의 상품 데이터를 읽었습니다.", productDataArray.length);

                // 스토어 정보 캐싱 (중복 저장 방지)
                Map<String, Store> storeCache = new HashMap<>();

                // 카테고리 정보 캐싱 (중복 저장 방지)
                Map<String, Category> categoryCache = new HashMap<>();

                int savedCount = 0;
                int skippedCount = 0;

                // 각 데이터를 Product 엔티티로 변환하여 저장
                for (ProductCrawlerData data : productDataArray) {
                    // 카테고리 조회 (새로 생성하지 않음)
                    Category category = getCategoryFromCache(categoryCache, data.getCategoryId());

                    // 카테고리가 없는 경우 스킵
                    if (category == null) {
                        log.warn("카테고리 ID {}가 존재하지 않아 상품 '{}' 저장을 건너뜁니다.",
                                data.getCategoryId(), data.getName());
                        skippedCount++;
                        continue;
                    }

                    // 스토어 조회 또는 생성
                    Store store = getStoreFromCache(storeCache, data.getStoreSeq(), data.getBrand());

                    // 상품 생성 및 저장
                    Product product = convertToProductEntity(data, category);
                    productRepository.save(product);
                    savedCount++;
                }

                log.info("모든 상품 데이터가 DB에 저장되었습니다. 저장: {}개, 스킵: {}개",
                        savedCount, skippedCount);
            }
        } catch (Exception e) {
            log.error("데이터베이스 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("데이터베이스 저장 실패", e);
        }
    }

    /**
     * 카테고리 캐시에서 카테고리를 조회합니다. 없으면 null 반환
     */
    private Category getCategoryFromCache(Map<String, Category> categoryCache, String categoryId) {
        if (categoryCache.containsKey(categoryId)) {
            return categoryCache.get(categoryId);
        }

        // DB에서 카테고리 조회
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            categoryCache.put(categoryId, category);
            return category;
        }

        // 카테고리가 없으면 null 반환
        return null;
    }

    /**
     * 스토어 캐시에서 스토어를 조회하거나 새로 생성합니다.
     */
    private Store getStoreFromCache(Map<String, Store> storeCache, String mallSeq, String mallName) {
        if (storeCache.containsKey(mallSeq)) {
            return storeCache.get(mallSeq);
        }

        // DB에서 스토어 조회
        Optional<Store> storeOpt = storeRepository.findByMallSeq(mallSeq);

        if (storeOpt.isPresent()) {
            Store store = storeOpt.get();
            storeCache.put(mallSeq, store);
            return store;
        }

        // 스토어가 없으면 생성
        Store newStore = Store.builder()
                .mallSeq(mallSeq)
                .mallName(mallName)
                .isBrandStore(false) // 여기를 완성합니다.
                .build();

        Store savedStore = storeRepository.save(newStore);
        storeCache.put(mallSeq, savedStore);
        return savedStore;
    }

        /**
         * ProductCrawlerData 객체를 Product 엔티티로 변환합니다.
         */
        private Product convertToProductEntity(ProductCrawlerData data, Category category) {
            // String을 LocalDateTime으로 변환
            LocalDateTime manufactureDate;
            try {
                manufactureDate = LocalDateTime.parse(data.getManufactureDate());
            } catch (Exception e) {
                manufactureDate = LocalDateTime.now();
            }

            // String 값을 해당 Enum 타입으로 변환
            SaleStatus saleStatus = SaleStatus.valueOf(data.getSaleStatus());
            DiscountStatus discountStatus = DiscountStatus.valueOf(data.getDiscountStatus());
            Labels labels = Labels.valueOf(data.getLabels());

            // 상품 엔티티 생성
            Product product = new Product(
                    category,
                    data.getLcatId(),
                    data.getMcatId(),
                    data.getScatId(),
                    data.getStoreSeq(),
                    data.getName(),
                    data.getDescription(),
                    data.getOriginPrice(),
                    data.getDiscountPrice(),
                    manufactureDate,
                    data.getSecret(),
                    saleStatus,
                    discountStatus,
                    data.getIsDiscount(),
                    data.getBrand(),
                    data.getRate(),
                    data.getReviewCount(),
                    data.getLikeCount(),
                    labels,
                    data.getDeleteFlag(),
                    data.getMainImage(),
                    data.getImage1(),
                    data.getImage2(),
                    data.getImage3(),
                    data.getImage4(),
                    data.getDetailImage()
            );

            return product;
        }
}