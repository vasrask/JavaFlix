# JavaFlix
JavaFlix is a Java Swing desktop application that simulates a simplified streaming platform.

## Project structure

- `src/javaflix/` contains the application entry points.
- `src/javaflix/model/` contains domain objects such as movies, series, and users.
- `src/javaflix/service/` contains catalog and user operations.
- `src/javaflix/ui/` contains the Swing interface.

## Build and Run

### Requirements
- Java 17+

### Command line compilation

From the project root, compile all Java sources:

```powershell
javac -d out (Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName)
```

### Run

#### Step 1: Generate the data files

The app stores data using Java serialization. Moving classes into packages changes their serialized class names, so data files created by older versions are not compatible. Back up any existing `users.dat` and `programs.dat` before regenerating them.

Run the following class to create starter data files:

```
java -cp out javaflix.InitializeData
```
This will create `users.dat` and `programs.dat`.
- All movie and series data were sourced from IMDb.
- Modify `src/javaflix/InitializeData.java` to add your own programs.
- Re-run InitializeData to reset the database.

#### Step 2: Run the main application

After generating the data files:
```
java -cp out javaflix.Main
```

## Author

Vasiliki Raskopoulou