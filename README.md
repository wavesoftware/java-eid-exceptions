# EID Runtime Exceptions and Utilities

[![Build Status](https://travis-ci.org/wavesoftware/java-eid-exceptions.svg?branch=master)](https://travis-ci.org/wavesoftware/java-eid-exceptions) [![Coverage Status](https://coveralls.io/repos/wavesoftware/java-eid-exceptions/badge.svg?branch=master&service=github)](https://coveralls.io/github/wavesoftware/java-eid-exceptions?branch=master) [![SonarQube Tech Debt](https://img.shields.io/sonar/http/sonar-ro.wavesoftware.pl/pl.wavesoftware:eid-exceptions/tech_debt.svg)](http://sonar-ro.wavesoftware.pl/dashboard/index/2600) [![Dependency Status](https://www.versioneye.com/user/projects/55aafc74306535001b000440/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55aafc74306535001b000440) [![Maven Central](https://img.shields.io/maven-central/v/pl.wavesoftware/eid-exceptions.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22pl.wavesoftware%22%20AND%20a%3A%22eid-exceptions%22)

This small library holds a set of exceptions and utilities that implements idea of fast, reusable, error codes that can be simply thrown fast in case of unpredictable and unrecoverable application failure. It is meant to be used for application bugs.

## Idea

The idea is to use a set of simple runtime exceptions. They should always take the Exception ID (Eid) object in the making. This eid object will then be reported when displaying or logging that exception. It can also be viewed on the professional fatal error window of the application as a bug reference. EidRuntimeExceptions contains also additional unique ID to distinguish each single exception from others with same Eid. This approach simplifies the management of exceptions in the application and allows developers to focus on functionalities rather than coming up with the correct statement for the exception.

This approach is best to use with tools and plugins like:

 * [EidGenerator for Netbeans IDE](http://plugins.netbeans.org/plugin/53137/exception-id-eid-generator)
 * [Generating Exception Id number in Intellij IDEA with Live Templates](https://github.com/wavesoftware/java-eid-exceptions/wiki/Generating%20Exception%20Id%20number%20in%20Intellij%20IDEA%20with%20Live%20Templates)

Example:

```java
throw new EidIllegalStateException("20150721:100554", cause);
```

Example log:

```
pl.wavesoftware.eid.exceptions.EidIllegalStateException: [20150721:100554]<g0qrwx> => Zipfile in invalid format
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57)
	
Caused by: java.util.zip.DataFormatException: Zipfile in invalid format
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:57)
	... 62 more
```


## Caution

This classes shouldn't be used in any public API or library. It is designed to be used for in-house development of end user applications which will report bugs in standardized error pages or post them to issue tracker.

## Maven

```xml
<dependency>
    <groupId>pl.wavesoftware</groupId>
    <artifactId>eid-exceptions</artifactId>
    <version>1.2.0</version>
</dependency>
```

### `EidPreconditions` class

#### General use

`EidPreconditions` class consists static methods that help to use Eid in a method or constructor. This is solely for convenience purposes. Use them to check whether method or constructor was invoked correctly (whether its preconditions have been met). These methods generally accept a `boolean` expression which is expected to be `true` (or in the case of `checkNotNull`, an object reference which is expected to be non-null). When `false` (or `null`) is passed instead, the `EidPreconditions` method throws an unchecked exception, which helps the calling method communicate to its caller that that caller has made a mistake.

Each method accepts a EID string or Eid object, which is designed to ease of use and provide strict ID for given exception usage. This approach speed up development of large application and helps support teams by giving both static and random ID for each possible bug that could occur.

Each example uses static import:

```java
import static pl.wavesoftware.eid.utils.EidPreconditions.*;
```

#### `checkArgument` method

`checkArgument` method should be used to check argument of the method, and validate it in technical terms (not business terms).

Example:

```java
// [..]
public static double sqrt(double value);
  checkArgument(value >= 0.0, "20150718:012333");
  // if ok, calculate the square root
}
```
 
In this example, `checkArgument` throws an `EidIllegalArgumentException` to indicate that developer made an error in its call to `sqrt`. 

#### `checkState` method

`checkState` method should be used to check state of the class in given moment, and validate it in technical terms (not business terms).

Example:

```java
checkState(a >= 3.14 && b < 0., "20150721:115016");
```

#### `checkNotNull` method

`checkNotNull` method should be used to check if given non null argument is actually `null`

Example:

```java
String nonNullUserName = checkNotNull(userName, "20150721:115515");
```

#### `checkElementIndex` method

`checkElementIndex` method can be used to test parameters of an array, before being used

```java
checkElementIndex(index, list.size(), "20150721:115749");
```

#### Formatted message support

From release `1.1.0` there have been added methods to support additional formatted messages for `checkArgument`, `checkState`, `checkNotNull` and `checkElementIndex` method. Those method versions can sometimes be used to pass additional information to exceptions that will be displayed in log files.

Message formatting is done using `String.format(String, Object[])` method.

For example:

```java
checkState(transation.isValid(), "20151119:120238", "Invalid transaction: %s, transaction);
```

Will produce output similar to;

```
pl.wavesoftware.eid.exceptions.EidIllegalStateException: [20151119:120238]<xf4j1l> => Invalid transaction: <Transaction id=null, buyer=null, products=[]>
```
 
#### Functional try to execute blocks
 
You can use functional blocks to handle operations, that are intended to operate properly. This approach simplify the code and makes it more readable. It's also good way to deal with untested, uncovered `catch` blocks. It's easy and gives developers nice way of dealing with countless operations that suppose to work as intended.

There are two versions. One with `UnsafeSupplier` and one with `UnsafeProcedure`. The difference is that unsafe procedure do not return anything.

Example:

```java
InputStream is = EidPreconditions.tryToExecute(new UnsafeSupplier<InputStream>() {
    @Override @Nonnull
    public InputStream get() throws IOException {
        return this.getClass().getClassLoader()
            .getResourceAsStream("project.properties");
    }
}, "20150718:121521");
```

or with Java 8:

```java
import static pl.wavesoftware.eid.utils.EidPreconditions.tryToExecute;
// [..]
InputStream is = tryToExecute(() -> { resource("project.properties"); }, "20150718:121521");
```

#### Logging

Eid object can also be useful in logging. That are `makeLogMessage` method provided to do that. Message formatting is done using `String.format(String, Object[])` method.
For example:

```java
log.debug(new Eid("20151119:121814").makeLogMessage("REST request received: %s", request));
```

will unfold to something similar to:

```
2017-01-08T16:45:34,334 DEBUG [a.b.c.RestBroker] [20151119:121814]<d1afca> REST request received: <RestRequest user=<User id=345> flow=ShowLastTransactions step=Confirm>
```

### Contributing

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

- 1.2.0
  - Major performance tweaks and tests for `EidPreconditions` methods #4 
  - Major performance tweaks and tests for `Eid` class #2 
  - Switched to new OSSRH maven template
  - Switched to Git Flow via jgitflow plugin
- 1.1.0
  - Adding support for formatted messages in exceptions and also in utility methods of `EidPreconditions`  
- 1.0.1
  - Fixed handling for throwables as a cause with `message == null`. `cause.toString()` method is used 
- 1.0.0
  - Support for JDK >= 1.6
- 0.1.0
  - initial release
  - idea imported from Guava Library and COI code
