# Étape 1 : utiliser une image Java
FROM eclipse-temurin:21-jdk-jammy AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers Maven
COPY pom.xml .
COPY src ./src

# Construire le projet avec Maven
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Étape 2 : créer l'image finale
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copier le jar depuis l'étape build
COPY --from=build /app/target/trocapp-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port de l'application
EXPOSE 8081

# Lancer l'application
ENTRYPOINT ["java","-jar","app.jar"]
