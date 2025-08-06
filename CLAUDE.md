# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

This is a Gradle-based Java project. Use these commands:

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Clean build
./gradlew clean build

# Run the application
./gradlew run
```

## Architecture Overview

This is a custom HTTP server implementation with a Spring-like dependency injection framework. The architecture follows a layered approach:

### Core Components

- **MyServer**: The main HTTP server that listens on port 8080, handles raw socket connections, and parses HTTP requests
- **Dispatcher**: Central request dispatcher that routes requests using handler mappings and adapters (similar to Spring's DispatcherServlet)
- **ApplicationContext**: Custom dependency injection container that scans for components and manages bean lifecycle

### Request Processing Flow

1. `MyServer` accepts socket connections and parses HTTP requests into `HttpRequest` objects
2. `Dispatcher` receives the request and uses `HandlerMapping` to find the appropriate controller method
3. `HandlerAdapter` executes the controller method with proper parameter injection
4. Response is returned as `HttpResponse` and written back to the client

### Annotation System

The framework uses custom annotations similar to Spring:

- `@Component`: Marks classes for dependency injection
- `@Controller`: Marks HTTP request handlers
- `@Post`: Maps POST requests to controller methods
- `@RequestParam`, `@RequestBody`, `@RequestHeader`: Parameter injection annotations

### Key Classes

- `ApplicationContext`: DI container with component scanning using Reflections library
- `AnnotationHandlerMapping`: Maps HTTP requests to controller methods based on annotations
- `AnnotationHandlerAdapter`: Handles method parameter injection and invocation
- `ReflectionJsonParser`: Custom JSON parsing utility

## Development Notes

- The server runs on port 8080 by default
- Component scanning starts from the `org.example` package
- Uses reflection extensively for dependency injection and method parameter binding
- Custom HTTP request/response parsing without external web framework dependencies