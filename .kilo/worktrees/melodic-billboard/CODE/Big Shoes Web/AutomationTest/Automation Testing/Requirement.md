# Yêu cầu kiểm thử - Hệ thống Thương mại điện tử Shoes Web

## Mục lục
1. [Tổng quan hệ thống](#1-tổng-quan-hệ-thống)
2. [Luồng Xác thực & Phân quyền](#2-luồng-xác-thực--phân-quyền)
3. [Luồng Khám phá & Duyệt sản phẩm](#3-luồng-khám-phá--duyệt-sản-phẩm)
4. [Luồng Giỏ hàng & Thanh toán](#4-luồng-giỏ-hàng--thanh-toán)
5. [Luồng Sau thanh toán & Quản lý cá nhân](#5-luồng-sau-thanh-toán--quản-lý-cá-nhân)
6. [Luồng Quản trị (Admin Panel)](#6-luồng-quản-trị-admin-panel)
7. [Tổng hợp API Endpoints](#7-tổng-hợp-api-endpoints)
8. [Test Data gợi ý tổng hợp](#8-test-data-gợi-ý-tổng-hợp)
9. [Ghi chú kỹ thuật cho Selenium](#9-ghi-chú-kỹ-thuật-cho-selenium)

---

## 1. Tổng quan hệ thống

### 1.1 Các tác nhân (Actors)
- **Guest**: Người dùng chưa đăng nhập.
- **Customer**: Người dùng đã đăng nhập, có role `Customer`.
- **Admin**: Người dùng có role `Admin`, quản lý toàn hệ thống.

### 1.2 Các trang chính (Frontend Pages)
| Trang | Mô tả | API tương ứng |
|-------|--------|---------------|
| Home | Banner, danh mục, sản phẩm nổi bật | `GET /api/products` |
| Collection / Shop | Lọc theo category, brand, search, sort, phân trang | `GET /api/products` |
| Product Detail (PDP) | Chi tiết SP, biến thể, reviews, sản phẩm liên quan | `GET /api/products/{id}` |
| Cart | Xem giỏ hàng, cập nhật SL, xóa | `GET/PUT/DELETE /api/cart/{userId}` |
| Checkout | Form địa chỉ + phương thức thanh toán | `POST /api/orders/{userId}` |
| Order Confirmation | Mã đơn, chi tiết đơn vừa tạo | `GET /api/orders/{orderId}/user/{userId}` |
| My Orders | Lịch sử đơn hàng của user | `GET /api/orders/user/{userId}` |
| Admin Dashboard | Thống kê tổng quan | `GET /api/admin/dashboard/stats` |
| Admin Orders | Danh sách + cập nhật trạng thái đơn | `GET/PUT /api/admin/orders` |
| Admin Products | CRUD sản phẩm, biến thể, ảnh | `GET/POST/PUT/DELETE /api/admin/products` |
| Admin Brands | CRUD thương hiệu | `GET/POST/PUT/DELETE /api/admin/brands` |
| Admin Categories | CRUD danh mục | `GET/POST/PUT/DELETE /api/admin/categories` |

### 1.3 Luồng trạng thái đơn hàng (Order Status Flow)
```
Pending -> Processing -> Shipped -> Completed
   |         |           |
   |         |           v
   |         |        Cancelled (không được đổi từ Shipped/Completed)
   |         v
   |      Cancelled
   v
Cancelled
```
- **Cho phép đổi**: `Pending`, `Processing` → có thể đổi sang các trạng thái khác (trừ Shipped/Completed/Cancelled).
- **Không cho phép đổi**: `Shipped`, `Completed`, `Cancelled` → Admin không được cập nhật trạng thái nữa.

### 1.4 Luồng trạng thái thanh toán (Payment Status Flow)
```
Unpaid -> Paid -> Refunded
```
- COD: Mặc định `Unpaid` khi tạo đơn.
- CreditCard/BankTransfer/card/paypal: Mặc định `Paid` khi tạo đơn.
- Khi hủy đơn đã `Paid` → `Refunded`.

---

## 2. Luồng Xác thực & Phân quyền

### 2.1 Mô tả luồng nghiệp vụ

#### 2.1.1 Đăng ký (Register)
1. Guest truy cập trang **Register**.
2. Nhập thông tin: `FirstName`, `LastName`, `Email`, `Password`, `ConfirmPassword`.
3. Nhấn **Đăng ký** → Backend tạo tài khoản mới (`User` với `EmailConfirmed = true`).
4. Tự động đăng nhập hoặc chuyển sang trang Login.

#### 2.1.2 Đăng nhập (Login)
1. Guest truy cập trang **Login**.
2. Nhập `Email` + `Password`.
3. Nhấn **Đăng nhập** → Backend trả về `LoginResponseDto` chứa:
   - `Id`, `UserName`, `Email`, `Token` (JWT, hết hạn 1 giờ), `Role` (Customer/Admin).
4. Frontend lưu token vào `localStorage` / cookie.
5. Tự động điều hướng về trang trước đó (nếu có) hoặc trang chủ.

#### 2.1.3 Đăng xuất (Logout)
1. User đang đăng nhập nhấn **Đăng xuất**.
2. Xóa token khỏi client.
3. Chuyển về trang chủ dưới dạng Guest.

#### 2.1.4 Phân quyền trang
- **Guest**: Chỉ xem được trang chủ, danh sách sản phẩm, PDP. Không vào được Cart, Checkout, My Orders. Bị chuyển sang Login khi cố truy cập.
- **Customer**: Toàn quyền đặt hàng, xem đơn, quản lý giỏ.
- **Admin**: Toàn quyền quản trị (các trang `/admin/*`).

### 2.2 Validation Rules

#### RegisterDto (`POST /api/auth/register`)
| Field | Rule | Lỗi |
|-------|------|-----|
| `FirstName` | Required, 2-100 ký tự | `"Họ và tên là bắt buộc."` |
| `LastName` | Required, 2-100 ký tự | `"Họ và tên là bắt buộc."` |
| `Email` | Required, đúng định dạng email | DataAnnotations `[EmailAddress]` |
| `Password` | Required, >= 6 ký tự | DataAnnotations `[StringLength(100, MinimumLength = 6)]` |
| `ConfirmPassword` | Required, phải trùng `Password` | DataAnnotations `[Compare("Password")]` |

#### LoginDto (`POST /api/auth/login`)
| Field | Rule | Lỗi |
|-------|------|-----|
| `Email` | Required, đúng định dạng email | `"Email hoặc mật khẩu không đúng."` |
| `Password` | Required | `"Email hoặc mật khẩu không đúng."` |

#### Logic bổ sung trong AuthService
- Email đã tồn tại → `409 Conflict` - `"Email đã tồn tại."`
- Tài khoản bị khóa (`IsActive = false`) → `401 Unauthorized` - `"Tài khoản đã bị khóa."`
- Mật khẩu không đúng → `401 Unauthorized` - `"Email hoặc mật khẩu không đúng."`
- User không tồn tại → `401 Unauthorized` - `"Email hoặc mật khẩu không đúng."`

### 2.3 Danh sách Test Scenario

#### Happy Path
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-AUTH-01 | Đăng ký thành công | Nhập đầy đủ thông tin hợp lệ | 201 - `RegisterResponseDto`, user được tạo |
| HP-AUTH-02 | Đăng nhập thành công | Email + Password đúng | 200 - `LoginResponseDto` có `Token`, `Role = Customer` |
| HP-AUTH-03 | Đăng nhập Admin | Email + Password của Admin | 200 - `Role = Admin` |
| HP-AUTH-04 | Đăng xuất | Xóa token khỏi client | Chuyển về trang chủ dạng Guest |
| HP-AUTH-05 | Redirect sau login | Truy cập `/cart` khi chưa đăng nhập → Login → về `/cart` | Đăng nhập thành công, chuyển về `/cart` |

#### Unhappy Path
| ID | Tên Scenario | Điều kiện | Kết quả mong đợi |
|----|-------------|-----------|-----------------|
| UV-AUTH-01 | Đăng ký thiếu FirstName | `FirstName = ""` | 400 - `"Họ và tên là bắt buộc."` |
| UV-AUTH-02 | Đăng ký thiếu LastName | `LastName = ""` | 400 - `"Họ và tên là bắt buộc."` |
| UV-AUTH-03 | Đăng ký thiếu Email | `Email = ""` | 400 - Validation error |
| UV-AUTH-04 | Đăng ký thiếu Password | `Password = ""` | 400 - Validation error |
| UV-AUTH-05 | Đăng ký Password < 6 ký tự | `Password = "123"` | 400 - Validation error |
| UV-AUTH-06 | Đăng ký ConfirmPassword khác Password | `ConfirmPassword != Password` | 400 - Validation error |
| UV-AUTH-07 | Đăng ký Email đã tồn tại | Email đã có trong DB | 409 - `"Email đã tồn tại."` |
| UV-AUTH-08 | Đăng nhập sai Password | Email đúng, Password sai | 401 - `"Email hoặc mật khẩu không đúng."` |
| UV-AUTH-09 | Đăng nhập Email không tồn tại | Email chưa đăng ký | 401 - `"Email hoặc mật khẩu không đúng."` |
| UV-AUTH-10 | Đăng nhập tài khoản bị khóa | `IsActive = false` | 401 - `"Tài khoản đã bị khóa."` |
| UV-AUTH-11 | Đăng nhập thiếu Email | `Email = ""` | 400 - Validation error |
| UV-AUTH-12 | Đăng nhập thiếu Password | `Password = ""` | 400 - Validation error |
| UV-AUTH-13 | Truy cập trang Customer khi chưa đăng nhập | Guest mở `/cart` | 401 Unauthorized / redirect Login |

### 2.4 Test Data gợi ý

#### Đăng ký
```json
{
  "firstName": "Nguyen",
  "lastName": "Van A",
  "email": "nguyenvana@example.com",
  "password": "Test@123456",
  "confirmPassword": "Test@123456"
}
```

#### Đăng nhập (Customer)
```json
{
  "email": "nguyenvana@example.com",
  "password": "Test@123456"
}
```

#### Đăng nhập (Admin)
```json
{
  "email": "admin@shoes.com",
  "password": "Admin@123456"
}
```

#### Invalid
```json
{
  "firstName": "",
  "lastName": "",
  "email": "invalid-email",
  "password": "123",
  "confirmPassword": "456"
}
```

---

## 3. Luồng Khám phá & Duyệt sản phẩm

### 3.1 Trang chủ (Home Page)

#### 3.1.1 Mô tả luồng nghiệp vụ
1. Guest hoặc Customer truy cập trang chủ (`/`).
2. Backend trả về danh sách sản phẩm Active (mặc định sort by `CreatedAt` desc, page=1, pageSize=12).
3. Frontend hiển thị:
   - Banner carousel.
   - Danh sách danh mục (Brand/Category) - cần gọi API brands/categories riêng.
   - Grid sản phẩm nổi bật (lấy từ `GET /api/products`).
   - Mỗi product card hiển thị: ảnh chính, tên, giá gốc/giá sale, tên brand, rating (nếu có).

#### 3.1.2 Validation Rules
- Không có validation phức tạp. Chỉ filter `p.IsActive == true`.
- Nếu không có sản phẩm active → trả về mảng rỗng.

#### 3.1.3 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-HOME-01 | Xem trang chủ có sản phẩm | Có ít nhất 1 product active | Hiển thị grid SP, mỗi SP có đủ thông tin |
| HP-HOME-02 | Xem trang chủ không có sản phẩm | Chưa seed data | Grid rỗng, không lỗi |
| HP-HOME-03 | Product có SalePrice hiển thị giá sale | SP có `SalePrice < Price` | Hiển thị giá gạch ngang + giá sale |
| HP-HOME-04 | Product không có ảnh | SP chưa upload ảnh | Ảnh mặc định / placeholder |
| UV-HOME-01 | SP bị ẩn (IsActive=false) | SP có `IsActive=false` | Không xuất hiện trên trang chủ |

### 3.2 Trang danh mục / Bộ sưu tập (Collection Page)

#### 3.2.1 Mô tả luồng nghiệp vụ
1. User nhấn vào danh mục hoặc truy cập trang Shop (`/shop`, `/collection`).
2. Gọi API `GET /api/products` với các tham số:
   - `category`: lọc theo tên category (VD: `category=Sneaker`).
   - `categoryId`: lọc theo ID category.
   - `brandId`: lọc theo ID brand.
   - `q`: tìm kiếm theo từ khóa (không phân biệt hoa thường, search trên `Name`, `Brand.Name`, `Category.Name`).
   - `sortBy`: `"Price: low to high"`, `"Price: high to low"`, hoặc `null` (mặc định `CreatedAt desc`).
   - `page`: trang hiện tại (mặc định 1).
   - `pageSize`: số SP mỗi trang (mặc định 12).
3. Backend trả về `List<ProductPublicDto>`.
4. Frontend hiển thị:
   - Sidebar lọc: Category, Brand, Price range (nếu có).
   - Sort dropdown.
   - Search box.
   - Pagination.
   - Product grid.

#### 3.2.2 Validation Rules
| Tham số | Rule | Note |
|---------|------|------|
| `category` | Optional, string | Nếu `"All"` hoặc rỗng → bỏ lọc |
| `categoryId` | Optional, int? | Lọc chính xác theo ID |
| `brandId` | Optional, int? | Lọc chính xác theo ID |
| `q` | Optional, string | Không phân biệt hoa thường, search trên Name/Brand/Category |
| `sortBy` | Optional, string | Chấp nhận `"Price: low to high"`, `"Price: high to low"`, hoặc mặc định |
| `page` | Optional, int, default=1 | Phải >= 1 |
| `pageSize` | Optional, int, default=12 | Cố định 12 hoặc giá trị hợp lý |

#### 3.2.3 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-COLL-01 | Lấy danh sách SP mặc định | `GET /api/products` | 12 SP mới nhất, đúng cấu trúc `ProductPublicDto` |
| HP-COLL-02 | Lọc theo Category | `category=Sneaker` | Chỉ SP thuộc category "Sneaker" |
| HP-COLL-03 | Lọc theo CategoryId | `categoryId=1` | Chỉ SP thuộc category ID=1 |
| HP-COLL-04 | Lọc theo BrandId | `brandId=2` | Chỉ SP thuộc brand ID=2 |
| HP-COLL-05 | Tìm kiếm theo từ khóa | `q=nike` | SP có Name/Brand/Category chứa "nike" (case-insensitive) |
| HP-COLL-06 | Sort giá tăng dần | `sortBy=Price: low to high` | SP được sắp xếp theo `SalePrice ?? Price` ASC |
| HP-COLL-07 | Sort giá giảm dần | `sortBy=Price: high to low` | SP được sắp xếp theo `SalePrice ?? Price` DESC |
| HP-COLL-08 | Sort mặc định | Không có `sortBy` | SP sắp xếp theo `CreatedAt` DESC |
| HP-COLL-09 | Phân trang page 2 | `page=2&pageSize=12` | Trả về SP từ thứ 13 đến 24 |
| HP-COLL-10 | Kết hợp nhiều filter | `categoryId=1&brandId=2&q=nike&sortBy=Price: low to high` | Kết hợp đúng tất cả điều kiện |
| UV-COLL-01 | Category="All" | `category=All` | Không lọc category, trả về tất cả |
| UV-COLL-02 | Search không kết quả | `q=xyznonexistent` | Trả về mảng rỗng `[]` |
| UV-COLL-03 | Page âm | `page=-1` | Backend không validate, trả về kết quả lỗi hoặc rỗng |
| UV-COLL-04 | PageSize = 0 | `pageSize=0` | Backend không validate, trả về mảng rỗng |

### 3.3 Trang chi tiết sản phẩm (PDP)

#### 3.3.1 Mô tả luồng nghiệp vụ
1. User nhấn vào 1 product từ trang chủ/collection.
2. Gọi API `GET /api/products/{id}`.
3. Backend trả về `ProductDetailDto` nếu SP tồn tại và `IsActive = true`.
4. Frontend hiển thị:
   - Ảnh gallery (có ảnh chính `IsPrimary`).
   - Tên, mô tả, giá gốc/giá sale.
   - Brand, Category.
   - Danh sách biến thể (Size, StockQuantity, SKU, Price).
   - Reviews (nếu có): Rating trung bình, danh sách review.
   - **Sản phẩm liên quan**: Frontend tự gọi thêm `GET /api/products?categoryId={id}&pageSize=4` để hiển thị SP cùng category.

#### 3.3.2 Validation Rules
- SP phải tồn tại (`Product != null`).
- SP phải có `IsActive = true`.
- Nếu không thỏa → trả về `null` → `404 NotFound` - `"NotFound"`.

#### 3.3.3 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-PDP-01 | Xem chi tiết SP hợp lệ | `id=1` (SP active) | 200 - `ProductDetailDto` đầy đủ |
| HP-PDP-02 | Xem chi tiết SP có SalePrice | SP có `SalePrice` | Hiển thị giá sale, ẩn giá gốc hoặc gạch ngang |
| HP-PDP-03 | Xem chi tiết SP có nhiều biến thể | SP có nhiều Size | `Variants` đủ số size, mỗi size có StockQuantity |
| HP-PDP-04 | Xem chi tiết SP có Reviews | SP có >= 1 review | `Reviews` đầy đủ, `AverageRating` tính đúng |
| HP-PDP-05 | Xem chi tiết SP không có Review | SP chưa có review | `Reviews = []`, `AverageRating = 0` |
| HP-PDP-06 | Ảnh primary hiển thị đúng | SP có nhiều ảnh | Ảnh đầu tiên trong gallery có `IsPrimary = true` |
| HP-PDP-07 | Sản phẩm liên quan | PDP gọi `categoryId={current}` | Trả về SP cùng category, khác SP hiện tại |
| UV-PDP-01 | SP không tồn tại | `id=9999` | 404 - `"NotFound"` |
| UV-PDP-02 | SP bị ẩn (IsActive=false) | `id=1` nhưng `IsActive=false` | 404 - `"NotFound"` |
| UV-PDP-03 | SP không có ảnh | SP chưa upload ảnh | `Images = []`, `PrimaryImageUrl = ""` |
| UV-PDP-04 | SP không có biến thể | SP chưa tạo variant | `Variants = []`, `TotalStock = 0` |

### 3.4 Test Data gợi ý - Product

| Product | Brand | Category | Price | SalePrice | Stock | Status |
|---------|-------|----------|-------|-----------|-------|--------|
| Nike Air Max 90 | Nike | Sneaker | 2.500.000 | 2.000.000 | 10 | Active |
| Adidas Ultraboost | Adidas | Sneaker | 3.000.000 | null | 5 | Active |
| Puma RS-X | Puma | Running | 1.800.000 | 1.500.000 | 8 | Active |
| Nike Pegasus (ẩn) | Nike | Running | 2.200.000 | null | 3 | Inactive |

#### Variants gợi ý
| VariantId | ProductId | SizeId | SizeName | SKU | Stock | Price | SalePrice |
|-----------|-----------|--------|----------|-----|-------|-------|-----------|
| 1 | 1 | 1 | 40 | NK-AM90-40 | 5 | 2.500.000 | 2.000.000 |
| 2 | 1 | 2 | 42 | NK-AM90-42 | 5 | 2.500.000 | 2.000.000 |
| 3 | 2 | 3 | 41 | AD-UB-41 | 3 | 3.000.000 | null |
| 4 | 2 | 4 | 43 | AD-UB-43 | 2 | 3.000.000 | null |

---

## 4. Luồng Giỏ hàng & Thanh toán

### 4.1 Thêm sản phẩm vào giỏ hàng

#### 4.1.1 Mô tả luồng nghiệp vụ
1. Customer đang xem PDP, chọn Size + Số lượng.
2. Nhấn **"Thêm vào giỏ"**.
3. Gọi API `POST /api/cart/{userId}/add` với `AddToCartDto`.
4. Backend xử lý:
   - Validate `Quantity > 0`.
   - Kiểm tra `ProductVariant` tồn tại.
   - Kiểm tra tồn kho (`Quantity <= StockQuantity`).
   - GetOrCreate Cart cho user.
   - Nếu đã có trong giỏ → cộng dồn Quantity (kiểm tra tồn kho lại).
   - Nếu chưa có → tạo CartItem mới, lưu `UnitPrice = SalePrice ?? Price` tại thời điểm thêm.
   - Cập nhật `cart.UpdatedAt`.
5. Trả về `CartDto` mới nhất.

#### 4.1.2 Validation Rules
| Field | Rule | Lỗi |
|-------|------|-----|
| `ProductVariantId` | Phải tồn tại trong DB | `404 - "Không tìm thấy biến thể sản phẩm"` |
| `Quantity` | Phải > 0 | `400 - "Số lượng phải lớn hơn 0"` |
| `Quantity` | Phải <= `variant.StockQuantity` | `400 - "Số lượng vượt quá tồn kho hiện có"` |
| `Quantity` + existing | Tổng phải <= `variant.StockQuantity` | `400 - "Số lượng vượt quá tồn kho hiện có"` |

#### 4.1.3 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-CART-01 | Thêm SP mới vào giỏ | Variant tồn tại, Qty=1 | Cart có 1 item, TotalItems=1 |
| HP-CART-02 | Thêm SP đã có trong giỏ | Cộng dồn Qty | Qty tăng lên, TotalItems tăng |
| HP-CART-03 | Thêm SP có SalePrice | Variant có `SalePrice` | `UnitPrice = SalePrice` |
| HP-CART-04 | Thêm SP không có SalePrice | Variant có `Price` | `UnitPrice = Price` |
| UV-CART-01 | Thêm với Qty=0 | `Quantity = 0` | 400 - `"Số lượng phải lớn hơn 0"` |
| UV-CART-02 | Thêm với Qty âm | `Quantity = -1` | 400 - `"Số lượng phải lớn hơn 0"` |
| UV-CART-03 | Thêm Variant không tồn tại | `ProductVariantId = 9999` | 404 - `"Không tìm thấy biến thể sản phẩm"` |
| UV-CART-04 | Thêm vượt tồn kho | `StockQuantity = 3`, `Quantity = 5` | 400 - `"Số lượng vượt quá tồn kho"` |
| UV-CART-05 | Cộng dồn vượt tồn kho | Đã có Qty=2, thêm Qty=3, Stock=4 | 400 - `"Số lượng vượt quá tồn kho"` |

### 4.2 Xem giỏ hàng

#### 4.2.1 Mô tả luồng nghiệp vụ
1. Customer nhấn icon giỏ hàng.
2. Gọi API `GET /api/cart/{userId}`.
3. Backend:
   - Nếu chưa có Cart → trả về `CartDto` rỗng (`Id=0`, `TotalItems=0`, `TotalPrice=0`).
   - Nếu có Cart → build `CartDto` với đầy đủ thông tin item (tên SP, size, brand, ảnh, giá, thành tiền).
4. Frontend hiển thị:
   - Badge số lượng trên icon giỏ = `TotalItems`.
   - Danh sách item: ảnh, tên, size, brand, đơn giá, số lượng (có nút +/-), thành tiền, nút xóa.
   - Tổng tiền = `TotalPrice`.
   - Nút **"Thanh toán"** → chuyển sang trang Checkout.

#### 4.2.2 Validation Rules
- Không có validation đặc biệt. Trả về giỏ rỗng nếu user chưa có Cart.

#### 4.2.3 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-CART-05 | Xem giỏ có sản phẩm | Giỏ có 2 item | Trả về đủ thông tin 2 item, TotalItems=2, TotalPrice chính xác |
| HP-CART-06 | Xem giỏ rỗng (chưa có Cart) | User mới, chưa thêm SP | `Id=0`, `TotalItems=0`, `TotalPrice=0`, `Items=[]` |
| HP-CART-07 | Xem giỏ rỗng (đã xóa hết) | Giỏ có trước, xóa hết | `TotalItems=0`, `TotalPrice=0`, `Items=[]` |
| UV-CART-06 | UserId không tồn tại | `userId=9999` | Trả về giỏ rỗng (không lỗi, vì `FirstOrDefaultAsync` trả null) |

### 4.3 Cập nhật số lượng & Xóa sản phẩm

#### 4.3.1 Cập nhật số lượng (`PUT /api/cart/{userId}/items/{cartItemId}`)
1. Gửi `quantity` (int) trong body.
2. Backend:
   - Tìm Cart của user.
   - Tìm CartItem thuộc Cart đó.
   - Nếu `quantity <= 0` → xóa CartItem.
   - Kiểm tra `quantity <= variant.StockQuantity`.
   - Cập nhật `cartItem.Quantity = quantity`.
3. Trả về `CartDto` mới.

#### 4.3.2 Xóa sản phẩm (`DELETE /api/cart/{userId}/items/{cartItemId}`)
1. Tìm Cart + CartItem.
2. Xóa CartItem khỏi DB.
3. Trả về `{}` + message success.

#### 4.3.3 Xóa toàn bộ (`DELETE /api/cart/{userId}/clear`)
1. Xóa tất cả CartItem của Cart.
2. Cập nhật `cart.UpdatedAt`.

#### 4.3.4 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-CART-08 | Tăng số lượng | `quantity = 3` (đã có 1) | Item Qty=3, TotalPrice cập nhật |
| HP-CART-09 | Giảm số lượng | `quantity = 1` (đã có 3) | Item Qty=1, TotalPrice cập nhật |
| HP-CART-10 | Cập nhật Qty=0 (xóa) | `quantity = 0` | Item bị xóa khỏi giỏ |
| HP-CART-11 | Xóa 1 item | Gọi DELETE | Item biến mất, TotalItems giảm |
| HP-CART-12 | Xóa toàn bộ giỏ | Gọi CLEAR | `Items=[]`, `TotalItems=0`, `TotalPrice=0` |
| UV-CART-07 | Cập nhật Qty vượt tồn kho | `quantity = 10`, Stock=3 | 400 - `"Số lượng vượt quá tồn kho"` |
| UV-CART-08 | Cập nhật item không thuộc user | `cartItemId` của user khác | 404 - `"Không tìm thấy sản phẩm trong giỏ hàng"` |
| UV-CART-09 | Xóa item không tồn tại | `cartItemId = 9999` | 404 - `"Không tìm thấy sản phẩm trong giỏ hàng"` |
| UV-CART-10 | Xóa khi chưa có Cart | User chưa có Cart | 404 - `"Không tìm thấy giỏ hàng"` |

### 4.4 Thanh toán / Checkout

#### 4.4.1 Mô tả luồng nghiệp vụ
1. Customer ở trang Cart, nhấn **"Thanh toán"**.
2. Frontend hiển thị form Checkout:
   - **Địa chỉ giao hàng**:
     - Option A: Chọn từ danh sách địa chỉ đã lưu (`AddressId`).
     - Option B: Nhập địa chỉ mới (`FirstName`, `LastName`, `PhoneNumber`, `Address`, `City`, `State`, `Country`, `PostalCode`).
   - **Phương thức thanh toán**: Dropdown chọn `COD`, `CreditCard`, `BankTransfer`.
3. Nhấn **"Đặt hàng"** → gọi `POST /api/orders/{userId}` với `CheckoutDto`.
4. Backend xử lý (trong 1 transaction):
   - Kiểm tra giỏ không rỗng.
   - Xử lý địa chỉ (kiểm tra `AddressId` thuộc user HOẶC tạo mới từ form).
   - Validate địa chỉ mới: `RecipientName`, `Street`, `City`, `Country` bắt buộc.
   - Kiểm tra tồn kho đủ cho từng item trong giỏ.
   - Tạo `Order` (Status=`Pending`, PaymentStatus=`Unpaid`).
   - Tạo `OrderItem` cho từng CartItem (snapshot giá).
   - Trừ tồn kho.
   - Tạo `Payment` (COD → `Pending`, các hình thức khác → `Paid`).
   - Xóa toàn bộ CartItem.
   - Commit transaction.
5. Trả về `OrderDto` chi tiết đơn hàng.
6. Frontend chuyển sang trang **Thank You / Order Confirmation**.

#### 4.4.2 Validation Rules

| Field | Rule | Lỗi |
|-------|------|-----|
| Cart | Không được rỗng | `400 - "Giỏ hàng của bạn đang trống"` |
| AddressId | Nếu có, phải thuộc về user | `404 - "Không tìm thấy địa chỉ giao hàng"` |
| RecipientName | Bắt buộc (khi tạo địa chỉ mới) | `400 - "Vui lòng nhập tên người nhận"` |
| Street | Bắt buộc | `400 - "Vui lòng nhập địa chỉ cụ thể"` |
| City | Bắt buộc | `400 - "Vui lòng nhập thành phố"` |
| Country | Bắt buộc | `400 - "Vui lòng nhập quốc gia"` |
| PhoneNumber | Không bắt buộc; rỗng → auto `"0000000000"` | - |
| ProductVariant | Mỗi CartItem phải có Variant hợp lệ | `400 - "Một sản phẩm trong giỏ hàng không tồn tại"` |
| StockQuantity | `cartItem.Quantity <= variant.StockQuantity` | `400 - "Sản phẩm ... không đủ hàng. Còn X trong kho."` |

#### 4.4.3 Logic đặc biệt
- **Mapping địa chỉ**: `RecipientName = FirstName + " " + LastName` (nếu không gửi sẵn `RecipientName`).
- **Mapping Street**: `Street = Address` (trường đơn giản từ frontend).
- **PaymentMethod**: ưu tiên `Payment` (từ frontend) > `PaymentMethod` > mặc định `"COD"`.
- **Phí giao hàng**: Cố định `30.000đ`.
- **Mã đơn hàng**: `ORD-{yyyyMMddHHmmss}-{xxx}` (3 số ngẫu nhiên).
- **Tổng tiền**: `TotalAmount = Subtotal + 30000`.

#### 4.4.4 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-CHECK-01 | Đặt hàng COD + địa chỉ mới | Nhập đầy đủ, PaymentMethod=COD | 201 - Order `Pending`/`Unpaid`, Payment `Pending`, Cart rỗng |
| HP-CHECK-02 | Đặt hàng COD + địa chỉ đã lưu | Chọn `AddressId` có sẵn | 201 - Order `Pending`/`Unpaid`, Payment `Pending` |
| HP-CHECK-03 | Đặt hàng CreditCard | `PaymentMethod = "CreditCard"` | 201 - Order `Pending`, Payment `Paid` |
| HP-CHECK-04 | Đặt hàng BankTransfer | `PaymentMethod = "BankTransfer"` | 201 - Order `Pending`, Payment `Paid` |
| HP-CHECK-05 | Payment từ frontend | Gửi `Payment: "card"` | `PaymentMethod = "card"` |
| HP-CHECK-06 | Đặt SP có SalePrice | Variant có `SalePrice` | `Subtotal = SalePrice * Qty` |
| HP-CHECK-07 | Đặt nhiều loại SP | Giỏ có nhiều variant | `OrderItem` đủ số loại, tồn kho trừ đúng |
| HP-CHECK-08 | FirstName+LastName → RecipientName | Gửi `FirstName`, `LastName`, không gửi `RecipientName` | `RecipientName = "FirstName LastName"` |
| HP-CHECK-09 | PhoneNumber rỗng → auto 0000000000 | Không gửi `PhoneNumber` | `PhoneNumber = "0000000000"` |
| HP-CHECK-10 | Tồn kho đủ | `StockQuantity >= Qty` | Đặt thành công, `StockQuantity` giảm đúng |
| UV-CHECK-01 | Giỏ hàng rỗng | Không có CartItem | 400 - `"Giỏ hàng của bạn đang trống"` |
| UV-CHECK-02 | Thiếu RecipientName | `AddressId=null`, `RecipientName=""` | 400 - `"Vui lòng nhập tên người nhận"` |
| UV-CHECK-03 | Thiếu Street | `Street=""` | 400 - `"Vui lòng nhập địa chỉ cụ thể"` |
| UV-CHECK-04 | Thiếu City | `City=""` | 400 - `"Vui lòng nhập thành phố"` |
| UV-CHECK-05 | Thiếu Country | `Country=""` | 400 - `"Vui lòng nhập quốc gia"` |
| UV-CHECK-06 | AddressId không thuộc user | `AddressId = 999` (của user khác) | 404 - `"Không tìm thấy địa chỉ giao hàng"` |
| UV-CHECK-07 | AddressId không tồn tại | `AddressId = 0` | 404 - `"Không tìm thấy địa chỉ giao hàng"` |
| UV-CHECK-08 | Variant bị xóa khỏi DB | Xóa Variant trước khi đặt | 400 - `"Một sản phẩm trong giỏ hàng không tồn tại"` |
| UV-CHECK-09 | Số lượng đặt vượt tồn kho | `StockQuantity=1`, đặt `Qty=5` | 400 - `"Sản phẩm ... không đủ hàng. Còn 1 trong kho."` |
| UV-CHECK-10 | Đặt hàng khi chưa đăng nhập | Không có JWT / userId=0 | 401 Unauthorized |

### 4.5 Test Data gợi ý - Checkout

#### Valid - Địa chỉ mới
```json
{
  "addressId": null,
  "firstName": "Nguyen",
  "lastName": "Van A",
  "phoneNumber": "0909123456",
  "address": "123 Nguyen Trai",
  "city": "Ho Chi Minh",
  "state": "District 1",
  "country": "Vietnam",
  "postalCode": "700000",
  "paymentMethod": "COD",
  "payment": null
}
```

#### Valid - Địa chỉ đã lưu
```json
{
  "addressId": 1,
  "paymentMethod": "CreditCard"
}
```

#### Invalid - Thiếu thông tin
```json
{
  "addressId": null,
  "firstName": "",
  "lastName": "",
  "phoneNumber": "",
  "address": "",
  "city": "",
  "state": "",
  "country": "",
  "postalCode": "",
  "paymentMethod": "COD"
}
```

#### Expected Response OrderDto (sau khi đặt hàng)
```json
{
  "id": 1,
  "orderNumber": "ORD-20260902143000-123",
  "status": "Pending",
  "paymentStatus": "Unpaid",
  "subtotal": 4000000,
  "shippingFee": 30000,
  "totalAmount": 4030000,
  "createdAt": "2026-09-02T07:30:00Z",
  "address": {
    "recipientName": "Nguyen Van A",
    "phoneNumber": "0909123456",
    "street": "123 Nguyen Trai",
    "city": "Ho Chi Minh",
    "state": "District 1",
    "postalCode": "700000",
    "country": "Vietnam",
    "email": null
  },
  "items": [
    {
      "productId": 1,
      "productVariantId": 1,
      "productName": "Nike Air Max",
      "sizeName": "42",
      "color": "Do",
      "imageUrl": "https://...",
      "quantity": 2,
      "unitPrice": 2000000,
      "totalPrice": 4000000
    }
  ]
}
```

---

## 5. Luồng Sau thanh toán & Quản lý cá nhân

### 5.1 Trang Cảm ơn / Xác nhận đơn hàng (Thank You Page)

#### 5.1.1 Mô tả luồng nghiệc vụ
1. Sau khi đặt hàng thành công, frontend chuyển sang trang Thank You với `orderId` trên URL.
2. Gọi `GET /api/orders/{orderId}/user/{userId}` để lấy chi tiết đơn.
3. Hiển thị:
   - Mã đơn hàng (`OrderNumber`).
   - Tổng tiền, phí ship, phương thức thanh toán.
   - Địa chỉ giao hàng.
   - Danh sách sản phẩm đã đặt.
   - Nút **"Xem đơn hàng"** → `/my-orders`.
   - Nút **"Tiếp tục mua sắm"** → `/`.

### 5.2 Trang Lịch sử đơn hàng (My Orders)

#### 5.2.1 Mô tả luồng nghiệc vụ
1. Customer truy cập `/my-orders`.
2. Gọi `GET /api/orders/user/{userId}`.
3. Backend trả về `List<OrderDto>` (sort by `CreatedAt` desc).
4. Frontend hiển thị danh sách đơn hàng dạng card/list:
   - OrderNumber, ngày tạo, tổng tiền, trạng thái (Status + PaymentStatus).
   - Badge màu sắc: Pending (vàng), Completed (xanh), Cancelled (đỏ).
5. User có thể nhấn vào từng đơn để xem chi tiết.

#### 5.2.2 Validation Rules
- Chỉ trả về đơn hàng thuộc về user đó (`o.UserId == userId`).
- Nếu user chưa có đơn nào → trả về `[]`.

#### 5.2.3 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-POST-01 | Xem danh sách đơn có dữ liệu | User đã đặt >= 1 đơn | Trả về list đúng số đơn, sort by `CreatedAt` desc |
| HP-POST-02 | Xem chi tiết đơn | `GET /api/orders/{id}/user/{userId}` | Trả về `OrderDto` đầy đủ Address + Items |
| HP-POST-03 | Xem đơn hàng rỗng | User mới, chưa đặt | `[]` |
| UV-POST-01 | Xem đơn không tồn tại | `orderId = 9999` | 404 - `"Không tìm thấy đơn hàng"` |
| UV-POST-02 | Xem đơn của người khác | User A lấy Order của User B | 404 - `"Không tìm thấy đơn hàng"` |

### 5.3 Hủy đơn hàng

#### 5.3.1 Mô tả luồng nghiệp vụ
1. Customer ở trang My Orders hoặc Order Detail, nhấn **"Hủy đơn"** (chỉ hiển thị khi Status cho phép).
2. Gọi `POST /api/orders/{orderId}/cancel/user/{userId}`.
3. Backend xử lý (trong transaction):
   - Tìm Order thuộc user.
   - Kiểm tra `Status != Shipped/Completed/Cancelled`.
   - Hoàn lại tồn kho cho từng OrderItem.
   - Cập nhật `Status = "Cancelled"`.
   - Nếu `PaymentStatus == "Paid"` → `PaymentStatus = "Refunded"`.
4. Trả về `OrderDto` đã hủy.

#### 5.3.2 Validation Rules
| Điều kiện | Lỗi |
|-----------|-----|
| Order không tồn tại hoặc không thuộc user | 404 - `"Không tìm thấy đơn hàng"` |
| `Status == "Shipped"` | 400 - `"Đơn hàng này không thể hủy do đã giao hoặc đã kết thúc"` |
| `Status == "Completed"` | 400 - `"Đơn hàng này không thể hủy do đã giao hoặc đã kết thúc"` |
| `Status == "Cancelled"` | 400 - `"Đơn hàng này không thể hủy do đã giao hoặc đã kết thúc"` |

#### 5.3.3 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-CANCEL-01 | Hủy đơn Pending | Status=`Pending` | 200 - Status=`Cancelled`, tồn kho hoàn lại |
| HP-CANCEL-02 | Hủy đơn Processing | Status=`Processing` | 200 - Status=`Cancelled`, tồn kho hoàn lại |
| HP-CANCEL-03 | Hủy đơn đã thanh toán | Status=`Pending`, PaymentStatus=`Paid` | 200 - Status=`Cancelled`, PaymentStatus=`Refunded` |
| UV-CANCEL-01 | Hủy đơn Shipped | Status=`Shipped` | 400 - không thể hủy |
| UV-CANCEL-02 | Hủy đơn Completed | Status=`Completed` | 400 - không thể hủy |
| UV-CANCEL-03 | Hủy đơn đã hủy | Status=`Cancelled` | 400 - không thể hủy |
| UV-CANCEL-04 | Hủy đơn của người khác | User A hủy Order của User B | 404 - `"Không tìm thấy đơn hàng"` |
| UV-CANCEL-05 | Hủy đơn không tồn tại | `orderId = 9999` | 404 - `"Không tìm thấy đơn hàng"` |

---

## 6. Luồng Quản trị (Admin Panel)

> **Lưu ý**: Tất cả API Admin đều yêu cầu Role = `Admin` (`[Authorize(Roles = "Admin")]`).
> Nếu Customer cố truy cập → `403 Forbidden`.

### 6.1 Admin Dashboard

#### 6.1.1 API
- `GET /api/admin/dashboard/stats`

#### 6.1.2 Response (`DashboardStatsDto`)
```json
{
  "totalOrders": 100,
  "totalProducts": 50,
  "totalUsers": 200,
  "totalRevenue": 150000000,
  "pendingOrders": 5,
  "recentOrders": [
    {
      "id": 1,
      "orderNumber": "ORD-20260902143000-123",
      "customerName": "Nguyen Van A",
      "totalAmount": 4030000,
      "status": "Pending",
      "createdAt": "2026-09-02T07:30:00Z"
    }
  ]
}
```

#### 6.1.3 Logic
- `TotalRevenue` = tổng `Amount` của các Payment có `Status = "Paid"`.
- `PendingOrders` = số Order có `Status = "Pending"`.
- `RecentOrders` = 5 đơn gần nhất, sort by `CreatedAt` desc.

#### 6.1.4 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-ADM-01 | Xem dashboard có data | Có orders, products, users | Trả về đầy đủ stats |
| HP-ADM-02 | Xem dashboard không có data | DB trống | Tất cả field = 0, `recentOrders=[]` |
| UV-ADM-01 | Customer truy cập dashboard | Token của Customer | 403 Forbidden |
| UV-ADM-02 | Chưa đăng nhập | Không có token | 401 Unauthorized |

### 6.2 Admin Orders

#### 6.2.1 Danh sách đơn hàng (`GET /api/admin/orders`)
- Query param: `status` (optional, VD: `?status=Pending`).
- Trả về `List<AdminOrderDto>`.

#### 6.2.2 Chi tiết đơn hàng (`GET /api/admin/orders/{id}`)
- Trả về `AdminOrderDetailDto` bao gồm:
  - Thông tin đơn cơ bản.
  - `ShippingAddress` (AddressDto).
  - `Items` (AdminOrderItemDto).
  - `Payment` (PaymentInfoDto).

#### 6.2.3 Cập nhật trạng thái (`PUT /api/admin/orders/{id}/status`)
- Body: `{ "status": "Processing" }`.
- Validation: `Status` không được rỗng.
- Logic:
  - Nếu `Status` hiện tại là `Shipped`, `Completed`, hoặc `Cancelled` → throw `ValidationError`.
  - Cập nhật `order.Status = dto.Status`.
- Trả về `AdminOrderDetailDto` đã cập nhật.

#### 6.2.4 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-ADM-03 | Xem danh sách đơn | Có nhiều đơn | Trả về đủ `AdminOrderDto` |
| HP-ADM-04 | Lọc đơn theo Status | `?status=Pending` | Chỉ đơn có Status="Pending" |
| HP-ADM-05 | Xem chi tiết đơn | `id=1` | Trả về đủ thông tin + Items + Payment |
| HP-ADM-06 | Cập nhật Pending → Processing | `PUT /api/admin/orders/1/status` body `{"status":"Processing"}` | 200 - Status đổi thành Processing |
| HP-ADM-07 | Cập nhật Processing → Shipped | `{"status":"Shipped"}` | 200 - Status đổi thành Shipped |
| HP-ADM-08 | Cập nhật Shipped → Completed | `{"status":"Completed"}` | 200 - Status đổi thành Completed |
| HP-ADM-09 | Cập nhật trạng thái rỗng | `{"status":""}` | 400 - `"Trạng thái đơn hàng là bắt buộc."` |
| UV-ADM-03 | Cập nhật Shipped → Pending | `{"status":"Pending"}` | 400 - `"Không thể thay đổi trạng thái..."` |
| UV-ADM-04 | Cập nhật Completed → Processing | `{"status":"Processing"}` | 400 - `"Không thể thay đổi trạng thái..."` |
| UV-ADM-05 | Cập nhật Cancelled → Pending | `{"status":"Pending"}` | 400 - `"Không thể thay đổi trạng thái..."` |
| UV-ADM-06 | Cập nhật đơn không tồn tại | `id=9999` | 404 - `"Không tìm thấy đơn hàng"` |
| UV-ADM-07 | Customer cập nhật đơn | Token Customer | 403 Forbidden |
| UV-ADM-08 | Chưa đăng nhập | Không có token | 401 Unauthorized |

### 6.3 Admin Products

#### 6.3.1 CRUD Sản phẩm
| Endpoint | Method | Mô tả |
|----------|--------|--------|
| `/api/admin/products` | GET | Danh sách tất cả SP (kể cả inactive) |
| `/api/admin/products/{id}` | GET | Chi tiết SP |
| `/api/admin/products` | POST | Tạo SP mới |
| `/api/admin/products/{id}` | PUT | Cập nhật SP |
| `/api/admin/products/{id}` | DELETE | Xóa SP |

#### 6.3.2 Validation - CreateProductDto / ProductDto
| Field | Rule | Lỗi |
|-------|------|-----|
| `Name` | Required, không được rỗng | `"Tên sản phẩm là bắt buộc."` |
| `Price` | >= 0 | `"Giá sản phẩm không hợp lệ."` |
| `Slug` | Optional; nếu rỗng → auto generate từ Name | `name.ToLower().Replace(" ", "-")` |
| `BrandId` | Required | EF Core constraint |
| `CategoryId` | Required | EF Core constraint |

#### 6.3.3 Biến thể (Variants)
| Endpoint | Method | Mô tả |
|----------|--------|--------|
| `/api/admin/products/{productId}/variants` | GET | Danh sách variant của SP |
| `/api/admin/products/{productId}/variants` | POST | Tạo variant mới |
| `/api/admin/products/variants/{variantId}` | PUT | Cập nhật variant |
| `/api/admin/products/variants/{variantId}` | DELETE | Xóa variant |

#### 6.3.4 Ảnh sản phẩm
| Endpoint | Method | Mô tả |
|----------|--------|--------|
| `/api/admin/products/{productId}/images` | GET | Danh sách ảnh |
| `/api/admin/products/{productId}/images` | POST | Upload ảnh (multipart/form-data) |
| `/api/admin/products/images/{imageId}` | DELETE | Xóa ảnh |
| `/api/admin/products/images/{imageId}/primary` | PUT | Đặt ảnh làm chính |

#### 6.3.5 Validation - Upload ảnh
| Điều kiện | Lỗi |
|-----------|-----|
| `file == null` hoặc `file.Length == 0` | `"File ảnh không hợp lệ."` |
| Extension không phải `.jpg`, `.jpeg`, `.png`, `.webp` | `"Định dạng ảnh không hợp lệ. Chỉ cho phép JPG, JPEG, PNG, WEBP."` |
| `file.Length > 5MB` | `"Kích thước ảnh không được vượt quá 5MB."` |

#### 6.3.6 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-ADM-10 | Tạo SP thành công | DTO hợp lệ | 201 - `ProductDto` |
| HP-ADM-11 | Cập nhật SP thành công | `PUT` với data mới | 200 - `ProductDto` cập nhật |
| HP-ADM-12 | Xóa SP thành công | `DELETE /api/admin/products/{id}` | 200 - `"Xóa sản phẩm thành công"` |
| HP-ADM-13 | Tạo variant thành công | `POST /variants` | 201 - `ProductVariantDto` |
| HP-ADM-14 | Cập nhật stock variant | `PUT /variants/{id}` với `StockQuantity=100` | 200 - Stock cập nhật |
| HP-ADM-15 | Upload ảnh hợp lệ | File `.jpg` < 5MB | 201 - `ProductImageDto` |
| HP-ADM-16 | Đặt ảnh primary | `PUT /images/{id}/primary` | 200 - Ảnh đó `IsPrimary=true`, các ảnh khác `false` |
| UV-ADM-09 | Tạo SP thiếu Name | `Name=""` | 400 - `"Tên sản phẩm là bắt buộc."` |
| UV-ADM-10 | Tạo SP giá âm | `Price = -1` | 400 - `"Giá sản phẩm không hợp lệ."` |
| UV-ADM-11 | Upload ảnh sai định dạng | File `.gif` | 400 - `"Định dạng ảnh không hợp lệ..."` |
| UV-ADM-12 | Upload ảnh quá 5MB | File 6MB | 400 - `"Kích thước ảnh không được vượt quá 5MB."` |
| UV-ADM-13 | Upload ảnh SP không tồn tại | `productId = 9999` | 404 - `"Không tìm thấy sản phẩm."` |
| UV-ADM-14 | Xóa variant không tồn tại | `variantId = 9999` | 404 - `"Không tìm thấy biến thể"` |

### 6.4 Admin Brands & Categories

#### 6.4.1 Brands (`/api/admin/brands`)
| Endpoint | Method |
|----------|--------|
| `/api/admin/brands` | GET |
| `/api/admin/brands/{id}` | GET |
| `/api/admin/brands` | POST |
| `/api/admin/brands/{id}` | PUT |
| `/api/admin/brands/{id}` | DELETE |

#### 6.4.2 Categories (`/api/admin/categories`)
| Endpoint | Method |
|----------|--------|
| `/api/admin/categories` | GET |
| `/api/admin/categories/{id}` | GET |
| `/api/admin/categories` | POST |
| `/api/admin/categories/{id}` | PUT |
| `/api/admin/categories/{id}` | DELETE |

#### 6.4.3 Test Scenario
| ID | Tên Scenario | Mô tả | Kết quả mong đợi |
|----|-------------|--------|-----------------|
| HP-ADM-17 | Tạo Brand | `POST /api/admin/brands` body `{"name":"Nike"}` | 201 - BrandDto |
| HP-ADM-18 | Cập nhật Brand | `PUT /api/admin/brands/1` | 200 - BrandDto cập nhật |
| HP-ADM-19 | Xóa Brand | `DELETE /api/admin/brands/1` | 200 - `"Xóa thương hiệu thành công"` |
| HP-ADM-20 | Tạo Category | `POST /api/admin/categories` body `{"name":"Sneaker", "slug":"sneaker"}` | 201 - CategoryDto |
| HP-ADM-21 | Cập nhật Category | `PUT /api/admin/categories/1` | 200 - CategoryDto cập nhật |
| HP-ADM-22 | Xóa Category | `DELETE /api/admin/categories/1` | 200 - `"Xóa danh mục thành công"` |
| UV-ADM-15 | Brand không tồn tại | `GET /api/admin/brands/9999` | 404 - `"Không tìm thấy thương hiệu"` |
| UV-ADM-16 | Category không tồn tại | `GET /api/admin/categories/9999` | 404 - `"Không tìm thấy danh mục"` |
| UV-ADM-17 | Customer truy cập Brand API | Token Customer | 403 Forbidden |
| UV-ADM-18 | Customer truy cập Category API | Token Customer | 403 Forbidden |

---

## 7. Tổng hợp API Endpoints

### 7.1 Public APIs (Không cần auth)
| Method | Endpoint | Mô tả |
|--------|----------|--------|
| POST | `/api/auth/register` | Đăng ký |
| POST | `/api/auth/login` | Đăng nhập |
| GET | `/api/products` | Lấy danh sách SP public (có filter, sort, phân trang) |
| GET | `/api/products/{id}` | Chi tiết SP |

### 7.2 Customer APIs (Cần JWT, Role = Customer)
| Method | Endpoint | Mô tả |
|--------|----------|--------|
| GET | `/api/cart/{userId}` | Lấy giỏ hàng |
| POST | `/api/cart/{userId}/add` | Thêm SP vào giỏ |
| PUT | `/api/cart/{userId}/items/{cartItemId}` | Cập nhật số lượng |
| DELETE | `/api/cart/{userId}/items/{cartItemId}` | Xóa 1 item khỏi giỏ |
| DELETE | `/api/cart/{userId}/clear` | Xóa toàn bộ giỏ |
| POST | `/api/orders/{userId}` | Tạo đơn hàng (Checkout) |
| GET | `/api/orders/{orderId}/user/{userId}` | Chi tiết đơn hàng |
| GET | `/api/orders/user/{userId}` | Danh sách đơn hàng của user |
| POST | `/api/orders/{orderId}/cancel/user/{userId}` | Hủy đơn hàng |

### 7.3 Admin APIs (Cần JWT, Role = Admin)
| Method | Endpoint | Mô tả |
|--------|----------|--------|
| GET | `/api/admin/dashboard/stats` | Thống kê dashboard |
| GET | `/api/admin/orders` | Danh sách đơn hàng (có filter status) |
| GET | `/api/admin/orders/{id}` | Chi tiết đơn hàng (admin view) |
| PUT | `/api/admin/orders/{id}/status` | Cập nhật trạng thái đơn |
| GET | `/api/admin/products` | Danh sách sản phẩm (admin) |
| GET | `/api/admin/products/{id}` | Chi tiết SP (admin) |
| POST | `/api/admin/products` | Tạo SP mới |
| PUT | `/api/admin/products/{id}` | Cập nhật SP |
| DELETE | `/api/admin/products/{id}` | Xóa SP |
| GET | `/api/admin/products/{productId}/variants` | Danh sách variant |
| POST | `/api/admin/products/{productId}/variants` | Tạo variant |
| PUT | `/api/admin/products/variants/{variantId}` | Cập nhật variant |
| DELETE | `/api/admin/products/variants/{variantId}` | Xóa variant |
| GET | `/api/admin/products/{productId}/images` | Danh sách ảnh |
| POST | `/api/admin/products/{productId}/images` | Upload ảnh |
| DELETE | `/api/admin/products/images/{imageId}` | Xóa ảnh |
| PUT | `/api/admin/products/images/{imageId}/primary` | Đặt ảnh chính |
| GET | `/api/admin/brands` | Danh sách brand |
| GET | `/api/admin/brands/{id}` | Chi tiết brand |
| POST | `/api/admin/brands` | Tạo brand |
| PUT | `/api/admin/brands/{id}` | Cập nhật brand |
| DELETE | `/api/admin/brands/{id}` | Xóa brand |
| GET | `/api/admin/categories` | Danh sách category |
| GET | `/api/admin/categories/{id}` | Chi tiết category |
| POST | `/api/admin/categories` | Tạo category |
| PUT | `/api/admin/categories/{id}` | Cập nhật category |
| DELETE | `/api/admin/categories/{id}` | Xóa category |

---

## 8. Test Data gợi ý tổng hợp

### 8.1 Người dùng (Users)

#### Customer
| Field | Value |
|-------|-------|
| Email | `nguyenvana@example.com` |
| Password | `Test@123456` |
| FirstName | `Nguyen` |
| LastName | `Van A` |
| Role | `Customer` |
| IsActive | `true` |

#### Admin
| Field | Value |
|-------|-------|
| Email | `admin@shoes.com` |
| Password | `Admin@123456` |
| FirstName | `Admin` |
| LastName | `User` |
| Role | `Admin` |
| IsActive | `true` |

### 8.2 Sản phẩm (Products)
| Id | Name | Brand | Category | Price | SalePrice | IsActive |
|----|------|-------|----------|-------|-----------|----------|
| 1 | Nike Air Max 90 | Nike | Sneaker | 2500000 | 2000000 | true |
| 2 | Adidas Ultraboost | Adidas | Sneaker | 3000000 | null | true |
| 3 | Puma RS-X | Puma | Running | 1800000 | 1500000 | true |
| 4 | Nike Pegasus (ẩn) | Nike | Running | 2200000 | null | false |

### 8.3 Biến thể (Variants)
| Id | ProductId | SizeId | SizeName | SKU | Stock | Price | SalePrice |
|----|-----------|--------|----------|-----|-------|-------|-----------|
| 1 | 1 | 1 | 40 | NK-AM90-40 | 5 | 2500000 | 2000000 |
| 2 | 1 | 2 | 42 | NK-AM90-42 | 5 | 2500000 | 2000000 |
| 3 | 2 | 3 | 41 | AD-UB-41 | 3 | 3000000 | null |
| 4 | 2 | 4 | 43 | AD-UB-43 | 2 | 3000000 | null |
| 5 | 3 | 5 | 39 | PM-RSX-39 | 4 | 1800000 | 1500000 |

### 8.4 Địa chỉ (Addresses)
| Id | UserId | RecipientName | PhoneNumber | Street | City | State | PostalCode | Country | IsDefault |
|----|--------|---------------|-------------|--------|------|-------|------------|---------|-----------|
| 1 | {customerId} | Nguyen Van A | 0909123456 | 123 Nguyen Trai | Ho Chi Minh | District 1 | 700000 | Vietnam | true |
| 2 | {customerId} | Nguyen Van B | 0912345678 | 456 Le Loi | Ha Noi | Ba Dinh | 100000 | Vietnam | false |

### 8.5 Đơn hàng (Orders)
| Id | OrderNumber | UserId | Status | PaymentStatus | Subtotal | ShippingFee | TotalAmount |
|----|-------------|--------|--------|---------------|----------|-------------|-------------|
| 1 | ORD-20260902143000-123 | {customerId} | Pending | Unpaid | 4000000 | 30000 | 4030000 |
| 2 | ORD-20260901120000-456 | {customerId} | Completed | Paid | 3000000 | 30000 | 3030000 |
| 3 | ORD-20260830100000-789 | {customerId} | Shipped | Paid | 1500000 | 30000 | 1530000 |

---

## 9. Ghi chú kỹ thuật cho Selenium

### 9.1 Cấu trúc Test
```
Test Suite: Shoes Web E2E
├── AuthTests
│   ├── Register (valid, invalid, duplicate email)
│   ├── Login (valid customer, valid admin, invalid, locked account)
│   └── Logout + Redirect
├── ProductTests
│   ├── HomePage (load products, banners)
│   ├── CollectionPage (filter, sort, search, pagination)
│   └── ProductDetail (view detail, related products)
├── CartTests
│   ├── AddToCart (valid, out of stock, invalid variant)
│   ├── ViewCart (empty, with items)
│   ├── UpdateQuantity (increase, decrease, set to 0)
│   └── RemoveItem / ClearCart
├── CheckoutTests
│   ├── Checkout with new address (valid, invalid fields)
│   ├── Checkout with saved address
│   └── Payment methods (COD, CreditCard, BankTransfer)
├── OrderTests
│   ├── View order history
│   ├── View order detail
│   └── Cancel order (valid, invalid status)
├── AdminTests
│   ├── Dashboard stats
│   ├── Manage orders (list, filter, update status)
│   ├── Manage products (CRUD, variants, images)
│   └── Manage brands/categories
```

### 9.2 Cách lưu trữ và sử dụng Token
```csharp
// Sau khi login
var token = loginResponse.Token;
driver.Manage().Cookies.AddCookie(new Cookie("token", token, "/", domain));

// Hoặc truyền qua header nếu dùng API trực tiếp
var headers = new Dictionary<string, string>
{
    { "Authorization", $"Bearer {token}" }
};
```

### 9.3 Thứ tự thao tác UI chuẩn (Happy Path)
1. **Đăng ký** → Đăng nhập → Lưu token.
2. **Trang chủ** → Xem sản phẩm → Nhấn vào 1 SP.
3. **PDP** → Chọn Size → Nhập Qty → Thêm vào giỏ.
4. **Cart** → Nhấn "Thanh toán".
5. **Checkout** → Nhập địa chỉ → Chọn COD → Đặt hàng.
6. **Thank You** → Ghi nhận OrderNumber.
7. **My Orders** → Xác nhận đơn mới xuất hiện.
8. **Admin** (đăng nhập Admin) → Xem đơn → Đổi Status → `Processing`.

### 9.4 Ghi chú về xử lý Redirect
- Khi Guest truy cập trang cần auth (Cart, Checkout, My Orders):
  - Frontend lưu URL hiện tại vào `localStorage` hoặc query param `?redirect=/cart`.
  - Sau khi login thành công, đọc redirect URL và `driver.Navigate().GoToUrl(redirectUrl)`.

### 9.5 Ghi chú về Validation Error trên UI
- Backend trả về `400` với message tiếng Việt.
- Frontend cần hiển thị message này gần field tương ứng hoặc dạng toast.
- Selenium cần kiểm tra:
  - Message text xuất hiện đúng trên UI.
  - Field bị lỗi có style đỏ (nếu có).
  - Form không submit được.

### 9.6 Ghi chú về Image
- `ResolveImageUrl` trong backend tự động thêm domain vào URL ảnh nếu là relative path.
- Tuy nhiên trong Selenium, nếu frontend render ảnh từ API trả về `ImageUrl` đã là absolute URL thì không cần xử lý.
- Nếu là relative (`/images/products/xxx.jpg`), cần prepend base URL.

### 9.7 Ghi chú về phân trang
- Page size mặc định: 12 sản phẩm/trang.
- Selenium nên test:
  - Trang 1 có đủ 12 item (nếu có đủ data).
  - Trang 2 có item khác trang 1.
  - Last page có thể ít hơn 12 item.

### 9.8 Ghi chú về Stock
- Sau khi đặt hàng thành công: `StockQuantity` giảm đúng số lượng.
- Sau khi hủy đơn: `StockQuantity` hoàn lại.
- Selenium nên kiểm tra stock thay đổi qua API `GET /api/admin/products/{id}/variants` (nếu có admin) hoặc quan sát UI (nếu hiển thị "Còn X sản phẩm").

### 9.9 Error Response Format
Tất cả lỗi đều trả về JSON:
```json
{
  "success": false,
  "message": "Chi tiết lỗi",
  "statusCode": 400
}
```

### 9.10 Success Response Format
```json
{
  "success": true,
  "message": "Thành công",
  "data": { ... }
}
```

---

## Phụ lục: Các Entity chính

### User
- `Id` (int, PK)
- `UserName`, `Email`, `EmailConfirmed`
- `FirstName`, `LastName`
- `IsActive`
- Navigation: `Addresses`, `Orders`, `Reviews`, `Cart`

### Product
- `Id`, `Name`, `Slug`, `Description`, `Price`, `SalePrice`, `Color`
- `IsActive`, `BrandId`, `CategoryId`
- Navigation: `Variants`, `Images`, `Reviews`, `OrderItems`

### ProductVariant
- `Id`, `ProductId`, `SizeId`, `SKU`, `StockQuantity`, `Price`, `SalePrice`
- Navigation: `Product`, `Size`, `CartItems`, `OrderItems`

### Cart / CartItem
- `Cart`: `Id`, `UserId`, `CreatedAt`, `UpdatedAt`
- `CartItem`: `Id`, `CartId`, `ProductVariantId`, `Quantity`, `UnitPrice`

### Order / OrderItem
- `Order`: `Id`, `UserId`, `AddressId`, `OrderNumber`, `Subtotal`, `ShippingFee`, `TotalAmount`, `Status`, `PaymentStatus`, `CreatedAt`
- `OrderItem`: `Id`, `OrderId`, `ProductId`, `ProductVariantId`, `Quantity`, `UnitPrice`, `TotalPrice`

### Address
- `Id`, `UserId`, `RecipientName`, `PhoneNumber`, `Street`, `City`, `State`, `PostalCode`, `Country`, `IsDefault`, `CreatedAt`

### Payment
- `Id`, `OrderId`, `PaymentMethod`, `Status`, `Amount`, `TransactionId`, `CreatedAt`

### Review
- `Id`, `ProductId`, `UserId`, `Rating`, `Comment`, `CreatedAt`

### Brand / Category / Size
- `Brand`: `Id`, `Name`, `Description`, `LogoUrl`
- `Category`: `Id`, `Name`, `Slug`, `Description`
- `Size`: `Id`, `Name`, `Description`

### ProductImage
- `Id`, `ProductId`, `ImageUrl`, `AltText`, `IsPrimary`, `CreatedAt`
