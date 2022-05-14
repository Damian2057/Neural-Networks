import matplotlib.pyplot as plt
from numpy import genfromtxt

data = genfromtxt('statistics/Neurons_4_ALL.csv', delimiter=",")

epoch = data[:,0]
error = data[:,1]

plt.plot(epoch, error)
plt.title('Neurons Network')
plt.xlabel('Epoch')
plt.ylabel('Error')
plt.show()