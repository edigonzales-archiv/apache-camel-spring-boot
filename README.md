# apache-camel-spring-boot
apache-camel with spring-boot

## DB-Schema vorbereiten

### ili2pg
Noch nicht i.O.:
```
java -jar /Users/stefan/apps/ili2pg-4.0.0-20190301.105458-20-bindist/ili2pg-4.0.0-SNAPSHOT.jar --createBasketCol --createDatasetCol --createFk --createFkIdx --coalesceJson --createEnumTabs --nameByTopic --dbschema agi_gb2av --createscript fubar.sql --modeldir . --models GB2AV
```

Bis auf weiteres:

V4:
```
java -jar /Users/stefan/apps/ili2pg-4.0.0-20190301.105458-20-bindist/ili2pg-4.0.0-SNAPSHOT.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --createFk --createFkIdx --coalesceJson --createEnumTabs --nameByTopic --dbschema agi_gb2av --createscript fubar.sql --modeldir setup/. --models GB2AV --schemaimport
```

V3:
```
java -jar /Users/stefan/apps/ili2pg-3.12.2/ili2pg-3.12.2.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --createFk --createFkIdx --createEnumTabs --nameByTopic --dbschema agi_gb2av --createscript fubar.sql --modeldir setup/. --models GB2AV --schemaimport
```


### Datenmodell
(Funktioniert erst mit V4) `KS3-20060703.ili` wird lokal vorgehalten, da das Attribut `Mutationsnummer` in der Klasse `GB2AV.Vollzugsgegenstaende.Vollzugsgegenstand` mit einem Metaattribut versehen wird, damit die Struktur als JSON abgebildet wird und alles in einer DB-Tabelle vorliegt (was uns momentan interessiert).

### Testimport
V4:
```
java -jar /Users/stefan/apps/ili2pg-4.0.0-20190301.105458-20-bindist/ili2pg-4.0.0-SNAPSHOT.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --dbschema agi_gb2av --modeldir . --models GB2AV --import setup/VOLLZUG_SO0200002401_1531_20180105113131.xml
```

V3:
```
java -jar /Users/stefan/apps/ili2pg-3.12.2/ili2pg-3.12.2.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --createFk --createFkIdx --createEnumTabs --nameByTopic --dbschema agi_gb2av --createscript fubar.sql --modeldir setup/. --models GB2AV --dataset VOLLZUG_SO0200002401_1531_20180105113131.xml --import setup/VOLLZUG_SO0200002401_1531_20180105113131.xml 
```

## Env-Variablen

### macOS
```
launchctl setenv ftpUserInfogrips xxxx
launchctl setenv ftpPwdInfogrips yyyy
launchctl setenv ftpUrlInfogrips ftp.infogrips.ch
launchctl setenv idempotentFileUrl /Users/stefan/tmp/gb2av_idempotent.txt
```

### Linux
export ftpUserInfogrips=vaso
export ftpPwdInfogrips=vaso123
export ftpUrlInfogrips=ftp.infogrips.ch
export idempotentFileUrl=/Users/stefan/tmp/gb2av_idempotent.txt