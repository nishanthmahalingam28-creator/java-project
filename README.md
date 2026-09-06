# Food Ordering and Management System using Java OOP

## Project Objective

A menu-driven Java console application where customers can register, log in, browse and search food items, manage a cart, place orders, make payments, view order history, and cancel orders. Administrators can manage the food menu, view all orders, update order statuses, view customers, and generate sales summaries.

This project was developed as part of an academic activity on **AI-Assisted Development and Analysis of a Java Console Application using Bolt AI**. It is designed to clearly demonstrate all major Java OOP concepts in a real, working application.

---

## Technologies Used

- **Language:** Java (JDK 8 or higher)
- **Libraries:** Standard Java libraries only (`java.util`, `java.io`, `java.time`, `java.nio.file`, `java.util.stream`)
- **No external dependencies** — no Maven, no Spring Boot, no databases, no GUI frameworks
- **IDE Compatibility:** IntelliJ IDEA, Eclipse, NetBeans

## Java Version

- Developed and tested with **Java 8+**
- Uses lambda expressions and the Stream API (Java 8 feature)

---

## Project Architecture

The application follows a simple layered architecture:

```
Presentation Layer  (Main.java / Console Menus)
        ↓
Service Layer       (UserService, FoodService, OrderService, CartService)
        ↓
Model Layer         (User, Customer, Admin, FoodItem, Cart, Order, etc.)
        ↓
File/Data Layer     (FileManager — reads/writes text files)
```

Each layer has a clear responsibility:
- **Presentation:** Menu display and user interaction
- **Service:** Business logic and data management
- **Model:** Data structures and domain objects
- **File/Data:** Persistence to text files

---

## Package Structure

```
src/
└── com/
    └── foodordering/
        ├── main/
        │   └── Main.java
        ├── model/
        │   ├── User.java
        │   ├── Customer.java
        │   ├── Admin.java
        │   ├── FoodItem.java
        │   ├── CartItem.java
        │   ├── Cart.java
        │   ├── Order.java
        │   └── OrderItem.java
        ├── service/
        │   ├── UserService.java
        │   ├── FoodService.java
        │   ├── OrderService.java
        │   └── CartService.java
        ├── payment/
        │   ├── PaymentMethod.java
        │   ├── UpiPayment.java
        │   ├── CardPayment.java
        │   └── CashPayment.java
        ├── exception/
        │   ├── InvalidInputException.java
        │   ├── FoodNotFoundException.java
        │   ├── InsufficientStockException.java
        │   └── EmptyCartException.java
        ├── util/
        │   ├── InputValidator.java
        │   ├── FileManager.java
        │   └── BillGenerator.java
        └── interface_pkg/
            └── OrderOperations.java
```

---

## Class Descriptions

### Model Package

| Class | Description |
|-------|-------------|
| `User` | Abstract base class with common user properties (userId, name, email, phone, password). Defines abstract `displayUserDetails()`. |
| `Customer` | Extends User. Represents a customer with an address field. Overrides `displayUserDetails()`. |
| `Admin` | Extends User. Represents an administrator with a role field. Overrides `displayUserDetails()`. |
| `FoodItem` | Represents a food menu item with ID, name, category, price, quantity, description, and availability status. |
| `CartItem` | Represents a line item in the cart (FoodItem + quantity). Calculates line totals. |
| `Cart` | Manages a list of CartItems. Supports add, remove, update quantity, view, and total calculation. |
| `Order` | Represents a customer order with order items, date, total, payment status, and order status. |
| `OrderItem` | Represents a line item in an order (snapshot of food name, price, quantity at order time). |

### Service Package

| Class | Description |
|-------|-------------|
| `UserService` | Handles customer registration, login (customer and admin), and user lookup. Uses HashMap for fast email-based lookup. |
| `FoodService` | Manages food items (add, update, delete, search). Demonstrates method overloading and Stream API. |
| `OrderService` | Manages orders (place, cancel, update status). Implements the `OrderOperations` interface. Handles payments via polymorphism. |
| `CartService` | Manages cart operations (add, remove, view). Validates stock and input. |

### Payment Package

| Class | Description |
|-------|-------------|
| `PaymentMethod` | Interface defining `pay()` and `getPaymentType()`. |
| `UpiPayment` | Implements PaymentMethod for UPI payments. |
| `CardPayment` | Implements PaymentMethod for card payments. |
| `CashPayment` | Implements PaymentMethod for cash payments. |

### Exception Package

| Class | Description |
|-------|-------------|
| `InvalidInputException` | Thrown for invalid user input (invalid email, negative quantity, etc.). |
| `FoodNotFoundException` | Thrown when a food item is not found. |
| `InsufficientStockException` | Thrown when requested quantity exceeds available stock. |
| `EmptyCartException` | Thrown when trying to place an order with an empty cart. |

### Util Package

| Class | Description |
|-------|-------------|
| `InputValidator` | Validates all user inputs (name, email, phone, password, price, quantity, menu choices). |
| `FileManager` | Handles all file I/O (reading/writing customers, food items, orders, and bills). |
| `BillGenerator` | Generates formatted bills after successful orders. |

### Interface Package

| Class | Description |
|-------|-------------|
| `OrderOperations` | Interface defining standard order operations (place, cancel, history, all orders). Implemented by `OrderService`. |

---

## OOP Concepts Used

| # | Concept | Where it is demonstrated |
|---|---------|------------------------|
| 1 | **Classes** | Every class in the project (User, FoodItem, Order, etc.) |
| 2 | **Objects** | Instances created in Main.java and service classes (e.g., `new Customer(...)`, `new Cart()`) |
| 3 | **Constructors** | `User(int, String, String, String, String)`, `FoodItem(...)`, `Order(int, Customer)`, `Cart()` |
| 4 | **Encapsulation** | All model fields are private with public getters/setters. Setters validate inputs (e.g., `FoodItem.setPrice()` rejects negative values) |
| 5 | **Inheritance** | `Customer extends User`, `Admin extends User` |
| 6 | **Polymorphism** | `PaymentMethod payment = new UpiPayment(); payment.pay(amount);` — same reference, different implementations (runtime polymorphism in `OrderService.processPayment()`) |
| 7 | **Method Overloading** | `FoodService.addFoodItem(FoodItem)` and `FoodService.addFoodItem(String, String, double, int, String)`; `FoodService.searchFood(String)` and `FoodService.searchFood(double)` |
| 8 | **Method Overriding** | `Customer.displayUserDetails()` and `Admin.displayUserDetails()` override `User.displayUserDetails()` with `@Override` |
| 9 | **Abstraction** | `User` is an abstract class with abstract method `displayUserDetails()` — subclasses must implement it |
| 10 | **Interfaces** | `PaymentMethod` interface (pay, getPaymentType) and `OrderOperations` interface (placeOrder, cancelOrder, getOrderHistory, getAllOrders) |
| 11 | **Packages** | `com.foodordering.model`, `com.foodordering.service`, `com.foodordering.payment`, `com.foodordering.exception`, `com.foodordering.util`, `com.foodordering.interface_pkg`, `com.foodordering.main` |
| 12 | **Exception Handling** | Custom exceptions (InvalidInputException, FoodNotFoundException, InsufficientStockException, EmptyCartException) with try-catch throughout the app |
| 13 | **Collections** | `ArrayList<FoodItem>`, `ArrayList<Order>`, `ArrayList<CartItem>`, `HashMap<Integer, FoodItem>`, `HashMap<String, User>` |
| 14 | **Generics** | `List<FoodItem>`, `List<Order>`, `Map<Integer, FoodItem>`, `List<Customer>` — all collections use generic type parameters |
| 15 | **File Handling** | `FileManager` uses `BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`, and `java.nio.file.Files` to persist customers, food items, orders, and bills |
| 16 | **Stream Processing** | `FoodService.searchFood()` uses `stream().filter()`, `FoodService.sortByPrice()` uses `stream().sorted()`, `OrderService.getOrderHistory()` uses `stream().filter().collect()`, sales summary uses `stream().mapToDouble().sum()` and `stream().count()` |

---

## How to Compile

### From the project root directory:

```bash
# Compile all Java files
javac -d bin src/com/foodordering/exception/*.java src/com/foodordering/interface_pkg/*.java src/com/foodordering/payment/*.java src/com/foodordering/model/*.java src/com/foodordering/util/*.java src/com/foodordering/service/*.java src/com/foodordering/main/*.java
```

### In IntelliJ IDEA / Eclipse / NetBeans:
1. Open the project folder
2. The IDE will automatically detect the source structure
3. Build the project (Build → Build Project)

---

## How to Run

### From the command line:

```bash
java -cp bin com.foodordering.main.Main
```

### In an IDE:
Run the `Main.java` file (it contains the `main` method).

---

## Demo Login Credentials

> **NOTE:** This is an academic console application. Passwords are stored in plain text for demonstration purposes only. Do NOT use real credentials.

### Admin Login
- **Email:** `admin@foodordering.com`
- **Password:** `Admin@123`

### Customer Login
- Register a new customer account (option 2 in the main menu) or use any previously registered account.

---

## Sample Execution

```
========================================
  FOOD ORDERING & MANAGEMENT SYSTEM
  (Academic Java OOP Demo Project)
========================================

========================================
              MAIN MENU
========================================
1. Customer Login
2. Customer Registration
3. Admin Login
4. Exit
========================================
Enter your choice: 2

========================================
       CUSTOMER REGISTRATION
========================================
Enter your name: Rahul Sharma
Enter your email: rahul@example.com
Enter your phone (10 digits): 9876543210
Enter password (min 5 chars): pass123
✓ Registration successful! Your Customer ID is: 1001

========================================
              MAIN MENU
========================================
1. Customer Login
2. Customer Registration
3. Admin Login
4. Exit
========================================
Enter your choice: 1

========================================
          CUSTOMER LOGIN
========================================
Enter email: rahul@example.com
Enter password: pass123
✓ Login successful! Welcome, Rahul Sharma!

========================================
          CUSTOMER MENU
========================================
1.  View Food Menu
2.  Search Food
3.  Add Food to Cart
4.  Remove Food from Cart
5.  View Cart
6.  Place Order
7.  Make Payment
8.  View Order History
9.  Cancel Order
10. View Profile
11. Logout
========================================
Enter your choice: 1

========================================
             FOOD MENU
========================================
ID     Name                 Category        Price      Qty      Status
----------------------------------------
1      Chicken Biryani      Main Course     ₹150.00   20       Available
2      Veg Biryani          Main Course     ₹120.00   15       Available
3      Fried Rice           Main Course     ₹100.00   25       Available
...
========================================

Enter your choice: 3
Enter Food ID to add to cart: 1
Enter quantity: 2
✓ Chicken Biryani (x2) added to cart.

Enter your choice: 6
✓ Order placed successfully! Order ID: 1001

Enter your choice: 7

========================================
              PAYMENT
========================================
Order Total: ₹315.00
----------------------------------------
1. UPI
2. Card
3. Cash
========================================
Select payment method (1-3): 1
✓ Payment Successful!
  Payment ID   : UPI-A1B2C3D4
  Method       : UPI
  Status       : SUCCESS

========================================
          FOOD ORDER BILL
===============

Order ID       : 1001
Customer Name  : Rahul Sharma
Date           : 06-09-2026

---

Item                  Qty       Price

Chicken Biryani       2         ₹150.00
------------------------------

Subtotal                  : ₹300.00
Tax (5%)                  : ₹15.00
Grand Total               : ₹315.00

Payment Method            : UPI
Payment Status            : SUCCESS
Order Status              : PLACED

========================================
       THANK YOU FOR ORDERING
======================
```

---

## Testing Scenarios

| # | Scenario | How to Test |
|---|----------|-------------|
| 1 | Customer registration | Main Menu → 2. Customer Registration → Enter valid details |
| 2 | Customer login | Main Menu → 1. Customer Login → Enter registered email and password |
| 3 | Invalid login | Enter wrong email or password → Should show error message |
| 4 | View menu | Customer Menu → 1. View Food Menu |
| 5 | Search food | Customer Menu → 2. Search Food → Search by name or price |
| 6 | Add food to cart | Customer Menu → 3. Add Food to Cart → Enter food ID and quantity |
| 7 | Remove food from cart | Customer Menu → 4. Remove Food from Cart → Enter food ID |
| 8 | Empty cart checkout | Add nothing to cart → 6. Place Order → Should show EmptyCartException |
| 9 | Insufficient stock | Add more quantity than available → Should show InsufficientStockException |
| 10 | Successful order | Add items → Place Order → Should generate order |
| 11 | UPI payment | Place order → 7. Make Payment → Select 1 (UPI) |
| 12 | Card payment | Place order → 7. Make Payment → Select 2 (Card) |
| 13 | Cash payment | Place order → 7. Make Payment → Select 3 (Cash) |
| 14 | Order cancellation | Customer Menu → 9. Cancel Order → Enter order ID |
| 15 | Admin add food | Admin Menu → 1. Add Food Item → Enter details |
| 16 | Admin update food | Admin Menu → 3. Update Food Item → Enter ID and new details |
| 17 | Admin delete food | Admin Menu → 4. Delete Food Item → Enter ID |
| 18 | Invalid input | Enter letters when numbers expected → Should show InvalidInputException |
| 19 | File loading | Close app, reopen → Data should load from files |
| 20 | File saving | Make changes → Check data/ folder for updated files |

---

## Limitations

1. **No password hashing** — passwords are stored in plain text (academic demo only)
2. **No real payment gateway** — payments are simulated
3. **Single-user console** — only one user can use the app at a time
4. **Text file storage** — data is stored in simple text files, not a database
5. **No concurrent access** — not designed for multi-user environments
6. **No GUI** — console-only interface

---

## Future Enhancements

1. **Database integration** — migrate from text files to a relational database
2. **Password hashing** — use BCrypt or similar for secure password storage
3. **Multi-user support** — add networking for concurrent users
4. **GUI interface** — add a JavaFX or Swing-based frontend
5. **Real payment integration** — integrate with actual payment gateways
6. **Email notifications** — send order confirmation emails
7. **Delivery tracking** — add real-time delivery tracking
8. **Rating and reviews** — allow customers to rate food items
9. **Loyalty program** — add points and rewards for repeat customers
10. **Analytics dashboard** — graphical sales analytics for admins
