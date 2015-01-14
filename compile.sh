mkdir -p bin
find . -name "*.java" > source.txt;
javac -cp $1bin:$1lib/* -d bin @source.txt;
