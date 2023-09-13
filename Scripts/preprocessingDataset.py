import os
import re
import pandas as pd
from io import StringIO



root_directory = './MobiFall_Dataset_v2.0' # replace with the path to your root directory


newDf = pd.DataFrame(columns = ['TestID','SampleNum', 'X-Axis-Accelerometer', 'Y-Axis-Accelerometer', 'Z-Axis-Accelerometer','X-Axis-Gyroscopic', 'Y-Axis-Gyroscopic', 'Z-Axis-Gyroscopic','X-Axis-Orientation', 'Y-Axis-Orientation', 'Z-Axis-Orientation','Fall'])


rows = {}

i = 0
for root, dirs, files in os.walk(root_directory):
    regex = re.compile('.*acc.*')
    newFiles = [s for s in files if regex.match(s)] + [s for s in files if not regex.match(s)]
    for file in newFiles:
        print(i)
        i = i + 1
        file_path = os.path.join(root, file)
        with open(file_path, 'r') as f:
                data = f.read()
                index = re.search('@DATA', data).start()
                clean_data  = 'Timestamp,X-Axis,Y-Axis,Z-Axis\n' + data[index + 6:]
                df = pd.read_csv(StringIO(clean_data))

                splitted = file_path.split('/')
                splitted = splitted[-1].split('_')
                sub = splitted[2]
                test = splitted[3].split('.')[0]
                TestId = splitted[0] + sub + test
                

                if 'acc' in file_path:
                    rows[TestId] = {
                        'rows':{},
                        'items':0
                    }
                    fall = 0
                    if 'FALLS' in file_path:
                        fall = 1

                    for index, row in df.iterrows():
                        rows[TestId]['rows'][index] = {'TestID':TestId,'SampleNum':index, 'X-Axis-Accelerometer':row['X-Axis'], 'Y-Axis-Accelerometer':row['Y-Axis'], 'Z-Axis-Accelerometer':row['Z-Axis'],'X-Axis-Gyroscopic':0, 'Y-Axis-Gyroscopic':0, 'Z-Axis-Gyroscopic':0,'X-Axis-Orientation':0, 'Y-Axis-Orientation':0, 'Z-Axis-Orientation':0,'Fall':fall}
                        rows[TestId]['items'] = rows[TestId]['items'] + 1

                else:
                    if 'gyro' in file_path:
                        for index, row in df.iterrows():
        
                            if index < rows[TestId]['items']:
                                rows[TestId]['rows'][index]['X-Axis-Gyroscopic'] = row['X-Axis']
                                rows[TestId]['rows'][index]['Y-Axis-Gyroscopic'] = row['Y-Axis']
                                rows[TestId]['rows'][index]['Z-Axis-Gyroscopic'] = row['Z-Axis']
                            else:
                                break
                            

                    else:
                        for index, row in df.iterrows():
                            if index < rows[TestId]['items']:
                                rows[TestId]['rows'][index]['X-Axis-Orientation'] = row['X-Axis']
                                rows[TestId]['rows'][index]['Y-Axis-Orientation'] = row['Y-Axis']
                                rows[TestId]['rows'][index]['Z-Axis-Orientation'] = row['Z-Axis']
                            else:
                                break
i = 0
print(len(rows.keys()))           
for test in rows.values():
    print(i)
    i = i + 1
    for row in test['rows'].values():
        newDf = newDf.append(row, ignore_index=True)


newDf.to_csv('my_data.csv', index=False)

                        

           


                

                
                
            

        
            




