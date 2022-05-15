import matplotlib.pyplot as plt
from numpy import genfromtxt

data = genfromtxt('statistics/Neurons_4_output_3.csv', delimiter=",")
data2 = genfromtxt('statistics/Neurons_4_hidden_0.csv', delimiter=",")

epoch = data[:,0]
error = data[:,1]

plt.plot(epoch, error)
plt.plot(data2[:,0], data2[:,1])
plt.title('Neurons Network')
plt.xlabel('Epoch')
plt.ylabel('Error')
plt.show()