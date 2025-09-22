# Backend Setup Instructions

## Prerequisites
- Java 11 or higher
- Maven
- MySQL database

## Step 1: Database Setup
1. Create a MySQL database named `ecom-web`
2. Update database credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecom-web
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

## Step 2: Run the Backend
### Option 1: Using Maven
```bash
mvn spring-boot:run
```

### Option 2: Using IDE
- Open the project in your IDE (IntelliJ IDEA, Eclipse, etc.)
- Run the `EcomWebApplication.java` file

### Option 3: Build and Run JAR
```bash
mvn clean package
java -jar target/ecom-web-0.0.1-SNAPSHOT.jar
```

## Step 3: Verify Backend is Running
The backend will start on `http://localhost:8080`

Test the API:
```bash
curl http://localhost:8080/api/products
```

## Backend Features
- RESTful API endpoints
- JWT authentication
- Product management
- Shopping cart functionality
- Order management
- Stripe payment integration
- Admin and customer roles
- Image upload and serving

## API Endpoints
- Authentication: `/api/login`, `/api/register/customer`, `/api/register/admin`
- Products: `/api/products`, `/api/product/{id}`
- Cart: `/api/cart`, `/api/cart/items`
- Orders: `/api/create`, `/api/my-orders`

## Configuration
- Server port: 8080
- CORS enabled for `http://localhost:3000`
- JWT token expiration: configurable
- Stripe keys: Add your Stripe keys in application.properties

## Troubleshooting
- Check if MySQL is running
- Verify database connection
- Check application logs for errors
- Ensure port 8080 is not in use
