Pour compiler, il suffit de lancer le script "compile.sh"

OU

Dans le répertoire courant, lancer ces commandes:
    find . -name "*.java" > paths.txt
    javac -d "bin" @paths.txt -cp "src/"
    cd ./bin/
    find . -name "*.class" > classes.txt
    jar cfe PetRescue.jar com.badjed.petrescue.Main @classes.txt
    mv PetRescue.jar ../PetRescue.jar
    cd ..

Pour lancer:
    java -jar PetRescue.jar
