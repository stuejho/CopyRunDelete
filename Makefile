JAVAC=javac
JFLAGS=
JAR=jar
JAROPTIONS=cfe
SOURCE_DIR=src/
BIN_DIR=bin/
ENTRY=CopyRunDelete
SOURCES=CopyRunDelete.java
CLASSES=CopyRunDelete.class

VPATH=src/

.SUFFIXES: .java .class

all: $(addprefix $(BIN_DIR), $(CLASSES))
	$(JAR) $(JAROPTIONS) $(BIN_DIR)$(ENTRY).jar $(ENTRY) -C $(BIN_DIR) $(ENTRY).class

$(BIN_DIR)%.class: %.java
	$(JAVAC) $(JFLAGS) $< -d $(BIN_DIR)

.PHONY: clean
clean:
	rm -rf bin/
