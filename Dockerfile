FROM java:8
VOLUME /tmp

RUN mkdir -p /gb2av_config /gb2av_data/download /gb2av_data/unzipped /gb2av_data/error 

COPY src/main/resources/application.properties /gb2av_config/application.properties
COPY src/main/resources/application-dev.properties /gb2av_config/application-dev.properties
COPY src/main/resources/application-prod.properties /gb2av_config/application-prod.properties

ENV USER_NAME gb2av
ENV APP_HOME /home/$USER_NAME/app

RUN useradd -ms /bin/bash $USER_NAME
RUN mkdir $APP_HOME

ADD build/libs/apache-camel-spring-boot*.jar $APP_HOME/app.jar
RUN chown $USER_NAME $APP_HOME/app.jar
RUN chown -R $USER_NAME /gb2av_config
RUN chown -R $USER_NAME /gb2av_data

USER $USER_NAME
WORKDIR $APP_HOME
RUN bash -c 'touch app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.config.location=file:/gb2av_config/","-jar","app.jar"]
