echo "Generating a list of .java files..."
find . -name "*.java" > paths.txt
echo "Compiling .java files into ./bin/ folder"
javac -d "bin" @paths.txt -cp "src/"
cd ./bin/
echo "Generating a list of .class files..."
find . -name "*.class" > classes.txt
jar cfe PetRescue.jar com.badjed.petrescue.Main @classes.txt
echo "Generated a JAR file"
mv PetRescue.jar ../PetRescue.jar
echo "Moved file to project directory"
echo "Use 'java -jar PetRescue.jar' to run the game!"
echo "Cleaning up..."
rm classes.txt
cd ..
rm paths.txt

