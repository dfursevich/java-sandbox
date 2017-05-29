library(DBI)
library(scatterplot3d)
library(rgl)

con <- dbConnect(RMySQL::MySQL(), user="root", password="root", dbname="forex2", host="127.0.0.1")
summary <- dbReadTable(con, "summary")

with(summary, {
  scatterplot3d(stop_loss,   # x axis
                take_profit,     # y axis
                profit,    # z axis
                color="blue", pch=19, # filled blue circles
                type="h")
})


with(summary, {
  plot3d(stop_loss, take_profit, profit, col="red", size=3)
})