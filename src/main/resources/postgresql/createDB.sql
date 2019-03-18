CREATE TABLE notification_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
 
CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    checked BOOLEAN NOT NULL,
    fk_category INTEGER NOT NULL,
    CONSTRAINT fk_notification_category FOREIGN KEY (fk_category) REFERENCES notification_category(id) ON DELETE CASCADE,
    -- CONSTRAINT fk_notification_user FOREIGN KEY (user) REFERENCES users (ID) ON DELETE SET NULL,
);

CREATE TABLE message (
    --No es esta la primary
    id SERIAL,
    text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    CONSTRAINT pk_message PRIMARY KEY (id,/* falta lo del alumno y profesor */)
    -- FALTAN LAS RELACIONES ENVIA
);

CREATE TABLE university (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE subject (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    fk_university INTEGER,
    CONSTRAINT fk_subject_university FOREIGN KEY (fk_university) REFERENCES university(id) ON DELETE SET NULL
);

CREATE TABLE video (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    fk_subject INTEGER NOT NULL,
    CONSTRAINT fk_video_subject FOREIGN KEY (fk_subject) REFERENCES subject(id) ON DELETE CASCADE
);

CREATE TABLE video_tag (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    fk_video INTEGER NOT NULL,
    CONSTRAINT fk_video_tag FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE
);

CREATE TABLE reproduction_list (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    -- falta relacion con usuario
);


CREATE TABLE commentary (
    id SERIAL,
    text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    fk_video INTEGER NOT NULL,
    fk_commentary INTEGER,
    video_timestamp TIMESTAMP NOT NULL,
    CONSTRAINT fk_commentary_commentary FOREIGN KEY (fk_commentary) REFERENCES commentary(id) ON DELETE SET NULL,
    CONSTRAINT fk_commentary_video FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE,
    CONSTRAINT pk_commentary PRIMARY KEY (id, fk_video, /* falta lo del usuario */)
    -- falta relacion con usuario
);

CREATE TABLE video_list_relation(
    position INTEGER NOT NULL,
    fk_video INTEGER,
    fk_list INTEGER,
    CONSTRAINT fk_video_list_relation_list FOREIGN KEY (fk_list) REFERENCES reproduction_list(id) ON DELETE CASCADE,
    CONSTRAINT fk_video_list_relation_video FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE,
    CONSTRAINT pk_video_list_relation PRIMARY KEY (fk_video, fk_list)
);

CREATE TABLE user(
    id SERIAL PRIMARY KEY,
    nickname VARCHAR(100) UNIQUE,
    photo VARCHAR(1000),
    email VARCHAR(255),
    description TEXT,
    -- Igual es otro tipo
    password BYTEA(60) NOT NULL,
)

--Falta relacion de video con usuario N-M(visualiza)
--Falta relacion alumno con asignatura N-M
--Falta relacion profesor con asignatura N-M
--Falta relacion video y usuario N-M(vota)
