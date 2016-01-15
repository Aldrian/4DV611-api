# 4DV611-api
Api for Race Tracks app.  
### Prerequisites 
Applciation worked on Ubuntu 14.04.
1) Install Java 8 SDK
Windows - http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html  
For Ubuntu 14.04 - http://askubuntu.com/questions/521145/how-to-install-oracle-java-on-ubuntu-14-04

2) Install maven 3.x
Windows - http://www.mkyong.com/maven/how-to-install-maven-in-windows/  
Ubuntu - http://www.mkyong.com/maven/how-to-install-maven-in-ubuntu/

3) Jetty 9 http://www.eclipse.org/jetty/
Install Jetty in Ubuntu 14.04 - http://idroot.net/tutorials/how-to-install-jetty-web-server-on-ubuntu-14-04/

4) PostgreSql
Ubuntu 14.04 installation tutorial - https://www.digitalocean.com/community/tutorials/how-to-install-and-use-postgresql-on-ubuntu-14-04

## File Structure
<pre>
───src
   ├───main
   │   └───java
   │   │   └───se
   │   │       └───travappar
   │   │           └───api
   │   │               ├───controller 		- controllers
   │   │               ├───dal				- data access layer package
   │   │               │   └───impl			- implementations for all entities
   │   │               ├───model			- entities
   │   │               │   ├───dto			- data transfer object
   │   │               │   ├───enums		
   │   │               │   ├───external		- DTO for external source
   │   │               │   └───filter		- beans for filtering
   │   │               └───utils
   │   │                   ├───converter	
   │   │                   ├───filter		- CORS support filter
   │   │                   ├───label
   │   │                   ├───mapper		- Hibernate mapper
   │   │                   ├───publish		- MailChimp and OneSignal helpers
   │   │                   │   ├───mailchimp
   │   │                   │   └───onesignal
   │   │                   └───security		- classes for Spring Security
   │   ├───resources
   │   └───webapp
   │       └───WEB-INF						- all configurations and properties
   └───test
       └───se
           └───travappar
              └───api
                   └───controller			- test classes for controllers
</pre>

### Configuration

## Images
For serving offer images change this lines in file {jetty_directory}/etc/jetty.xml from
```sh
<Set name="handler">
  <New id="Handlers" class="org.eclipse.jetty.server.handler.HandlerCollection">
    <Set name="handlers">
     <Array type="org.eclipse.jetty.server.Handler">
       <Item>
         <New id="Contexts" class="org.eclipse.jetty.server.handler.ContextHandlerCollection"/>
       </Item>
       <Item>
         <New id="DefaultHandler" class="org.eclipse.jetty.server.handler.DefaultHandler"/>
       </Item>
     </Array>
    </Set>
  </New>
</Set>
```
To this
```sh
<Set name="handler">
  <New id="Handlers" class="org.eclipse.jetty.server.handler.HandlerCollection">
    <Set name="handlers">
     <Array type="org.eclipse.jetty.server.Handler">
       <Item>
         <New class="org.eclipse.jetty.server.handler.ContextHandler">
           <Set name="contextPath">/static</Set>
           <Set name="handler">
             <New class="org.eclipse.jetty.server.handler.ResourceHandler">
               <Set name="directoriesListed">false</Set>
               <Set name="resourceBase">{path_to_folder}</Set>
             </New>
           </Set>
         </New>
       </Item>
       <Item>
         <New id="Contexts" class="org.eclipse.jetty.server.handler.ContextHandlerCollection"/>
       </Item>
       <Item>
         <New id="DefaultHandler" class="org.eclipse.jetty.server.handler.DefaultHandler"/>
       </Item>
     </Array>
    </Set>
  </New>
</Set>
```
Where ``` {path_to_folder} ``` is path where will be saved offer images.
In file ```se.travappar.api.utils.ImageHelper``` you should change ```PATHNAME``` field value to ``` {path_to_folder} ``` and ``` IMAGE_URL ``` value to your server url. Offer images will be accessible by ``` IMAGE_URL ```.

## Database

```4DV611-api\src\main\webapp\WEB-INF\database.properties``` file contains database properties. You need to change it to your values.
```
db_name={name_of_database}
db_user={name_of_user}
db_pass={user_password}
```
When application is starting Hibernate will create all database tables.

### Build
For build application go to 4DV611-api folder and use maven from cmd
``` mvn package```
When package is completed find ```4DV611-api/target/travappar-api.war``` file, rename it to ```root.war``` and move it to you Jetty folder ```{jetty_directory}/webapps/```
### Start Jetty
To start Jetty use
```sudo service jetty start```
To stop Jetty use
```sudo service jetty stop```

##External Sources
```se.travappar.api.utils.publish.MailChimpHelper``` contains MailChimp iteraction logic.
```se.travappar.api.utils.publish.OneSignalHelper``` contains OneSignal iteraction logic.
When app is started every day at 3.00 o'clock ```se.travappar.api.dal.ExternalSourceCaller``` refresh all race events data. Every day at 4.00 o'clock ```se.travappar.api.dal.ExternalSourceCaller```refresh MailChimp segments and subscription data.

## Basic workflow
All incoming request go through ```SimpleCORSFilter``` and than (if URL mapping is OK and HTTP method allowed for this mapping) request will be handled by one of the ```Controller```. ```Controller``` works with database through ```DAO``` classes (data access object). ```Controller``` handle request,  save or get data from database with ```DAO``` class, and then return data in JSON or just HTTP status code with some message.