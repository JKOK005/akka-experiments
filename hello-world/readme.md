## Hello world example of scala script

Simple starter package for hello world, created using sbt. 

To create a new package, use the following commands:
```
sbt new scala/hello-world.g8
```

This automatically pulls a hello world example from GitHub

To run the script in Main.scala, use 
```
sbt run 
```

We can customize the logging level by changing the build.sbt script. 
To set the respective debug levels, add the following line
```
logLevel := Level.__LogLevel__ 
```

Sbt only accepts the following __LogLevel__:
* Debug
* Info
* Warn 
* Error

