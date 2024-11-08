DROP TABLE IF EXISTS special_outreach_completion_records; -- depends on special_outreach
DROP TABLE IF EXISTS special_outreach; -- depends on plants and users
DROP TABLE IF EXISTS outreach; -- depends on plants and users
DROP TABLE IF EXISTS plants; -- depends on users and plants_config
DROP TABLE IF EXISTS plants_config; -- no dependencies
DROP TABLE IF EXISTS users; -- no dependencies

CREATE TABLE IF NOT EXISTS users (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     email VARCHAR(255) NOT NULL,
     lastname VARCHAR(255),
     firstname VARCHAR(255),
     creationDatetime DATETIME DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS plants_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    notes TEXT NOT NULL,
    approxOutreachDurationDays INT NOT NULL,
    width INT NOT NULL,
    height INT NOT NULL
);

-- one-to-many w/ users
CREATE TABLE IF NOT EXISTS plants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT NOT NULL,
    plantType INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    notes TEXT NOT NULL,
    outreachDurationDays INT NOT NULL,
    nextOutreachTime DATETIME,
    stage SMALLINT NOT NULL, -- stage of growth the plant is in (1, 2, 3)
    xCoord INT NOT NULL, -- top left
    yCoord INT NOT NULL, -- top left
    creationDatetime DATETIME DEFAULT CURRENT_DATE,
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (plantType) REFERENCES plants_config(id)
);

-- special_outreach = future outreaches to make
CREATE TABLE IF NOT EXISTS special_outreach (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT NOT NULL,
    plantId BIGINT NOT NULL,
    notes TEXT NOT NULL,
    outreachTime DATETIME NOT NULL,
    completed BOOLEAN NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (plantId) REFERENCES plants(id)
);

CREATE TABLE IF NOT EXISTS special_outreach_completion_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    specialOutreachId BIGINT NOT NULL,
    creationDatetime DATETIME DEFAULT CURRENT_DATE,
    notes TEXT,
    FOREIGN KEY (specialOutreachId) REFERENCES special_outreach(id)
);

-- outreach = list of past outreaches
CREATE TABLE IF NOT EXISTS outreach (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plantId BIGINT NOT NULL,
    userId BIGINT NOT NULL, -- may not actually need both but to show in ledger makes it easier
    creationDatetime DATETIME DEFAULT CURRENT_DATE,
    contents TEXT,
    FOREIGN KEY (plantId) REFERENCES plants(id),
    FOREIGN KEY (userId) REFERENCES users(id)
);

INSERT INTO users (email, lastname, firstname) values ('test@test.com', 'testLast', 'testFirst');

INSERT INTO plants_config (
    name, notes, approxOutreachDurationDays, width, height
) values (
             'Prickly Pear Cactus', 'Info about the cactus here', 365, 3, 3
);
INSERT INTO plants_config (
    name, notes, approxOutreachDurationDays, width, height
) values (
             'Redwood', 'Info about the redwood here', 40, 5, 5
);
INSERT INTO plants_config (
    name, notes, approxOutreachDurationDays, width, height
) values (
             'Sunflower', 'Info about the sunflower here', 10, 1, 1
);
INSERT INTO plants_config (
    name, notes, approxOutreachDurationDays, width, height
) values (
             'Strawberry', 'Info about the strawberry here', 5, 1, 1
);
INSERT INTO plants_config (
    name, notes, approxOutreachDurationDays, width, height
) values (
             'Sequoia', 'Info about the sequoia here', 100, 7, 7
);

/*

INSERT INTO plants (
    userId, plantType, name, notes, outreachDurationDays, nextOutreachTime, stage, xCoord, yCoord
) values (
    1, 1, 'Name of contact here', 'Reach out on linkedIn, blah blah blah', 120, CURRENT_DATE, 1, 5, 5
);
INSERT INTO plants (
    userId, plantType, name, notes, outreachDurationDays, nextOutreachTime, stage, xCoord, yCoord
) values (
    1, 1, 'Name of contact 2 here', 'Reach out via email, blah blah blah', 180, CURRENT_DATE, 2, 2, 2
);

INSERT INTO special_outreach (
    userId, plantId, notes, outreachTime, completed
) values (
    1, 1, 'Notes about why to outreach here', CURRENT_DATE, false
);

INSERT INTO outreach (
    userId, plantId, contents
) values (
    1, 1, 'Here is what I said last outreach blah blah'
);
*/