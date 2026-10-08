# Test Plan - Hệ thống Thương mại điện tử Shoes Web

**Phiên bản:** 1.0  
**Ngày tạo:** 2026-09-02  
**Tác giả:** QA Team  
**Dự án:** Shoes Web E-Commerce  

---

## Mục lục
1. [Tổng quan dự án](#1-tổng-quan-dự-án)
2. [Phạm vi kiểm thử](#2-phạm-vi-kiểm-thử)
3. [Chiến lược kiểm thử](#3-chiến-lược-kiểm-thử)
4. [Tiêu chí bắt đầu và kết thúc kiểm thử](#4-tiêu-chí-bắt-đầu-và-kết-thúc-kiểm-thử)
5. [Môi trường và công cụ test dự kiến](#5-môi-trường-và-công-cụ-test-dự-kiến)
6. [Kế hoạch thực thi và phân bổ nguồn lực](#6-kế-hoạch-thực-thi-và-phân-bổ-nguồn-lực)
7. [Rủi ro và biện pháp giảm thiểu](#7-rủi-ro-và-biện-pháp-giảm-thiểu)

---

## 1. Tổng quan dự án

### 1.1 Mục tiêu dự án
- Xây dựng nền tảng thương mại điện tử bán giày dép với đầy đủ chức năng: khám phá sản phẩm, giỏ hàng, thanh toán, quản lý đơn hàng và quản trị hệ thống.

### 1.2 Các tác nhân (Actors)
- **Guest**: Người dùng chưa đăng nhập.
- **Customer**: Người dùng đã đăng nhập, có role `Customer`.
- **Admin**: Người dùng có role `Admin`, quản lý toàn hệ thống.

### 1.3 Các module chính
1. **Authentication & Authorization** - Đăng ký, đăng nhập, đăng xuất, phân quyền.
2. **Product Management** - Hiển thị danh sách SP, chi tiết SP, filter/sort/search.
3. **Shopping Cart** - Thêm/xóa/sửa số lượng SP trong giỏ hàng.
4. **Checkout & Order** - Tạo đơn hàng, xử lý địa chỉ, thanh toán, tự động trừ tồn kho.
5. **Post-Purchase** - Xem lịch sử đơn, chi tiết đơn, hủy đơn, hoàn tồn kho.
6. **Admin Panel** - Dashboard, quản lý đơn hàng, sản phẩm, biến thể, ảnh, thương hiệu, danh mục.

---

## 2. Phạm vi kiểm thử

### 2.1 In-scope (Trong phạm vi kiểm thử)

#### Module 1: Authentication & Authorization
- Đăng ký tài khoản mới (valid/invalid).
- Đăng nhập với Customer và Admin.
- Đăng xuất.
- Validation message trên UI (Email đã tồn tại, sai password, tài khoản bị khóa).
- Phân quyền trang: Guest bị chặn truy cập Cart/Checkout/My Orders/Admin.
- Redirect thông minh sau khi login.
- Lưu trữ và sử dụng JWT token trên client.

#### Module 2: Product Management
- Trang chủ: Load sản phẩm active, banner, grid sản phẩm.
- Collection/Shop page: Filter theo category, brand, search keyword, sort (giá tăng/giảm, mới nhất), phân trang.
- Product Detail Page (PDP): Hiển thị thông tin SP, biến thể (size, stock), gallery ảnh, reviews, sản phẩm liên quan.
- Xử lý SP không có ảnh, không có biến thể, không có review.
- SP ẩn (`IsActive = false`) không hiển thị trên trang public.

#### Module 3: Shopping Cart
- Thêm SP vào giỏ (variant tồn tại, có sale price).
- Cộng dồn số lượng khi thêm SP đã có trong giỏ.
- Validation: Qty > 0, Qty <= StockQuantity, cộng dồn không vượt stock.
- Xem giỏ hàng (có item / rỗng).
- Cập nhật số lượng (tăng/giảm/set 0 để xóa).
- Xóa 1 item, xóa toàn bộ giỏ.
- Hiển thị đúng TotalItems, TotalPrice, UnitPrice.

#### Module 4: Checkout & Order
- Form checkout với địa chỉ mới (validate RecipientName, Street, City, Country).
- Chọn địa chỉ đã lưu (`AddressId`).
- Mapping: FirstName + LastName → RecipientName, Address → Street.
- PhoneNumber rỗng → auto `"0000000000"`.
- Chọn phương thức thanh toán: COD, CreditCard, BankTransfer.
- Logic Payment: COD → `Pending`/`Unpaid`; CreditCard/BankTransfer → `Paid`.
- Phí giao hàng cố định `30.000đ`.
- Tạo mã đơn hàng định dạng `ORD-{timestamp}-{random}`.
- Tạo đơn hàng thành công: Order + OrderItems + Payment + trừ tồn kho + xóa CartItems (transaction).
- Validation lỗi: giỏ rỗng, thiếu thông tin địa chỉ, AddressId không hợp lệ, vượt tồn kho, variant không tồn tại.

#### Module 5: Post-Purchase
- Trang Thank You: Hiển thị OrderNumber, tổng tiền, địa chỉ, danh sách SP.
- Trang My Orders: Danh sách đơn hàng (sort by CreatedAt desc), badge trạng thái.
- Xem chi tiết đơn hàng.
- Hủy đơn hàng: Pending/Processing → Cancelled, hoàn tồn kho.
- Hủy đơn đã thanh toán → PaymentStatus = Refunded.
- Không hủy được đơn Shipped/Completed/Cancelled.

#### Module 6: Admin Panel
- Dashboard: Hiển thị tổng quan stats (TotalOrders, TotalProducts, TotalUsers, TotalRevenue, PendingOrders, RecentOrders).
- Quản lý đơn hàng: Danh sách, lọc theo status, xem chi tiết, cập nhật trạng thái (Pending → Processing → Shipped → Completed).
- Không cho phép cập nhật trạng thái đơn Shipped/Completed/Cancelled.
- Quản lý sản phẩm: CRUD sản phẩm, biến thể (variant), ảnh.
- Upload ảnh: validate định dạng (JPG, JPEG, PNG, WEBP) và kích thước (<= 5MB).
- Đặt ảnh primary.
- Quản lý thương hiệu (Brand) và danh mục (Category): CRUD cơ bản.

### 2.2 Out-of-scope (Ngoài phạm vi kiểm thử)

#### Không kiểm thử
- **Backend Unit Test / Integration Test**: Tập trung vào E2E UI test bằng Selenium.
- **Performance/Load Testing**: Kiểm tra throughput, response time, concurrent users.
- **Security Testing**: Penetration test, SQL injection, XSS, CSRF (trừ phân quyền cơ bản).
- **Cross-browser Testing**: Chỉ test trên 1 browser (Chrome/Edge) theo môi trường test.
- **Mobile/Responsive Testing**: Chỉ test trên desktop viewport.
- **API Rate Limiting**: Không có throttle/rate limit trong backend hiện tại.
- **Email Verification**: Backend set `EmailConfirmed = true` ngay khi register, không gửi email xác nhận.
- **Refresh Token**: Chỉ có JWT access token (expires 1h), không có refresh token mechanism trong test scope.
- **Payment Gateway Integration**: Không có tích hợp VNPAY/Stripe thực tế, chỉ mock logic PaymentStatus.
- **Image Upload từ Frontend**: Test upload ảnh qua API Admin, không test UI upload (có thể test sau nếu cần).
- **Search Elastic/Full-text**: Search hiện tại là `LIKE` query trên SQL, không phải full-text search engine.
- **Notification/Push Notification**: Không có.
- **Multi-language/i18n**: Chỉ test tiếng Việt.

---

## 3. Chiến lược kiểm thử

### 3.1 Tổng quan chiến lược
- **Level 1: Functional Testing** - Kiểm tra từng chức năng độc lập theo module.
- **Level 2: Integration Testing** - Kiểm tra luồng dữ liệu giữa các module liên quan.
- **Level 3: E2E Flow Testing** - Kiểm tra luồng nghiệp vụ end-to-end từ Guest → Customer → Admin.

### 3.2 Functional Testing (Kiểm thử chức năng)

#### Mục tiêu
- Mỗi chức năng hoạt động đúng theo yêu cầu trong Requirement.md.
- Validation rules được thực thi đúng trên UI.
- Error message hiển thị chính xác.

#### Phương pháp
- **Positive Testing**: Nhập dữ liệu hợp lệ → chức năng hoạt động đúng.
- **Negative Testing**: Nhập dữ liệu không hợp lệ → hiển thị lỗi đúng, không crash.
- **Boundary Testing**: Test tại các giá trị biên (min/max length, 0, âm, vượt tồn kho).

#### Phạm vi áp dụng
- Tất cả các form: Register, Login, Checkout, Add to Cart.
- Tất cả các nút chức năng: Add to cart, Update quantity, Delete item, Place order, Cancel order, Update order status.
- Hiển thị dữ liệu: Product grid, Cart summary, Order detail, Dashboard stats.

### 3.3 UI/UX Testing (Kiểm thử giao diện & trải nghiệm)

#### Mục tiêu
- Giao diện hiển thị đúng theo thiết kế.
- Trải nghiệm người dùng mượt mà, không có lỗi hiển thị.
- Responsive cơ bản (desktop viewport).

#### Phương pháp
- **Layout Testing**: Kiểm tra vị trí, kích thước, màu sắc của các element.
- **Navigation Testing**: Kiểm tra menu, breadcrumb, redirect đúng trang.
- **Data Display Testing**: Kiểm tra text, số tiền, ngày tháng hiển thị đúng format.
- **State Testing**: Kiểm tra disabled/enabled state của nút, badge số lượng giỏ hàng.
- **Error State Testing**: Kiểm tra toast/message lỗi hiển thị đúng vị trí.

#### Phạm vi áp dụng
- Trang chủ: Banner, product cards, category/brand badges.
- Cart page: Item list, quantity controls, total price, checkout button.
- Checkout page: Form fields, address selection, payment method dropdown.
- Order pages: Order cards, status badges, order detail.
- Admin pages: Dashboard stats cards, order table, product table, forms.

### 3.4 E2E Flow Testing (Kiểm thử luồng end-to-end)

#### Mục tiêu
- Kiểm tra toàn bộ luồng nghiệp vụ từ đầu đến cuối.
- Đảm bảo dữ liệu được lưu trữ và đồng bộ đúng giữa các bước.

#### Các luồng E2E chính

##### Flow 1: Guest → Register → Login → Browse → Add to Cart → Checkout → Order → My Orders
1. Guest truy cập trang chủ.
2. Đăng ký tài khoản mới.
3. Đăng nhập (lưu token).
4. Xem danh sách sản phẩm → lọc theo category → sort.
5. Xem chi tiết SP → chọn size → thêm vào giỏ.
6. Xem giỏ hàng → cập nhật số lượng → thanh toán.
7. Nhập địa chỉ mới → chọn COD → đặt hàng.
8. Xem trang Thank You → ghi nhận OrderNumber.
9. Vào My Orders → xác nhận đơn mới xuất hiện.

##### Flow 2: Customer → View Orders → Cancel Order → Verify Stock Restored
1. Customer đã có đơn Pending.
2. Vào My Orders → xem danh sách.
3. Nhấn hủy đơn → xác nhận.
4. Kiểm tra đơn chuyển sang Cancelled.
5. Kiểm tra tồn kho được hoàn lại (qua Admin hoặc API).

##### Flow 3: Admin → Dashboard → Manage Orders → Update Status
1. Admin đăng nhập.
2. Xem Dashboard stats.
3. Vào trang Orders → xem danh sách → lọc Pending.
4. Cập nhật Pending → Processing → Shipped → Completed.
5. Kiểm tra không thể cập nhật đơn Completed/Cancelled.

##### Flow 4: Admin → Manage Products → Add Product → Add Variant → Upload Image
1. Admin vào trang Products.
2. Tạo sản phẩm mới.
3. Thêm variant (size, stock, price).
4. Upload ảnh cho sản phẩm.
5. Đặt ảnh primary.
6. Kiểm tra sản phẩm hiển thị đúng trên trang public.

##### Flow 5: Redirect Flow (Guest → Protected Page → Login → Redirect Back)
1. Guest truy cập `/cart` (chưa đăng nhập).
2. Hệ thống chuyển sang trang Login, lưu redirect URL.
3. Đăng nhập thành công.
4. Tự động chuyển về `/cart`.

### 3.5 Test Levels Summary

| Level | Loại | Mô tả | Phạm vi |
|-------|------|--------|---------|
| 1 | Functional | Kiểm tra từng chức năng độc lập | Tất cả các module |
| 2 | Integration | Kiểm tra luồng dữ liệu giữa các module | Cart → Order, Order → Payment, Order → Stock |
| 3 | E2E | Kiểm tra luồng nghiệp vụ hoàn chỉnh | 5 luồng chính đã nêu |
| 4 | UI/UX | Kiểm tra giao diện và trải nghiệm | Tất cả các trang |

---

## 4. Tiêu chí bắt đầu và kết thúc kiểm thử

### 4.1 Entry Criteria (Tiêu chí bắt đầu kiểm thử)

#### Tiêu chí bắt đầu bắt buộc
- [ ] Backend đã được deploy lên môi trường test và chạy ổn định.
- [ ] Frontend đã được build và deploy lên môi trường test.
- [ ] Database đã được seed với test data đầy đủ (ít nhất 4-5 sản phẩm, 2-3 brands, 2-3 categories, 1 customer, 1 admin).
- [ ] Tài khoản test đã được tạo sẵn:
  - Customer: `nguyenvana@example.com` / `Test@123456`
  - Admin: `admin@shoes.com` / `Admin@123456`
- [ ] Test Plan đã được review và approve bởi PM/Team Lead.
- [ ] Test Environment đã sẵn sàng (driver, test data, tools).

#### Tiêu chí bắt đầu từng module
- **Auth**: Backend đăng ký/đăng nhập API hoạt động, database user table sẵn sàng.
- **Product**: Đã có ít nhất 4 sản phẩm active trong DB.
- **Cart**: User đã đăng nhập, có thể truy cập trang cart.
- **Checkout**: Giỏ hàng có ít nhất 1 item, user có địa chỉ hoặc form nhập địa chỉ hoạt động.
- **Post-Purchase**: Đã có ít nhất 1 đơn hàng trong DB để test.
- **Admin**: Tài khoản Admin đã có role Admin trong DB.

### 4.2 Exit Criteria (Tiêu chí kết thúc kiểm thử)

#### Tiêu chí kết thúc bắt buộc
- [ ] 100% các test case trong-scope đã được thực thi.
- [ ] 100% các test case Happy Path đều PASS.
- [ ] Tất cả các lỗi nghiêm trọng (Critical/High) đã được fix và retest PASS.
- [ ] Tất cả các lỗi trung bình (Medium) đã được ghi nhận và assign cho dev.
- [ ] Test Summary Report đã được tạo và approve.
- [ ] Tất cả các test data đã được cleanup (nếu cần).

#### Tiêu chí kết thúc theo mức độ lỗi
| Mức độ | Định nghĩa | Tiêu chí kết thúc |
|--------|-----------|-------------------|
| Critical | Hệ thống crash, mất dữ liệu, security breach | 0 lỗi Critical còn mở |
| High | Chức năng chính bị lỗi, không dùng được | 0 lỗi High còn mở |
| Medium | Chức năng phụ bị lỗi, workaround có sẵn | <= 5 lỗi Medium còn mở (đã assign) |
| Low | Lỗi UI nhỏ, không ảnh hưởng chức năng | Không bắt buộc fix trước release |

#### Tiêu chí kết thúc từng module
- **Auth**: Tất cả test case PASS, redirect hoạt động đúng.
- **Product**: Tất cả filter/sort/search/pagination đúng, PDP hiển thị đầy đủ.
- **Cart**: Add/Update/Remove/Clear đều hoạt động đúng, tồn kho không bị ảnh hưởng sai.
- **Checkout**: Đặt hàng thành công tạo đủ Order/OrderItem/Payment, tồn kho trừ đúng, giỏ rỗng.
- **Post-Purchase**: Xem đơn, hủy đơn hoạt động đúng, hoàn tồn kho chính xác.
- **Admin**: Dashboard stats đúng, cập nhật trạng thái đơn theo luồng cho phép, CRUD sản phẩm/biến thể/ảnh hoạt động.

### 4.3 Suspend/Resume Criteria

#### Suspend Criteria (Tạm dừng kiểm thử)
- Backend/frontend bị crash liên tục, không thể thực thi test.
- Test data bị mất/sai, cần restore lại.
- Lỗi Critical phát hiện cần dev fix trước khi tiếp tục.

#### Resume Criteria (Tiếp tục kiểm thử sau khi tạm dừng)
- Backend/frontend đã fix và ổn định.
- Test data đã được restore.
- Lỗi Critical đã được fix và retest PASS.

---

## 5. Môi trường và công cụ test dự kiến

### 5.1 Test Environment

#### 5.1.1 Môi trường Test
| Thành phần | Chi tiết |
|------------|----------|
| **Test URL** | `http://localhost:5000` (hoặc URL test environment) |
| **Frontend URL** | `http://localhost:3000` (React/Vue/Next.js dev server hoặc built) |
| **Backend URL** | `http://localhost:5000/api` (.NET 9.0 API) |
| **Database** | SQL Server / SQLite (test database) |
| **OS** | Windows 10/11 |
| **Browser** | Google Chrome (mới nhất) hoặc Microsoft Edge |

#### 5.1.2 Test Data Strategy
- **Seeded Data**: Database seed script chạy trước khi test, tạo sẵn:
  - 4-5 sản phẩm active, 1 sản phẩm inactive.
  - 2-3 brands, 2-3 categories.
  - 1 customer, 1 admin.
  - Mỗi SP có ít nhất 2 variants với stock > 0.
- **Test Data Files**: JSON files chứa thông tin đăng nhập, checkout data.
- **Data Cleanup**: Sau mỗi test case/flow, rollback lại DB về trạng thái ban đầu hoặc dùng transaction rollback.

#### 5.1.3 Test Accounts
| Role | Email | Password | Mục đích |
|------|-------|----------|----------|
| Customer | `nguyenvana@example.com` | `Test@123456` | Test các chức năng Customer |
| Admin | `admin@shoes.com` | `Admin@123456` | Test các chức năng Admin |
| New Customer | Tạo mới trong quá trình test | `Test@123456` | Test register flow |

### 5.2 Test Tools

#### 5.2.1 Test Automation Framework
| Công cụ | Mục đích | Phiên bản dự kiến |
|---------|----------|-----------------|
| **Selenium WebDriver** | Tự động hóa UI test | Selenium 4.x |
| **NUnit / xUnit** | Test runner, assertions | NUnit 3.x hoặc xUnit 2.x |
| **C# (.NET)** | Ngôn ngữ viết test script | .NET 9.0 |
| **WebDriverManager** | Tự động download và quản lý browser driver | Latest |
| **Newtonsoft.Json / System.Text.Json** | Parse JSON response từ API | Built-in .NET 9 |
| **FluentAssertions** (Optional) | Assertions dễ đọc | Latest |

#### 5.2.2 Supporting Tools
| Công cụ | Mục đích |
|---------|----------|
| **Postman / Bruno** | Test API độc lập, debug request/response |
| **SQL Server Management Studio / DB Browser** | Kiểm tra trực tiếp DB, verify data |
| **Git** | Quản lý source code test |
| **VS Code / Rider / Visual Studio** | IDE viết code test |
| **Allure Report / ExtentReports** | Test report generation |
| **Chrome DevTools** | Debug UI, inspect element, network tab |

#### 5.2.3 Cấu trúc Project Test
```
ShoesWeb.Tests/
├── Drivers/
│   └── SeleniumDriver.cs          # Khởi tạo WebDriver, quản lý browser lifecycle
├── Pages/
│   ├── BasePage.cs                # Base class cho các Page Object
│   ├── LoginPage.cs
│   ├── RegisterPage.cs
│   ├── HomePage.cs
│   ├── ProductDetailPage.cs
│   ├── CartPage.cs
│   ├── CheckoutPage.cs
│   ├── OrderConfirmationPage.cs
│   ├── MyOrdersPage.cs
│   └── Admin/
│       ├── AdminDashboardPage.cs
│       ├── AdminOrdersPage.cs
│       └── AdminProductsPage.cs
├── Tests/
│   ├── AuthTests/
│   │   ├── RegisterTests.cs
│   │   ├── LoginTests.cs
│   │   └── LogoutTests.cs
│   ├── ProductTests/
│   │   ├── HomePageTests.cs
│   │   ├── CollectionPageTests.cs
│   │   └── ProductDetailTests.cs
│   ├── CartTests/
│   │   ├── AddToCartTests.cs
│   │   ├── UpdateCartTests.cs
│   │   └── RemoveCartTests.cs
│   ├── CheckoutTests/
│   │   ├── CheckoutWithNewAddressTests.cs
│   │   └── CheckoutWithSavedAddressTests.cs
│   ├── OrderTests/
│   │   ├── OrderHistoryTests.cs
│   │   └── CancelOrderTests.cs
│   └── AdminTests/
│       ├── DashboardTests.cs
│       ├── AdminOrderTests.cs
│       └── AdminProductTests.cs
├── Utilities/
│   ├── TestDataHelper.cs          # Load test data từ JSON
│   ├── ApiHelper.cs               # Gọi API trực tiếp khi cần
│   └── DatabaseHelper.cs          # Kiểm tra trực tiếp DB
├── TestData/
│   ├── users.json
│   ├── products.json
│   └── checkoutData.json
└── TestPlan.md
```

### 5.3 Cấu hình WebDriver

#### 5.3.1 Browser Options
```csharp
var options = new ChromeOptions();
options.AddArgument("--start-maximized");
options.AddArgument("--disable-notifications");
options.AddArgument("--disable-infobars");
options.AddArgument("--no-sandbox");
options.AddArgument("--disable-dev-shm-usage");
options.AddUserProfilePreference("profile.default_content_setting_values.notifications", 2);
```

#### 5.3.2 Wait Strategy
- **Implicit Wait**: 10 giây (khuyến nghị không dùng, thay vào Explicit Wait).
- **Explicit Wait**: Sử dụng `WebDriverWait` với timeout 15-30 giây cho từng element.
- **Fluent Wait**: Poll mỗi 500ms, timeout 30 giây cho element dynamic.

#### 5.3.3 Page Object Pattern
- Mỗi trang là 1 class kế thừa `BasePage`.
- Mỗi element được định nghĩa bằng `By` selector.
- Mỗi action là 1 method trong Page class.
- Test class chỉ gọi Page methods, không trực tiếp thao tác WebDriver.

---

## 6. Kế hoạch thực thi và phân bổ nguồn lực

### 6.1 Timeline dự kiến

| Giai đoạn | Thời gian | Nội dung |
|-----------|-----------|----------|
| **Week 1** | 5 ngày | Setup môi trường, cấu trúc project, viết Base Page/Driver, test Auth module. |
| **Week 2** | 5 ngày | Viết test Product module (Home, Collection, PDP). |
| **Week 3** | 5 ngày | Viết test Cart + Checkout module. |
| **Week 4** | 5 ngày | Viết test Post-Purchase + Admin module. |
| **Week 5** | 5 ngày | Chạy toàn bộ test suite, fix lỗi test script, tạo báo cáo. |

### 6.2 Phân bổ nguồn lực

| Vai trò | Số lượng | Trách nhiệm |
|---------|----------|--------------|
| **QA Engineer** | 1-2 người | Viết test script, chạy test, báo cáo lỗi. |
| **Dev** | Hỗ trợ | Fix lỗi backend/frontend khi test phát hiện. |
| **PM** | 1 người | Review test plan, ưu tiên lỗi, quyết định release. |

### 6.3 Test Execution Schedule

| Ngày | Hoạt động |
|------|-----------|
| Day 1-2 | Setup environment, seed data, viết Driver/BasePage. |
| Day 3-4 | Viết và chạy Auth Tests. |
| Day 5-6 | Viết và chạy Product Tests. |
| Day 7-8 | Viết và chạy Cart Tests. |
| Day 9-10 | Viết và chạy Checkout Tests. |
| Day 11-12 | Viết và chạy Post-Purchase Tests. |
| Day 13-14 | Viết và chạy Admin Tests. |
| Day 15-16 | Chạy lại toàn bộ regression, retest các lỗi đã fix. |
| Day 17-20 | Tạo báo cáo, tổng kết, họp review với team. |

### 6.4 Test Metrics (Chỉ số đánh giá)

| Metric | Mục tiêu | Cách tính |
|--------|----------|-----------|
| **Test Coverage** | >= 90% các chức năng chính | Số test case thực thi / Tổng số test case |
| **Pass Rate** | >= 95% | Số test case PASS / Tổng số test case đã chạy |
| **Defect Detection Rate** | Cao | Số lỗi phát hiện / Tổng số lỗi thực tế |
| **Test Execution Time** | < 2 giờ cho toàn bộ suite | Thời gian chạy hết tất cả test case |
| **Flaky Test Rate** | < 5% | Số test case không ổn định / Tổng số test case |

---

## 7. Rủi ro và biện pháp giảm thiểu

| Rủi ro | Mức độ | Biện pháp giảm thiểu |
|--------|--------|----------------------|
| Backend chưa ổn định, API thay đổi | Cao | Chạy song song API test bằng Postman, thống nhất contract trước. |
| Test data không đầy đủ | Trung bình | Chuẩn bị seed script đầy đủ, backup DB trước khi test. |
| Test script bị lỗi do UI thay đổi | Trung bình | Dùng Page Object Pattern, selector ổn định (data-testid nếu có). |
| Môi trường test chậm/treo | Trung bình | Chạy test vào giờ thấp điểm, dùng explicit wait thay vì sleep. |
| Lỗi không reproduce được | Thấp | Ghi lại log, screenshot, video của lần test bị lỗi. |
| Timeout do network/server | Thấp | Tăng timeout, retry mechanism cho các test nhạy cảm. |
| Quyết định release không rõ ràng | Trung bình | Làm rõ Exit Criteria từ đầu, họp định kỳ với PM. |

---

## Phụ lục

### A. Tài liệu tham chiếu
- Requirement.md - Chi tiết yêu cầu kiểm thử từng module.
- API Documentation (nếu có) - Swagger/OpenAPI spec.
- Database Schema - Entity Relationship Diagram.

### B. Thuật ngữ
- **E2E (End-to-End)**: Kiểm thử toàn bộ luồng nghiệp vụ từ đầu đến cuối.
- **PDP (Product Detail Page)**: Trang chi tiết sản phẩm.
- **PO (Page Object)**: Design pattern đóng gói thao tác trang web thành class.
- **Test Data**: Dữ liệu đầu vào dùng để kiểm thử.
- **Smoke Test**: Kiểm tra nhanh các chức năng chính sau mỗi lần build.

### C. Lịch sử phiên bản
| Phiên bản | Ngày | Thay đổi | Tác giả |
|-----------|------|----------|---------|
| 1.0 | 2026-09-02 | Tạo test plan ban đầu | QA Team |
