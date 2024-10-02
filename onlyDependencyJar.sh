# Use for compile only the jar with dependencies. This increase compile time because you don't are compiling 2 jars (with and without dependencies)
mvn compile assembly:single