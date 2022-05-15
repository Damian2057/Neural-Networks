import matplotlib.pyplot as plt
from numpy import genfromtxt
import os


def generatechart(filename):
    data = genfromtxt("statistics/"+filename, delimiter=",")
    epoch = data[:, 0]
    error = data[:, 1]
    plt.plot(epoch, error)
    plt.title(filename)
    plt.xlabel('Epoch')
    plt.ylabel('Error')
    plt.show()

def generateFromData():
    for x in os.listdir("statistics"):
        if x.__contains__('ALL'):
            data = genfromtxt("statistics/"+x, delimiter=",")
            plt.plot(data[:, 0], data[:, 1])
    plt.title('Sum')
    plt.xlabel('Epoch')
    plt.ylabel('Error')
    plt.show()

for x in os.listdir("statistics"):
    if x.endswith(".csv"):
        generatechart(x)

generateFromData()