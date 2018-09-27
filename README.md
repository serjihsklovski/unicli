# Unicli

*Unicli* (pronounced as "uniquely") is a frontend framework for building CLI applications in Java. Based on use of
annotations, Unicli makes it much easier to create multifunctional CLIs.

## Installation

To add Unicli in your projects you can use [JitPack](https://jitpack.io). A Gradle build script example:
```groovy
// ...

allprojects {
    repositories {
        // ...
        maven {
            url 'https://jitpack.io'
        }
    }
}

dependencies {
    compile 'com.github.serjihsklovski:unicli:0.3.0'
}

// ...
```

## Getting Started

The main Unicli concepts are:
* *Task* &#8212; a class that is considered as a function with a set of usage rules;
* *Usage* &#8212; a method that is considered as a single action that could be performed conforming to some rules
(given arguments, flags, and parameters).

The task class contains a set of usage methods. To execute a task you need to specify its name as the first JAR
argument:
```
$ java -jar your-app.jar your-task
```

If the task is not specified, Unicli will try to invoke the *root task*.

### Flags

A set of flags might either specify different usages, or be considered as the logical (boolean) parameters to the
usages. To define the usage flags, use the `@Usage`'s `flags` property, and `@Flag` annotation to assign an array to it.

For example:
```java
// [some task class context]
// ...
@Usage(flags = {
        @Flag("red"),
        @Flag("blue")
})
public static void mixRedAndBlueColors() {
    System.out.println("red + blue = violet");
}
// ...
```

To call this usage method, specify both these flags in the CLI invocation:
```
$ java -jar your-app.jar your-task --red --blue
red + blue = violet
```

The order of the flags in the CLI is not important.

Also, you can define usage flags as method's `boolean`/`Boolean` parameters:

```java
// [some task class context]
// ...
@Usage
public static void performSomeAutomatedProcess(
        @Flag("verbose") boolean verbose,
        @Flag("send-email-report") boolean sendEmailReport) {

    if (verbose) {
        // [logging some debug messages]
    }

    // [some actions]

    if (sendEmailReport) {
        // [sending an email]
    }
}
// ...
```

In this case, you can combine both flags as you want. For example, you want to send the email, but not to see the debug
messages:
```
$ java -jar your-app.jar your-task --send-email-report
```

And this sets the usage method's parameter `verbose` to `false` (as it is not specified in CLI), and parameter
`sendEmailReport` to `true`.

### Creating a Unicli Application

Let's create a simple Unicli application. It will define the root task and its single usage.

#### Project Structure
```
demoapp
├── gradle/wrapper
|   ├── gradle-wrapper.jar
|   └── gradle-wrapper.properties
├── src/main/java/com/someone/demoapp
|   ├── cli
|   |   ├── RootTask.java
|   |   └── DisplayVersionTask.java
|   └── Application.java
├── build.gradle
├── gradlew
├── gradlew.bat
└── settings.gradle
```

#### `build.gradle`
```groovy
plugins {
    id 'java'
    id 'application'
}

group = 'com.someone.demoapp'
version = '0.1.0'

sourceCompatibility = JavaVersion.VERSION_1_8
targetCompatibility = JavaVersion.VERSION_1_8

mainClassName = 'com.someone.demoapp.Application'

jar {
    manifest.attributes 'Main-Class': mainClassName

    from {
        configurations.compile.collect {
            it.isDirectory() ? it : zipTree(it)
        }
    }
}

allprojects {
    repositories {
        maven {
            url 'https://jitpack.io'
        }
    }
}

dependencies {
    compile 'com.github.serjihsklovski:unicli:0.3.0'
}
```

#### `settings.properties`
```groovy
rootProject.name = 'demoapp'
```

#### `src/main/java/com/someone/demoapp/Application.java`
```java
package com.someone.demoapp;

import com.serjihsklovski.unicli.Unicli;

public class Application {

    public static void main(String[] args) {
        Unicli.run("com.someone.demoapp.cli", args);
    }

}
```

#### `src/main/java/com/someone/demoapp/cli/RootTask.java`
```java
package com.someone.demoapp.cli;

import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task(root = true)
public class RootTask {

    @Usage
    public static void printWelcomeMessage() {
        System.out.println("Welcome to Unicli!");
    }

}
```

#### `src/main/java/com/someone/demoapp/cli/DisplayVersionTask.java`
```java
package com.someone.demoapp.cli;

import com.serjihsklovski.unicli.annotation.Flag;
import com.serjihsklovski.unicli.annotation.Task;
import com.serjihsklovski.unicli.annotation.Usage;

@Task("version")
public class DisplayVersionTask {

    @Usage
    public static void displayVersion(@Flag("verbose") boolean verbose) {
        if (verbose) {
            System.out.println("Demo Application v0.1.0, 2018.09");
        } else {
            System.out.println("0.1.0");
        }
    }

}
```

Now let's build the project (it will be a fat JAR) and run it. By default the root
task's `printWelcome` usage method will be performed.
```
$ ./gradlew build
$ java -jar build/libs/demoapp-0.1.0.jar
Welcome to Unicli!
```

But you can also print your application's version:
```
$ java -jar build/libs/demoapp-0.1.0.jar version
0.1.0
```

Or, with the `--verbose` flag specified:
```
$ java -jar build/libs/demoapp-0.1.0.jar version --verbose
Demo Application v0.1.0, 2018.09
```
