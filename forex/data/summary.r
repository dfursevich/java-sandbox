library(DBI)
library(scatterplot3d)
library(rgl)
library(ggplot2)
library(grid)

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

p1 <- ggplot(summary, aes(x=stop_loss, y=profit)) + geom_point(shape=1)    

p2 <- ggplot(summary, aes(x=take_profit, y=profit)) + geom_point(shape=1) 

grid.draw(cbind(ggplotGrob(p1), ggplotGrob(p2), size="last"))