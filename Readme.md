# HTTP Process Runner
A remote application management tool with an HTTP interface.  
See the Design Document for more details.

## Features
- [x] Servlet accepts HTTP requests
- [x] Client can get process information sending GET requests to servlet
    - [x] Can get running status of process
- [x] Client can start or stop a process using PUT on servlet
- [x] Local server exposes processes on the same machine to run
    - [x] Specify which processes to expose in a file

## Requirements
## Building
- Git
- Java JDK 8 or higher
- Maven 2+
- Docker (If building image)
## Setup

### Creating the "executions.json"
This application requires a special JSON configuration be setup before running.  
Create a directory named `.hprocrunner` in your `$HOME` directory.  
Create a file named `executions.json` in the `.hprocrunner` directory.
The file must contain the information to run your proceses. See the example `executions.json`.

### To Build
Run `mvn package`.  
(Optional) To build the Docker image, run `docker build -t \<image name\> .` after building.


### Running the jar
After building, in the base directory, run `java -jar target/project1-1.0.0.jar`.  

### Running the Docker container
NOTE: in order to run processes on your local machine, you must mount both your local root directory to a subdirectory of the container's root filesystem and the .hprocrunner directory to the container's `/root/` directory. Your `executions.json` must also account for the subdirectory that you have mounted your local root to.

Run `docker run -it -v <root-of-local-filesystem>:/<subdirectory-of-container-root> -v <.hprocrunner-path>:/root/.hprocrunner/ -p <exposed port>:8081 <image-name>`

## Operation
Connect to `<ip-of-running-machine>:<port>/project1/api/process/` on an HTTP client.  For example, `localhost:8081/project1/api/process/`.
- GET `localhost:8081/project1/api/process/` returns information about all registered processes.
- GET `localhost:8081/project1/api/process/<integer>` returns information about a specific process.
- PUT `localhost:8081/project1/api/process/<integer>` allows you to toggle on and off a running process. The body of the request must be identical to the result of a GET request, with the exception of the "running" field. The "running" field may be set to true or false to start or stop the process, respectively.


test7
