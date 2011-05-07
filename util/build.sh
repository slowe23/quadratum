rm -rf bin
mkdir bin
find . -name '*.java' | xargs javac -d bin
