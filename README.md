# automated-testing-ozon-example
An example project which shows the abilities of automated testing using JUnit4, Selenium WebDriver and Allure.

Yet there is extensive usage of AJAX by Ozon site, therefore some

DISCLAIMER
-----------
It's known issue that _Test will fail in case if there 2 or more 'delete all' buttons in basket because of dynamic DOM building.
I looking for the issue fix, but I'd be happy if someone could help me with that._

Test do not check if there some items in basket before new test starts, it might be another issue which lead test to fail (if so, manually empty the basket).

This test is just an example for using automated QA tools. I presume you will not use it in any illegal matter.

General setup
----------
The example is set to use Internet Explorer driver, therefore some preinstallation steps required:
1. Download [GeckoDriver](https://github.com/mozilla/geckodriver/releases) to run tests in Firefox
2. Configure `System.setProperty("webdriver.gecko.driver")` in source code `OzonShoppingBasketTest.java` - specify the full path to downloaded InternetExplorerDriver.exe
3. You will need [Maven](https://maven.apache.org/download.cgi) installed on your computer (to check if you have one, run "mvn -version" in command line)
4. Set `JAVA_HOME` system variable to your JDK (Java Developmen Kit) library. If you don't have the JDK yet, it's time to download one. 

NOTE – a path to JDK (not JRE) is requires, e.g. see example below:

![Set JAVA_HOME system variable](http://testercity.ru/wp-content/uploads/2017/11/Java8_JAVA_HOME.png)



Usage
----------
Import into your favourite IDE (e.g., Eclipse or IDEA) and run the project. It configured to trigger the JUnit tests and show you the results.

To just run the JUnit test, open command line, change dir to project folder where you have cloned this repository and where pom.xml is, then run following command:
```shell
mvn clean test
```
 -> Maven will clean up old precompiled code and artefacts, then run tests in browser


Once the tests have passed (or failed :), you can generate Allure report using one of the following command:
```shell
mvn allure:serve
```
 -> Report will be generated into temp folder. Web server with results will start and open in browser.
You can terminate the plugin with a ctrl-c in the terminal window where it is running.


```shell
mvn allure:report
```
 -> Report will be generated tо directory: target/site/allure-report/index.html

This plugin generates Allure report by [existing XML files](https://github.com/allure-framework/allure-core/wiki#gathering-information-about-tests) during the Maven build process.
More details on setup (for new projects only) https://docs.qameta.io/allure/2.0/#_maven_4


To conclude, all-in-one command to prepare maven project, then run tests, then build and show Allure report is:
```shell
mvn clean test allure:serve
```
_This should work in most cases._

