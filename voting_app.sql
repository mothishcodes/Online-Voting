
CREATE DATABASE online_voting;
USE online_voting;



CREATE TABLE voters (
voter_id VARCHAR(64) PRIMARY KEY,
name VARCHAR(255),
has_voted TINYINT(1) DEFAULT 0,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE votes (
id INT AUTO_INCREMENT PRIMARY KEY,
voter_id VARCHAR(64),
candidate VARCHAR(255),
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (voter_id) REFERENCES voters(voter_id)
);


INSERT INTO voters (voter_id, name) VALUES
('VOTER001','Ram'),
('VOTER002','Rahul'),
('VOTER003','Mohit');
