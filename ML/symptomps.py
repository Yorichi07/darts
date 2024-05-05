import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from collections import Counter
from sklearn import tree
import pickle
from sklearn2pmml import sklearn2pmml
from sklearn2pmml import PMMLPipeline

pd.set_option('display.max_colwidth',None)
dis_sym_data = pd.read_csv("Original_Dataset.csv")
cols_to_check = []
for col in dis_sym_data.columns:
    if col != 'Disease':
        cols_to_check.append(col)

symps = dis_sym_data.iloc[:,1:].values.flatten()
symps = list(set(symps))

for symp in symps:
    dis_sym_data[symp] = dis_sym_data.iloc[:,1:].apply(lambda row: int(symp in row.values),axis=1)

dis_sym_data_v1 = dis_sym_data.drop(columns=cols_to_check)
dis_sym_data_v1 = dis_sym_data_v1.loc[:,dis_sym_data_v1.columns.notna()]

dis_sym_data_v1.columns = dis_sym_data_v1.columns.str.strip()

#Encoding Disease
var_mod = ['Disease']
le = LabelEncoder()
for i in var_mod:
    dis_sym_data_v1[i] = le.fit_transform(dis_sym_data_v1[i])

X = dis_sym_data_v1.drop(columns='Disease')
Y = dis_sym_data_v1['Disease']
model = PMMLPipeline([("classifier",tree.DecisionTreeClassifier())])
model.fit(X=X,y=Y)

#Map Description and Specialist for Disease Pred
doc_data = pd.read_csv("./Doctor_Versus_Disease.csv",encoding='latin1',names=['Disease','Specialist'])
doc_data['Specialist'] = np.where((doc_data['Disease']=='Tuberculosis'),'Pulmonologist',doc_data['Specialist'])

test_col = []
for col in dis_sym_data_v1.columns:
    if col != 'Disease':
        test_col.append(col)
sympom = ["chills","knee_pain","acidity"]
testcas = {}
for col in test_col:
    testcas[col] = 1 if col in sympom else 0
    test_df = pd.DataFrame(testcas,index=[0])
predict = model.predict(test_df)
print(le.inverse_transform(predict))
print(test_col)

sklearn2pmml(model,"model.pmml")