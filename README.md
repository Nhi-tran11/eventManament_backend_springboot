

# EventLime - Spring Boot Backend

This is the backend API for the Event Management web application, built with Spring Boot.

## Features

- RESTful API for event management
- User authentication and authorization
- Event creation, update, deletion
- Booking management
- File upload support

## Prerequisites

- Java 17 or higher
- Maven

## Getting Started

### Clone the repository

```bash
  git clone <https://github.com/Nhi-tran11/eventManament_backend_springboot.git>
  cd EventLime


## Build the project

```bash
./mvnw clean install
```

### Run the application

```bash
./mvnw spring-boot:run
```

The backend will start at http://localhost:8080.

## API Endpoints

- `/api/events` - Manage events
- `/api/bookings` - Manage bookings
- `/api/users` - User operations
- `/api/uploads` - File uploads

## Configuration

Edit `src/main/resources/application.properties` to configure database and other settings.

## Project Structure

- `src/main/java/com/example/EventLime/controller/` - REST controllers
- `src/main/java/com/example/EventLime/service/` - Service layer
- `src/main/java/com/example/EventLime/model/` - Entity models
- `src/main/resources/` - Configuration files
# Event Web - React Frontend

This is the frontend for the Event Management web application, built with [Create React App](https://github.com/facebook/create-react-app).

## Demo

https://github.com/user-attachments/assets/4997dc7a-1d07-4e35-861a-b2ad86cb41be

## Features

- User authentication (Sign Up, Log In)
- Event creation and management
- Booking events
- View event details
- Manage your bookings

## Getting Started

### Prerequisites

- Node.js (v14 or higher recommended)
- npm

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Nhi-tran11/EvenManagement.git
   cd my-react-app
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

### Running the App

Start the development server:
   npm start