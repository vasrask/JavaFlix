# JavaFlix
JavaFlix is a JavaFX desktop application that simulates a simplified streaming platform.

## Project structure

- `src/javaflix/` contains the application entry points.
- `src/javaflix/model/` contains domain objects such as movies, series, and users.
- `src/javaflix/service/` contains catalog and user operations.
- `src/javaflix/ui/` contains the JavaFX application interface.
- `src/main/resources/javaflix/ui/` contains the JavaFX stylesheet.

## Build and Run

### Requirements
- Java 17+
- Maven 3.8+

### Run the application

From the project root:

```powershell
New-Item -ItemType Directory -Force run-data | Out-Null
mvn compile
# If needed, initialize sample data (run from run-data so data stays separate)
Push-Location run-data
java -cp ..\target\classes javaflix.InitializeData
Pop-Location
mvn javafx:run "-Djavafx.workingDirectory=run-data"
```

The app stores user and catalog data in `run-data\users.dat` and `run-data\programs.dat`. Keep the two files together.

- All movie and series data were sourced from IMDb.
- Modify `src/javaflix/InitializeData.java` to change the sample catalog.

### Tests

Run the automated model and catalog tests from the project root:

```powershell
mvn test
```

## Author

Vasiliki Raskopoulou