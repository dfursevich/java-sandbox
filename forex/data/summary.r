library(DBI)
library(scatterplot3d)
library(rgl)
library(ggplot2)
library(grid)

con <- dbConnect(RMySQL::MySQL(), user="root", password="root", dbname="forex2", host="127.0.0.1")

summary_0 <- dbReadTable(con, "summary_0")
summary_4 <- dbReadTable(con, "summary_4")

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

p1 <- ggplot(summary_4, aes(x=stop_loss, y=profit)) + geom_point(shape=1)    
p2 <- ggplot(summary_4, aes(x=take_profit, y=profit)) + geom_point(shape=1) 
p3 <- ggplot(summary_0, aes(x=stop_loss, y=profit)) + geom_point(shape=1)    
p4 <- ggplot(summary_0, aes(x=take_profit, y=profit)) + geom_point(shape=1) 
grid.draw(rbind(ggplotGrob(p1), ggplotGrob(p2), size="last"), rbind(ggplotGrob(p3), ggplotGrob(p4), size="last")))
