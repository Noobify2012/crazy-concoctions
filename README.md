# Concoctions

Written by 
- Matt Greene greene.matthew@northeastern.edu
- Daniel Blum blum.da@northeastern.edu


## Requirements
1. Java 11+ (we like [sdkman](https://sdkman.io/) to manage our java)
1. Docker ***OR*** MySql
1. A can do attitude

## Running

<<<<<<< Updated upstream
=======
First, just go ahead an unzip `[insert zip file name here]` and cd into the new folder.

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
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


=======
So, from the main project folder, go ahead a run:

```bash
java -jar [insert final backend jar file here]
```

and in a another terminal (unless you're running `nohup`)

```bash
java -jar [insert finale frontend jar file here]
```

#### **--- Compile and run**
Both projects are using maven so make sure that's insalled.
Then, `cd` into either sub-project's folder and run 

```bash
mvn clean
mvn package
```

`mvn package` will create a jar file in the sub-projects `target` directory.

For the backend, there will only be one jar to run, so just `java -jar [new_jar_file].jar` that file.

For the front end, you'll see two jars, so you'll want to run the jar that's appended with `with-dependencies.jar`

## Technical Specifications
### Database
The database is a MySQL database

### Backend 
The backend uses a java with libraries like Spring to simplify communication with the MySQL server and create a simplified API over http (using a *rest like* protocol. . . .though it's very *not* rest).

### Frontend
The frontend uses java to create a CLI as a client for the backend api.

## Lessons Learned
### Daniel Blum
I spent a lot of time learning a bit about Spring (and it's "inversion of control") and Spring-Boot to make this backend server.

Once I got a little handle on Spring, it took sometime to understand how to import "objects" from the database to persist as objects in java. Mainly, about how to get objects that had relations through table (like drinks to ingredients) into java objects that had fields (like a drinks field of a List<Ingredient>).

After all that, I had to get a handle on how to get a json object from "a user" and convert that into a proper java object that *then* could be sent to be persisted in the database.

So, not as much as working in SQL, but more about how to get java to work with SQL.

### Matt Greene
I can haz drink?

## Future Work
### Backend
- better transaction management:
right now, I'm not implementing much custom transaction management and letter spring use it's default setting.
This is definitely and issue as some transactions make additional calls to the database (like saving a drink) and I want to make sure all these calls to the db
- better api calls:
the api calls aren't super consistent. I'm learning, so I'm making some rookie mistakes and need to understand better, more consistent ways to make the calls
- better object representation:
this relates to the previous point, as the backend currently has DTO and regular objects. But, this can be refactored to use one object.
- validation:
There's currently ***NO*** backend validation :)
- error handing:
I mean, the server sends the right status code, but I'm not sure the user wants a full stack dump of the error in the body of the response when one occurs :)

### Frontend
>>>>>>> Stashed changes


