# Eureka Server Setup Guide

## Step 1: Create Eureka Server Project

Create a new Spring Boot project with the following dependencies:

### pom.xml for Eureka Server
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.example</groupId>
    <artifactId>eureka-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>Eureka Server</name>
    <description>Eureka Service Registry</description>
    
    <properties>
        <java.version>17</java.version>
        <spring-cloud.version>2023.0.0</spring-cloud.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

### Main Application Class
```java
package com.example.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
```

### application.yml
```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://localhost:8761/eureka/
      
  server:
    enable-self-preservation: false
    eviction-interval-timer-in-ms: 5000

logging:
  level:
    com.netflix.eureka: DEBUG
    com.netflix.discovery: DEBUG
```

## Step 2: Startup Order

1. **Start Eureka Server** (Port 8761)
   ```bash
   mvn spring-boot:run
   ```
   Access at: http://localhost:8761

2. **Start user-service** (Port 8082)
3. **Start auth-service** (Port 8081)

## Step 3: Verification

After starting all services, visit http://localhost:8761

You should see:
- **AUTH-SERVICE** - localhost:8081
- **USER-SERVICE** - localhost:8082

## Step 4: Test Service Communication

Your auth-service can now call user-service using:

```java
@Autowired
private UserServiceClient userServiceClient;

// This will automatically resolve to http://localhost:8082
Map<String, Object> user = userServiceClient.getUserById(1L);
```

## Troubleshooting

### Service Not Registering
- Check Eureka server is running on 8761
- Verify `defaultZone` URL is correct
- Check network connectivity

### Version Compatibility
Ensure Spring Boot and Spring Cloud versions are compatible:
- Spring Boot 3.2.x → Spring Cloud 2023.0.x
- Spring Boot 3.1.x → Spring Cloud 2022.0.x

### Common Issues
- **Port conflicts**: Ensure 8761 is free
- **Firewall**: Check if ports are blocked
- **Network**: Verify localhost resolution
