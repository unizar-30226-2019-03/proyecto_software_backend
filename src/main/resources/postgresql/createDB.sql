CREATE TABLE notification_category(
    ID SERIAL PRIMARY KEY NOT NULL,
    NAME VARCHAR(100) NOT NULL
);

CREATE TABLE notification(
    ID SERIAL PRIMARY KEY NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    DESCRIPTION TEXT NOT NULL,
    TIMESTAMP TIMESTAMP NOT NULL,
    foreign key (CATEGORY)
    references notification_category (ID) NOT NULL ON DELETE CASCADE
    foreign key (USER)
    references users (ID) ON DELETE SET NULL
    VIEWED BOOLEAN NOT NULL 
);

CREATE TABLE message(
    ID SERIAL PRIMARY KEY NOT NULL,
    DESCRIPTION TEXT NOT NULL,
    TIMESTAMP VARCHAR(20) NOT NULL,
    TIMESTAMP TIMESTAMP NOT NULL,
    #FALTAN LAS RELACIONES ENVIA
);

CREATE TABLE university(
    ID SERIAL PRIMARY KEY NOT NULL,
    NAME VARCHAR(100) NOT NULL
);

CREATE TABLE subject(
    ID SERIAL PRIMARY KEY NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    foreign key (UNIVERSITY)
    references university (ID) ON DELETE SET NULL
);

CREATE TABLE video(
    ID SERIAL PRIMARY KEY NOT NULL,
    TITLE VARCHAR(200) NOT NULL,
    DESCRIPTION TEXT NOT NULL,
    TIMESTAMP TIMESTAMP NOT NULL,
    foreign key (SUBJECT) 
    references subject (ID) ON DELETE SET NULL
);

CREATE TABLE reproduction_list(
    ID SERIAL PRIMARY KEY NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    #falta relacion con usuario
);


CREATE TABLE commentary(
    ID SERIAL PRIMARY KEY NOT NULL,
    DESCRIPTION TEXT NOT NULL,
    TIMESTAMP TIMESTAMP NOT NULL,
    foreign key (VIDEO)
    references video (ID) ON DELETE SET NULL,
    foreign key (COMMENTARY)
    references commentary (ID) NOT NULL ON DELETE CASCADE,
    TIME_VIDEO TIMESTAMP NOT NULL,
    #falta relacion con usuario
);

CREATE TABLE VIDEO_LISTA(
    ID SERIAL PRIMARY KEY NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    foreign key (VIDEO)
    references video (ID),
    foreign key (LIST)
    references reproduction_list (ID),
    #falta relacion con usuario
);

#Falta relacion de video con usuario N-M(visualiza)
#Falta relacion alumno con asignatura N-M
#Falta relacion profesor con asignatura N-M
#Falta relacion video y usuario N-M(vota)



