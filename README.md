# ShoeZilla - Online Shoe Store

![ShoeZilla Logo](path/to/logo.png)

ShoeZilla is an online shoe store that allows users to browse, manage, and purchase shoes effortlessly. The application provides a user-friendly interface, a robust backend, and various features for both customers and administrators.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Usage](#usage)
- [Contact](#contact)

## Features

### Customer Features
- **User Account Management**: Register, log in, and manage user accounts.
- **Product Listing**: Browse through a variety of shoes with detailed descriptions.
- **Cart Management**: Add or remove products in your cart and proceed to checkout.
- **Wishlist**: Save favorite products for later purchase.
- **Checkout Options**: Seamless checkout process with multiple payment gateways.
  - **Razorpay**: Secure online payment processing.
  - **Cash on Delivery (COD)**: Pay for products upon delivery.
  - **Wallet Integration**: Manage payments through your wallet.
- **Order Management**: Track orders and download order PDFs.

### Admin Features
- **Admin Dashboard**: Comprehensive overview of store operations.
- **Product Management**: Add, edit, and delete products, including images and descriptions.
- **Category Management**: Organize products into various categories.
- **Coupon Management**: Create and manage discount coupons for customers.
- **Offers Management**: Implement promotional offers for products.
- **User Management**: Monitor and manage customer accounts.

## Technology Stack

- **Backend**: Spring Boot
- **Database**: PostgreSQL
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **Architecture**: MVC (Model-View-Controller)

## Project Structure



- **admin**: Contains all components related to the admin dashboard, including controllers  and views for managing products, categories, coupons, and offers.
- **customer**: Manages customer functionalities, such as user account management, product listings, cart operations, and order processing including controllers  and views .
- **library**: Includes services, repositories, utility classes, configuration files, and exception handling for the application.
- **resources**: Contains application properties, static resources (CSS, JavaScript, images), and templates.
- **src**: The main source code and test directories for the application.
- **pom.xml**: Maven configuration file for managing project dependencies and build settings.


## Installation

To set up ShoeZilla locally, follow these steps:

1. Clone the repository:
```bash
   git clone https://github.com/yourusername/shoezilla.git
  
```

2. Navigate to the project directory:
```bash
cd shoezilla

````
3. Install the required dependencies:
 ```bash
 ./mvnw install

```
4. Configure your application.properties file with your database credentials:
```bash

spring.datasource.url=jdbc:postgresql://localhost:5432/shoezilla
spring.datasource.username=your_username
spring.datasource.password=your_password
```
5. Run the application:
```bash
./mvnw spring-boot:run
```

## Usage

1. Open your web browser and navigate to [http://localhost:8080](http://localhost:8080).
2. Create an account or log in to access the store.
3. Browse products, add items to your cart or wishlist, and proceed to checkout.

## Contact

For any inquiries or feedback, feel free to reach out:

- **Email:** [ivinmanuel007@gmail.com](mailto:ivinmanuel007@gmail.com)
- **GitHub:** [ivinmanuel](https://github.com/ivinmanuel)

