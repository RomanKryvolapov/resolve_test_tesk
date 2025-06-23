CREATE TABLE app_users
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);
CREATE TABLE app_tasks
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    description   TEXT         NOT NULL,
    due_date      DATE         NOT NULL,
    status        VARCHAR(50)  NOT NULL,
    user_id       BIGINT,
    depends_on_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES app_users (id),
    FOREIGN KEY (depends_on_id) REFERENCES app_tasks (id)
);