# scalismo-seed.g8
A minimal seed template for a project using [Scalismo](https://github.com/unibas-gravis/scalismo).

Once you have [sbt](http://www.scala-sbt.org/release/tutorial/Setup.html) installed, you can run the command:
~~~
sbt new unibas-gravis/scalismo-seed.g8
~~~

You will be asked for the name of your new project that will be created for you.

Once the project is created, you can also check the general [tutorial](https://scalismo.org/tutorials.html) for a quick tour of Scalismo's capabilities or checkout how to use scalismo in an [IDE](https://scalismo.org/tutorials/ide.html).

### Compiling executable jars
To compile your application as an executable Jar, you should change into the created projects directory, if you have not already. Then you can use the assembly command:
~~~
sbt assembly
~~~
This will dump an executable jar file in the target/scala-2.12/ directory. To run the jar:

~~~
java -jar target/scala-2.12/executable.jar
~~~

The name as well as the Main class to be used for the executable jar can be changed in the [build.sbt](https://github.com/unibas-gravis/minimal-scalismo-seed/blob/master/build.sbt) file
