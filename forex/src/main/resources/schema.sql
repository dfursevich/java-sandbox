CREATE TABLE IF NOT EXISTS eurusd (
  id int(11) NOT NULL AUTO_INCREMENT,
  time timestamp NOT NULL,
  open decimal(6,5) NULL,
  high decimal(6,5) NULL,
  low decimal(6,5) NULL,
  close decimal(6,5) NULL,
  PRIMARY KEY (id)
);