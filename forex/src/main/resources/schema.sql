CREATE TABLE IF NOT EXISTS eurusd (
  id int(11) NOT NULL AUTO_INCREMENT,
  time timestamp NOT NULL,
  open decimal(6,5) NULL,
  high decimal(6,5) NULL,
  low decimal(6,5) NULL,
  close decimal(6,5) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS positions (
  id int(11) NOT NULL AUTO_INCREMENT,
  open decimal(6,5) NOT NULL,
  close decimal(6,5) NOT NULL,
  profit decimal(6,5) NOT NULL,
  open_date timestamp NOT NULL,
  close_date timestamp NOT NULL default CURRENT_TIMESTAMP,
  duration int(11) NOT NULL,
  PRIMARY KEY (id)
);