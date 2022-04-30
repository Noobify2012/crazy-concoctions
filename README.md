# Concoctions

Written by 
- Matt Greene greene.matthew@northeastern.edu
- Daniel Blum blum.da@northeastern.edu


## Requirements
1. Java 11+ (we like [sdkman](https://sdkman.io/) to manage our java)
1. Docker ***OR*** MySql
1. A can do attitude

## Running

### The Database

### **Docker**
If you to dockerize the DB, from the main project directory run:
```bash
docker-compose -f ./docker/docker-compose.yql up --build -d
```

If you want to create and initialize the database yourself, run the sql files `01_init.sql` and `02_starterData.sql` in your MySQL instance.


