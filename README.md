# Sawaglabs
 https://www.saucedemo.com/index.html
 
 FrameWork : TestNG-Hybrid Framework
---------------------------------------------------

1)Download the Project from github
2)Go to eclipse -> rightclick -> import project-> install maven project -> Next -> Finish
3)Make sure the eclipse should be in Java Prospective
4)Please update the class path for FireFox browser to run in 
    C:\Users\manish\git\Sawaglabs2\jtafi18n\src\main\java\com\factories\WebDriverFactory.java
    
    Example: "C:\\work_new\\geckodriver.exe"
    
5) And also in eclipse please install TestNg ,SELENIUM-JAVA webdrivers 
     Project ->Buildpath ->configure buildpath ->libraries ->Add external jars ->upload all Selenium-Java webdrivers
6)After all project setup make sure to have
    right click on Pom.xml ->Run as ->Maven Clean
    right click on Project ->Maven ->Update project
    
7)Final Step : Right click on testNGSwagLabs.xml ->run as ->TestNG Suite
    
    
