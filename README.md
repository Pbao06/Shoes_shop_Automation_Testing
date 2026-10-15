# Selenium WebDriver Automation Testing Demo

A Selenium WebDriver automation testing project for a shoes e-commerce web application ([Big Shoes](https://shoes-shop-bice.vercel.app/)). This project demonstrates end-to-end UI test automation covering user authentication, product browsing, cart management, checkout validation, and order information verification for a QA Automation portfolio.

## Tech Stack

| Technology | Version / Details |
|---|---|
| **Java** | 17+ |
| **Selenium WebDriver** | 4.35.0 |
| **WebDriverManager** | 5.7.0 (auto-manages ChromeDriver) |
| **TestNG** | 7.11.0 |
| **Maven** | Build and dependency management |
| **Maven Surefire Plugin** | 3.5.3 (test execution via `testng.xml`) |
| **Chrome / ChromeDriver** | Test execution browser |
| **Git** | Version control |

## Project Structure

```
demo/
├── pom.xml                         # Maven configuration (deps + build plugins)
├── testng.xml                      # TestNG suite definition (controls which tests run)
├── src/main/java/com/example/
│   ├── models/
│   │   ├── Checkout.java           # Checkout form data (name, email, address, etc.)
│   │   ├── CheckoutData.java       # Order summary data (product name, qty, size, price)
│   │   └── Order.java              # Order domain model
│   └── page/
│       ├── BasePage.java           # Base page: WebDriverWait, explicit waits, input helpers
│       ├── LoginPage.java          # Login form: enter credentials, submit, read errors
│       ├── HomePage.java          # Home page: navigate to product detail, search, admin
│       ├── SearchPage.java         # Search: enter search term, click product result
│       ├── ProductDetail.java      # Product detail: select size, add to cart, view badge
│       ├── CartPage.java           # Cart: click Checkout button
│       ├── CheckoutPage.java       # Checkout: fill form, place order, read order summary
│       └── OrderCustomerPage.java  # My Orders: verify status tabs, order details
└── src/test/java/com/example/testing/
    ├── BaseTest.java               # Base test: driver setup/teardown, URL config
    ├── LoginTest.java              # Login test
    ├── RegisterTest.java           # Registration validation tests
    ├── HomeTest.java               # Home/search navigation tests
    ├── SearchTest.java             # Product search tests
    ├── ProductDetailTest.java      # Product detail & cart badge tests
    ├── CheckoutTest.java           # Checkout form validation (data-driven)
    └── OrderTesting.java           # Order verification after placement (data-driven)
```

> The full Maven project lives under the `demo/` directory. A separate `admin/` package contains Admin automation tests that are **under development** and not yet complete.

## Implemented User Testing Scenarios

### Login
- **Successful login** — navigates to `/login`, enters `admin@example.com` / `Admin@123`, and asserts the browser redirects to the `/home` page.

### Registration (Form Validation)
- **Registration validation errors** *(data-driven via `@DataProvider`)* — submits incomplete registration forms and asserts the displayed validation messages:
  - Empty first name → "First Name must be at least 2 characters."
  - Single-character first name → same message
  - Empty email → "Please enter a valid email address."

### Product Browsing
- **Search page navigation** — opens the home page, clicks the search icon, and asserts the URL contains "Search".
- **Product search** — searches for "shoes", clicks a product result, and asserts the URL navigates to `/product/3`.

### Shopping Cart
- **Add to cart & badge update** — logs in, navigates to a product, selects a size, adds the product to the bag, adds it again, and verifies that the cart badge count increments by exactly 1 using an explicit wait for the updated text.

### Checkout (Form Validation)
- **Checkout validation errors** *(data-driven, 16 scenarios via `@DataProvider`)* — fills the checkout form with invalid data and asserts the correct per-field validation error appears. Validated rules include:
  - First name: required, min 2 chars, max 50 chars
  - Email: required, valid format, max 254 chars
  - Address: required, min 5 chars, max 200 chars
  - City: required, min 2 chars, max 100 chars, letters/spaces/hyphens/apostrophes/dots only
  - Postal code: required, min 3 chars, max 20 chars, alphanumeric with limited special characters

### Order Information Validation
- **Order status filter tabs** — completes a valid checkout with `admin@example.com`/`Admin@123`, navigates to the **My Orders** page, and verifies that every status filter tab that does **not** match the order's current status shows the "no orders in this category." placeholder. This confirms orders appear in the correct status tab.
- The order detail matching method (`checkinformationProduct`) is implemented in `OrderCustomerPage` but the corresponding test method is currently commented out in `OrderTesting`.

### Main User Purchase Workflow

```
Login → Product Detail → Select Size → Add to Cart →
Cart → Checkout → Fill Shipping Info → Place Order →
My Orders → Verify Order Placement & Status Filtering
```

The workflow is driven across `ProductDetailTest`, `CheckoutTest`, and `OrderTesting`. Page Objects return the next page in the flow (e.g., `LoginPage.typeLogin()` returns a `HomePage`, `ProductDetail.clickCart()` returns a `CartPage`), enabling a fluent chain of user actions. Meaningful assertions include cart badge count increments, URL changes after navigation, and validation error message matching against expected text.

## Automation Framework Design

### Page Object Model (POM)
Each web page is encapsulated as a Java class in `com.example.page` with locators as `By` definitions and user-facing methods that return the next page object. This keeps test code free of locator details and supports a fluent, readable flow.

### Base Classes
- **BasePage** — holds the shared `WebDriver` and `WebDriverWait` (50-second timeout). Provides reusable helpers: `waitForVisible`, `waitForClickable` (explicit waits via `ExpectedConditions`), `TypeInput`, `visiableToSendData`, and file-upload utilities.
- **BaseTest** — uses TestNG `@BeforeMethod` to set up ChromeDriver via WebDriverManager, and `@AfterMethod` to quit the driver. Holds the base URL (`https://shoes-shop-bice.vercel.app/`) and an `openUrl()` helper.

### Explicit Waits
All element interactions use explicit waits (`WebDriverWait` + `ExpectedConditions`) rather than implicit sleeps, improving test reliability against dynamic page loads.

### Data-Driven Testing
TestNG `@DataProvider` is used in `CheckoutTest` (16 validation scenarios), `RegisterTest` (3 validation scenarios), and `OrderTesting` (1 valid checkout scenario) to run the same test logic with different input combinations. Test data is expressed as inline `Object[][]` arrays of model objects (e.g., `Checkout`, `CheckoutData`).

### TestNG Orchestration
Tests are executed via `testng.xml` (configured in the Maven Surefire plugin). The suite currently includes `LoginTest`. To run additional test classes, add them under the corresponding `<classes>` block in `testng.xml`.

## How to Run the Tests

### Prerequisites
- **JDK 17** or higher
- **Maven 3.8+**
- **Google Chrome** (ChromeDriver is auto-managed by WebDriverManager — no manual driver download required)
- The application must be accessible at `https://shoes-shop-bice.vercel.app/`

### Run via Maven

```bash
cd demo
mvn test
```

This runs the TestNG suite defined in `testng.xml`. The suite currently includes only `LoginTest`. To run other test classes, add them to `testng.xml`:

```xml
<classes>
    <class name="com.example.testing.CheckoutTest"/>
</classes>
```

Alternatively, run a specific test class by temporarily updating `testng.xml` or by using a targeted surefire invocation:

```bash
mvn test -Dtest=CheckoutTest
```

> Note: Because the Surefire plugin is configured with `<suiteXmlFile>testng.xml</suiteXmlFile>`, the `-Dtest=` flag may be overridden by the suite file. For reliable control of which tests execute, ensure `testng.xml` lists the desired classes.

### Test Credentials
- **Email:** `admin@example.com`
- **Password:** `Admin@123`

## Current Development Status

The **User-facing automation scenarios** documented above represent the current focus of this project. All User-facing Page Objects (`LoginPage`, `HomePage`, `SearchPage`, `ProductDetail`, `CartPage`, `CheckoutPage`, `OrderCustomerPage`) and their corresponding test classes are implemented and documented here.

The **Admin automation testing section** (`com.example.page.admin.*` and `com.example.testing.admin.*`) is still under development and is **not yet complete**. It has been excluded from this README.

## Future Improvements

- Expand negative and edge-case tests across all user flows (e.g., login error scenarios currently commented out in `LoginTest`)
- Improve test-data management by externalizing `@DataProvider` data into JSON or Excel files instead of inline arrays
- Broaden the `testng.xml` suite to include all implemented test classes for full regression runs
- Add test reporting (e.g., Allure or TestNG HTML reports) for richer failure analysis
- Integrate the test suite into a CI/CD pipeline (GitHub Actions) for automated regression on each commit
- Implement the commented-out `Order_Smoke_test` to validate order detail matching (size, quantity, price) against checkout data
