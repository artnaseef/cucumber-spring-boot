# Cucumber in Spring Boot Application

Running Cucumber within a Spring Boot application is tricky becuase cucumber-java performs its own
class scanning, and translation between paths and class names.  With Spring boot's nested JAR-in-a-jar
and BOOT-INF/classes path, getting Cucumber's scanning to work within a Spring Boot application is
tricky.

NOTE: This is not an example of using Cucumber to integration test a Spring Boot application.  Rather,
it is the integration necessary to use the Cucumber runtime within a Spring Boot application itself
(i.e. scope=runtime, not scope=test).


## To Use

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    SpringBootCucumberResourceLoader resourceLoader = new SpringBootCucumberResourceLoader(classLoader);
    ClassFinder classFinder = new SpringBootCucumberResourceLoader(classLoader);

    String[] args = { "--glue", "classpath:com.artnaseef.cucumber.springboot.example" };
    RuntimeOptions runtimeOptions = new RuntimeOptions(Arrays.asList(args));

    cucumber.runtime.Runtime runtime = new Runtime(resourcesLoader, classFinder, classLoader, runtimeOptions);
