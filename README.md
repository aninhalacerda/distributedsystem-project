distributedsystem-project
=========================

INSTALATION

First, make sure Java is running OK!

Download jar file:

https://www.dropbox.com/s/567oa9yr27qql6e/jgroups-sd.jar
http://downloads.sourceforge.net/project/javagroups/JGroups/3.4.0.Alpha3/jgroups-3.4.0.Alpha3.jar

Add both files to your classpath:
$ export CLASSPATH=$CLASSPATH:path_to_jar


RUNNING

Running as many times you want, each time will create a new node called [user_name] and connect to the singles cluster:
$ java -Djava.net.preferIPv4Stack=true jgroups.ChatJGroups user_name
