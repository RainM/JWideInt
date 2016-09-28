# JWideInt
Package provides wide-length (128bit) integer class.
Implemented via JNI library written in C++.

##Steps to build:

1. Update JAVA_HOME environment variable if necessary
2. Be sure 'javah' and 'gcc' are available via PATH
3. Run 'mvn install' from shell

C++ build step is integrated in maven via ant build step, which runs make, Therefore, there is no need to run gcc/make explicitly.

## WInt128
general.Mutable 128-bit integer class
