# API

Api is a gradle plugin to remove all code from methods

## How To use it

To use it you can add this to your build.gradle

```groovy
buildscript {
     repositories(){
         maven {
             url 'https://artifactory.aresrpg.fr/gradle'
             credentials {
                 username project.hasProperty('aresuser') ? aresuser : System.getenv('aresuser')
                 password project.hasProperty('arespass') ? arespass : System.getenv('arespass')
             }
          }
     }
     dependencies {
         classpath 'fr.aresrpg.gradle:Api:0.+'
     }
 }
 ```
 
 It will add to your build.gradle two tasks described below

## Task:

* ### api
This task write all the api class to build/api 

* ### apijar
This task create a jar from build/api , it use by default the same folder as gradle's jar task but with classifier api . It can be customized like all [Jar](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.Jar.html) tasks

## Components

The plugin add one component api , it use same dependencies as java components

### Maven

```groovy
    publishing {
        repositories {
            ...
        }
    
        publications {
            name(MavenPublication){
                from components.api
            }
        }
    }
 ```
 
## Links

* [TeamCity](https://ci.aresrpg.fr/viewType.html?buildTypeId=AresRPG_Gradle_Api)
* [Artifactory](https://artifactory.aresrpg.fr/gradle/fr/aresrpg/gradle/Api/)