COMP=javac
FILES= driver.java dir.java data.txt branch.java Makefile node.java puzzle.java rbfs.java tile.java

clean:
	rm -f *.class *~

compile: driver.java 
	javac driver.java 
createTar: $(FILES)
	tar cfvz isa170030.tar.gz $(FILES) 
