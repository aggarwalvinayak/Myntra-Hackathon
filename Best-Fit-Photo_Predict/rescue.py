import pandas as pd
import os.path
df = pd.read_csv("./data/train_modified.csv")

count=0
for i in df['image_path']:
	if not (os.path.isfile(i)):
		df=df.drop(count)
		# print("_0")
	#df=df['category'].iloc(i)
	count+=1
df.to_csv("./data/train_modified.csv",index=False)
print(df)
