#g++ wideint_WInt.cpp -shared -I/usr/lib/jvm/java-8-openjdk-amd64/include/ -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux -o libwideint.so -fPIC

CLASS_OUT_DIR=target/classes
CPP_SRC_PATH=src/main/cpp
CPP_ALL_SRC=$(wildcard $(CPP_SRC_PATH)/*.cpp)
CXX=g++
CXXFLAGS?=-O3 -g -fPIC
JAVAH=javah
JAVA_HOME?=/usr/lib/jvm/java-8-openjdk-amd64
NATIVE_CLASS=WInt128
NATIVE_PACKAGE=wideint
OUT_DIR=target/main/cpp
CLASSES_DIR=target/classes/
OUT_DLL=libwideint.so

$(CLASSES_DIR)/$(OUT_DLL): $(CPP_ALL_SRC) $(OUT_DIR)/$(NATIVE_PACKAGE)_$(NATIVE_CLASS).h
	$(CXX) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -I$(OUT_DIR) -shared $(CPP_ALL_SRC) $(CXXFLAGS) -o $@

jnilib: jniheaders | $(CLASSES_DIR)/$(OUT_DLL)

$(OUT_DIR)/$(NATIVE_PACKAGE)_$(NATIVE_CLASS).h: $(CLASS_OUT_DIR)/$(NATIVE_PACKAGE)/$(NATIVE_CLASS).class
	$(JAVAH) -force -d $(OUT_DIR) -cp $(CLASS_OUT_DIR) $(NATIVE_PACKAGE).$(NATIVE_CLASS)

jniheaders: $(OUT_DIR)/$(NATIVE_PACKAGE)_$(NATIVE_CLASS).h
