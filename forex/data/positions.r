data = read.csv('./positions.csv')
profit = data[data$Profit > 0,]
loss = data[data$Profit <= 0, ]