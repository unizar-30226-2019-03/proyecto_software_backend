--Las entidades debiles del E/R no incluyen las relaciones debil como parte de la PK por motivos de eficiencia

CREATE TABLE notification_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);
 
CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    fk_category INTEGER NOT NULL,
    CONSTRAINT fk_notification_category FOREIGN KEY (fk_category) REFERENCES notification_category(id) ON DELETE CASCADE
);

CREATE TABLE degree(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE university (
    id SERIAL PRIMARY KEY,
    uni_photo VARCHAR(1000) UNIQUE,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    surnames VARCHAR(100) NOT NULL,
    photo_path VARCHAR(1000) UNIQUE,
    email VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    enabled BOOLEAN NOT NULL,
    -- Igual es otro tipo, incluye hash de pass y salt
    password VARCHAR(80) NOT NULL,
    fk_university INTEGER,
    CONSTRAINT fk_app_user_university FOREIGN KEY (fk_university) REFERENCES university(id) ON DELETE SET NULL,
    fk_degree INTEGER,
    CONSTRAINT fk_app_user_degree FOREIGN KEY (fk_degree) REFERENCES degree(id) ON DELETE SET NULL
);

CREATE TABLE reproduction_list (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    fk_app_user INTEGER NOT NULL,
    CONSTRAINT fk_reproduction_list_app_user FOREIGN KEY (fk_app_user) REFERENCES app_user(id) ON DELETE CASCADE
);

-- TODO: Alumnos solo pueden enviar mensajes a profesores que den alguna de sus asignaturas y viceversa
CREATE TABLE message (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    fk_receiver INTEGER NOT NULL,
    fk_sender INTEGER NOT NULL,
    CHECK (fk_receiver != fk_sender),
    CONSTRAINT fk_message_teacher FOREIGN KEY (fk_receiver) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_alumn FOREIGN KEY (fk_sender) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE subject (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    abbreviation VARCHAR(100) UNIQUE NOT NULL,
    fk_university INTEGER,
    CONSTRAINT fk_subject_university FOREIGN KEY (fk_university) REFERENCES university(id) ON DELETE SET NULL
);

CREATE TABLE video (
    id SERIAL PRIMARY KEY,
    title VARCHAR(200) UNIQUE NOT NULL,
    description TEXT NOT NULL,
    s3_path VARCHAR(1000),
    timestamp TIMESTAMP NOT NULL,
    fk_subject INTEGER NOT NULL,
    CONSTRAINT fk_video_subject FOREIGN KEY (fk_subject) REFERENCES subject(id) ON DELETE CASCADE
);

CREATE TABLE video_tag (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE comment (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    fk_app_user INTEGER NOT NULL,
    fk_video INTEGER NOT NULL,
    fk_comment INTEGER,
    secs_from_beg INTEGER NOT NULL,
    CONSTRAINT fk_comment_comment FOREIGN KEY (fk_comment) REFERENCES comment(id) ON DELETE SET NULL,
    CONSTRAINT fk_comment_video FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_app_user FOREIGN KEY (fk_app_user) REFERENCES app_user(id) ON DELETE CASCADE
);

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE privilege (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE notification_app_user (
    fk_notification INTEGER,
    fk_app_user INTEGER,
    checked BOOLEAN NOT NULL,
    CONSTRAINT fk_notification_app_user_notification FOREIGN KEY (fk_notification) REFERENCES notification(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_app_user_app_user FOREIGN KEY (fk_app_user) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT pk_notification_app_user PRIMARY KEY (fk_notification, fk_app_user)
);

CREATE TABLE video_list (
    fk_video INTEGER,
    fk_list INTEGER,
    position INTEGER NOT NULL,
    CONSTRAINT fk_video_list_video FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE,
    CONSTRAINT fk_video_list_list FOREIGN KEY (fk_list) REFERENCES reproduction_list(id) ON DELETE CASCADE,
    CONSTRAINT pk_video_list PRIMARY KEY (fk_video, fk_list)
);

-- TODO: Faltan los atributos !!!!!!
CREATE TABLE app_user_video_vote (
    fk_app_user INTEGER,
    fk_video INTEGER,
    likes INTEGER NOT NULL,
    dislikes INTEGER NOT NULL,
    clarity INTEGER NOT NULL,
    quality INTEGER NOT NULL,
    CONSTRAINT fk_app_user_video_vote_video FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE,
    CONSTRAINT fk_app_user_video_vote_app_user FOREIGN KEY (fk_app_user) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT pk_app_user_video_vote PRIMARY KEY (fk_video, fk_app_user)
);

CREATE TABLE app_user_video_watches (
    fk_app_user INTEGER,
    fk_video INTEGER,
    timestamp_last_time TIMESTAMP NOT NULL,
    secs_from_beg INTEGER NOT NULL,
    CONSTRAINT fk_app_user_video_watches_app_user FOREIGN KEY (fk_app_user) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_app_user_video_watches_video FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE,
    CONSTRAINT pk_app_user_video_watches PRIMARY KEY (fk_app_user, fk_video)
);

CREATE TABLE role_privilege (
    fk_role INTEGER,
    fk_privilege INTEGER,
    CONSTRAINT fk_role_privilege_role FOREIGN KEY (fk_role) REFERENCES role(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_privilege_privilege FOREIGN KEY (fk_privilege) REFERENCES privilege(id) ON DELETE CASCADE,
    CONSTRAINT pk_role_privilege PRIMARY KEY (fk_role, fk_privilege)
);

CREATE TABLE role_app_user (
    fk_role INTEGER,
    fk_app_user INTEGER,
    CONSTRAINT fk_role_app_user_role FOREIGN KEY (fk_role) REFERENCES role(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_app_user_app_user FOREIGN KEY (fk_app_user) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT pk_role_app_user PRIMARY KEY (fk_role, fk_app_user)
);

-- Es la relacion de alumno con asignatura y profesor con asignatura, comprobar roles etc en java
CREATE TABLE app_user_subject (
    fk_subject INTEGER,
    fk_app_user INTEGER,
    CONSTRAINT fk_app_user_subject_subject FOREIGN KEY (fk_subject) REFERENCES subject(id) ON DELETE CASCADE,
    CONSTRAINT fk_app_user_subject_app_user FOREIGN KEY (fk_app_user) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT pk_app_user_subject PRIMARY KEY (fk_subject, fk_app_user)
);

CREATE TABLE video_video_tag (
    fk_video INTEGER,
    fk_video_tag INTEGER,
    CONSTRAINT fk_video_video_tag_video FOREIGN KEY (fk_video) REFERENCES video(id) ON DELETE CASCADE,
    CONSTRAINT fk_video_video_tag_video_tag FOREIGN KEY (fk_video_tag) REFERENCES video_tag(id) ON DELETE CASCADE,
    CONSTRAINT pk_video_video_tag PRIMARY KEY (fk_video, fk_video_tag)
);



CREATE TABLE degree_university(
    fk_degree INTEGER,
    fk_university INTEGER,
    CONSTRAINT fk_degree_university_degree FOREIGN KEY (fk_degree) REFERENCES degree(id) ON DELETE CASCADE,
    CONSTRAINT fk_degree_university_university FOREIGN KEY (fk_university) REFERENCES university(id) ON DELETE CASCADE,
    CONSTRAINT pk_degree_university PRIMARY KEY (fk_degree, fk_university)
);

CREATE TABLE degree_subject(
    fk_degree INTEGER,
    fk_subject INTEGER,
    CONSTRAINT fk_degree_subject_degree FOREIGN KEY (fk_degree) REFERENCES degree(id) ON DELETE CASCADE,
    CONSTRAINT fk_degree_subject_subject FOREIGN KEY (fk_subject) REFERENCES subject(id) ON DELETE CASCADE,
    CONSTRAINT pk_degree_subject PRIMARY KEY (fk_degree, fk_subject)
);
