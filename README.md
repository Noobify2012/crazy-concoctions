# Concoctions

Written by 
- Matt Greene greene.matthew@northeastern.edu
- Daniel Blum blum.da@northeastern.edu


## Requirements
1. Java 11+ (we like [sdkman](https://sdkman.io/) to manage our java)
1. Docker ***OR*** MySql
1. A can do attitude

## Running

### Database

#### **--- Docker**

If you to dockerize the DB, from the main project directory run:
```bash
docker-compose -f ./docker/docker-compose.yql up --build -d
```

#### **--- Local MySQL**

If you want to create and initialize the database yourself, run the sql files `01_init.sql` and `02_starterData.sql` in your MySQL instance.

### **Backend/Frontend**

#### **--- Jars**
Both the backend and frontend are packaged in fatjars, so the only thing you need install is Java 11+.
So, just run

```bash
[insert final backend jar file here]
```

and in a another terminal (unless you're running this `nohup`)

```bash
[insert finale frontend jar file here]
```


#### **--- Compile and run**
Both the frontend and backend are maven projects with maven scripts. 
For the backend, `cd` into `concoctionsBackend` and run:
```bash
./mvnw spring-boot:run
```
or, 
and this will la




