# AppiumFrameworkLocal

A robust and scalable mobile automation framework built using Appium, TestNG, and Java, designed for testing Android applications both locally and on cloud platforms like BrowserStack.

Code
AppiumFrameworkLocal/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── resources/               # Config files and APKs
│   │       └── utils/                   # Appium utilities
│   └── test/
│       └── java/
│           ├── automationtestingedge/   # Test classes
│           └── pageObjects/             # Page object models
├── testNGSuites/
│   └── testng_regression.xml            # TestNG suite config
├── pom.xml                              # Maven build file
└── README.md                            # Project documentation

Run tests:
mvn clean install -PRegression
mvn clean install -PSmoke
mvn clean install -PMaster

Reporting
TestNG HTML reports generated in /test-output/index.html

Includes pass/fail stats, stack traces, and method-level breakdown
