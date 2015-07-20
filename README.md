# EID Runtime Exceptions and Utilities

[![Build Status](https://travis-ci.org/wavesoftware/java-eid-exceptions.svg?branch=master)](https://travis-ci.org/wavesoftware/java-eid-exceptions) [![Coverage Status](https://coveralls.io/repos/wavesoftware/java-eid-exceptions/badge.svg?branch=master&service=github)](https://coveralls.io/github/wavesoftware/java-eid-exceptions?branch=master) [![SonarQube Tech Debt](https://img.shields.io/sonar/http/sonar-ro.wavesoftware.pl/pl.wavesoftware:eid-exceptions/tech_debt.svg)](http://sonar-ro.wavesoftware.pl/dashboard/index/2600) [![Dependency Status](https://www.versioneye.com/user/projects/55aafc74306535001b000440/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55aafc74306535001b000440) [![Maven Central](https://img.shields.io/maven-central/v/pl.wavesoftware/eid-exceptions.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22pl.wavesoftware%22%20AND%20a%3A%22eid-exceptions%22)

This small library holds a set of Exceptions that implements idea of fast, reusable, error codes that can be simple thrown fast in case of unpredictable and unrecoverable application failure.

## Idea

The idea is to have a set of simple runtime exceptions. They should always take the field Exception ID (Eid) in the making. This field will then be reported when displaying or logging that exception. It can also be viewed on the professional fatal error window of the application as a bug reference. This approach simplifies the management of exceptions in the application and allows developers to focus on functionalities rather than coming up with the correct statement for the exception.

This approach is best to use with tools and plugins like:

 * [EidGenerator for Netbeans IDE](http://plugins.netbeans.org/plugin/53137/exception-id-eid-generator)
 * [Generating Exception Id number in Intellij IDEA with Live Templates](https://github.com/wavesoftware/java-eid-exceptions/wiki/Generating%20Exception%20Id%20number%20in%20Intellij%20IDEA%20with%20Live%20Templates)

## Caution

This classes shouldn't be used in any public API or library. It is designed to be used for in-house development of end user applications which will report bugs in standardized error pages or post them to issue tracker.

## Maven

```xml
<dependency>
    <groupId>pl.wavesoftware</groupId>
    <artifactId>eid-exceptions</artifactId>
    <version>1.0.0</version>
</dependency>
```

### `EidPreconditions` class

#### General use

Static convenience methods that help a method or constructor check whether it was invoked correctly (whether its preconditions have been met). These methods generally accept a `boolean` expression which is expected to be `true` (or in the case of `checkNotNull`, an object reference which is expected to be non-null). When `false` (or `null`) is passed instead, the `EidPreconditions` method throws an unchecked exception, which helps the calling method communicate to its caller that that caller has made a mistake.

Each method accepts a EID string or Eid object, which is designed to ease of use and provide strict ID for given exception usage. This approach speed up development of large application and helps support teams by giving both static and random ID for each possible unpredicted bug.

Example:

```java
/**
 * Returns the positive square root of the given value.
 * 
 * @param value value to be square rooted
 * @return the square root of input value
 * @throws EidIllegalArgumentException if the value is negative
 */
public static double sqrt(double value) {
  EidPreconditions.checkArgument(value >= 0.0, "20150718:012333");
  // calculate the square root
}

void exampleBadCaller() {
  // will throw EidIllegalArgumentException with "20150718:012333" as ID
  double d = sqrt(-1.0);
}
```
 
In this example, `checkArgument` throws an `EidIllegalArgumentException` to indicate that `exampleBadCaller` made an error in its call to sqrt. Exception, when it will be printed will contain user given EID and also randomly generated ID. Those fields can be displayed to end user on error page on posted directly to issue tracker.

Example:

```java
// Main application class for ex.: http servlet
try {
    performRequest(request, response);
} catch (EidRuntimeException ex) {
    issuesTracker.putIssue(ex);
    throw ex;
}
```
 
#### Functional try to execute blocks
 
Using functional blocks to handle operations, that are intended to operate properly, simplify the code and makes it more readable. It's also good way to deal with untested, uncovered `catch` blocks. It's easy and gives developers nice way of dealing with countless operations that suppose to work as intended.

Example:

```java
InputStream is = EidPreconditions.tryToExecute(new RiskyCode<InputStream>() {
    @Override
    public InputStream execute() throws IOException {
        return this.getClass().getClassLoader()
            .getResourceAsStream("project.properties");
    }
}, "20150718:121521");
```

###Contributing

Contributions are welcome!

To contribute, follow the standard [git flow](http://danielkummer.github.io/git-flow-cheatsheet/) of:

1. Fork it
1. Create your feature branch (`git checkout -b feature/my-new-feature`)
1. Commit your changes (`git commit -am 'Add some feature'`)
1. Push to the branch (`git push origin feature/my-new-feature`)
1. Create new Pull Request

Even if you can't contribute code, if you have an idea for an improvement please open an [issue](https://github.com/wavesoftware/java-eid-exceptions/issues).

## Requirements

* JDK >= 1.6

### Releases

- 1.0.0
 - Support for JDK >= 1.6
- 0.1.0
 - initial release
 - idea imported from Guava Library and COI code
