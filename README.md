# Installations:
- Download code editor - IntelliJ
- Add JDK 21 - corretto-21 (Amazon Corretto 21.0.4)
- Goto `pom.xml` and refresh maven to install the external libraries

# Run test: 
- Goto `testRunner.xml` 
- Change the value of `productName` param to see that the code is dynamic for different products
- Right click and run the `Run ...\src\test\testRunner.xml` command
- You can use the user-friendly run buttons on IntelliJ
- Page object model is implemented in the project

## Notes: 
- `https://www.amazon.com/` retail website is used to create the test case script
- Some products don't include exact product name key in the title because of the nature of search on the website. Related/similar words used instead.
- I printed out the titles not matching the exact search on the console. Please check
- Sometimes Amazon wants you to verify that you are not robot. Annoying :)
