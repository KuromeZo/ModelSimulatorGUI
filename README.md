# Economic Model Simulator

A Java-based economic modeling application that allows users to load economic data, run simulation models, and execute custom Groovy scripts for advanced calculations. The application features a user-friendly GUI built with Swing for interactive data analysis and visualization.

## Features

### 📊 Economic Data Management
- Load economic data from text files with structured format
- Support for multiple years of economic indicators
- Automatic data parsing and validation
- Dynamic variable binding using annotations

### 🔧 Model Simulation
- Built-in economic model with key indicators:
  - **PKB** (GDP) - Gross Domestic Product
  - **KI** - Capital Investment
  - **KS** - Consumer Spending
  - **INW** - Investments
  - **EKS** - Exports
  - **IMP** - Imports
- Automatic calculation of economic relationships
- Time-series data processing for multiple years

### 📝 Groovy Scripting Engine
- Execute custom Groovy scripts for advanced calculations
- Built-in script editor with syntax highlighting
- Load scripts from external files
- Dynamic variable access from the economic model
- Real-time script execution and result display

### 🖥️ Interactive GUI
- User-friendly Swing-based interface
- Tabular data display with automatic formatting
- Real-time results visualization
- Integrated script editor
- File selection dialogs for data and script loading

## Requirements

### System Requirements
- Java 23 or higher
- Maven 3.6+ for building
- Windows, macOS, or Linux

### Dependencies
- **Groovy 3.0.8** - For scripting engine
- **Java Swing** - For GUI (included in JDK)
- **Java Reflection API** - For dynamic binding (included in JDK)

## Installation

1. **Clone the repository:**
```bash
git clone link
cd economic-model-simulator
```

2. **Build with Maven:**
```bash
mvn clean compile
```

3. **Run the application:**
```bash
mvn exec:java -Dexec.mainClass="org.example.ModelSimulatorGUI"
```

Alternatively, you can run directly from your IDE by executing the `ModelSimulatorGUI.main()` method.

## Usage

### Getting Started
1. **Launch Application:** Run the main class `ModelSimulatorGUI`
2. **Load Data:** Click "Load Data" and select a data file (e.g., `data1.txt`, `data2.txt`, or `data3.txt`)
3. **Run Model:** Click "Run Model" to execute the economic simulation
4. **View Results:** Examine the calculated results in the table

### Data File Format
Data files should follow this structure:
```
LATA    2015 2016 2017 2018 2019
twKI    1.03 1.05 1.07
twKS    1.04
twINW   1.12
twEKS   1.13
twIMP   1.14
KI      1023752.2
KS      315397
INW     348358
EKS     811108.6
IMP     784342.4
```

Where:
- **LATA** - Years (timeline)
- **tw*** - Growth rates (time-varying coefficients)
- **Economic indicators** - Base values for the first year

### Using Groovy Scripts

#### Loading External Scripts
1. Click "Load Script" and select a `.groovy` file
2. Click "Run Script" to execute the loaded script
3. Results will be automatically updated in the table

#### Creating Custom Scripts
1. Click "Create Script" to open the script editor
2. Write your Groovy code using available variables:
   - `LL` - Number of years
   - `KI`, `KS`, `INW`, `EKS`, `IMP` - Economic indicator arrays
   - `PKB` - GDP array
3. Click "Run Script" to execute and see results

#### Example Script
```groovy
// Calculate export-to-GDP ratio
ZDEKS = new double[LL]
for (i = 0; i < LL; i++) {
    ZDEKS[i] = EKS[i] / PKB[i]
}
```

## Project Structure

```
economic-model-simulator/
├── pom.xml                           # Maven configuration
├── data1.txt, data2.txt, data3.txt   # Sample economic data
├── script1.groovy                    # Sample Groovy script
├── src/main/java/org/example/
│   ├── Main.java                     # Main entry point
│   ├── ModelSimulatorGUI.java        # GUI application
│   ├── Controller.java               # Data and model controller
│   ├── Model1.java                   # Economic model implementation
│   └── Bind.java                     # Annotation for field binding
└── .idea/                            # IntelliJ IDEA configuration
```

## Architecture

### Key Components

#### Model1.java
- Contains economic variables annotated with `@Bind`
- Implements the core economic simulation logic
- Supports dynamic variable creation through Groovy scripts
- Manages time-series calculations

#### Controller.java
- Handles data loading from text files
- Manages Groovy script execution
- Provides data binding between files and model
- Formats output data for display

#### ModelSimulatorGUI.java
- Swing-based user interface
- Handles user interactions and file operations
- Manages table display and script editor
- Coordinates between UI and business logic

#### Bind Annotation
- Custom annotation for automatic field binding
- Enables dynamic data loading from configuration files
- Supports different data types (arrays, primitives)

## Economic Model Details

The application implements a simplified economic model where:

1. **Base Year Calculation:**
   ```
   PKB[0] = KI[0] + KS[0] + INW[0] + EKS[0] - IMP[0]
   ```

2. **Subsequent Years:**
   ```
   KI[t] = twKI[t] * KI[t-1]
   KS[t] = twKS[t] * KS[t-1]
   INW[t] = twINW[t] * INW[t-1]
   EKS[t] = twEKS[t] * EKS[t-1]
   IMP[t] = twIMP[t] * IMP[t-1]
   PKB[t] = KI[t] + KS[t] + INW[t] + EKS[t] - IMP[t]
   ```

Where `tw*` variables represent growth/change coefficients for each economic indicator.

## Groovy Integration

### Available Variables
When writing Groovy scripts, you have access to:
- `LL` - Number of years in the dataset
- `KI[]` - Capital Investment array
- `KS[]` - Consumer Spending array
- `INW[]` - Investment array
- `EKS[]` - Export array
- `IMP[]` - Import array
- `PKB[]` - GDP array (calculated by model)

### Script Capabilities
- Create new calculated variables
- Perform complex mathematical operations
- Access Java libraries and functions
- Modify existing data arrays
- Generate derived economic indicators

## Building and Development

### Maven Commands
```bash
# Clean and compile
mvn clean compile

# Run tests (if any)
mvn test

# Package the application
mvn package

# Run with specific main class
mvn exec:java -Dexec.mainClass="org.example.ModelSimulatorGUI"
```

### IDE Setup
The project includes IntelliJ IDEA configuration files in the `.idea/` directory. Simply open the project in IntelliJ IDEA for full IDE support.

## Technical Notes

### Data Handling
- The application automatically handles missing data by carrying forward the last known value
- Growth rates (tw* variables) are applied multiplicatively year-over-year
- All calculations maintain double precision for accuracy

### Error Handling
- Comprehensive error handling for file loading operations
- Script execution errors are caught and displayed to the user
- Data validation ensures model consistency

## Educational Use

This application is designed for educational purposes in:
- Economic modeling and simulation
- Java application development
- Groovy scripting integration
- GUI programming with Swing
- Data processing and analysis

---

**Happy Economic Modeling! 📈💼**
