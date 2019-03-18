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
    fk_user INTEGER NOT NULL,
    CONSTRAINT fk_notification_category FOREIGN KEY (fk_category) REFERENCES notification_category(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_user FOREIGN KEY (user) REFERENCES users (id) ON DELETE SET NULL,
);

CREATE TABLE message (
    id SERIAL,
    text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    fk_teacher INTEGER NOT NULL,
    fk_alumn INTEGER NOT NULL,
    CONSTRAINT fk_message_teacher FOREIGN KEY (fk_teacher) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_alumn FOREIGN KEY (fk_alumn) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT pk_message PRIMARY KEY (id,fk_alumn,fk_teacher)
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
    fk_user INTEGER NOT NULL,
    CONSTRAINT fk_reproduction_list_user FOREIGN KEY (fk_user) REFERENCES user(id) ON DELETE CASCADE,
);


CREATE TABLE commentary (
    id SERIAL,
    text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    fk_user INTEGER NOT NULL,
    fk_video INTEGER NOT NULL,
    fk_commentary INTEGER,
    secs_from_beg INTEGER NOT NULL,
    CONSTRAINT fk_commentary_commentary FOREIGN KEY (fk_commentary) REFERENCES commentary(id) ON DELETE SET NULL,
    CONSTRAINT fk_commentary_video FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE,
    CONSTRAINT fk_commentary_user FOREIGN KEY (fk_user) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT pk_commentary PRIMARY KEY (id, fk_video,fk_user)
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
);

CREATE TABLE user_video_vote(
    fk_user INTEGER,
    fk_video INTEGER,
    -- Faltan los atributos
    CONSTRAINT fk_user_video_vote_video FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_video_vote_user FOREIGN KEY (fk_user) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT pk_user_video_vote PRIMARY KEY (fk_video, fk_user)
);

CREATE TABLE user_video_watches(
    fk_user INTEGER,
    fk_video INTEGER,
    last_watch_timestamp TIMESTAMP NOT NULL,
    secs_from_beg INTEGER NOT NULL,
    CONSTRAINT fk_user_video_watches_video FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_video_watches_user FOREIGN KEY (fk_user) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT pk_user_video_watches PRIMARY KEY (fk_video, fk_user)
);


--Falta relacion alumno con asignatura N-M
--Falta relacion profesor con asignatura N-M
