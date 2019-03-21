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
java -jar /Users/stefan/apps/ili2pg-4.0.0-20190314.084652-23-bindist/ili2pg-4.0.0-SNAPSHOT.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --createFk --createFkIdx --coalesceJson --createEnumTabs --nameByTopic --createscript fubar.sql --dbschema agi_gb2av --modeldir setup/. --models GB2AV --schemaimport
```

Testimport:
```
java -jar /Users/stefan/apps/ili2pg-4.0.0-20190314.084652-23-bindist/ili2pg-4.0.0-SNAPSHOT.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --dbschema agi_gb2av --modeldir setup/. --models GB2AV --dataset VOLLZUG_SO0200002401_1531_20180105113131.xml --import setup/VOLLZUG_SO0200002401_1531_20180105113131.xml
```

### 3.12.2

Schemaimport:
```
java -jar /Users/stefan/apps/ili2pg-3.12.2/ili2pg-3.12.2.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --createFk --createFkIdx --createEnumTabs --nameByTopic --dbschema agi_gb2av --createscript fubar.sql --models GB2AV --schemaimport
```

Testimport:
```
java -jar /Users/stefan/apps/ili2pg-3.12.2/ili2pg-3.12.2.jar --dbhost 192.168.50.8 --dbdatabase pub --dbusr ddluser --dbpwd ddluser --createBasketCol --createDatasetCol --createFk --createFkIdx --createEnumTabs --nameByTopic --dbschema agi_gb2av --createscript fubar.sql --models GB2AV --dataset VOLLZUG_SO0200002401_1531_20180105113131.xml --import setup/VOLLZUG_SO0200002401_1531_20180105113131.xml 
```

## Docker lokal
```
docker build -t sogis/gb2av-integrator .
docker run --restart always -p 8888:8888 -e "SPRING_PROFILES_ACTIVE=prod" -e "ftpUserInfogrips=xxx" -e "ftpPwdInfogrips=yyy" sogis/gb2av-integrator
docker run --restart always -p 8888:8888 -v /Users/stefan/tmp/gb2av_data:/gb2av_data -e "SPRING_PROFILES_ACTIVE=prod" -e "ftpUserInfogrips=xxx" -e "ftpPwdInfogrips=yyy" sogis/gb2av-integrator
```

```
-v somewhere/to/config:/gb2av_config
```
mit den `application[-xxx].properties`-Dateien.

```
-v somewhere/to/data:/gb2av_data
```
für die heruntergeladenen Daten und das idempotent File.



## Env-Variablen

### macOS
```
launchctl setenv APP_GB2AV_ENV dev
launchctl setenv ftpUserInfogrips xxxx
launchctl setenv ftpPwdInfogrips yyyy
```

### Linux
```
export APP_GB2AV_ENV=dev
```

## Old code snippets (to be deleted)
```
// ENV Variablen "direkt": {{env:ftpUserInfogrips}}

//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\gb2av\\?password={{env:ftpPwdInfogrips}}&antInclude=VOLLZUG*.zip&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\dm01avso24lv95_2\\shp\\?password={{env:ftpPwdInfogrips}}&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")
//        from("ftp://{{env:ftpUserInfogrips}}@ftp.infogrips.ch/\\dm01avso24lv95_2\\shp\\?password={{env:ftpPwdInfogrips}}&autoCreate=false&noop=true&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=10000")

        from("ftp://"+ftpUserInfogrips+"@"+ftpUrlInfogrips+"/\\gb2av\\?fileName=VOLLZUG_SO0200002401_1531_20170420081516.zip&password="+ftpPwdInfogrips+"&antInclude=VOLLZUG*.zip&autoCreate=false&noop=true&readLock=changed&stepwise=false&separator=Windows&passiveMode=true&binary=true&delay=5000&initialDelay=2000&idempotentRepository=#fileConsumerRepo&idempotentKey=${file:name}-${file:size}")


```
## SQL-Queries

Mit ili2pg-JSON-Mapping:
```
SELECT
	aimport.importdate,
	vollzugsgegenstand.t_datasetname AS datasetname,
	vollzugsgegenstand.status,
	TO_DATE(vollzugsgegenstand.grundbucheintrag, 'YYYY-MM-DD') AS grundbucheintrag,
	TO_DATE(vollzugsgegenstand.tagebucheintrag, 'YYYY-MM-DD') AS tagebucheintrag,
	vollzugsgegenstand.mutationsnummer->>'Nummer' AS mutationsnummer,
	vollzugsgegenstand.mutationsnummer->>'NBIdent' AS nbident
	
FROM
	agi_gb2av.vollzugsgegnstnde_vollzugsgegenstand AS vollzugsgegenstand
	LEFT JOIN agi_gb2av.t_ili2db_dataset AS dataset
	ON dataset.datasetname = vollzugsgegenstand.t_datasetname
	LEFT JOIN agi_gb2av.t_ili2db_import AS aimport
	ON aimport.dataset = dataset.t_id
ORDER BY
	TO_DATE(vollzugsgegenstand.tagebucheintrag, 'YYYY-MM-DD') DESC,
	TO_DATE(vollzugsgegenstand.grundbucheintrag, 'YYYY-MM-DD') DESC,
	aimport.importdate DESC
;
```

Ohne ili2pg-JSON-Mapping:
```
SELECT
  aimport.importdate,
  vollzugsgegenstand.t_datasetname AS datasetname,
  vollzugsgegenstand.status AS status,
  TO_DATE(vollzugsgegenstand.grundbucheintrag, 'YYYY-MM-DD') AS landRegisterEntryDate,
  TO_DATE(vollzugsgegenstand.tagebucheintrag, 'YYYY-MM-DD') AS journalEntryDate,
  mutationsnummer.nummer AS referenceNumber,
  mutationsnummer.nbident AS identnd
FROM
  agi_gb2av.vollzugsgegnstnde_vollzugsgegenstand AS vollzugsgegenstand
  LEFT JOIN agi_gb2av.mutationsnummer AS mutationsnummer
  ON mutationsnummer.vollzgsggnszgsggnstand_mutationsnummer = vollzugsgegenstand.t_id
  LEFT JOIN agi_gb2av.t_ili2db_dataset AS dataset
  ON dataset.datasetname = vollzugsgegenstand.t_datasetname
  LEFT JOIN agi_gb2av.t_ili2db_import AS aimport
  ON aimport.dataset = dataset.t_id
ORDER BY
  TO_DATE(vollzugsgegenstand.tagebucheintrag, 'YYYY-MM-DD') DESC,
  TO_DATE(vollzugsgegenstand.grundbucheintrag, 'YYYY-MM-DD') DESC,
  aimport.importdate DESC
;
```
