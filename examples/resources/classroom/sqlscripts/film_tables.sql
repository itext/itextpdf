DROP TABLE IF EXISTS festival_screening;
DROP TABLE IF EXISTS festival_entry;
DROP TABLE IF EXISTS film_category;
DROP TABLE IF EXISTS film_director;
DROP TABLE IF EXISTS director_name;
DROP TABLE IF EXISTS film_title;
CREATE TABLE film_title (
  film_id int NOT NULL,
  title varchar(60) NOT NULL,
  a_title varchar(60) default NULL,
  imdb varchar(7) NOT NULL,
  year int NOT NULL,
  duration int NOT NULL,
  PRIMARY KEY  (film_id)
);
CREATE TABLE director_name (
  director_id int NOT NULL,
  name varchar(80) NOT NULL,
  PRIMARY KEY  (director_id)
);
CREATE TABLE film_director (
  film_id int NOT NULL,
  director_id int NOT NULL,
  FOREIGN KEY (film_id) REFERENCES film_title (film_id),
  FOREIGN KEY (director_id) REFERENCES director_name (director_id)
);
CREATE TABLE film_category (
  category_id int NOT NULL,
  name varchar(40) default NULL,
  keyword varchar(6) NOT NULL,
  PRIMARY KEY  (category_id)
);
CREATE TABLE festival_entry (
  film_id int NOT NULL,
  year int NOT NULL,
  category int NOT NULL,
  explorezone int NOT NULL,
  PRIMARY KEY  (film_id,year),
  FOREIGN KEY (film_id) REFERENCES film_title (film_id),
  FOREIGN KEY (category) REFERENCES film_category (category_id)
);
CREATE TABLE festival_screening (
  day date NOT NULL,
  time time NOT NULL,
  place varchar(4) NOT NULL,
  film_id int NOT NULL,
  press int NOT NULL,
  shortfilm int NOT NULL,
  PRIMARY KEY  (day,time,place),
  FOREIGN KEY (film_id) REFERENCES film_title (film_id)
);