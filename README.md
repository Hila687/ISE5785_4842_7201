# ISE5785 Mini Project – Java Ray Tracer

This is a mini-project for the *Introduction to Software Engineering* course at jct.  
The project focuses on implementing a basic Java ray tracer and gradually extending its capabilities through structured development stages.

## Project Structure

This ray tracer was developed in Java using principles of clean code and object-oriented design. The project includes:

- Primitives (e.g., Point, Vector, Ray, Color)
- Geometries (Sphere, Triangle, Plane, Polygon, etc.)
- Scene and Camera setup
- Image rendering and output
- Lighting and material models
- Shadowing, reflection and refraction
- Performance and image-quality enhancements

## Implemented Stages

### Stage 1–5
- Implementation of primitives, basic geometries, camera, renderer, scene, and lighting.
- Unit tests using JUnit.

### Stage 6
- Added material support (diffuse, specular, shininess).
- Implemented the Phong lighting model.
- Support for ambient, directional, point, and spot lights.
- Multi-light support.

### Stage 7
- Added soft shadows using shadow rays and transparency calculation.
- Implemented reflection and refraction (basic transparency).
- Bonus: Combined scene with dozens of objects, viewed from multiple angles.
- Bonus: Shadow ray maximum distance handling.

### Performance Improvements
- Implemented BVH (Bounding Volume Hierarchy) for faster ray-geometry intersection.
- Added support for multi-threading (raw threads and parallel streams).
- Integrated `PixelManager` for synchronized pixel access and progress indication.

## Multi-threading

This project supports several rendering modes:
- **Single-threaded rendering** (default)
- **Parallel stream rendering**
- **Raw threads rendering** (using `PixelManager`)
The number of threads and debug print frequency can be configured via the `Camera.Builder` class.

## Code Style and Design

The code adheres to Java conventions:
- CamelCase naming
- Proper indentation and block formatting
- JavaDoc documentation for all public classes, fields, and methods
- Project built with modularity and responsibility-driven design (RDD)

## Running the Project

- Ensure you have **Java 21+** installed.
- Clone the repository.
- Build and run using IntelliJ IDEA or your preferred IDE.
- To render scenes, use the `main()` method in your runner class.

## Documentation

- JavaDoc is generated under the `/doc` folder (excluded in `.gitignore`).
- To regenerate the documentation:

## Contributors

This project was developed by:
- Hila Rosental
- Hila Miller

Under the supervision of:
- Eliezer Gensburger
- Dan Zilberstein

---

> Final project for course ISE5785 – Introduction to Software Engineering and Computer Graphics

