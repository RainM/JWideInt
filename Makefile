#g++ wideint_WInt.cpp -shared -I/usr/lib/jvm/java-8-openjdk-amd64/include/ -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux -o libwideint.so -fPIC

CLASS_OUT_DIR=target/classes
CPP_ALL_OBJ=$(CPP_ALL_SRC:.cpp=.cpp.o)
CPP_ALL_SRC=$(wildcard $(CPP_SRC_PATH)/*.cpp)
CPP_SRC_PATH=src/main/cpp
CXX=g++
CXXFLAGS?=-O3 -g -fPIC
JAVAH=javah
JAVA_HOME?=/usr/lib/jvm/java-8-openjdk-amd64
NATIVE_CLASS=WInt
NATIVE_PACKAGE=wideint
OUT_DIR=target/main/cpp
CLASSES_DIR=target/classes/
OUT_DLL=libwideint.so
LINKFLAGS?=

%.cpp.o: %.cpp
	$(CXX) -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -I$(OUT_DIR) -c $< -o $@ $(CXXFLAGS)

$(OUT_DLL): $(CPP_ALL_OBJ)
	$(CXX) -shared $(CPP_ALL_OBJ) -o $(CLASSES_DIR)/$@

jnilib: $(OUT_DLL)
	@echo "Done"

headers: $(OUT_DIR)/$(NATIVE_PACKAGE)_$(NATIVE_CLASS).h
	@echo $(CPP_ALL_SRC)
	@echo 'JNI headers generated'

$(OUT_DIR)/$(NATIVE_PACKAGE)_$(NATIVE_CLASS).h: $(CLASS_OUT_DIR)/$(NATIVE_PACKAGE)/$(NATIVE_CLASS).class
	$(JAVAH) -force -d $(OUT_DIR) -cp $(CLASS_OUT_DIR) $(NATIVE_PACKAGE).$(NATIVE_CLASS)
