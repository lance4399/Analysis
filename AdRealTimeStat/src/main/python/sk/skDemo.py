#coding:utf-8
import numpy as np
import pandas as pd
if __name__ =="__main__":

    a = [np.array([1.0, 10.0, 100.0]), np.array([2.0, 20.0, 200.0]), np.array([3.0, 30.0, 300.0])]
    d = pd.DataFrame([1,2,3],[4,5,6],columns = ['a,','b','c'])
    d.describe()
    print('the numNonzeros is ',a)