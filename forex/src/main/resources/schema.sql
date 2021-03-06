CREATE TABLE IF NOT EXISTS eurusd (
  id int(11) NOT NULL AUTO_INCREMENT,
  time timestamp NOT NULL,
  open decimal(6,5) NULL,
  high decimal(6,5) NULL,
  low decimal(6,5) NULL,
  close decimal(6,5) NULL,
  PRIMARY KEY (id),
  INDEX (time)
);

CREATE TABLE IF NOT EXISTS indicators (
  id int(11) NOT NULL AUTO_INCREMENT,
  time timestamp NOT NULL,
  short_ema decimal(6,5) NULL,
  long_ema decimal(6,5) NULL,
  PRIMARY KEY (id),
  INDEX (time)
);

CREATE TABLE IF NOT EXISTS positions (
  id int(11) NOT NULL AUTO_INCREMENT,
  open decimal(6,5) NOT NULL,
  close decimal(6,5) NOT NULL,
  max_profit decimal(6,5) NOT NULL,
  profit decimal(6,5) NOT NULL,
  open_date timestamp NOT NULL,
  close_date timestamp NOT NULL default CURRENT_TIMESTAMP,
  duration int(11) NOT NULL,
  PRIMARY KEY (id),
  INDEX (open_date),
  INDEX (close_date)
);

CREATE TABLE IF NOT EXISTS summary (
  id int(11) NOT NULL AUTO_INCREMENT,
  stop_loss decimal(6,5) NULL,
  take_profit decimal(6,5) NULL,
  duration int(11) NOT NULL,
  profit decimal(6,5) NOT NULL,
  total_count int(11) NOT NULL,
  profit_count int(11) NOT NULL,
  loss_count int(11) NOT NULL,
  PRIMARY KEY (id)
);