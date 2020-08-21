A remote application management tool with a RESTful interface, 
allowing a user to monitor the running state of an application, as well as launch/terminate it remotely.

## Core Features
Users can launch and terminate processes remotely by making HTTP requests to a servlet endpoint.  
User can get the running status of an application by making HTTP requests to a servlet endpoint.  

### Stretch Features (In order of priority)
1. Log in system to protect against malicious use
2. Manage processes on multiple machines. 
3. Additional metrics about processes

## User Stories
As a server admin, I want to be able to see the running status of my servers so that I can respond to unexpected server failure.

As a developer, I want to be able to launch an application remotely so I can test it on a machine other than my own.