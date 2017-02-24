# AirportLocator
A tool to get nearest airport with respect to user location.

## Design Overview

A sample file containing airport location details are given. These will be stored in redis using geohash technique.
Redis offers easy way to store, retrieve and processing these geo data.

An input file which contains user data is given. The batch processing application powered by apache flink processes these data and finds the nearest point by doing geo radius query from **redis**.

**Apache Flink** is an open-source stream processing framework for distributed, high-performing, always-available, and accurate data streaming applications and/or batch processing application.
Flink is designed to run on large-scale clusters with many thousands of nodes, and in addition to a standalone cluster mode.

## Tools used
- Java 1.8
- Redis 3.2
- Apache Flink 1.2.0

## How to run the program

Follow these steps to run the program in standalone mode.

- Clone the repo
- run _mvn clean package_
- Modify config.yml to put correct file paths for airport data, sample data, redis details.
- run _java -jar target/airport-locator-1.0-SNAPSHOT.jar config.yml_ for running in standalone mode. Program will be executed in local. There is no need of flink cluster.
- Generated fat jar can also be run on flink cluster using _./bin/flink run /path/to/airport-locator-1.0-SNAPSHOT.jar /path/to/config.yml_
