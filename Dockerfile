FROM java:8
VOLUME /tmp

RUN mkdir -p /gb2av/download /gb2av/unzipped /gb2av/error && \
    chmod g+w -R /gb2av

ENV USER_NAME gb2av
ENV APP_HOME /home/$USER_NAME/app

RUN useradd -ms /bin/bash $USER_NAME
RUN mkdir $APP_HOME

ADD build/libs/apache-camel-spring-boot*.jar $APP_HOME/app.jar
RUN chown $USER_NAME $APP_HOME/app.jar

USER $USER_NAME
WORKDIR $APP_HOME
RUN bash -c 'touch app.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]