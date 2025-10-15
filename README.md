# JavaFlix
JavaFlix is a Java-based console application that simulates a simplified streaming platform.

## Build and Run

### Requirements
- Java 17+

### Command line compilation
```
javac -d out src/*.java
```
### Run

#### Step 1: Generate the data files

Run the following class once to create users.dat and programs.dat:
```
java -cp out InitializeData
```
This will create `users.dat` and `programs.dat`.
- All movie and series data were sourced from IMDb.
- Modify InitializeData.java to add your own programs.
- Re-run InitializeData to reset the database.

#### Step 2: Run the main application

After generating the data files:
```
java -cp out Main
```

## Author

Vasiliki Raskopoulou