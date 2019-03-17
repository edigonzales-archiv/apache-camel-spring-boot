# apache-camel-spring-boot
apache-camel with spring-boot

## DB-Schema mit ili2pg vorbereiten
### Datenmodell
(Funktioniert nur mit V4) `KS3-20060703.ili` wird lokal vorgehalten, da das Attribut `Mutationsnummer` in der Klasse `GB2AV.Vollzugsgegenstaende.Vollzugsgegenstand` mit einem Metaattribut versehen wird, damit die Struktur als JSON abgebildet wird und alles in einer DB-Tabelle vorliegt (was uns momentan interessiert).

### SQL:
Zuerst "nur" SQL-Datei von ili2pg erstellen lassen und dann diese ausführen, funktioniert mit 4.0.0-SNAPSHOT momentan noch nicht:
```
java -jar /Users/stefan/apps/ili2pg-4.0.0-20190314.084652-23-bindist/ili2pg-4.0.0-SNAPSHOT.jar --createBasketCol --createDatasetCol --createFk --createFkIdx --coalesceJson --createEnumTabs --nameByTopic --dbschema agi_gb2av --createscript fubar.sql --modeldir . --models GB2AV
```
Grund dafür: https://github.com/claeis/ili2db/issues/261

### 4.0.0-SNAPSHOT
Schemaimport:
```
java -jar /Users/stefan/apps/ili2pg-4.0.0-20190314.084652-23-bindist/ili2pg-4.0.0-SNAPSHOT.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --createFk --createFkIdx --coalesceJson --createEnumTabs --nameByTopic --dbschema agi_gb2av --modeldir setup/. --models GB2AV --schemaimport
```

Testimport:
```
java -jar /Users/stefan/apps/ili2pg-4.0.0-20190314.084652-23-bindist/ili2pg-4.0.0-SNAPSHOT.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --dbschema agi_gb2av --modeldir setup/. --models GB2AV --dataset VOLLZUG_SO0200002401_1531_20180105113131.xml --import setup/VOLLZUG_SO0200002401_1531_20180105113131.xml
```

### 3.12.2

Schemaimport:
```
java -jar /Users/stefan/apps/ili2pg-3.12.2/ili2pg-3.12.2.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --createFk --createFkIdx --createEnumTabs --nameByTopic --dbschema agi_gb2av --createscript fubar.sql --modeldir setup/. --models GB2AV --schemaimport
```

Testimport:
```
java -jar /Users/stefan/apps/ili2pg-3.12.2/ili2pg-3.12.2.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --createFk --createFkIdx --createEnumTabs --nameByTopic --dbschema agi_gb2av --createscript fubar.sql --modeldir setup/. --models GB2AV --dataset VOLLZUG_SO0200002401_1531_20180105113131.xml --import setup/VOLLZUG_SO0200002401_1531_20180105113131.xml 
```

## Docker lokal
```
docker build -t sogis/gb2av-integrator .
docker run --restart always -p 8888:8888 -e APP_GB2AV_ENV='prod' -e ftpUserInfogrips='xxx' -e ftpPwdInfogrips='yyy' sogis/gb2av-integrator
```


## Env-Variablen

### macOS
```
launchctl setenv APP_GB2AV_ENV dev

launchctl setenv ftpUserInfogrips xxxx
launchctl setenv ftpPwdInfogrips yyyy
launchctl setenv ftpUrlInfogrips ftp.infogrips.ch
launchctl setenv idempotentFileUrl /Users/stefan/tmp/gb2av_idempotent.txt
launchctl setenv pathToDownloadFolder /Users/stefan/Downloads/output/
launchctl setenv pathToUnzipFolder /Users/stefan/Downloads/output_unzipped/
```

### Linux
export ftpUserInfogrips=yyyy
export ftpPwdInfogrips=xxxx
export ftpUrlInfogrips=ftp.infogrips.ch
export idempotentFileUrl=/Users/stefan/tmp/gb2av_idempotent.txt

## Old code snippets (to be deleted)
```
// ENV Variablen "direkt": {{env:ftpUserInfogrips}}

//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\gb2av\\?password={{env:ftpPwdInfogrips}}&antInclude=VOLLZUG*.zip&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\dm01avso24lv95_2\\shp\\?password={{env:ftpPwdInfogrips}}&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\dm01avso24lv95_2\\shp\\?password={{env:ftpPwdInfogrips}}&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000")

```

