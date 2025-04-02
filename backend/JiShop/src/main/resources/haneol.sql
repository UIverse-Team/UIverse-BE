
-- 📌 1. 최상위 카테고리 (대분류) - ID 자동 증가
INSERT INTO category (
    delete_status, deleted, juvenile_harmful, last_level, level, sell_blog_use,
    sort_order, created_at, current_id, parent_id, updated_at, name,
    whole_category_id, whole_category_name
) VALUES
      (0, 0, 0, 0, 1, 1, 1, NOW(), 1, NULL, NOW(), '의류', '1', '의류'),
      (0, 0, 0, 0, 1, 1, 2, NOW(), 2, NULL, NOW(), '신발', '2', '신발'),
      (0, 0, 0, 0, 1, 1, 3, NOW(), 3, NULL, NOW(), '가방', '3', '가방');

-- 📌 2. 중간 카테고리 (중분류) - `parent_id`를 올바르게 설정
INSERT INTO category (
    delete_status, deleted, juvenile_harmful, last_level, level, sell_blog_use,
    sort_order, created_at, current_id, parent_id, updated_at, name,
    whole_category_id, whole_category_name
) VALUES
      (0, 0, 0, 0, 2, 1, 1, NOW(), 4, 1, NOW(), '남성 의류', '1>4', '의류>남성 의류'),
      (0, 0, 0, 0, 2, 1, 2, NOW(), 5, 1, NOW(), '여성 의류', '1>5', '의류>여성 의류'),
      (0, 0, 0, 0, 2, 1, 3, NOW(), 6, 2, NOW(), '운동화', '2>6', '신발>운동화'),
      (0, 0, 0, 0, 2, 1, 4, NOW(), 7, 2, NOW(), '정장 구두', '2>7', '신발>정장 구두'),
      (0, 0, 0, 0, 2, 1, 5, NOW(), 8, 3, NOW(), '백팩', '3>8', '가방>백팩');

-- 📌 3. 하위 카테고리 (소분류) - `parent_id`를 올바르게 설정
INSERT INTO category (
    delete_status, deleted, juvenile_harmful, last_level, level, sell_blog_use,
    sort_order, created_at, current_id, parent_id, updated_at, name,
    whole_category_id, whole_category_name
) VALUES
      (0, 0, 0, 1, 3, 1, 1, NOW(), 9, 4, NOW(), '티셔츠', '1>4>9', '의류>남성 의류>티셔츠'),
      (0, 0, 0, 1, 3, 1, 2, NOW(), 10, 4, NOW(), '청바지', '1>4>10', '의류>남성 의류>청바지'),
      (0, 0, 0, 1, 3, 1, 3, NOW(), 11, 5, NOW(), '블라우스', '1>5>11', '의류>여성 의류>블라우스'),
      (0, 0, 0, 1, 3, 1, 4, NOW(), 12, 5, NOW(), '스커트', '1>5>12', '의류>여성 의류>스커트'),
      (0, 0, 0, 1, 3, 1, 5, NOW(), 13, 6, NOW(), '런닝화', '2>6>13', '신발>운동화>런닝화'),
      (0, 0, 0, 1, 3, 1, 6, NOW(), 14, 6, NOW(), '축구화', '2>6>14', '신발>운동화>축구화'),
      (0, 0, 0, 1, 3, 1, 7, NOW(), 15, 7, NOW(), '정장 구두', '2>7>15', '신발>정장 구두>정장 구두'),
      (0, 0, 0, 1, 3, 1, 8, NOW(), 16, 8, NOW(), '학생 백팩', '3>8>16', '가방>백팩>학생 백팩');

-- 📌 1. 최상위 카테고리 (대분류)




INSERT INTO product (
    delete_flag, delete_status, discount_price, is_discount, like_count,
    origin_price, secret, category_id, created_at, manufacture_date,
    updated_at, brand, description, detail_image, image1, image2, image3,
    image4, l_cat_id, m_cat_id, main_image, name, s_cat_id, store_seq,
    discount_status, labels, sale_status
) VALUES
-- 📌 제품 1: 할인 적용, 판매 중 (카테고리: 남성 의류)
(0, 0, 5000, 1, 120,
 20000, 0, 4, NOW(), '2024-01-10',
 NOW(), 'Nike', '편안한 러닝화', 'detail1.jpg',
 'image1.jpg', 'image2.jpg', 'image3.jpg', 'image4.jpg',
 '1', '4', 'main1.jpg', 'Nike Air Max 2024', '9', 'ST001',
 'MEMBER_ONLY', 'PROMOTION', 'SELLING'),

-- 📌 제품 2: 할인 없음, 예약 판매 (카테고리: 여성 의류)
(0, 0, 0, 0, 75,
 30000, 0, 5, NOW(), '2023-12-15',
 NOW(), 'Adidas', '클래식 디자인의 스니커즈', 'detail2.jpg',
 'image5.jpg', 'image6.jpg', 'image7.jpg', 'image8.jpg',
 '1', '5', 'main2.jpg', 'Adidas Superstar', '11', 'ST002',
 'NONE', NULL, 'RESERVED'),

-- 📌 제품 3: 타임 세일 적용, 품절 상태 (카테고리: 운동화)
(0, 0, 3000, 1, 95,
 15000, 0, 6, NOW(), '2023-11-20',
 NOW(), 'Puma', '스포티한 러닝화', 'detail3.jpg',
 'image9.jpg', 'image10.jpg', 'image11.jpg', 'image12.jpg',
 '2', '6', 'main3.jpg', 'Puma Runner', '13', 'ST003',
 'TIME_SALE', 'SPECIAL_PRICE', 'SOLD_OUT'),

-- 📌 제품 4: 할인 없음, 단종 (카테고리: 정장 구두)
(1, 1, 0, 0, 50,
 18000, 1, 7, NOW(), '2023-10-05',
 NOW(), 'New Balance', '한정판 러닝화', 'detail4.jpg',
 'image13.jpg', 'image14.jpg', 'image15.jpg', 'image16.jpg',
 '2', '7', 'main4.jpg', 'NB 990v5', '15', 'ST004',
 'NONE', NULL, 'DISCONTINUED'),

-- 📌 제품 5: 회원 전용 할인, 판매 중 (카테고리: 백팩)
(0, 0, 7000, 1, 200,
 25000, 0, 8, NOW(), '2023-09-15',
 NOW(), 'Reebok', '클래식 가죽 스니커즈', 'detail5.jpg',
 'image17.jpg', 'image18.jpg', 'image19.jpg', 'image20.jpg',
 '3', '8', 'main5.jpg', 'Reebok Classic', '16', 'ST005',
 'MEMBER_ONLY', 'PROMOTION', 'SELLING');











INSERT INTO options (
    delete_status, option_extra, created_at, updated_at, option_value, category_type
) VALUES
-- 📌 디지털/가전 옵션
(0, 10000, NOW(), NOW(), '추가 배터리', 'DIGITAL_APPLIANCES'),
(0, 5000, NOW(), NOW(), '고속 충전기', 'DIGITAL_APPLIANCES'),
(0, 0, NOW(), NOW(), '블루투스 연결 지원', 'DIGITAL_APPLIANCES'),

-- 📌 패션잡화 옵션
(0, 0, NOW(), NOW(), '가죽 스트랩', 'FASHION_ACCESSORIES'),
(0, 3000, NOW(), NOW(), '메탈 스트랩', 'FASHION_ACCESSORIES'),
(0, 5000, NOW(), NOW(), '커스텀 각인 추가', 'FASHION_ACCESSORIES'),

-- 📌 패션의류 옵션
(0, 0, NOW(), NOW(), '사이즈 S', 'FASHION_CLOTHING'),
(0, 0, NOW(), NOW(), '사이즈 M', 'FASHION_CLOTHING'),
(0, 0, NOW(), NOW(), '사이즈 L', 'FASHION_CLOTHING'),
(0, 5000, NOW(), NOW(), '고급 원단 사용', 'FASHION_CLOTHING'),

-- 📌 식품 옵션
(0, 0, NOW(), NOW(), '스파이시 맛', 'FOOD'),
(0, 0, NOW(), NOW(), '오리지널 맛', 'FOOD'),
(0, 2000, NOW(), NOW(), '건강식 버전', 'FOOD'),

-- 📌 가구/인테리어 옵션
(0, 10000, NOW(), NOW(), '원목 소재 변경', 'FURNITURE_INTERIOR'),
(0, 15000, NOW(), NOW(), '고급 가죽 쿠션', 'FURNITURE_INTERIOR'),
(0, 5000, NOW(), NOW(), 'DIY 조립 키트 포함', 'FURNITURE_INTERIOR');




INSERT INTO sales_products (
    delete_status, product_rating, quantity, created_at, updated_at,
    option_id, product_id, image, name
) VALUES
-- 📌 디지털/가전 제품 (추가 배터리)
(0, 4.8, 50, NOW(), NOW(), 1, 1, 'battery_extra.jpg', '추가 배터리 - 디지털 기기'),

-- 📌 디지털/가전 제품 (고속 충전기)
(0, 4.7, 40, NOW(), NOW(), 2, 1, 'fast_charger.jpg', '고속 충전기 - 디지털 기기'),

-- 📌 디지털/가전 제품 (블루투스 연결 지원)
(0, 4.6, 60, NOW(), NOW(), 3, 1, 'bluetooth_support.jpg', '블루투스 연결 지원 - 디지털 기기'),

-- 📌 패션잡화 제품 (가죽 스트랩)
(0, 4.9, 30, NOW(), NOW(), 4, 2, 'leather_strap.jpg', '가죽 스트랩 - 패션 잡화'),

-- 📌 패션잡화 제품 (메탈 스트랩)
(0, 4.5, 25, NOW(), NOW(), 5, 2, 'metal_strap.jpg', '메탈 스트랩 - 패션 잡화'),

-- 📌 패션잡화 제품 (커스텀 각인 추가)
(0, 4.3, 20, NOW(), NOW(), 6, 2, 'custom_engraving.jpg', '커스텀 각인 추가 - 패션 잡화'),

-- 📌 패션의류 제품 (사이즈 S)
(0, 4.8, 60, NOW(), NOW(), 7, 3, 'size_s.jpg', '사이즈 S - 패션 의류'),

-- 📌 패션의류 제품 (사이즈 M)
(0, 4.7, 55, NOW(), NOW(), 8, 3, 'size_m.jpg', '사이즈 M - 패션 의류'),

-- 📌 패션의류 제품 (사이즈 L)
(0, 4.6, 50, NOW(), NOW(), 9, 3, 'size_l.jpg', '사이즈 L - 패션 의류'),

-- 📌 패션의류 제품 (고급 원단 사용)
(0, 4.9, 20, NOW(), NOW(), 10, 3, 'premium_fabric.jpg', '고급 원단 사용 - 패션 의류'),

-- 📌 식품 제품 (스파이시 맛)
(0, 4.5, 70, NOW(), NOW(), 11, 4, 'spicy_flavor.jpg', '스파이시 맛 - 식품'),

-- 📌 식품 제품 (오리지널 맛)
(0, 4.8, 80, NOW(), NOW(), 12, 4, 'original_flavor.jpg', '오리지널 맛 - 식품'),

-- 📌 식품 제품 (건강식 버전)
(0, 4.6, 30, NOW(), NOW(), 13, 4, 'healthy_version.jpg', '건강식 버전 - 식품'),

-- 📌 가구/인테리어 제품 (원목 소재 변경)
(0, 4.7, 15, NOW(), NOW(), 14, 5, 'wood_material.jpg', '원목 소재 변경 - 가구'),

-- 📌 가구/인테리어 제품 (고급 가죽 쿠션)
(0, 4.9, 10, NOW(), NOW(), 15, 5, 'leather_cushion.jpg', '고급 가죽 쿠션 - 가구'),

-- 📌 가구/인테리어 제품 (DIY 조립 키트 포함)
(0, 4.6, 20, NOW(), NOW(), 16, 5, 'diy_kit.jpg', 'DIY 조립 키트 포함 - 가구');

INSERT INTO order_number (
    delete_status, created_at, updated_at, order_number
) VALUES
-- 📌 주문번호 1
(0, NOW(), NOW(), '20240313-00001'),

-- 📌 주문번호 2
(0, NOW(), NOW(), '20240313-00002'),

-- 📌 주문번호 3
(0, NOW(), NOW(), '20240313-00003'),

-- 📌 주문번호 4
(0, NOW(), NOW(), '20240313-00004');


INSERT INTO orders (
    delete_status, total_price, created_at, updated_at, order_number_id,
    base_address, detail_address, zip_code, receiver, receiver_number,
    main_product_name, status
) VALUES
-- 📌 주문 1: 패션잡화 - 가죽 스트랩
(0, 25000, NOW(), NOW(), 1,
 '서울특별시 강남구', '테헤란로 123', '06164', '김철수', '010-1234-5678',
 '가죽 스트랩 - 패션 잡화', 'ORDER_COMPLETED'),

-- 📌 주문 2: 디지털/가전 - 고속 충전기
(0, 55000, NOW(), NOW(), 2,
 '부산광역시 해운대구', '센텀중앙로 45', '48058', '이영희', '010-9876-5432',
 '고속 충전기 - 디지털 기기', 'ORDER_RECEIVED'),

-- 📌 주문 3: 패션의류 - 고급 원단 사용
(0, 75000, NOW(), NOW(), 3,
 '대구광역시 수성구', '범어로 256', '42020', '박민수', '010-1111-2222',
 '고급 원단 사용 - 패션 의류', 'ORDER_CANCELED'),

-- 📌 주문 4: 식품 - 건강식 버전
(0, 35000, NOW(), NOW(), 4,
 '대전광역시 유성구', '관평로 37', '34167', '최지은', '010-3333-4444',
 '건강식 버전 - 식품', 'PURCHASED_CONFIRMED');



INSERT INTO order_detail (
    delete_status, price, quantity, created_at, updated_at, order_id, sale_product_id
) VALUES
-- 📌 주문 1 상세 (가죽 스트랩)
(0, 25000, 1, NOW(), NOW(), 1, 4),

-- 📌 주문 2 상세 (고속 충전기)
(0, 55000, 1, NOW(), NOW(), 2, 2),

-- 📌 주문 3 상세 (고급 원단 사용)
(0, 75000, 1, NOW(), NOW(), 3, 10),

-- 📌 주문 4 상세 (건강식 버전)
(0, 35000, 1, NOW(), NOW(), 4, 13);




INSERT INTO users (
    delete_status, created_at, updated_at, birth_date, gender, login_id,
    name, password, phone, provider
) VALUES
-- 📌 LOCAL 가입 사용자
(0, NOW(), NOW(), '1990-05-21', 'Male', 'local_user1', '김철수',
 '$2y$10$ABC123hashedpasswordXYZ', '010-1234-5678', 'LOCAL'),

(0, NOW(), NOW(), '1988-11-03', 'Female', 'local_user2', '이영희',
 '$2y$10$DEF456hashedpasswordUVW', '010-9876-5432', 'LOCAL'),

-- 📌 GOOGLE 로그인 사용자
(0, NOW(), NOW(), '1995-07-14', 'Male', 'google_user1', '박민수',
 NULL, '010-1111-2222', 'GOOGLE'),

(0, NOW(), NOW(), '1992-03-25', 'Female', 'google_user2', '최지은',
 NULL, '010-3333-4444', 'GOOGLE'),

-- 📌 KAKAO 로그인 사용자
(0, NOW(), NOW(), '2000-09-10', 'Male', 'kakao_user1', '정우성',
 NULL, '010-5555-6666', 'KAKAO'),

(0, NOW(), NOW(), '1999-06-18', 'Female', 'kakao_user2', '한예리',
 NULL, '010-7777-8888', 'KAKAO'),

-- 📌 NAVER 로그인 사용자
(0, NOW(), NOW(), '1985-12-05', 'Male', 'naver_user1', '이강인',
 NULL, '010-9999-0000', 'NAVER'),

(0, NOW(), NOW(), '1993-04-30', 'Female', 'naver_user2', '김지원',
 NULL, '010-2222-3333', 'NAVER');
