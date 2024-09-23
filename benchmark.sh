#!/bin/bash
# The recommended way to run a JMH benchmark is to use Maven to setup a standalone project that depends on the jar files of your application.
# This approach is preferred to ensure that the benchmarks are correctly initialized and produce reliable results.
# It is possible to run benchmarks from within an existing project, and even from within an IDE,
# however setup is more complex and the results are less reliable.

main() {
  mvn package
  java -jar target/benchmarks.jar Main
}

main
